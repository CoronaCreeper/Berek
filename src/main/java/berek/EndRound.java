package berek;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.TimerTask;

public class EndRound extends TimerTask {
    @Override
    public void run() {
        File f = new File("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);
        if (!(yamlFile.getString("berek").equalsIgnoreCase("none"))) {
            if (yamlFile.getInt("endTimer") != 1) {

                if(yamlFile.getInt("endTimer")==5) {
                    last10();
                }

                if(yamlFile.getInt("endTimer")==10) {
                    last20();
                }

                if(yamlFile.getInt("endTimer")==15) {
                    last30();
                }

                int lastTime = yamlFile.getInt("endTimer") - 1;
                yamlFile.set("endTimer", lastTime);

                try {
                    yamlFile.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bukkit.getServer().getOnlinePlayers().forEach(pl ->
                        pl.spigot().sendMessage(
                                ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatColor.GREEN + "Do zakończenia gry pozostało " + ChatColor.RED + lastTime + " sekund!")));
            } else {
                try {
                    EndGame();
                    Bukkit.getServer().getOnlinePlayers().forEach(pl ->
                            pl.spigot().sendMessage(
                                    ChatMessageType.ACTION_BAR,
                                    new TextComponent(ChatColor.RED+">>Gra zakończona!<<")));
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Gra zakończona");
                }
            }
        }
    }

    public  void EndGame() {
        File f = new File("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);

        Player tokill = Bukkit.getPlayer(String.valueOf(yamlFile.get("berek")));
        Bukkit.broadcastMessage("§6[Berek]" + ChatColor.GREEN + " Gra zakończona! " + ChatColor.RED + tokill.getName() + ChatColor.GREEN + " wyeliminowany!");

        Bukkit.getOnlinePlayers().forEach(all ->
                all.removePotionEffect(PotionEffectType.GLOWING));
        Bukkit.getOnlinePlayers().forEach(all ->
                all.getInventory().clear());
        yamlFile.set("berek", "none");
        yamlFile.set("endTimer", 60);
        try {
            yamlFile.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tokill.setHealth(0);
    }


    public void last10() {
        File f = new File("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);
        Bukkit.getOnlinePlayers().forEach(all ->
                all.sendTitle(ChatColor.RED+"5", ChatColor.RED+"SEKUND DO KOŃCA")
        );

        Bukkit.getOnlinePlayers().forEach(all ->
                all.playSound(all.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100F, 100F)
        );
    }

    private void last20() {
        File f = new File("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);
        Bukkit.getOnlinePlayers().forEach(all ->
                all.sendTitle(ChatColor.GOLD+"10", ChatColor.GOLD+"SEKUND DO KOŃCA")
        );

        Bukkit.getOnlinePlayers().forEach(all ->
                all.playSound(all.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100F, 100F)
        );
    }

    public void last30() {
        File f = new File("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);
        Bukkit.getOnlinePlayers().forEach(all ->
                all.sendTitle(ChatColor.YELLOW+"15", ChatColor.YELLOW+"SEKUND DO KOŃCA")
        );

        Bukkit.getOnlinePlayers().forEach(all ->
                all.playSound(all.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100F, 100F)
        );
    }
}
