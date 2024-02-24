package gloryz.com.uniqueafk.configs;

import gloryz.com.uniqueafk.UniqueAFK;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {
    private final UniqueAFK plugin;

    public PlayerDataManager(UniqueAFK plugin) {
        this.plugin = plugin;
        File playersDir = new File(plugin.getDataFolder(), "players");
        if (!playersDir.exists()) {
            playersDir.mkdirs();
        }

    }

    public YamlConfiguration getPlayerData(Player player) {
        UUID playerId = player.getUniqueId();
        File playerFile = new File(plugin.getDataFolder() + File.separator + "players", playerId.toString() + ".yml");

        // Vérifier si le fichier YAML du joueur existe
        if (!playerFile.exists()) {
            // Créer un nouveau fichier YAML pour le joueur
            try {
                playerFile.createNewFile();
                // Initialiser les données de base du joueur
                YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
                playerData.set("location", player.getLocation());
                playerData.set("afk_count", 0); // Initialiser le nombre de fois où le joueur est AFK à zéro
                playerData.set("message.afk", "The player " + player.getName() + " is afk !");
                playerData.save(playerFile);
                return playerData;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return YamlConfiguration.loadConfiguration(playerFile);
    }


    public void savePlayerData(Player player, YamlConfiguration playerData) {
        UUID playerId = player.getUniqueId();
        File playerFile = new File(plugin.getDataFolder() + File.separator + "players", playerId.toString() + ".yml");

        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
