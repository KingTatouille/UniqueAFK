package gloryz.com.uniqueafk.listener;

import gloryz.com.uniqueafk.UniqueAFK;
import gloryz.com.uniqueafk.configs.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    private final UniqueAFK plugin;
    private final FileConfiguration config;
    private final int afkTime; // Temps en secondes avant qu'un joueur soit considéré comme AFK

    private PlayerDataManager playerDataManager;


    public PlayerListener(UniqueAFK plugin) {
        this.plugin = plugin;
        this.config = plugin.getPluginConfig();
        this.afkTime = config.getInt("afk_time");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isPlayerAfk(player)) {
            // Réinitialiser le statut AFK du joueur lorsqu'il bouge
            player.setMetadata("afk", new FixedMetadataValue(plugin, false));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Supprimer les données de statut AFK lorsque le joueur se déconnecte
        player.removeMetadata("afk", plugin);
    }

    private boolean isPlayerAfk(Player player) {
        return !player.hasMetadata("afk") || !player.getMetadata("afk").get(0).asBoolean();
    }

    private void setPlayerAfk(Player player, boolean afk) {
        player.setMetadata("afk", new FixedMetadataValue(plugin, afk));

        // Récupérer les données du joueur depuis son fichier YAML
        YamlConfiguration playerData = playerDataManager.getPlayerData(player);
        if (playerData == null) {
            plugin.getLogger().warning("Failed to load player data for " + player.getName());
            return;
        }

        // Mettre à jour les données du joueur
        if (afk) {
            // Exécuter une commande si le joueur devient AFK
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("commands.afk"));

            // Incrémenter le nombre de fois où le joueur a été afk
            int afkCount = playerData.getInt("afk_count", 0);
            playerData.set("afk_count", afkCount + 1);

            // Enregistrer les données du joueur
            playerDataManager.savePlayerData(player, playerData);

            // Vérifier si l'option de téléportation AFK est activée
            if (plugin.getConfig().getBoolean("teleport_enabled", false)) {
                // Récupérer l'emplacement de la salle AFK depuis la configuration
                if (plugin.getConfig().contains("afk_room_location")) {
                    Location afkRoomLocation = (Location) plugin.getConfig().get("afk_room_location");
                    // Téléporter le joueur à l'emplacement de la salle AFK
                    player.teleport(afkRoomLocation);
                } else {
                    plugin.getLogger().warning("AFK room location is not set in the configuration!");
                }
            }
        } else {
            // Exécuter une commande si le joueur n'est plus AFK
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("commands.not_afk"));
        }
    }

    private void scheduleAfkCheck(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) return; // Vérifier si le joueur est toujours en ligne
                if (!player.isDead() && isPlayerAfk(player)) {
                    // Vérifier si le joueur est inactif
                    setPlayerAfk(player, true);
                }
            }
        }.runTaskLater(plugin, afkTime * 20L); // Convertir le temps en secondes en ticks
    }
}