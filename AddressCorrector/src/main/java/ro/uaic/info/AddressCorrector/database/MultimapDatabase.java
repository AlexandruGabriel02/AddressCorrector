package ro.uaic.info.AddressCorrector.database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.List;

/**
 * Class that has stores a collection of nodes. Every node represents an entity(city, state, country). Because each entity can have multiple names and
 * constant time access to the nodes was needed, the multimap data structure was used.
 * The {@link com.google.common.collect.Multimap} from guava library is used.
 *
 */
public enum MultimapDatabase {
    INSTANCE;
    private static final Multimap<String, Node> tokenToNodeMap = ArrayListMultimap.create();
    public void add(String key, Node value) {
        tokenToNodeMap.put(key, value);
    }
    public List<Node> get(String key) {
        return (List<Node>) tokenToNodeMap.get(key);
    }
    public Multimap<String, Node> getTokenToNodeMap() {
        return tokenToNodeMap;
    }
}
