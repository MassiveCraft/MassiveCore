package com.massivecraft.massivecore;

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

import com.massivecraft.massivecore.adapter.InventoryAdapter;
import com.massivecraft.massivecore.adapter.ItemStackAdapter;
import com.massivecraft.massivecore.adapter.JsonElementAdapter;
import com.massivecraft.massivecore.adapter.MassiveListAdapter;
import com.massivecraft.massivecore.adapter.MassiveMapAdapter;
import com.massivecraft.massivecore.adapter.MassiveSetAdapter;
import com.massivecraft.massivecore.adapter.MassiveTreeSetAdapter;
import com.massivecraft.massivecore.adapter.ModdedEnumTypeAdapter;
import com.massivecraft.massivecore.adapter.PlayerInventoryAdapter;
import com.massivecraft.massivecore.adapter.MassiveTreeMapAdapter;
import com.massivecraft.massivecore.adapter.UUIDAdapter;
import com.massivecraft.massivecore.cmd.massivecore.CmdMassiveCore;
import com.massivecraft.massivecore.cmd.massivecore.CmdMassiveCoreBuffer;
import com.massivecraft.massivecore.cmd.massivecore.CmdMassiveCoreCmdurl;
import com.massivecraft.massivecore.cmd.massivecore.CmdMassiveCoreStore;
import com.massivecraft.massivecore.cmd.massivecore.CmdMassiveCoreUsys;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveListDef;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.collections.MassiveSetDef;
import com.massivecraft.massivecore.collections.MassiveTreeMap;
import com.massivecraft.massivecore.collections.MassiveTreeMapDef;
import com.massivecraft.massivecore.collections.MassiveTreeSet;
import com.massivecraft.massivecore.collections.MassiveTreeSetDef;
import com.massivecraft.massivecore.event.EventMassiveCoreUuidUpdate;
import com.massivecraft.massivecore.fetcher.Fetcher;
import com.massivecraft.massivecore.fetcher.IdAndName;
import com.massivecraft.massivecore.integration.vault.IntegrationVault;
import com.massivecraft.massivecore.mixin.EngineTeleportMixinCause;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSAdapter;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.ExamineThread;
import com.massivecraft.massivecore.teleport.EngineScheduledTeleport;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;
import com.massivecraft.massivecore.xlib.gson.JsonArray;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;

public class MassiveCore extends MassivePlugin
{
	// -------------------------------------------- //
	// COMMON CONSTANTS
	// -------------------------------------------- //
	
	public final static String INSTANCE = "instance";
	public final static String DEFAULT = "default";
	
	public final static Set<String> NOTHING = MUtil.treeset("", "none", "null", "nothing");
	public final static Set<String> REMOVE = MUtil.treeset("clear", "c", "delete", "del", "d", "erase", "e", "remove", "rem", "r", "reset", "res");
	public final static Set<String> NOTHING_REMOVE = MUtil.treeset("", "none", "null", "nothing", "clear", "c", "delete", "del", "d", "erase", "e", "remove", "rem", "r", "reset", "res");
	
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveCore i;
	public static MassiveCore get() { return i; }
	public MassiveCore() { i = this; }
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static Random random = new Random();
	public static Gson gson = getMassiveCoreGsonBuilder().create();
	
	public static GsonBuilder getMassiveCoreGsonBuilder()
	{
		return new GsonBuilder()
		.setPrettyPrinting()
		.disableHtmlEscaping()
		.excludeFieldsWithModifiers(Modifier.TRANSIENT)
		.registerTypeAdapter(JsonNull.class, JsonElementAdapter.get())
		.registerTypeAdapter(JsonPrimitive.class, JsonElementAdapter.get())
		.registerTypeAdapter(JsonArray.class, JsonElementAdapter.get())
		.registerTypeAdapter(JsonObject.class, JsonElementAdapter.get())
		.registerTypeAdapter(UUID.class, UUIDAdapter.get())
		.registerTypeAdapter(ItemStack.class, ItemStackAdapter.get())
		.registerTypeAdapter(Inventory.class, InventoryAdapter.get())
		.registerTypeAdapter(PlayerInventory.class, PlayerInventoryAdapter.get())
		.registerTypeAdapter(PS.class, PSAdapter.get())
		
		.registerTypeAdapter(MassiveList.class, MassiveListAdapter.get())
		.registerTypeAdapter(MassiveListDef.class, MassiveListAdapter.get())
		.registerTypeAdapter(MassiveMap.class, MassiveMapAdapter.get())
		.registerTypeAdapter(MassiveMapDef.class, MassiveMapAdapter.get())
		.registerTypeAdapter(MassiveSet.class, MassiveSetAdapter.get())
		.registerTypeAdapter(MassiveSetDef.class, MassiveSetAdapter.get())
		.registerTypeAdapter(MassiveTreeMap.class, MassiveTreeMapAdapter.get())
		.registerTypeAdapter(MassiveTreeMapDef.class, MassiveTreeMapAdapter.get())
		.registerTypeAdapter(MassiveTreeSet.class, MassiveTreeSetAdapter.get())
		.registerTypeAdapter(MassiveTreeSetDef.class, MassiveTreeSetAdapter.get())
		
		.registerTypeAdapterFactory(ModdedEnumTypeAdapter.ENUM_FACTORY);
	}
	
