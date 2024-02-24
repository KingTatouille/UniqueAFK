package gloryz.com.uniqueafk.commands;

import gloryz.com.uniqueafk.UniqueAFK;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UniqueAfkCommand implements CommandExecutor {

    private final UniqueAFK plugin;

    public UniqueAfkCommand(UniqueAFK plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Vérifier si la commande est exécutée par un joueur
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        // Vérifier les permissions (si nécessaire)
        if (!player.hasPermission("uniqueafk.use")) {
            player.sendMessage("You don't have permission to use this command!");
            return true;
        }

        // Vérifier les arguments de la commande
        if (args.length != 2 || !args[0].equalsIgnoreCase("message") || !args[1].equalsIgnoreCase("list")) {
            player.sendMessage("Usage: /afk message list");
            return true;
        }

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
}