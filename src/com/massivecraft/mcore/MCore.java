package com.massivecraft.mcore;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.mcore.adapter.InventoryAdapter;
import com.massivecraft.mcore.adapter.ItemStackAdapter;
import com.massivecraft.mcore.adapter.JsonElementAdapter;
import com.massivecraft.mcore.adapter.ModdedEnumTypeAdapter;
import com.massivecraft.mcore.adapter.ObjectIdAdapter;
import com.massivecraft.mcore.adapter.PlayerInventoryAdapter;
import com.massivecraft.mcore.adapter.UUIDAdapter;
import com.massivecraft.mcore.event.MCoreUuidUpdateEvent;
import com.massivecraft.mcore.fetcher.Fetcher;
import com.massivecraft.mcore.fetcher.IdAndName;
import com.massivecraft.mcore.integration.protocollib.ProtocolLibFeatures;
import com.massivecraft.mcore.integration.vault.VaultFeatures;
import com.massivecraft.mcore.mcorecmd.CmdMCore;
import com.massivecraft.mcore.mcorecmd.CmdMCoreMStore;
import com.massivecraft.mcore.mcorecmd.CmdMCoreUsys;
import com.massivecraft.mcore.mixin.EngineTeleportMixinCause;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.ps.PSAdapter;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.ExamineThread;
import com.massivecraft.mcore.teleport.EngineScheduledTeleport;
import com.massivecraft.mcore.util.IdUtil;
import com.massivecraft.mcore.util.PlayerUtil;
import com.massivecraft.mcore.util.Txt;
import com.massivecraft.mcore.xlib.bson.types.ObjectId;
import com.massivecraft.mcore.xlib.gson.Gson;
import com.massivecraft.mcore.xlib.gson.GsonBuilder;
import com.massivecraft.mcore.xlib.gson.JsonArray;
import com.massivecraft.mcore.xlib.gson.JsonNull;
import com.massivecraft.mcore.xlib.gson.JsonObject;
import com.massivecraft.mcore.xlib.gson.JsonPrimitive;

public class MCore extends MPlugin
{
	// -------------------------------------------- //
	// COMMON CONSTANTS
	// -------------------------------------------- //
	
	public final static String INSTANCE = "instance";
	public final static String DEFAULT = "default";
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MCore i;
	public static MCore get() { return i; }
	public MCore() { i = this; }
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static Random random = new Random();
	public static Gson gson = getMCoreGsonBuilder().create();
	
	public static GsonBuilder getMCoreGsonBuilder()
	{
		return new GsonBuilder()
		.setPrettyPrinting()
		.disableHtmlEscaping()
		.excludeFieldsWithModifiers(Modifier.TRANSIENT)
		.registerTypeAdapter(JsonNull.class, JsonElementAdapter.get())
		.registerTypeAdapter(JsonPrimitive.class, JsonElementAdapter.get())
		.registerTypeAdapter(JsonArray.class, JsonElementAdapter.get())
		.registerTypeAdapter(JsonObject.class, JsonElementAdapter.get())
		.registerTypeAdapter(ObjectId.class, ObjectIdAdapter.get())
		.registerTypeAdapter(UUID.class, UUIDAdapter.get())
		.registerTypeAdapter(ItemStack.class, ItemStackAdapter.get())
		.registerTypeAdapter(Inventory.class, InventoryAdapter.get())
		.registerTypeAdapter(PlayerInventory.class, PlayerInventoryAdapter.get())
		.registerTypeAdapter(PS.class, PSAdapter.get())
		.registerTypeAdapterFactory(ModdedEnumTypeAdapter.ENUM_FACTORY);
	}
	
	public static String getServerId() { return ConfServer.serverid; }
	//private static Db<?> db;
	//public static Db<?> getDb() { return db; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Commands
	private CmdMCore outerCmdMCore;
	public CmdMCore getOuterCmdMCore() { return this.outerCmdMCore; }
	
	private CmdMCoreUsys outerCmdMCoreUsys;
	public CmdMCoreUsys getOuterCmdMCoreUsys() { return this.outerCmdMCoreUsys; }
	
	private CmdMCoreMStore outerCmdMCoreMStore;
	public CmdMCoreMStore getOuterCmdMCoreMStore() { return this.outerCmdMCoreMStore; }
	
	// Runnables
	// TODO: Make this one a singleton
	private Runnable collTickTask = new Runnable()
	{
		public void run()
		{
			for (Coll<?> coll : Coll.getInstances())
			{
				coll.onTick();
			}
		}
	};
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	public boolean doderp = false;
	