	public static String getServerId() { return ConfServer.serverid; }
	//private static Db<?> db;
	//public static Db<?> getDb() { return db; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Commands
	private CmdMassiveCore outerCmdMassiveCore;
	public CmdMassiveCore getOuterCmdMassiveCore() { return this.outerCmdMassiveCore; }
	
	private CmdMassiveCoreUsys outerCmdMassiveCoreUsys;
	public CmdMassiveCoreUsys getOuterCmdMassiveCoreUsys() { return this.outerCmdMassiveCoreUsys; }
	
	private CmdMassiveCoreStore outerCmdMassiveCoreStore;
	public CmdMassiveCoreStore getOuterCmdMassiveCoreStore() { return this.outerCmdMassiveCoreStore; }
	
	private CmdMassiveCoreBuffer outerCmdMassiveCoreBuffer;
	public CmdMassiveCoreBuffer getOuterCmdMassiveCoreBuffer() { return this.outerCmdMassiveCoreBuffer; }
	
	private CmdMassiveCoreCmdurl outerCmdMassiveCoreCmdurl;
	public CmdMassiveCoreCmdurl getOuterCmdMassiveCoreCmdurl() { return this.outerCmdMassiveCoreCmdurl; }
	
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
		MassiveCoreEngineMain.get().activate();
		MassiveCoreEngineVariable.get().activate();
		EngineScheduledTeleport.get().activate();
		EngineTeleportMixinCause.get().activate();
		MassiveCoreEngineWorldNameSet.get().activate();
		MassiveCoreEngineCommandRegistration.get().activate();
		PlayerUtil.get().activate();
		
		// Tasks
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.collTickTask, 1, 1);
		
		// Collections
		MultiverseColl.get().init();
		AspectColl.get().init();
		MassiveCoreMConfColl.get().init();
		
		// Register commands
		this.outerCmdMassiveCore = new CmdMassiveCore() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesOuterMassiveCore; } };
		this.outerCmdMassiveCore.register();
		
		this.outerCmdMassiveCoreUsys = new CmdMassiveCoreUsys() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesOuterMassiveCoreUsys; } };
		this.outerCmdMassiveCoreUsys.register();
		
		this.outerCmdMassiveCoreStore = new CmdMassiveCoreStore() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesOuterMassiveCoreStore; } };
		this.outerCmdMassiveCoreStore.register();
		
		this.outerCmdMassiveCoreBuffer = new CmdMassiveCoreBuffer() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesOuterMassiveCoreBuffer; } };
		this.outerCmdMassiveCoreBuffer.register();
		
		this.outerCmdMassiveCoreCmdurl = new CmdMassiveCoreCmdurl() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesOuterMassiveCoreCmdurl; } };
		this.outerCmdMassiveCoreCmdurl.register();
		
		// Integration
		this.integrate(
			IntegrationVault.get()
		);
		
		// Delete Files (at once and additionally after all plugins loaded)
		MassiveCoreTaskDeleteFiles.get().run();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, MassiveCoreTaskDeleteFiles.get());
		
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
				
				EventMassiveCoreUuidUpdate event = new EventMassiveCoreUuidUpdate();
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
		MassiveCoreTaskDeleteFiles.get().run();
		IdUtil.saveCachefileDatas();
	}
	
}
