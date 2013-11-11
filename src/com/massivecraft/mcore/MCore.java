package com.massivecraft.mcore;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.mcore.adapter.InventoryAdapter;
import com.massivecraft.mcore.adapter.ItemStackAdapter;
import com.massivecraft.mcore.adapter.JsonElementAdapter;
import com.massivecraft.mcore.adapter.ObjectIdAdapter;
import com.massivecraft.mcore.adapter.PlayerInventoryAdapter;
import com.massivecraft.mcore.adapter.UUIDAdapter;
import com.massivecraft.mcore.cmd.MCoreBukkitSimpleCommandMap;
import com.massivecraft.mcore.integration.protocollib.ProtocolLibFeatures;
import com.massivecraft.mcore.integration.vault.VaultFeatures;
import com.massivecraft.mcore.mcorecmd.CmdMCore;
import com.massivecraft.mcore.mcorecmd.CmdMCoreMStore;
import com.massivecraft.mcore.mcorecmd.CmdMCoreUsys;
import com.massivecraft.mcore.mixin.SenderIdMixinDefault;
import com.massivecraft.mcore.mixin.EngineTeleportMixinCause;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.ps.PSAdapter;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.ExamineThread;
import com.massivecraft.mcore.teleport.EngineScheduledTeleport;
import com.massivecraft.mcore.util.PlayerUtil;
import com.massivecraft.mcore.util.TimeDiffUtil;
import com.massivecraft.mcore.util.TimeUnit;
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
		.registerTypeAdapter(PS.class, PSAdapter.get());
	}
	
	public static String getServerId() { return ConfServer.serverid; }
	//private static Db<?> db;
	//public static Db<?> getDb() { return db; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Aspects
	private Aspect moneyAspect;
	public Aspect getMoneyAspect() { return this.moneyAspect; }
	public Multiverse getMoneyMultiverse() { return this.getMoneyAspect().getMultiverse(); }
	
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
	
	@Override
	public void onEnable()
	{
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
		
		// Setup PlayerUtil and it's events
		SenderIdMixinDefault.get().setup();
		
		// Register events
		EngineMainMCore.get().activate();
		EngineScheduledTeleport.get().activate();
		EngineTeleportMixinCause.get().activate();
		EngineWorldNameSet.get().activate();
		EngineOfflineCase.get().activate(); // TODO: Make all engines
		PlayerUtil.get().setup();
		
		// Tasks
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.collTickTask, 1, 1);
		
		// Collections
		MultiverseColl.get().init();
		AspectColl.get().init();
		MCoreConfColl.get().init();
		
		// Aspects
		this.moneyAspect = AspectColl.get().get("mcore_money", true);
		this.moneyAspect.register();
		this.moneyAspect.setDesc(
			"<i>The aspect used for how much money a player has"
		);
		
		// Inject our command map with dynamic tweaks
		MCoreBukkitSimpleCommandMap.inject();
		
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
		
		/*
		test("");
		test("+1day");
		test("1day");
		test("1 day");
		test("-1day");
		test("1week4d");
		test("+1week-4d");
		test("day");
		test("1month");
		test("1months");
		test("1months2ms");
		*/
		
		this.postEnable();
	}
	
	public void test(String diffString)
	{
		log("===========================");
		log("Testing Diff String \""+diffString+"\":");
		
		try
		{
			Map<TimeUnit, Long> unitcounts = TimeDiffUtil.unitcounts(diffString);
			for (Entry<TimeUnit, Long> entry : unitcounts.entrySet())
			{
				System.out.println(entry.getValue()+": "+entry.getKey());
			}
			
			System.out.println("---");
			
			long millis = TimeDiffUtil.millis(unitcounts);
			log("millis: "+millis);
			
			String verboose = ChatColor.stripColor(TimeDiffUtil.formatedVerboose(unitcounts));
			String minimal = ChatColor.stripColor(TimeDiffUtil.formatedMinimal(unitcounts));
			log("verboose: "+verboose);
			log("minimal: "+minimal);
			
			long millisRec = TimeDiffUtil.millis(minimal);
			log("millisRec: "+millisRec);
			log("matches: "+(millis == millisRec));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
		ExamineThread.get().interrupt();
	}
	
}
