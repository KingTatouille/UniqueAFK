package gloryz.com.uniqueafk.commands;

import gloryz.com.uniqueafk.UniqueAFK;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UniqueAfkRoomCommand implements CommandExecutor {

    private final UniqueAFK plugin;

    public UniqueAfkRoomCommand(UniqueAFK plugin) {
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

        if (args.length != 0) {
            player.sendMessage(plugin.getPrefix() + " " + "Usage: /afk room");
            return true;
        }

        // Récupérer la configuration du plugin
        FileConfiguration config = plugin.getConfig();

        // Vérifier si l'option de la salle AFK est activée
        boolean teleportEnabled = config.getBoolean("teleport_enabled", false);
        if (!teleportEnabled) {
            player.sendMessage(plugin.getPrefix() + " " +"AFK teleportation is not enabled in the plugin configuration.");
            return true;
        }

        // Enregistrer l'emplacement actuel du joueur comme emplacement de la salle AFK
        config.set("afk_room_location", player.getLocation());
        plugin.saveConfig();

        player.sendMessage(plugin.getPrefix() + " " + "AFK room location set successfully!");

        return true;
    }
}
