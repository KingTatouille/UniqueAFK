package gloryz.com.uniqueafk;

import gloryz.com.uniqueafk.commands.UniqueAfkCommand;
import gloryz.com.uniqueafk.listener.PlayerListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class UniqueAFK extends JavaPlugin {

    private UniqueAFK instance;

    private String prefix;
    private FileConfiguration config;

    private PlayerListener playerListener; // Ajout de l'attribut playerListener

    @Override
    public void onEnable() {
        instance = this;
        prefix = this.getConfig().getString("prefix");
        loadConfig();
        // Enregistrement des écouteurs d'événements
        playerListener = new PlayerListener(this); // Initialisation de playerListener
        getServer().getPluginManager().registerEvents(playerListener, this);
        getCommand("afk").setExecutor(new UniqueAfkCommand(this));
        getLogger().info("UniqueAfk has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("UniqueAfk has been disabled.");
    }

    public String getPrefix(){
        return prefix;
    }

    private void loadConfig() {
        // Charger la configuration depuis le fichier config.yml
        saveDefaultConfig();
        config = getConfig();
        // Charger d'autres configurations personnalisées si nécessaire
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }

    // Méthode pour obtenir le listener du joueur
    public PlayerListener getPlayerListener() {
        return playerListener;
    }
}
