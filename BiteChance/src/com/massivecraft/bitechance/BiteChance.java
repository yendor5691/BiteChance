package com.massivecraft.bitechance;


import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.skills.fishing.FishingManager;
import com.gmail.nossr50.util.Misc;
import com.gmail.nossr50.util.Permissions;
import com.gmail.nossr50.util.player.UserManager;
import com.massivecraft.mcore.MPlugin;

public class BiteChance extends MPlugin implements Listener 
{
    @Override
    public void onEnable(){
    	getServer().getPluginManager().registerEvents(this, this);
    	Bukkit.getPluginManager().registerEvents(this, this);
    	getLogger().info("BiteChance enabled!");
    }

public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
	if (cmd.getName().equalsIgnoreCase("bitechance")) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
		} else {
		    FishingManager fishingManager = UserManager.getPlayer(sender.getName()).getFishingManager();
			double biteChance = Math.min(0.01 * ((fishingManager.getSkillLevel() + 250.0 )/ 250.0), 0.06);
			DecimalFormat df = new DecimalFormat("0.00");
			String bc = "Bite Chance: "+df.format(biteChance);
			sender.sendMessage(bc);
		}
		return true;
	}
	return false;
}
@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
public void onPlayerFishHigh(PlayerFishEvent event){
    Player player = event.getPlayer();

    if (Misc.isNPCEntity(player) || !Permissions.skillEnabled(player, SkillType.FISHING)) {        
    	return;
    }

    FishingManager fishingManager = UserManager.getPlayer(player).getFishingManager();

    switch (event.getState()) {
        case FISHING:
            if (fishingManager.canMasterAngler()) {
                double biteChance = Math.min(0.01 * ((fishingManager.getSkillLevel() + 250.0 )/ 250.0), 0.06);
                event.getHook().setBiteChance(biteChance);
    		}
            return;

        default:
        	return;
    }
}

}
