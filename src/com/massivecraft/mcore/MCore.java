package com.massivecraft.mcore;

import java.lang.reflect.Modifier;
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
import com.massivecraft.mcore.adapter.ObjectIdAdapter;
import com.massivecraft.mcore.adapter.PlayerInventoryAdapter;
import com.massivecraft.mcore.adapter.UUIDAdapter;
import com.massivecraft.mcore.cmd.CmdMcore;
import com.massivecraft.mcore.integration.protocollib.ProtocolLibFeatures;
import com.massivecraft.mcore.integration.vault.VaultFeatures;
import com.massivecraft.mcore.mixin.ScheduledTeleportEngine;
import com.massivecraft.mcore.mixin.SenderIdMixinDefault;
import com.massivecraft.mcore.mixin.TeleportMixinCauseEngine;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.ps.PSAdapter;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.Db;
import com.massivecraft.mcore.store.ExamineThread;
import com.massivecraft.mcore.store.MStore;
import com.massivecraft.mcore.usys.cmd.CmdUsys;
import com.massivecraft.mcore.util.PlayerUtil;
import com.massivecraft.mcore.util.TimeDiffUtil;
import com.massivecraft.mcore.util.TimeUnit;
import com.massivecraft.mcore.xlib.bson.types.ObjectId;
import com.massivecraft.mcore.xlib.gson.Gson;
import com.massivecraft.mcore.xlib.gson.GsonBuilder;

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
		.registerTypeAdapter(ObjectId.class, ObjectIdAdapter.get())
		.registerTypeAdapter(UUID.class, UUIDAdapter.get())
		.registerTypeAdapter(ItemStack.class, ItemStackAdapter.get())
		.registerTypeAdapter(Inventory.class, InventoryAdapter.get())
		.registerTypeAdapter(PlayerInventory.class, PlayerInventoryAdapter.get())
		.registerTypeAdapter(PS.class, PSAdapter.get());
	}
	
	public static String getServerId() { return ConfServer.serverid; }
	private static Db<?> db;
	public static Db<?> getDb() { return db; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Commands
	public CmdUsys cmdUsys;
	public CmdMcore cmdMcore;
	
	// Aspects
	private Aspect moneyAspect;
	public Aspect getMoneyAspect() { return this.moneyAspect; }
	public Multiverse getMoneyMultiverse() { return this.getMoneyAspect().getMultiverse(); }
	
	// Runnables
	private Runnable collTickTask = new Runnable()
	{
		public void run()
		{
			for (Coll<?> coll : Coll.instances)
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
		Coll.instances.clear();
		
		// Start the examine thread
		ExamineThread.get().start();
		
		if ( ! preEnable()) return;
		
		// Load Server Config
		ConfServer.get().load();
		
		// Setup the default database
		db = MStore.getDb(ConfServer.dburi);
		
		// Setup PlayerUtil and it's events
		new PlayerUtil(this);
		SenderIdMixinDefault.get().setup();
		
		// Register events
		InternalListener.get().setup();
		ScheduledTeleportEngine.get().setup();
		TeleportMixinCauseEngine.get().setup();
		WorldNameSetEngine.get().setup();
		
		// Schedule the collection ticker.
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.collTickTask, 1, 1);
		
		// Initialize Internal Collections
		MultiverseColl.get().init();
		AspectColl.get().init();
		MCoreConfColl.get().init();
		
		// Init aspects
		this.moneyAspect = AspectColl.get().get("mcore_money", true);
		this.moneyAspect.register();
		this.moneyAspect.setDesc(
			"<i>The aspect used for how much money a player has"
		);
		
		// Register commands
		this.cmdUsys = new CmdUsys();
		this.cmdUsys.register(this, true);
		
		this.cmdMcore = new CmdMcore();
		this.cmdMcore.register(this, true);
		
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
