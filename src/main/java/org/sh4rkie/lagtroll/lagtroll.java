package org.sh4rkie.lagtroll;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class LagTroll extends JavaPlugin implements Listener, CommandExecutor {

    private Map<String, ArmorStand[]> playerArmorStands = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Using /" + cmd.getName() + " <Player-Name>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player with this name was not found.");
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("troll")) {
            spawnTrollArmorStands(target);
        } else if (cmd.getName().equalsIgnoreCase("detroll")) {
            removeTrollArmorStands(target);
        }

        return true;
    }

    private void spawnTrollArmorStands(Player target) {
        removeTrollArmorStands(target);

        ArmorStand[] armorStands = new ArmorStand[1000];
        for (int i = 0; i < 1000; i++) {
            ArmorStand armorStand = target.getWorld().spawn(target.getLocation().add(new Vector(Math.random() * 2 - 1, -5, Math.random() * 2 - 1)), ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCustomName("TrollArmorStand");
            armorStand.setCustomNameVisible(false);
            armorStands[i] = armorStand;
        }

        playerArmorStands.put(target.getName(), armorStands);
    }

    private void removeTrollArmorStands(Player target) {
        ArmorStand[] armorStands = playerArmorStands.get(target.getName());
        if (armorStands != null) {
            for (ArmorStand armorStand : armorStands) {
                if (armorStand != null) {
                    armorStand.remove();
                }
            }
            playerArmorStands.remove(target.getName());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            event.setCancelled(true);
        }
    }
}