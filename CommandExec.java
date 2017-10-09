package hash.cache.deathtag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import hash.cache.deathtag.Main;
import net.md_5.bungee.api.ChatColor;

public class CommandExec implements Listener, CommandExecutor {

	private Main plugin;

	public CommandExec(Main beep) {
		this.plugin = beep;
	}

	public void getNewIt(){
		HashSet<Player> listToSet = new HashSet<Player>(this.plugin.pInGame);
		ArrayList<Player> pInGameNoDup = new ArrayList<Player>(listToSet);
		if (pInGameNoDup.size() > 2) {
		for (Player p : pInGameNoDup) {
			p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + plugin.it.getName() + " has quit!");
			p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + "Chosing new player to be it randomly now...");
		}
		Player quitter = this.plugin.it;
		this.plugin.pInGame.removeAll(Collections.singleton(quitter));
		randomIt();
		for (Player p : pInGameNoDup) {
			p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + plugin.it.getName() + " is now it!");
		}
		} if (pInGameNoDup.size() <= 2) {
			Player quitter = this.plugin.it;
			this.plugin.pInGame.removeAll(Collections.singleton(quitter));
			Player winner = pInGameNoDup.get(0);
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + plugin.it.getName() + " has quit!");
				p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + winner.getName() + " has won the game of DeathTag!");
			}
			setDefault();
		}
	}
	
	int time1;
	
	public void gameTimer() {
		time1 = plugin.getConfig().getInt("RoundTime");
		HashSet<Player> listToSet = new HashSet<Player>(this.plugin.pInGame);
		ArrayList<Player> pInGameNoDup = new ArrayList<Player>(listToSet);
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			public void run() {
				if (time1 != -1 && plugin.gActive == true) {
					if (time1 != 0) {
						if (time1 % 5 == 0 || time1 == 1 || time1 == 2 || time1 == 3) {
							for (Player p : pInGameNoDup) {
								p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + time1 + "s left in the round!");
							}
							time1--;
						} else {
							time1--;
						}
					} else {
						startKilling();
					}
				}

			}

		}, 0L, 20L);
	}

	public void randomIt() {
		HashSet<Player> listToSet = new HashSet<Player>(this.plugin.pInGame);
		ArrayList<Player> pInGameNoDup = new ArrayList<Player>(listToSet);
		Random random = new Random();
		this.plugin.it = pInGameNoDup.get(random.nextInt(pInGameNoDup.size()));
	}

	public void setDefault() {
		this.plugin.wActive = false;
		this.plugin.gActive = false;
		this.plugin.it = null;
		this.plugin.wasIt = null;
		this.plugin.pInGame.clear();
	}

	public void startKilling() {
		HashSet<Player> listToSet = new HashSet<Player>(this.plugin.pInGame);
		ArrayList<Player> pInGameNoDup = new ArrayList<Player>(listToSet);
		int sizeCheck = pInGameNoDup.size();
		Player kenny = null;
		if (sizeCheck > 2) {
			kenny = this.plugin.it;
			this.plugin.pInGame.removeAll(Collections.singleton(kenny));
			kenny.setHealth(0.0D); // Oh my god! They killed Kenny!
									// You Bastards!
			for (Player p : pInGameNoDup) {
				p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + kenny.getName() + " has been killed. Chosing next player to be it...");
			}
			randomIt();
			for (Player p : pInGameNoDup) {
				p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + this.plugin.it + " is now it.");
			}
			gameTimer();
		}
		if (sizeCheck == 2) {
			kenny = this.plugin.it;
			this.plugin.pInGame.removeAll(Collections.singleton(kenny));
			kenny.setHealth(0.0D);
			for (Player p : pInGameNoDup) {
				p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + kenny.getName() + " has been killed.");
			}
			Player winner = pInGameNoDup.get(0);
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + winner.getName() + " has won the game of DeathTag!");
			}
			setDefault();
		}
	}

	int time0 = 30;

	@SuppressWarnings("deprecation")
	public void startTimer() {
		plugin.gActive = true;
		plugin.wActive = true;
		time0 = 30;
		Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {

			public void run() {

				if (time0 != -1 && plugin.wActive == true) {
					if (time0 != 0) {
						if (time0 == 1 || time0 == 2 || time0 == 3 || time0 == 10 || time0 == 20 || time0 == 30)
							for (Player p : Bukkit.getOnlinePlayers()) {
								if (p.hasPermission("tag.join")) {
									p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + "The game of DeathTag will begin in " + time0 + "s");
								}
							}
						time0--;
					} else {

						plugin.wActive = false;
						checkStart();

						time0--;
					}
				}

			}

		}, 0L, 20L);
	}

	public void checkStart() {
		HashSet<Player> listToSet = new HashSet<Player>(this.plugin.pInGame);
		ArrayList<Player> pInGameNoDup = new ArrayList<Player>(listToSet);

		int sizeCheck = pInGameNoDup.size();
		if (sizeCheck >= 2) {
			randomIt();
			for (Player p : pInGameNoDup) {
				if (pInGameNoDup.contains(p)) {
					p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + "The Game of DeathTag has begun! Now chosing who will be it...");
					p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + this.plugin.it.getName() + " has been randomly chosen to be it!");
				}
			}
			gameTimer();
		} else {
			setDefault();
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("tag.join"))
					p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + "Not Enough People have joined, the game will not start");
			}
		}
	}

	public void startWindow() {
		this.plugin.getServer().broadcastMessage("To join the game of DeathTag do /tag join");
		this.plugin.getServer().broadcastMessage("The game will begin in 30s!");
		this.plugin.wActive = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("tag") && args.length == 0) {

			if (p.hasPermission("tag.admin") || p.isOp()) {
				p.sendMessage(ChatColor.YELLOW + "DeathTag Minigame Pluign Made by HashCache v1.0");
				p.sendMessage(ChatColor.YELLOW + "Usage: /tag <join | leave>");
				p.sendMessage(ChatColor.BLUE + "Admin Options:");
				p.sendMessage(ChatColor.BLUE + "Usage: /tag <start | stop>");
				return true;
			}
			if (p.hasPermission("tag") || p.isOp()) {
				p.sendMessage(ChatColor.YELLOW + "DeathTag Minigame Pluign Made by HashCache v1.0");
				p.sendMessage(ChatColor.YELLOW + "Usage: /tag <join | leave>");
				return true;
			}

		} else if (cmd.getName().equalsIgnoreCase("tag") && args[0].equalsIgnoreCase("start")) {
			if (p.hasPermission("tag.admin") || p.isOp()) {
				if (plugin.gActive == false) {
					this.plugin.getServer().broadcastMessage(
							this.plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + ChatColor.GREEN + "A game of DeathTag is about to start!");
					startWindow();
					startTimer();
					return true;
				} else {
					p.sendMessage(this.plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + ChatColor.RED + "A game has already been started!");
					return true;
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("tag") && args[0].equalsIgnoreCase("stop")) {
			if (sender.hasPermission("tag.admin") || p.isOp()) {
				if (this.plugin.gActive == true) {
					setDefault();
					this.plugin.getServer().broadcastMessage(
							this.plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + ChatColor.GREEN + "The game has ended been ended by an Admin!");
					return true;
				} else {
					p.sendMessage(this.plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + ChatColor.RED + "There is no active game!");
					return true;
				}
			} else {
				p.sendMessage(this.plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + ChatColor.RED + "You do not have permission to do that!");
				return false;
			}
		} else if (cmd.getName().equalsIgnoreCase("tag") && args[0].equalsIgnoreCase("join")) {
			if (p.hasPermission("tag.join") || p.isOp()) {
				if (this.plugin.wActive == true) {
					this.plugin.pInGame.add(p);
					p.sendMessage(this.plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + ChatColor.GREEN + "You have joined the game.");
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("tag") && args[0].equalsIgnoreCase("reload")) {
			if (p.hasPermission("tag.reload")) {
				plugin.reloadConfig();
				p.sendMessage(plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + "reload complete!");
				return true;
			} else {
				p.sendMessage(this.plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + ChatColor.RED + "You do not have permission to do that!");
				return false;
			}

		} else {
			p.sendMessage(this.plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + ChatColor.RED + "Incorrect usage!");
			return false;
		}
		return false;

	}

}