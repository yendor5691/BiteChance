package com.massivecraft.bitechance;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.skills.fishing.FishingManager;
import com.gmail.nossr50.util.Misc;
import com.gmail.nossr50.util.Permissions;
import com.gmail.nossr50.util.player.UserManager;
import com.massivecraft.mcore.MPlugin;

public class BiteChance extends MPlugin
{

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static BiteChance i;

	public static BiteChance get()
	{
		return i;
	}

	public BiteChance()
	{
		BiteChance.i = this;
	}

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	// Commands
	private BiteChanceCmd bitechance;

	public BiteChanceCmd getbitechance()
	{
		return this.bitechance;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void onEnable()
	{
		if (!preEnable()) return;

		// Collections
		MConfColl.get().init();

		// Commands
		this.bitechance = new BiteChanceCmd();
		this.bitechance.register();

		postEnable();
	}

	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerFishHigh(PlayerFishEvent event)
	{
		Player player = event.getPlayer();

		// Must be a player with the mcMMO Fishing Skill enabled
		if (Misc.isNPCEntity(player)
				|| !Permissions.skillEnabled(player, SkillType.FISHING)) return;

		// Require FISHING (casting) event state
		if (event.getState() != State.FISHING) return;

		// Set the Bite Chance
		FishingManager fishingManager = UserManager.getPlayer(player)
				.getFishingManager();
		event.getHook().setBiteChance(biteCalc(fishingManager));
	}

	public static double biteCalc(FishingManager fishingManager)
	{
		return Math.min(0.01 * (fishingManager.getSkillLevel() / 250.0 + 1.0),
				0.05);
	}
}
