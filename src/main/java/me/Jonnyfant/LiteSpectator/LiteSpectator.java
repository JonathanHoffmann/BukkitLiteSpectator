package me.Jonnyfant.LiteSpectator;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class LiteSpectator extends JavaPlugin {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players");
            return false;
        }
        Player p = (Player) sender;
        //if(command.getName().equals("spectate"))
        //{
        if (p.getGameMode().equals(GameMode.SPECTATOR)) {
            File locationFile = new File(this.getDataFolder(), p.getUniqueId() + ".yml");
            if (locationFile.exists()) {
                YamlConfiguration locationConfig = YamlConfiguration.loadConfiguration(locationFile);
                Location location = p.getLocation();
                location.setX(locationConfig.getDouble("warpX"));
                location.setY(locationConfig.getDouble("warpY"));
                location.setZ(locationConfig.getDouble("warpZ"));
                location.setYaw((float) locationConfig.getDouble("warpYaw"));
                location.setPitch((float) locationConfig.getDouble("warpPitch"));
                location.setWorld(Bukkit.getWorld(locationConfig.getString("warpWorld")));
                p.teleport(location);
            } else {
                p.teleport(p.getBedSpawnLocation());
                p.sendMessage("Something went wrong and we can't find your previous location. Sending you to your bed spawn.");
            }
            p.setGameMode(GameMode.SURVIVAL);
            return true;
        } else if (p.getGameMode().equals(GameMode.SURVIVAL)) {
            File positionFile = new File(this.getDataFolder(), p.getUniqueId() + ".yml");
            YamlConfiguration positionSave = YamlConfiguration.loadConfiguration(positionFile);
            positionSave.set("warpX", p.getLocation().getX());
            positionSave.set("warpY", p.getLocation().getY());
            positionSave.set("warpZ", p.getLocation().getZ());
            positionSave.set("warpWorld", p.getLocation().getWorld().getName());
            positionSave.set("warpPitch", p.getLocation().getPitch());
            positionSave.set("warpYaw", p.getLocation().getYaw());
            try {
                positionSave.save(positionFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.setGameMode(GameMode.SPECTATOR);
            return true;
        } else {
            p.sendMessage("Something went wrong");
            return false;
        }
        /*}
        else
        {
            sender.sendMessage("Unknown Command");
            return false;
        }*/
    }
}
