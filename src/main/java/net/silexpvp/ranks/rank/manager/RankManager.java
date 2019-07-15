package net.silexpvp.ranks.rank.manager;

import lombok.Getter;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class RankManager {

    private final RanksPlugin plugin;

    public RankManager(RanksPlugin plugin) {
        this.plugin = plugin;

        for (Object object : plugin.getRankCollection().find()) {
            Document document = (Document) object;

            ranks.add(new Rank(document));
        }
    }

    private final List<Rank> ranks = new ArrayList<>();

    public Rank getByName(String rankName) {
        return ranks.stream().flatMap(rank -> Stream.of(rank.getName()).filter(rankName::equalsIgnoreCase).map(name -> rank)).findAny().orElse(null);
    }
}
