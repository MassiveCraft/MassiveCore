package com.massivecraft.massivecore.command.type;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.PotionEffectWrap;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.collections.ExceptionSet;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.type.combined.TypeEntry;
import com.massivecraft.massivecore.command.type.combined.TypePotionEffectWrap;
import com.massivecraft.massivecore.command.type.combined.TypeSoundEffect;
import com.massivecraft.massivecore.command.type.container.TypeExceptionSet;
import com.massivecraft.massivecore.command.type.container.TypeList;
import com.massivecraft.massivecore.command.type.container.TypeMap;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.command.type.enumeration.TypeBiome;
import com.massivecraft.massivecore.command.type.enumeration.TypeChatColor;
import com.massivecraft.massivecore.command.type.enumeration.TypeDifficulty;
import com.massivecraft.massivecore.command.type.enumeration.TypeDyeColor;
import com.massivecraft.massivecore.command.type.enumeration.TypeEntityType;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnvironment;
import com.massivecraft.massivecore.command.type.enumeration.TypeEventPriority;
import com.massivecraft.massivecore.command.type.enumeration.TypeGameMode;
import com.massivecraft.massivecore.command.type.enumeration.TypeHorseColor;
import com.massivecraft.massivecore.command.type.enumeration.TypeHorseStyle;
import com.massivecraft.massivecore.command.type.enumeration.TypeHorseVariant;
import com.massivecraft.massivecore.command.type.enumeration.TypeMaterial;
import com.massivecraft.massivecore.command.type.enumeration.TypeOcelotType;
import com.massivecraft.massivecore.command.type.enumeration.TypeParticleEffect;
import com.massivecraft.massivecore.command.type.enumeration.TypeRabbitType;
import com.massivecraft.massivecore.command.type.enumeration.TypeSkeletonType;
import com.massivecraft.massivecore.command.type.enumeration.TypeSound;
import com.massivecraft.massivecore.command.type.enumeration.TypeVillagerProfession;
import com.massivecraft.massivecore.command.type.enumeration.TypeWorldType;
import com.massivecraft.massivecore.command.type.primitive.TypeBoolean;
import com.massivecraft.massivecore.command.type.primitive.TypeByte;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import com.massivecraft.massivecore.command.type.primitive.TypeFloat;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.primitive.TypeLong;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import com.massivecraft.massivecore.command.type.sender.TypeSender;
import com.massivecraft.massivecore.command.type.store.TypeAspect;
import com.massivecraft.massivecore.command.type.store.TypeMultiverse;
import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.teleport.Destination;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class RegistryType
{
	// -------------------------------------------- //
	// REGISTRY
	// -------------------------------------------- //
	
	private static final Map<Class<?>, Type<?>> registry = new MassiveMap<>();
	public static <T> void register(Class<T> clazz, Type<? super T> type) { registry.put(clazz, type); }
	@SuppressWarnings("unchecked") public static <T> Type<? super T> unregister(Class<T> clazz) { return (Type<T>) registry.remove(clazz); }
	public static boolean isRegistered(Class<?> clazz) { return registry.containsKey(clazz); }
	
	public static Type<?> getType(Field field)
	{
		EditorType annotation = field.getAnnotation(EditorType.class);
		if (annotation != null)
		{
			Class<?> clazz = annotation.value();
			if (clazz == void.class) clazz = getType(field.getGenericType()).getClass();
			return ReflectionUtil.getField(clazz, annotation.fieldName(), null);
		}
		
		return getType(field.getGenericType());
	}
	
	public static Type<?> getType(java.lang.reflect.Type reflectType)
	{
		if (reflectType instanceof Class)
		{
			Type<?> type = registry.get(reflectType);
			if (type == null) throw new IllegalStateException(reflectType + " is not registered.");
			return type;
		}
		
		if (reflectType instanceof ParameterizedType)
		{
			ParameterizedType paramType = (ParameterizedType) reflectType;
			Class<?> parent = (Class<?>) paramType.getRawType();
			
			if (Map.class.isAssignableFrom(parent))
			{
				TypeEntry<?, ?> typeEntry = TypeEntry.get(getType(paramType.getActualTypeArguments()[0]), getType(paramType.getActualTypeArguments()[1]));
				return TypeMap.get(typeEntry);
			}
			
			if (List.class.isAssignableFrom(parent))
			{
				return TypeList.get(getType(paramType.getActualTypeArguments()[0]));
			}
			
			if (Set.class.isAssignableFrom(parent))
			{
				return TypeSet.get(getType(paramType.getActualTypeArguments()[0]));
			}
			
			if (Entry.class.isAssignableFrom(parent))
			{
				return TypeEntry.get(getType(paramType.getActualTypeArguments()[0]), getType(paramType.getActualTypeArguments()[1]));
			}
			
			if (ExceptionSet.class.isAssignableFrom(parent))
			{
				return TypeExceptionSet.get(getType(paramType.getActualTypeArguments()[0]));
			}
		}
		
		throw new IllegalArgumentException("Unknown type: " + reflectType);
	}
	
	// -------------------------------------------- //
	// DEFAULTS
	// -------------------------------------------- //
	
	static
	{
		registerAll();
	}
	
	public static void registerAll()
	{
		// Primitive
		register(Boolean.TYPE, TypeBoolean.getTrue());
		register(Boolean.class, TypeBoolean.getTrue());
		
		register(Byte.TYPE, TypeByte.get());
		register(Byte.class, TypeByte.get());
		
		register(Double.TYPE, TypeDouble.get());
		register(Double.class, TypeDouble.get());
		
		register(Float.TYPE, TypeFloat.get());
		register(Float.class, TypeFloat.get());
		
		register(Integer.TYPE, TypeInteger.get());
		register(Integer.class, TypeInteger.get());
		
		register(Long.TYPE, TypeLong.get());
		register(Long.class, TypeLong.get());
		
		register(String.class, TypeString.get());
		
		// Bukkit
		register(Destination.class, TypeDestination.get());
		register(ItemStack.class, TypeItemStack.get());
		register(Permission.class, TypePermission.get());
		register(PotionEffectType.class, TypePotionEffectType.get());
		register(PS.class, TypePS.get());
		register(World.class, TypeWorld.get());
		register(PotionEffectWrap.class, TypePotionEffectWrap.get());
		register(SoundEffect.class, TypeSoundEffect.get());
		
		// Enum
		register(Biome.class, TypeBiome.get());
		register(ChatColor.class, TypeChatColor.get());
		register(Difficulty.class, TypeDifficulty.get());
		register(DyeColor.class, TypeDyeColor.get());
		register(EntityType.class, TypeEntityType.get());
		register(Environment.class, TypeEnvironment.get());
		register(EventPriority.class, TypeEventPriority.get());
		register(GameMode.class, TypeGameMode.get());
		register(Horse.Color.class, TypeHorseColor.get());
		register(Horse.Style.class, TypeHorseStyle.get());
		register(Horse.Variant.class, TypeHorseVariant.get());
		register(Material.class, TypeMaterial.get());
		register(Ocelot.Type.class, TypeOcelotType.get());
		register(ParticleEffect.class, TypeParticleEffect.get());
		
		// About 15% of all servers are still using 1.7.x.
		// We catch NoClassDefFoundError and silently move along on those servers.
		try
		{
			register(org.bukkit.entity.Rabbit.Type.class, TypeRabbitType.get());
		}
		catch (Throwable t)
		{
			
		}
		
		register(SkeletonType.class, TypeSkeletonType.get());
		register(Sound.class, TypeSound.get());
		register(Profession.class, TypeVillagerProfession.get());
		register(WorldType.class, TypeWorldType.get());
		
		// Sender
		register(Player.class, TypePlayer.get());
		register(CommandSender.class, TypeSender.get());
		
		// Store
		register(Aspect.class, TypeAspect.get());
		register(Multiverse.class, TypeMultiverse.get());
		
		// Collection
		register(WorldExceptionSet.class, TypeExceptionSet.get(TypeWorld.get()));
	}
	
}
