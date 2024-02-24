package gloryz.com.uniqueafk.configs;

import gloryz.com.uniqueafk.UniqueAFK;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerDataManager {
    private final UniqueAFK plugin;

    public PlayerDataManager(UniqueAFK plugin) {
        this.plugin = plugin;
    }

    public YamlConfiguration getPlayerData(Player player) {
        String playerName = player.getName();
        File playerFile = new File(plugin.getDataFolder(), playerName + ".yml");

        // Vérifier si le fichier YAML du joueur existe
        if (!playerFile.exists()) {
            // Créer un nouveau fichier YAML pour le joueur
            try {
                playerFile.createNewFile();
                // Initialiser les données de base du joueur
                YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
                playerData.set("location", player.getLocation());
                playerData.set("afk_count", 0); // Initialiser le nombre de fois où le joueur est AFK à zéro
                playerData.set("command.afk", "The player " + player.getName() + " is afk !");
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
        String playerName = player.getName();
        File playerFile = new File(plugin.getDataFolder(), playerName + ".yml");

        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
