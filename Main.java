package hash.cache.deathtag;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin {

	public ArrayList<Player> pInGame = new ArrayList<Player>(); //hehe the  thing spells pig

	public Player it = null;
	public Player wasIt = null;
	public boolean gActive = false;
	public boolean startBool = true;
	public boolean wActive = false;

   // String uncolored = getConfig().getString("ChatTag").replaceAll("&", "§");
	
	private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("A config was not found, creating it!");
                saveDefaultConfig();
            } else {
                getLogger().info("Loading the config...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().info("Oh no, Something has gone horribly wrong! This was probably caused by the DeathTag plugin :/ sorry!");
        }

    }
	
    @Override
    public void onEnable() {
       this.getCommand("tag").setExecutor(new CommandExec(this));
       createConfig();
       PluginManager pm = getServer().getPluginManager();
       pm.registerEvents(new GameListener(this, new CommandExec(this) ), this);
       pm.registerEvents(new CommandExec(this), this);
       getLogger().info("DeathTag Minigame plugin has been enabled!");
		gActive = false;
		wActive = false;
		it = null;
		wasIt = null;
		startBool = true;
    }
   
    @Override
    public void onDisable() {
       getLogger().info("DeathTag plugin has been disabled!");
		gActive = false;
		wActive = false;
		it = null;
		wasIt = null;
		pInGame.clear();
		
    }
	
	
}
