package net.silexpvp.ranks;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.silexpvp.nightmare.Nightmare;
import net.silexpvp.ranks.rank.command.RankCommand;
import net.silexpvp.ranks.rank.manager.RankManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class RanksPlugin extends JavaPlugin {

    private Nightmare nightmare;

    private MongoDatabase mongoDatabase;

    private MongoCollection profileCollection;
    private MongoCollection rankCollection;

    private RankManager rankManager;

    @Override
    public void onLoad() {
        nightmare = (Nightmare) getServer().getPluginManager().getPlugin("Nightmare");
    }

    @Override
    public void onEnable() {
        mongoDatabase = nightmare.getMongoConnection().getClient().getDatabase("rankSystemDB");

        rankCollection = mongoDatabase.getCollection("ranks");
        profileCollection = mongoDatabase.getCollection("profiles");

        registerManagers();
        registerCommands();
    }

    private void registerManagers() {
        rankManager = new RankManager(this);
    }

    private void registerCommands() {
        nightmare.registerCommand(new RankCommand());
    }
}
