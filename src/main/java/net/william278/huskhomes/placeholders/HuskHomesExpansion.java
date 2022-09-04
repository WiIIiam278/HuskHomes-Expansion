package net.william278.huskhomes.placeholders;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.william278.huskhomes.api.HuskHomesAPI;
import net.william278.huskhomes.player.OnlineUser;
import net.william278.huskhomes.player.UserData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

/**
 * PlaceholderAPI expansion for HuskHomes v3.x
 */
@SuppressWarnings("unused")
public class HuskHomesExpansion extends PlaceholderExpansion {

    @NotNull
    @Override
    public String getIdentifier() {
        return "huskhomes";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "William278";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "2.0";
    }

    @NotNull
    @Override
    public String getRequiredPlugin() {
        return "HuskHomes";
    }

    @NotNull
    private String getBooleanValue(final boolean bool) {
        return bool ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }

    @Override
    public String onRequest(@Nullable OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) {
            return "Player not online";
        }

        // Adapt the player to an OnlineUser
        final HuskHomesAPI huskHomesAPI = HuskHomesAPI.getInstance();
        final OnlineUser player = huskHomesAPI.adaptUser(offlinePlayer.getPlayer());

        // Return the requested data
        return switch (params) {
            case "homes_count" -> String.valueOf(huskHomesAPI.getUserHomes(player).join().size());

            case "max_homes" -> String.valueOf(huskHomesAPI.getMaxHomeSlots(player));

            case "max_public_homes" -> String.valueOf(huskHomesAPI.getMaxPublicHomeSlots(player));

            case "free_home_slots" -> String.valueOf(huskHomesAPI.getFreeHomeSlots(player));

            case "home_slots" -> String.valueOf(huskHomesAPI.getUserData(player.uuid).join()
                    .map(UserData::homeSlots)
                    .orElse(0));

            case "homes_list" -> huskHomesAPI.getUserHomes(player).join()
                    .stream()
                    .map(home -> home.meta.name)
                    .collect(Collectors.joining(", "));

            case "public_homes_count" -> String.valueOf(huskHomesAPI.getUserHomes(player).join()
                    .stream()
                    .filter(home -> home.isPublic)
                    .count());

            case "public_homes_list" -> huskHomesAPI.getUserHomes(player).join()
                    .stream()
                    .filter(home -> home.isPublic)
                    .map(home -> home.meta.name)
                    .collect(Collectors.joining(", "));

            case "ignoring_tp_requests" -> getBooleanValue(huskHomesAPI.getUserData(player.uuid).join()
                    .map(UserData::ignoringTeleports)
                    .orElse(false));

            default -> null;
        };
    }

}
