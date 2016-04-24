package com.massivecraft.massivecore;

import java.lang.reflect.Modifier;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.adapter.BackstringEnumSetAdapter;
import com.massivecraft.massivecore.adapter.EntryAdapter;
import com.massivecraft.massivecore.adapter.InventoryAdapter;
import com.massivecraft.massivecore.adapter.ItemStackAdapter;
import com.massivecraft.massivecore.adapter.JsonElementAdapter;
import com.massivecraft.massivecore.adapter.MassiveListAdapter;
import com.massivecraft.massivecore.adapter.MassiveMapAdapter;
import com.massivecraft.massivecore.adapter.MassiveSetAdapter;
import com.massivecraft.massivecore.adapter.MassiveTreeMapAdapter;
import com.massivecraft.massivecore.adapter.MassiveTreeSetAdapter;
import com.massivecraft.massivecore.adapter.ModdedEnumTypeAdapter;
import com.massivecraft.massivecore.adapter.MsonAdapter;
import com.massivecraft.massivecore.adapter.MsonEventAdapter;
import com.massivecraft.massivecore.adapter.PlayerInventoryAdapter;
import com.massivecraft.massivecore.adapter.SoundAdapter;
import com.massivecraft.massivecore.adapter.UUIDAdapter;
import com.massivecraft.massivecore.collections.BackstringEnumSet;
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
import com.massivecraft.massivecore.command.massivecore.CmdMassiveCore;
import com.massivecraft.massivecore.command.massivecore.CmdMassiveCoreBuffer;
import com.massivecraft.massivecore.command.massivecore.CmdMassiveCoreClick;
import com.massivecraft.massivecore.command.massivecore.CmdMassiveCoreCmdurl;
import com.massivecraft.massivecore.command.massivecore.CmdMassiveCoreStore;
import com.massivecraft.massivecore.command.massivecore.CmdMassiveCoreUsys;
import com.massivecraft.massivecore.engine.EngineMassiveCoreGank;
import com.massivecraft.massivecore.engine.EngineMassiveCoreChestGui;
import com.massivecraft.massivecore.engine.EngineMassiveCoreCollTick;
import com.massivecraft.massivecore.engine.EngineMassiveCoreCommandRegistration;
import com.massivecraft.massivecore.engine.EngineMassiveCoreDatabase;
import com.massivecraft.massivecore.engine.EngineMassiveCoreDestination;
import com.massivecraft.massivecore.engine.EngineMassiveCoreMain;
import com.massivecraft.massivecore.engine.EngineMassiveCorePlayerLeave;
import com.massivecraft.massivecore.engine.EngineMassiveCorePlayerState;
import com.massivecraft.massivecore.engine.EngineMassiveCorePlayerUpdate;
import com.massivecraft.massivecore.engine.EngineMassiveCoreScheduledTeleport;
import com.massivecraft.massivecore.engine.EngineMassiveCoreSponsor;
import com.massivecraft.massivecore.engine.EngineMassiveCoreTeleportMixinCause;
import com.massivecraft.massivecore.engine.EngineMassiveCoreVariable;
import com.massivecraft.massivecore.engine.EngineMassiveCoreWorldNameSet;
import com.massivecraft.massivecore.integration.vault.IntegrationVault;
import com.massivecraft.massivecore.item.WriterBannerPattern;
import com.massivecraft.massivecore.item.WriterFireworkEffect;
import com.massivecraft.massivecore.item.WriterItemStack;
import com.massivecraft.massivecore.item.WriterPotionEffect;
import com.massivecraft.massivecore.mixin.MixinActionbar;
import com.massivecraft.massivecore.mixin.MixinActual;
import com.massivecraft.massivecore.mixin.MixinCommand;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mixin.MixinEvent;
import com.massivecraft.massivecore.mixin.MixinGamemode;
import com.massivecraft.massivecore.mixin.MixinInventory;
import com.massivecraft.massivecore.mixin.MixinKick;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.mixin.MixinModification;
import com.massivecraft.massivecore.mixin.MixinPlayed;
import com.massivecraft.massivecore.mixin.MixinSenderPs;
import com.massivecraft.massivecore.mixin.MixinTeleport;
import com.massivecraft.massivecore.mixin.MixinTitle;
import com.massivecraft.massivecore.mixin.MixinVisibility;
import com.massivecraft.massivecore.mixin.MixinWorld;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.mson.MsonEvent;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSAdapter;
import com.massivecraft.massivecore.store.ModificationPollerLocal;
import com.massivecraft.massivecore.store.ModificationPollerRemote;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.TimeUnit;
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
	public static final String NONE = Txt.parse("<silver>none");
	
	public final static Set<String> NOTHING = MUtil.treeset("", "none", "null", "nothing");
	public final static Set<String> REMOVE = MUtil.treeset("clear", "c", "delete", "del", "d", "erase", "e", "remove", "rem", "r", "reset", "res");
	public final static Set<String> NOTHING_REMOVE = MUtil.treeset("", "none", "null", "nothing", "clear", "c", "delete", "del", "d", "erase", "e", "remove", "rem", "r", "reset", "res");
	
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveCore i;
	public static MassiveCore get() { return i; }
	public MassiveCore()
	{
		i = this;
	}
	
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
		.registerTypeAdapter(Sound.class, SoundAdapter.get())
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
		
		.registerTypeAdapter(Mson.class, MsonAdapter.get())
		.registerTypeAdapter(MsonEvent.class, MsonEventAdapter.get())
		
		.registerTypeAdapter(BackstringEnumSet.class, BackstringEnumSetAdapter.get())
		.registerTypeAdapter(Entry.class, EntryAdapter.get())
		
		.registerTypeAdapterFactory(ModdedEnumTypeAdapter.ENUM_FACTORY);
	}
	
	public static String getServerId() { return ConfServer.serverid; }
	public static String getTaskServerId() { return MassiveCoreMConf.get().taskServerId; }
	public static boolean isTaskServer()
	{
		String taskServerId = getTaskServerId();
		if (taskServerId == null) return true;
		if (getServerId().equals(taskServerId)) return true;
		return false;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		// Attempting to fix a race condition within the class asynchronous class loader.
		System.out.println("TimeUnit.MILLIS_PER_MINUTE: " + TimeUnit.MILLIS_PER_MINUTE);
	}
	
	@Override
	public void onEnableInner()
	{
		// This is safe since all plugins using Persist should bukkit-depend this plugin.
		// Note this one must be before preEnable. dooh.
		// TODO: Create something like "deinit all" (perhaps a forloop) to readd this.
		// TODO: Test and ensure reload compat.
		// Coll.instances.clear();
		
		// Load Server Config
		ConfServer.get().load();
		
		// Setup IdUtil
		IdUtil.setup();
		
		// Activate
		this.activate(
			// Coll
			MultiverseColl.class,
			AspectColl.class,
			MassiveCoreMConfColl.class,
			MassiveCoreMSponsorInfoColl.class,
			
			// Writer,
			WriterItemStack.class,
			WriterPotionEffect.class,
			WriterFireworkEffect.class,
			WriterBannerPattern.class,
		
			// Engine
			EngineMassiveCoreChestGui.class,
			EngineMassiveCoreCollTick.class,
			EngineMassiveCoreCommandRegistration.class,
			EngineMassiveCoreDatabase.class,
			EngineMassiveCoreDestination.class,
			EngineMassiveCoreGank.class,
			EngineMassiveCoreMain.class,
			EngineMassiveCorePlayerLeave.class,
			EngineMassiveCorePlayerState.class,
			EngineMassiveCorePlayerUpdate.class,
			EngineMassiveCoreScheduledTeleport.class,
			EngineMassiveCoreTeleportMixinCause.class,
			EngineMassiveCoreVariable.class,
			EngineMassiveCoreWorldNameSet.class,
			EngineMassiveCoreSponsor.class,
			
			// Util
			PlayerUtil.class,
			
			// Integration
			IntegrationVault.class,
			
			// Command
			CmdMassiveCore.class,
			CmdMassiveCoreUsys.class,
			CmdMassiveCoreStore.class,
			CmdMassiveCoreBuffer.class,
			CmdMassiveCoreCmdurl.class,
			CmdMassiveCoreClick.class,
			
			// Mixin
			MixinEvent.class, // NOTE: Should be first
			MixinActionbar.class,
			MixinActual.class,
			MixinCommand.class,
			MixinDisplayName.class,
			MixinGamemode.class,
			MixinInventory.class,
			MixinKick.class,
			MixinMessage.class,
			MixinModification.class,
			MixinPlayed.class,
			MixinSenderPs.class,
			MixinTeleport.class,
			MixinTitle.class,
			MixinVisibility.class,
			MixinWorld.class
		);
		
		// Start the examine threads
		// Start AFTER initializing the MConf, because they rely on the MConf.
		ModificationPollerLocal.get().start();
		ModificationPollerRemote.get().start();
		
		// Delete Files (at once and additionally after all plugins loaded)
		MassiveCoreTaskDeleteFiles.get().run();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, MassiveCoreTaskDeleteFiles.get());
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
		ModificationPollerLocal.get().interrupt();
		ModificationPollerRemote.get().interrupt();
		
		MassiveCoreTaskDeleteFiles.get().run();
		IdUtil.saveCachefileDatas();
	}

}
