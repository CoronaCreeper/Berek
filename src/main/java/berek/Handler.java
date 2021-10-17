package berek;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;

public class Handler implements Listener {

    @EventHandler
    public void nextgames(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        File f = new File ("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);

        if(!(yamlFile.get("berek").toString().equalsIgnoreCase("none"))) {
            final Player attacker = (Player) event.getDamager();
            final Player victim = (Player) event.getEntity();
            if(yamlFile.get("berek").toString().equalsIgnoreCase(attacker.getName())) {

                Bukkit.broadcastMessage("§6[Berek] " + ChatColor.GREEN + victim.getName() + " jest " + ChatColor.RED + "berkiem!");

                attacker.getInventory().setBoots(new ItemStack((Material.AIR)));
                attacker.getInventory().setHelmet(new ItemStack((Material.AIR)));
                attacker.getInventory().setChestplate(new ItemStack((Material.AIR)));
                attacker.getInventory().setLeggings(new ItemStack((Material.AIR)));

                attacker.removePotionEffect(PotionEffectType.BLINDNESS);


                victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 100));
                victim.sendTitle(ChatColor.GREEN + "Jesteś " + ChatColor.RED + "BERKIEM!", ChatColor.GREEN + "Złap kogoś zanim minie "+yamlFile.getInt("endTimer")+" sekund!");
                victim.playSound(victim.getLocation(), Sound.BLOCK_ANVIL_PLACE, 100.0f, 1.0f);
                victim.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                victim.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                victim.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                victim.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));

                yamlFile.set("berek", victim.getName());
                try {
                    yamlFile.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateScoreBoard();
            }
        }
    }

    public void updateScoreBoard() {
        File f = new File("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("Stats", "dummy");
        objective.setDisplayName(ChatColor.LIGHT_PURPLE + "BEREK");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);


        Score Berek = objective.getScore(ChatColor.GOLD + yamlFile.getString("berek"));
        Berek.setScore(1);

        Bukkit.getOnlinePlayers().forEach(all->
                all.setScoreboard(board));
    }
}
