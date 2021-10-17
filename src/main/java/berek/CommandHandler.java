package berek;

import jdk.jfr.Frequency;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (!(p.isOp())) {
            p.sendMessage(ChatColor.RED + "Nie masz uprawnień!");
        } else {

            if (args.length < 1) {
                p.sendMessage(ChatColor.RED + "Niewłaściwy argument. Poprawne argumenty:\n/berek start\n/berek stop\n/berek timer");
            } else {
                File f = new File("plugins/Berek/data.yml");
                YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);

                switch (args[0]) {
                    case "start": //rozpoczyna grę

                        if (yamlFile.get("berek").toString().equalsIgnoreCase("none")) {

                            final Player random = (Player) Bukkit.getOnlinePlayers().toArray()[new Random().nextInt(Bukkit.getOnlinePlayers().size())];
                            if (random.getGameMode().equals(GameMode.SPECTATOR)) {
                                p.sendMessage(ChatColor.RED + "Wystąpił błąd. Spróbuj jeszcze raz");
                            } else {
                                Bukkit.broadcastMessage("§6[Berek] " + ChatColor.GREEN + "Losuję berka...");
                                Bukkit.broadcastMessage("§6[Berek] " + ChatColor.GREEN + random.getName() + " jest " + ChatColor.RED + "berkiem!");
                                random.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 100));
                                random.sendTitle(ChatColor.GREEN + "Jesteś " + ChatColor.RED + "BERKIEM!", ChatColor.GREEN + "Złap kogoś zanim minie " + yamlFile.getInt("endTimer") + " sekund!");
                                random.playSound(random.getLocation(), Sound.BLOCK_ANVIL_PLACE, 100.0f, 1.0f);
                                random.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                                random.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                                random.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                                random.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));

                                yamlFile.set("berek", random.getName());
                                try {
                                    yamlFile.save(f);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                updateScoreBoard();
                                Timer time = new Timer();
                                time.schedule(new EndRound(), 0, 1000);
                            }
                        }
                        break;

                    case "stop": //Zatrzymuje grę
                        yamlFile.set("endTimer", 1);
                        try {
                            yamlFile.save(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "timer": //zmienia czas gry
                        if(args[1]==null) {
                            p.sendMessage(ChatColor.RED+"Potrzeba jescze jednego argumentu!");
                        } else {
                            int time = Integer.parseInt(args[1]);
                            yamlFile.set("endTimer", time);
                            try {
                                yamlFile.save(f);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            p.sendMessage(ChatColor.GREEN + "Ustawiono czas zakończenia gry na " + time + " sekund");
                            Bukkit.broadcastMessage(ChatColor.GRAY+"Ustawiono czas zakończenia gry na "+ time + " sekund");
                        }
                        break;

                    case "reboot":
                        f.delete();
                        Bukkit.reload();
                        break;
                    default:
                        p.sendMessage(ChatColor.RED + "Niewłaściwy argument. Poprawne argumenty:\n/berek start\n/berek stop\n/berek timer");
                        break;
                }
            }
        }
        return false;
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
