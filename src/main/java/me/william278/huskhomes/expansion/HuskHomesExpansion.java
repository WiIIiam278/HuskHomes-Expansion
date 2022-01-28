package me.william278.huskhomes.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.william278.huskhomes2.HuskHomes;
import me.william278.huskhomes2.api.HuskHomesAPI;
import me.william278.huskhomes2.teleport.points.Home;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class HuskHomesExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "huskhomes";
    }

    @Override
    public @NotNull String getAuthor() {
        return "William278";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.3";
    }

    @Override
    public @NotNull String getRequiredPlugin() {
        return "HuskHomes";
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    @Override
    public boolean register() {
        Plugin huskHomes = Bukkit.getPluginManager().getPlugin(getRequiredPlugin());
        if (huskHomes != null) {
            return super.register();
        }
        return false;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) { return "Player not online"; }

        Player player = offlinePlayer.getPlayer();
        if (player == null) return null;
        switch (params) {
            case "homes_set":
                return String.valueOf(HuskHomesAPI.getInstance().getHomeCount(player));
            case "homes_max":
                return String.valueOf(HuskHomesAPI.getInstance().getMaxSetHomes(player));
            case "homes_free":
                return String.valueOf(HuskHomesAPI.getInstance().getFreeSetHomes(player));
            case "homes_set_list":
                StringJoiner homeList = new StringJoiner(", ");
                for (Home h : HuskHomesAPI.getInstance().getHomes(player)) {
                    homeList.add(h.getName());
                }
                return homeList.toString();
            case "homes_set_public":
                int publicHomes = 0;
                for (Home h : HuskHomesAPI.getInstance().getHomes(player)) {
                    if (h.isPublic()) {
                        publicHomes++;
                    }
                }
                return String.valueOf(publicHomes);
            case "homes_set_public_list":
                StringJoiner publicHomeList = new StringJoiner(", ");
                for (Home h : HuskHomesAPI.getInstance().getHomes(player)) {
                    if (h.isPublic()) {
                        publicHomeList.add(h.getName());
                    }
                }
                return publicHomeList.toString();
            case "tpa_ignore":
                return HuskHomes.isIgnoringTeleportRequests(player.getUniqueId()) + "";
            default:
                return null;
        }
    }

}
