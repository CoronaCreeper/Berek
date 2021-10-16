package berek;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.TimerTask;

public class EndRound extends TimerTask {
    @Override
    public void run() {
        File f = new File ("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);
        if(yamlFile.getInt("endTimer")!=1) {
            int lastTime = yamlFile.getInt("endTimer")-1;
            yamlFile.set("endTimer", lastTime);

            try {
                yamlFile.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bukkit.getServer().getOnlinePlayers().forEach(pl ->
                   pl.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(ChatColor.GREEN+"Do zakończenia gry pozostało "+ChatColor.RED+lastTime+" sekund!")));
        }else {
            EndGame();
        }
    }

    public void EndGame() {
        File f = new File ("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);

        Player tokill = Bukkit.getPlayer(String.valueOf(yamlFile.get("berek")));
        Bukkit.broadcastMessage("§6[Berek]" + ChatColor.GREEN+ " Gra zakończona! " + ChatColor.RED + tokill.getName()+ChatColor.GREEN+" wyeliminowany!");
        yamlFile.set("berek", "none");
        try {
            yamlFile.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
