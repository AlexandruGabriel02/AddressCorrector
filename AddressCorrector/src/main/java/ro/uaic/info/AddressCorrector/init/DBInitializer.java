package ro.uaic.info.AddressCorrector.init;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ro.uaic.info.AddressCorrector.database.MultimapDatabase;
import ro.uaic.info.AddressCorrector.database.Node;
import ro.uaic.info.AddressCorrector.models.NodeType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that initializes the data from the files. The files are downloaded from an azure storage container, and then the hierarchy
 * is created.
 */
@Configuration
@Log4j2
public class DBInitializer implements CommandLineRunner {
    private Map<String, Node> idToNodeMap = new HashMap<>();
    private Multimap<String, Node> tokenToNodeHashmap = HashMultimap.create();
    private final static String allEntitiesFileName = "https://addresscorrectorstorage.blob.core.windows.net/data/allEntities.txt";
    private final static String hierarchyFileName = "https://addresscorrectorstorage.blob.core.windows.net/data/secondLevelHierarchy.txt";
    private final RestTemplate restTemplate = new RestTemplate();

    private Node createNewNode(String id) {
        Node node = new Node();
        idToNodeMap.put(id, node);
        return node;
    }

    private void mapNameToNode(Node node, String name) {
        if (name.isEmpty()) {
            return;
        }
        tokenToNodeHashmap.put(name.toLowerCase(), node);
    }

    private void setAlternateNames(Node node, String names) {
        String[] tokenNames = names.split(",");
        for (String name : tokenNames) {
            mapNameToNode(node, name);
        }
    }

    private void connectNodes(String parentId, String childId) {
        Node parentNode = idToNodeMap.get(parentId);
        Node childNode = idToNodeMap.get(childId);

        childNode.setParentNode(parentNode);
        parentNode.addChildNode(childNode);
    }

    /**
     * This method uses the file from {@link DBInitializer#allEntitiesFileName} to get all the information about the entities.
     * The file contains the official name, the ascii name and all the alternate names for each entity.
     * This method maps all the names to the corresponding node.
     */
    @Bean
    public void parseAllEntities() {
        ResponseEntity<Resource> exchange = restTemplate.exchange(allEntitiesFileName, HttpMethod.GET, null, Resource.class);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getBody().getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] tokens = line.split("\t");
                Node createdNode = createNewNode(tokens[0]);

                mapNameToNode(createdNode, tokens[1]);
                createdNode.setDefaultEntityName(tokens[1]);

                mapNameToNode(createdNode, tokens[2]);

                setAlternateNames(createdNode, tokens[3]);
            }
        } catch (IOException e) {
            log.error("IO exception at database creation: " + e.getMessage());
        }
    }

    /**
     * This method uses the file from {@link DBInitializer#hierarchyFileName} to create the hierarchy between the entities.
     * The file contains the id of the parent and the id of the child. In order to connect them, the {@link Node#addChildNode(Node)} is used.
     */
    public void createGraph() {
        ResponseEntity<Resource> exchange = restTemplate.exchange(hierarchyFileName, HttpMethod.GET, null, Resource.class);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getBody().getInputStream()))) {            String line;
            while ((line = in.readLine()) != null) {
                String[] ids = line.split("\t");
                connectNodes(ids[0], ids[1]);
            }
        } catch (IOException e) {
            log.error("IO exception at graph creation: " + e.getMessage());
        }
    }

    /**
     * This method assigns the type of each node. The type is assigned based on the level of the node in the hierarchy.
     * @param node the Node object that will have its type assigned
     */
    private void assignType(Node node) {
        if (!node.isValid()) {
            node.setType(NodeType.INVALID);
            return;
        }

        int level = 0;
        Node nodeCopy = node;
        while (nodeCopy.getParentNode() != null) {
            nodeCopy = nodeCopy.getParentNode();
            level++;
        }

        switch (level) {
            case 0 -> node.setType(NodeType.COUNTRY);
            case 1 -> node.setType(NodeType.STATE);
            case 2 -> node.setType(NodeType.CITY);
            default -> node.setType(NodeType.OTHER);
        }
    }

    private void assignNodeTypes() {
        for (Node node : idToNodeMap.values()) {
            assignType(node);
        }
    }

    private void copyHashmapToDatabase() {
        MultimapDatabase.INSTANCE.getTokenToNodeMap().putAll(tokenToNodeHashmap);
    }

    @Override
    public void run(String... args) {
        createGraph();
        assignNodeTypes();
        copyHashmapToDatabase();
        idToNodeMap = null;
        tokenToNodeHashmap = null;
        log.info("Initialized!");
    }
}
