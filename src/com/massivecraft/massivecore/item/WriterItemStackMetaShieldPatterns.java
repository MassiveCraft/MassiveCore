package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.collections.MassiveList;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.List;

public class WriterItemStackMetaShieldPatterns extends WriterAbstractItemStackMetaField<BlockStateMeta, List<DataBannerPattern>, List<Pattern>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaShieldPatterns i = new WriterItemStackMetaShieldPatterns();
	public static WriterItemStackMetaShieldPatterns get() { return i; }
	
	public WriterItemStackMetaShieldPatterns()
	{
		super(BlockStateMeta.class);
		this.setMaterial(Material.SHIELD);
		this.setConverterTo(ConverterToBannerPatterns.get());
		this.setConverterFrom(ConverterFromBannerPatterns.get());
		this.addDependencyClasses(
			WriterBannerPattern.class
		);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public List<DataBannerPattern> getA(DataItemStack ca, ItemStack d)
	{
		return ca.getBannerPatterns();
	}
	
	@Override
	public void setA(DataItemStack ca, List<DataBannerPattern> fa, ItemStack d)
	{
		ca.setBannerPatterns(fa);
	}
	
	@Override
	public List<Pattern> getB(BlockStateMeta cb, ItemStack d)
	{
		// Get
		boolean creative = false;
		Banner banner = getBanner(cb, creative);
		if (banner == null) return null;
		
		// Return
		try {
			return banner.getPatterns();
		} catch (NullPointerException e) {
			return new MassiveList<>();
		}
	}
	
	@Override
	public void setB(BlockStateMeta cb, List<Pattern> fb, ItemStack d)
	{
		// Get
		boolean creative = (fb != null && !fb.isEmpty());
		Banner banner = getBanner(cb, creative);
		if (banner == null) return;
		
		// Change
		banner.setPatterns(fb);
		
		// Ensure Base
		if (banner.getBaseColor() == null && !banner.getPatterns().isEmpty())
		{
			banner.setBaseColor(DyeColor.BLACK);
		}
		
		// Set
		setBanner(cb, banner);
	}
	
}
