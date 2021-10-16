package berek;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.TimedRegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Handler implements Listener {


    @EventHandler
    public void launcher(EntityDamageByEntityEvent event) throws InterruptedException, IOException {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        File f = new File ("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);

        if(yamlFile.get("berek").toString().equalsIgnoreCase("none")) {

            final Player random = (Player) Bukkit.getOnlinePlayers().toArray()[new Random().nextInt(Bukkit.getOnlinePlayers().size())];

            Bukkit.broadcastMessage("§6[Berek] " + ChatColor.GREEN + "Losuję berka...");
            Bukkit.broadcastMessage("§6[Berek] " + ChatColor.GREEN + random.getName() + " jest " + ChatColor.RED + "berkiem!");
            random.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 100));
            random.sendTitle(ChatColor.GREEN + "Jesteś " + ChatColor.RED + "BERKIEM!", ChatColor.GREEN + "Złap kogoś zanim miną 3 minuty!");
            random.playSound(random.getLocation(), Sound.BLOCK_ANVIL_PLACE, 100.0f, 1.0f);
            random.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
            random.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            random.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            random.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));

            yamlFile.set("berek", random.getName());
            yamlFile.save(f);

            Timer time = new Timer();
            time.schedule(new EndRound(), 0, 1000);


        }
    }


    @EventHandler
    public void nextgames(EntityDamageByEntityEvent event) throws InterruptedException, IOException {
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
                victim.sendTitle(ChatColor.GREEN + "Jesteś " + ChatColor.RED + "BERKIEM!", ChatColor.GREEN + "Złap kogoś zanim miną 3 minuty!");
                victim.playSound(victim.getLocation(), Sound.BLOCK_ANVIL_PLACE, 100.0f, 1.0f);
                victim.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                victim.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                victim.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                victim.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));

                yamlFile.set("berek", victim.getName());
                yamlFile.save(f);
            }
        }
    }
}
