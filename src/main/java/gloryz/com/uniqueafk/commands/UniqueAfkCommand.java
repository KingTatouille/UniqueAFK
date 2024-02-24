package gloryz.com.uniqueafk.commands;

import gloryz.com.uniqueafk.UniqueAFK;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class UniqueAfkCommand implements CommandExecutor {

    private final UniqueAFK plugin;

    public UniqueAfkCommand(UniqueAFK plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getPrefix() + " " + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("uniqueafk.use")) {
            player.sendMessage(plugin.getPrefix() + " " + "You don't have permission to use this command!");
            return true;
        }

        if(args.length == 0){
            plugin.getPlayerListener().setPlayerAfk(player, true);
            return true;
        }

        // Si la commande est "afk room", exécuter la commande /afk room
        if (args[0].equalsIgnoreCase("room")) {
            // Vérifier si l'option de la salle AFK est activée
            if (!plugin.getConfig().getBoolean("teleport_enabled")) {
                player.sendMessage(plugin.getPrefix() + " " + "AFK teleportation is not enabled in the plugin configuration.");
                return true;
            }

            // Enregistrer l'emplacement actuel du joueur comme emplacement de la salle AFK
            plugin.getConfig().set("afk_room_location", player.getLocation());
            plugin.saveConfig();

            player.sendMessage(plugin.getPrefix() + " " + "AFK room location set successfully!");
            return true;
        }

        // Si la commande est "afk message list", afficher la liste des messages personnalisés
        if (args[0].equalsIgnoreCase("message") && args[1].equalsIgnoreCase("list")) {
            // Récupérer les messages personnalisés de la configuration
            List<String> customMessages = plugin.getPluginConfig().getStringList("sentence-player-custom");
            if (customMessages.isEmpty()) {
                player.sendMessage("No custom messages found in the configuration!");
                return true;
            }

            // Afficher la liste des messages disponibles pour le joueur
            player.sendMessage("Available custom messages:");
            for (int i = 0; i < customMessages.size(); i++) {
                String[] parts = customMessages.get(i).split(":");
                if (parts.length != 2) {
                    player.sendMessage("Invalid message format for message number " + (i + 1));
                } else {
                    String permission = parts[1].trim();
                    if (player.hasPermission(permission)) {
                        player.sendMessage((i + 1) + ". " + parts[0].trim());
                    }
                }
            }
            return true;
        }

        if(args[0].equalsIgnoreCase("help")){

            // Si aucun des cas ci-dessus ne correspond, afficher un message d'aide
            player.sendMessage("Usage:");
            player.sendMessage("/afk - Set yourself as AFK");
            player.sendMessage("/afk room - Set the AFK room location");
            player.sendMessage("/afk message list - List available custom messages");

            return true;
        }

        return true;
    }
}
