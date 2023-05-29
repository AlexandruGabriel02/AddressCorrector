package ro.uaic.info.AddressCorrector.database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.List;

public enum MultimapDatabase {
    INSTANCE;
    private static final Multimap<String, Node> tokenToNodeMap = HashMultimap.create(); //TODO change to ArrayListMultimap and keep key-value entries unique (implement in DBInitializer)
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
