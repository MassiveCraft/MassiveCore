package com.massivecraft.mcore1.text;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

// TODO: Add some more cool stuff.. like titelization design.
// TODO: Should they be color-parsed? or cashed color parsed?

public class TxtDesign
{	
	public TxtDesign()
	{
		
	}
	
	// -------------------------------------------- //
	// COLOR THEME
	// -------------------------------------------- //
	private ChatColor logo = ChatColor.DARK_GREEN;
	public ChatColor getColorLogo() { return this.logo; }
	public void setColorLogo(ChatColor val) { this.logo = val; this.updateTag2Color(); }
	
	private ChatColor art = ChatColor.GOLD;
	public ChatColor getColorArt() { return this.art; }
	public void setColorArt(ChatColor val) { this.art = val; this.updateTag2Color(); }
	
	private ChatColor notice = ChatColor.GRAY;
	public ChatColor getColorNotice() { return this.notice; }
	public void setColorNotice(ChatColor val) { this.notice = val; this.updateTag2Color(); }
	
	private ChatColor info = ChatColor.YELLOW;
	public ChatColor getColorInfo() { return this.info; }
	public void setColorInfo(ChatColor val) { this.info = val; this.updateTag2Color(); }
	
	private ChatColor good = ChatColor.GREEN;
	public ChatColor getColorGood() { return this.good; }
	public void setColorGood(ChatColor val) { this.good = val; this.updateTag2Color(); }
	
	private ChatColor bad = ChatColor.RED;
	public ChatColor getColorBad() { return this.bad; }
	public void setColorBad(ChatColor val) { this.bad = val; this.updateTag2Color(); }
	
	private ChatColor hightlight = ChatColor.LIGHT_PURPLE;
	public ChatColor getColorHighlight() { return this.hightlight; }
	public void setColorHightlight(ChatColor val) { this.hightlight = val; this.updateTag2Color(); }
	
	private ChatColor command = ChatColor.AQUA;
	public ChatColor getColorCommand() { return this.command; }
	public void setColorCommand(ChatColor val) { this.command = val; this.updateTag2Color(); }
	
	private ChatColor parameter = ChatColor.DARK_AQUA;
	public ChatColor getColorParameter() { return this.parameter; }
	public void setColorParameter(ChatColor val) { this.parameter = val; this.updateTag2Color(); }
	
	private Map<String, String> tag2color = null;
	public Map<String, String> getTags()
	{
		if (tag2color == null)
		{
			this.updateTag2Color();
		}
		return this.tag2color;
	}
	
	private void updateTag2Color()
	{
		if (tag2color == null)
		{
			this.tag2color = new HashMap<String, String>();
		}
		this.tag2color.put("l", this.getColorLogo().toString());
		this.tag2color.put("logo", this.getColorLogo().toString());
		
		this.tag2color.put("a", this.getColorArt().toString());
		this.tag2color.put("art", this.getColorArt().toString());
		
		this.tag2color.put("n", this.getColorNotice().toString());
		this.tag2color.put("notice", this.getColorNotice().toString());
		
		this.tag2color.put("i", this.getColorInfo().toString());
		this.tag2color.put("info", this.getColorInfo().toString());
		
		this.tag2color.put("g", this.getColorGood().toString());
		this.tag2color.put("good", this.getColorGood().toString());
		
		this.tag2color.put("b", this.getColorBad().toString());
		this.tag2color.put("bad", this.getColorBad().toString());
		
		this.tag2color.put("h", this.getColorHighlight().toString());
		this.tag2color.put("highlight", this.getColorHighlight().toString());
		
		this.tag2color.put("c", this.getColorCommand().toString());
		this.tag2color.put("command", this.getColorCommand().toString());
		
		this.tag2color.put("p", this.getColorParameter().toString());
		this.tag2color.put("parameter", this.getColorParameter().toString());
	}
}
