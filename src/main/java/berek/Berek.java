
package berek;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Berek extends JavaPlugin implements Listener{

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents( this, this);
        this.getServer().getPluginManager().registerEvents(new Handler(), this);

        this.getCommand("berek").setExecutor(new CommandHandler());
        this.getCommand("berek").setTabCompleter(new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                return Arrays.asList("start", "stop", "timer");
            }
        });

        File f = new File ("plugins/Berek/data.yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);
        yamlFile.set("endTimer", 60);
        yamlFile.set("berek", "none");
        try {
            yamlFile.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {

    }
}