	@Override
	public void onEnable()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				doderp = true;
			}
		}, 20);
		
		// This is safe since all plugins using Persist should bukkit-depend this plugin.
		// Note this one must be before preEnable. dooh.
		// TODO: Create something like "deinit all" (perhaps a forloop) to readd this.
		// TODO: Test and ensure reload compat.
		// Coll.instances.clear();
		
		// Start the examine thread
		ExamineThread.get().start();
		
		if ( ! preEnable()) return;
		
		// Load Server Config
		ConfServer.get().load();
		
		// Setup the default database
		//db = MStore.getDb(ConfServer.dburi);
		
		// Setup IdUtil
		IdUtil.setup();
		
		// Register events
		EngineMainMCore.get().activate();
		EngineScheduledTeleport.get().activate();
		EngineTeleportMixinCause.get().activate();
		EngineWorldNameSet.get().activate();
		EngineCommandRegistration.get().activate(); // TODO: Make all engines
		PlayerUtil.get().setup();
		
		// Tasks
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.collTickTask, 1, 1);
		
		// Collections
		MultiverseColl.get().init();
		AspectColl.get().init();
		MCoreConfColl.get().init();
		
		// Register commands
		this.outerCmdMCore = new CmdMCore() { public List<String> getAliases() { return MCoreConf.get().aliasesOuterMCore; } };
		this.outerCmdMCore.register();
		
		this.outerCmdMCoreUsys = new CmdMCoreUsys() { public List<String> getAliases() { return MCoreConf.get().aliasesOuterMCoreUsys; } };
		this.outerCmdMCoreUsys.register();
		
		this.outerCmdMCoreMStore = new CmdMCoreMStore() { public List<String> getAliases() { return MCoreConf.get().aliasesOuterMCoreMStore; } };
		this.outerCmdMCoreMStore.register();
		
		// Integration
		this.integrate(
			ProtocolLibFeatures.get(),
			VaultFeatures.get()
		);
		
		// Delete Files (at once and additionally after all plugins loaded)
		TaskDeleteFiles.get().run();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, TaskDeleteFiles.get());
		
		// test();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				log(Txt.parse("<a>============================================"));
				log(Txt.parse("<i>We are preparing for Mojangs switch to UUIDs."));
				log(Txt.parse("<i>Learn more at: <aqua>https://forums.bukkit.org/threads/psa-the-switch-to-uuids-potential-plugin-server-breakage.250915/"));
				
				// TODO: NOTE!!! IMPORTANT EVEN LATER!
				IdUtil.loadDatas();
				
				log(Txt.parse("<i>Now updating database for plugins that are ready ..."));
				
				MCoreUuidUpdateEvent event = new MCoreUuidUpdateEvent();
				event.run();
				
				log(Txt.parse("<g> ... done!"));
				log(Txt.parse("<i>(database saving will now commence which might lock the server for a while)"));
				log(Txt.parse("<a>============================================"));
			}
		});
		
		
		this.postEnable();
	}
	
	public void test()
	{
		log("===========================");
		log("===========================");
		log("===========================");
		
		try
		{
			// whatever you feel like
			List<Object> objects = new ArrayList<Object>();
			
			//objects.add("Cayorion");
			objects.add("a2cce16b-9494-45ff-b5ff-0362ca687d4e");
			
			//objects.add("a2cce16b-9494-45ff-b5ff-0362ca687d4a");
			
			objects.add("hnnn");
			objects.add("hnnnbsarc");
			
			objects.add("NOT EVEN LEGIT");
			
			objects.add("MonMarty");
			objects.add("Thortuna");
			objects.add("yendor46");
			objects.add("Gethelp");
			objects.add("Th3_Drunk_Monk");
			objects.add("Ryciera");
			objects.add("Jamescl");
			objects.add("spectec");
			objects.add("Tom1804");
			objects.add("imboring56");
			objects.add("BigBellyBuddah");
			objects.add("MrHappyTinkles");
			objects.add("BabaManga");
			objects.add("_Omnomivore_");
			objects.add("Cielian");
			objects.add("BboyMVB");
			objects.add("MrWackeo");
			objects.add("Kellock93");
			objects.add("Feykronos");
			objects.add("Unluvable");
			objects.add("DanyWood");
			objects.add("jadex224");
			objects.add("MinecraftSpartan");
			objects.add("ravenwolfthorn");
			objects.add("ELtongo");
			objects.add("Azas");
			objects.add("TazeHD");
			objects.add("BillyA835");
			objects.add("duhsigil");
			objects.add("Sahrotaar");
			objects.add("Alj23");
			
			Set<IdAndName> idAndNames = Fetcher.fetch(objects);
			
			// Map<String, UUID> map = PlayerUtil.getPlayerIds(MUtil.list("Cayorion", "MonMarty", "Thortuna", "yendor46", "Gethelp", "Th3_Drunk_Monk", "Ryciera", "Jamescl", "spectec", "Tom1804", "imboring56", "BigBellyBuddah", "MrHappyTinkles", "BabaManga", "_Omnomivore_", "Cielian", "BboyMVB", "MrWackeo", "Kellock93", "Feykronos", "Unluvable", "DanyWood", "jadex224", "MinecraftSpartan", "ravenwolfthorn", "ELtongo", "Azas", "TazeHD", "BillyA835", "duhsigil", "Sahrotaar", "Alj23"));
			
			for (IdAndName idAndName: idAndNames)
			{
				String name = idAndName.getName();
				UUID id = idAndName.getId();
				log(Txt.parse("<k>%s <v>%s", name, id));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		log("===========================");
		log("===========================");
		log("===========================");
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
		ExamineThread.get().interrupt();
		TaskDeleteFiles.get().run();
		IdUtil.saveCachefileDatas();
	}
	
}
