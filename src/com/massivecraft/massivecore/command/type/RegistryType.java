package com.massivecraft.massivecore.command.type;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.massivecraft.massivecore.collections.ExceptionSet;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.editor.annotation.EditorTypeList;
import com.massivecraft.massivecore.command.editor.annotation.EditorTypeMap;
import com.massivecraft.massivecore.command.editor.annotation.EditorTypeSet;
import com.massivecraft.massivecore.command.type.combined.TypeDataBannerPattern;
import com.massivecraft.massivecore.command.type.combined.TypeDataPotionEffect;
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
import com.massivecraft.massivecore.command.type.enumeration.TypeFireworkEffectType;
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
import com.massivecraft.massivecore.util.ReflectionUtil;

public class RegistryType
{
	// -------------------------------------------- //
	// REGISTRY
	// -------------------------------------------- //
	
	private static final Map<Class<?>, Type<?>> registry = new MassiveMap<>();
	public static <T> void register(Class<T> clazz, Type<? super T> type)
	{
		if (clazz == null) throw new NullPointerException("clazz");
		if (type == null) throw new NullPointerException("type");
		registry.put(clazz, type);
	}
	public static <T> void register(Type<T> type)
	{
		if (type == null) throw new NullPointerException("type");
		register(type.getClazz(), type);
	}
	@SuppressWarnings("unchecked")
	public static <T> Type<? super T> unregister(Class<T> clazz)
	{
		if (clazz == null) throw new NullPointerException("clazz");
		return (Type<T>) registry.remove(clazz);
	}
	public static boolean isRegistered(Class<?> clazz)
	{
		if (clazz == null) throw new NullPointerException("clazz");
		return registry.containsKey(clazz);
	}
	
	public static Type<?> getType(Field field)
	{
		EditorType annotation = field.getAnnotation(EditorType.class);
		if (annotation != null)
		{
			Class<?> clazz = annotation.value();
			if (clazz == void.class) clazz = getType(field.getGenericType()).getClass();
			return getType(clazz, annotation.fieldName());
		}
		
		EditorTypeList annList = field.getAnnotation(EditorTypeList.class);
		if (annList != null)
		{
			return TypeList.get(getType(annList.value(), annList.fieldName()));
		}
		
		EditorTypeSet annSet = field.getAnnotation(EditorTypeSet.class);
		if (annSet != null)
		{
			return TypeSet.get(getType(annSet.value(), annSet.fieldName()));
		}
		
		EditorTypeMap annMap = field.getAnnotation(EditorTypeMap.class);
		if (annMap != null)
		{
			return TypeMap.get(getType(annMap.typeKey(), annMap.fieldNameKey()), getType(annMap.typeValue(), annMap.fieldNameValue()));
		}
		return getType(field.getGenericType());
	}
	private static Type<?> getType(Class<?> clazz, String fieldName)
	{
		return ReflectionUtil.getField(clazz, fieldName, null);
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
		
		register(TypeString.get());
		
		// Enum
		register(TypeBiome.get());
		register(TypeChatColor.get());
		register(TypeDifficulty.get());
		register(TypeDyeColor.get());
		register(TypeEntityType.get());
		register(TypeEnvironment.get());
		register(TypeEventPriority.get());
		register(TypeFireworkEffectType.get());
		register(TypeGameMode.get());
		register(TypeHorseColor.get());
		register(TypeHorseStyle.get());
		register(TypeHorseVariant.get());
		register(TypeMaterial.get());
		register(TypeOcelotType.get());
		register(TypeParticleEffect.get());
		
		// About 15% of all servers are still using 1.7.x.
		// We catch NoClassDefFoundError and silently move along on those servers.
		try
		{
			register(TypeRabbitType.get());
		}
		catch (Throwable t)
		{
			
		}
		
		register(TypeSkeletonType.get());
		register(TypeSound.get());
		register(TypeVillagerProfession.get());
		register(TypeWorldType.get());
		
		// Bukkit
		register(TypeDestination.get());
		register(TypeItemStack.get());
		register(TypeDataBannerPattern.get());
		register(TypeDataPotionEffect.get());
		register(TypeDataFireworkEffect.get());
		register(TypeDataItemStack.get());
		register(TypePermission.get());
		register(TypePotionEffectType.get());
		register(TypePS.get());
		register(TypeWorld.get());
		register(TypePotionEffectWrap.get());
		register(TypeSoundEffect.get());
		
		// Sender
		register(TypePlayer.get());
		register(TypeSender.get());
		
		// Store
		register(TypeAspect.get());
		register(TypeMultiverse.get());
		
		// Collection
		register(WorldExceptionSet.class, TypeExceptionSet.get(TypeWorld.get()));
	}
	
}
