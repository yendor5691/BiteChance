package com.massivecraft.bitechance;

import java.text.DecimalFormat;
import java.util.List;

import com.gmail.nossr50.skills.fishing.FishingManager;
import com.gmail.nossr50.util.player.UserManager;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class BiteChanceCmd extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public BiteChanceCmd()
	{
		// Requirements
		this.addRequirements(ReqIsPlayer.get());
		this.setErrorOnToManyArgs(false);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesBiteChance;
	}

	@Override
	public void perform()
	{
		FishingManager fishingManager = UserManager.getPlayer(sender.getName())
				.getFishingManager();
		DecimalFormat df = new DecimalFormat("0.00");
		String bc = "Bite Chance: "
				+ df.format(100.0 * BiteChance.biteCalc(fishingManager))
				+ "% per tick";
		sender.sendMessage(bc);
	}
}
