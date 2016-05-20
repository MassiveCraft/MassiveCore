package com.massivecraft.massivecore;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.adapter.AdapterBackstringEnumSet;
import com.massivecraft.massivecore.adapter.AdapterBannerPatterns;
import com.massivecraft.massivecore.adapter.AdapterEntry;
import com.massivecraft.massivecore.adapter.AdapterInventory;
import com.massivecraft.massivecore.adapter.AdapterItemStack;
import com.massivecraft.massivecore.adapter.AdapterJsonElement;
import com.massivecraft.massivecore.adapter.AdapterMassiveList;
import com.massivecraft.massivecore.adapter.AdapterMassiveMap;
import com.massivecraft.massivecore.adapter.AdapterMassiveSet;
import com.massivecraft.massivecore.adapter.AdapterMassiveTreeMap;
import com.massivecraft.massivecore.adapter.AdapterMassiveTreeSet;
import com.massivecraft.massivecore.adapter.AdapterModdedEnumType;
import com.massivecraft.massivecore.adapter.AdapterMson;
import com.massivecraft.massivecore.adapter.AdapterMsonEvent;
import com.massivecraft.massivecore.adapter.AdapterPlayerInventory;
import com.massivecraft.massivecore.adapter.AdapterSound;
import com.massivecraft.massivecore.adapter.AdapterUUID;
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
import com.massivecraft.massivecore.command.type.RegistryType;
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
import com.massivecraft.massivecore.item.DataBannerPattern;
import com.massivecraft.massivecore.item.WriterItemStack;
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
import com.massivecraft.massivecore.nms.NmsBasics;
import com.massivecraft.massivecore.nms.NmsBoard;
import com.massivecraft.massivecore.nms.NmsChat;
import com.massivecraft.massivecore.nms.NmsEntityGet;
import com.massivecraft.massivecore.nms.NmsItemStackCreate;
import com.massivecraft.massivecore.nms.NmsItemStackCreate17R4P;
import com.massivecraft.massivecore.nms.NmsItemStackTooltip;
import com.massivecraft.massivecore.nms.NmsPermissions;
import com.massivecraft.massivecore.nms.NmsPlayerInventoryCreate;
import com.massivecraft.massivecore.nms.NmsSkullMeta;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSAdapter;
import com.massivecraft.massivecore.store.ModificationPollerLocal;
import com.massivecraft.massivecore.store.ModificationPollerRemote;
import com.massivecraft.massivecore.util.BoardUtil;
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
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

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
		// Create
		GsonBuilder ret = new GsonBuilder();
		
		// Basic Behavior
		ret.setPrettyPrinting();
		ret.disableHtmlEscaping();
		ret.excludeFieldsWithModifiers(Modifier.TRANSIENT);
		
		// Raw Adapters
		ret.registerTypeAdapter(JsonNull.class, AdapterJsonElement.get());
		ret.registerTypeAdapter(JsonPrimitive.class, AdapterJsonElement.get());
		ret.registerTypeAdapter(JsonArray.class, AdapterJsonElement.get());
		ret.registerTypeAdapter(JsonObject.class, AdapterJsonElement.get());
		
		// Enumeration Annotation Dodge
		ret.registerTypeAdapterFactory(AdapterModdedEnumType.ENUM_FACTORY);
		
		// Massive Containers
		ret.registerTypeAdapter(MassiveList.class, AdapterMassiveList.get());
		ret.registerTypeAdapter(MassiveListDef.class, AdapterMassiveList.get());
		ret.registerTypeAdapter(MassiveMap.class, AdapterMassiveMap.get());
		ret.registerTypeAdapter(MassiveMapDef.class, AdapterMassiveMap.get());
		ret.registerTypeAdapter(MassiveSet.class, AdapterMassiveSet.get());
		ret.registerTypeAdapter(MassiveSetDef.class, AdapterMassiveSet.get());
		ret.registerTypeAdapter(MassiveTreeMap.class, AdapterMassiveTreeMap.get());
		ret.registerTypeAdapter(MassiveTreeMapDef.class, AdapterMassiveTreeMap.get());
		ret.registerTypeAdapter(MassiveTreeSet.class, AdapterMassiveTreeSet.get());
		ret.registerTypeAdapter(MassiveTreeSetDef.class, AdapterMassiveTreeSet.get());
		
		// Entries (Is this still needed?)
		ret.registerTypeAdapter(Entry.class, AdapterEntry.get());
		
		// Assorted Custom
		ret.registerTypeAdapter(BackstringEnumSet.class, AdapterBackstringEnumSet.get());
		ret.registerTypeAdapter(PS.class, PSAdapter.get());
		ret.registerTypeAdapter(Sound.class, AdapterSound.get());
		ret.registerTypeAdapter(UUID.class, AdapterUUID.get());
		
		// Mson
		ret.registerTypeAdapter(Mson.class, AdapterMson.get());
		ret.registerTypeAdapter(MsonEvent.class, AdapterMsonEvent.get());
		
		// Banner Patterns Upgrade Adapter
		// NOTE: Must come after the "MassiveContainers" section for priority.
		Type typeBannerPatterns = new TypeToken<MassiveListDef<DataBannerPattern>>(){}.getType();
		ret.registerTypeAdapter(typeBannerPatterns, AdapterBannerPatterns.get());
		
		// ItemStack
		ret.registerTypeAdapter(ItemStack.class, AdapterItemStack.get());
		Class<?> classCraftItemStack = NmsItemStackCreate17R4P.getClassCraftItemStackCatch();
		if (classCraftItemStack != null) ret.registerTypeAdapter(classCraftItemStack, AdapterItemStack.get());
		
		// Inventory
		ret.registerTypeAdapter(Inventory.class, AdapterInventory.get());
		ret.registerTypeAdapter(PlayerInventory.class, AdapterPlayerInventory.get());
		
		// Return
		return ret;
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
	// LOAD
	// -------------------------------------------- //
	
	@Override
	public void onLoadInner()
	{
		// Attempting to fix a race condition within the class asynchronous class loader.
		System.out.println("TimeUnit.MILLIS_PER_MINUTE: " + TimeUnit.MILLIS_PER_MINUTE);
	}
	
	// -------------------------------------------- //
	// ENABLE
	// -------------------------------------------- //
	
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
		
		// Setup RegistryType
		RegistryType.registerAll();
		
		// Activate
		this.activate(
			// Coll
			MultiverseColl.class,
			AspectColl.class,
			MassiveCoreMConfColl.class,
			MassiveCoreMSponsorInfoColl.class,
			
			// Nms
			NmsBasics.class,
			NmsBoard.class,
			NmsChat.class,
			NmsEntityGet.class,
			NmsItemStackCreate.class,
			NmsItemStackTooltip.class,
			NmsPermissions.class,
			NmsPlayerInventoryCreate.class,
			NmsSkullMeta.class,
			
			// Writer,
			WriterItemStack.class,
		
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
			BoardUtil.class,
			
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
	
	// -------------------------------------------- //
	// DISABLE
	// -------------------------------------------- //
	
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
