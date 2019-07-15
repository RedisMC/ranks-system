package net.silexpvp.ranks.rank;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import net.silexpvp.ranks.RanksPlugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Rank {

    private final RanksPlugin plugin = JavaPlugin.getPlugin(RanksPlugin.class);

    private int weight;

    private String name, prefix = "", suffix = "";

    private final List<String> permissions = new ArrayList<>();
    private final List<Rank> parents = new ArrayList<>();

    public Rank(String name) {
        this.name = name;

        plugin.getRankManager().getRanks().add(this);
    }

    public Rank(Document document) {
        name = (String) document.get("_id");

        if (document.containsKey("weight")) {
            weight = document.getInteger("weight");
        }

        if (document.containsKey("prefix")) {
            prefix = document.getString("prefix");
        }

        if (document.containsKey("suffix")) {
            suffix = document.getString("suffix");
        }

        if (document.containsKey("permissions")) {
            permissions.addAll((Collection<? extends String>) document.get("permissions"));
        }

        if (document.containsKey("parents")) {
            for (String rank : (ArrayList<String>) document.get("parents")) {
                System.out.println(rank);
            }
        }
    }

    public Document serialize() {
        Document document = new Document("_id", name).append("weight", weight);

        if (!prefix.isEmpty()) {
            document.put("prefix", prefix);
        }

        if (!suffix.isEmpty()) {
            document.put("suffix", suffix);
        }

        if (!permissions.isEmpty()) {
            document.put("permissions", permissions);
        }

        if (!parents.isEmpty()) {
            document.put("parents", parents.stream().map(Rank::getName).collect(Collectors.toList()));
        }

        return document;
    }

    public void delete() {
        plugin.getRankManager().getRanks().remove(this);
        plugin.getRankCollection().deleteOne(Filters.eq(name));
    }

    public void save() {
        plugin.getRankCollection().replaceOne(Filters.eq(name), serialize(), new UpdateOptions().upsert(true));
    }

    public void asyncSave() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::save);
    }

    public boolean addPermission(String permission) {
        return permissions.add(permission);
    }

    public boolean removePermission(String permission) {
        return permissions.remove(permission);
    }

    public boolean hasPermission(String permission) {
        return getEffectivePermissions().contains(permission);
    }

    public boolean canParent(Rank rank) {
        return rank != this;
    }

    public boolean isParent(Rank rank) {
        return parents.contains(rank);
    }

    public List<String> getEffectivePermissions() {
        List<String> permissions = new ArrayList<>(this.permissions);
        parents.stream().map(Rank::getEffectivePermissions).forEach(permissions::addAll);

        return permissions;
    }

    public ChatColor getMainColor() {
        if (prefix.isEmpty()) return ChatColor.WHITE;

        AtomicReference<Character> lastCode = new AtomicReference<>('f');
        Arrays.stream(prefix.split("&")).filter(not(String::isEmpty)).map(string -> string.toCharArray()[0]).flatMap(character -> Stream.of(character).map(ChatColor::getByChar).filter(Objects::nonNull).map(color -> character)).filter(code -> code != 'o').forEach(lastCode::set);

        return ChatColor.getByChar(lastCode.get());
    }

    private <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }
}
