
package berek;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Berek extends JavaPlugin implements Listener{

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents( this, this);
        this.getServer().getPluginManager().registerEvents(new Handler(), this);

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