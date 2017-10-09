package hash.cache.deathtag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class GameListener implements Listener {

	// Constructors
	private Main plugin;
	private CommandExec method;

	public GameListener(Main beep, CommandExec doot) {
		this.plugin = beep;
		this.method = doot;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void whenTagged(EntityDamageByEntityEvent event) {
		if (((event.getEntity() instanceof Player)) && ((event.getDamager() instanceof Player))) {
			String damager = ((HumanEntity) event.getDamager()).getName();
			String player = ((HumanEntity) event.getEntity()).getName();
			HashSet<Player> listToSet = new HashSet<Player>(this.plugin.pInGame);
			ArrayList<Player> pInGameNoDup = new ArrayList<Player>(listToSet);
			if (this.plugin.gActive == true) {
				if (pInGameNoDup.contains(Bukkit.getPlayer(damager))
						&& pInGameNoDup.contains(Bukkit.getPlayer(player))) {
					String it = this.plugin.it.getName();
					if (damager == it) {
						event.setDamage(0);
						this.plugin.it = ((Bukkit.getPlayer(player)));
						for (Player p : pInGameNoDup) {
							p.sendMessage(this.plugin.getConfig().getString("ChatTag").replaceAll("&", "§") + this.plugin.it.getName() + " was tagged by "
									+ ((Bukkit.getPlayer(damager).getName())));
						}
					} else {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (plugin.wActive == true) {
			Player quitter = event.getPlayer();
			this.plugin.pInGame.removeAll(Collections.singleton(quitter));
		}
		if (plugin.gActive == true &&event.getPlayer() == plugin.it) {
			method.getNewIt();
		}
	}

}
