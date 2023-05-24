package ro.uaic.info.AddressCorrector.init;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.uaic.info.AddressCorrector.database.MultimapDatabase;
import ro.uaic.info.AddressCorrector.database.Node;
import ro.uaic.info.AddressCorrector.models.NodeType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Log4j2
public class DBInitializer implements CommandLineRunner {
    private Map<String, Node> idToNodeMap = new HashMap<>();
    private final static String allEntitiesFileName = "src/main/resources/data/allEuropeanEntities.txt";
    private final static String hierarchyFileName = "src/main/resources/data/secondLevelHierarchy.txt";

    private Node createNewNode(String id) {
        Node node = new Node();
        idToNodeMap.put(id, node);

        return node;
    }

    private void mapNameToNode(Node node, String name, MultimapDatabase multimap) {
        multimap.add(name, node);
    }

    private void setAlternateNames(Node node, String names, MultimapDatabase multimap) {
        String[] tokenNames = names.split(",");
        for (String name : tokenNames) {
            mapNameToNode(node, name, multimap);
        }
    }

    private void connectNodes(String parentId, String childId) {
        Node parentNode = idToNodeMap.get(parentId);
        Node childNode = idToNodeMap.get(childId);

        childNode.setParentNode(parentNode);
        parentNode.addChildNode(childNode);
    }

    @Bean
    public MultimapDatabase parseAllEntities() {
        MultimapDatabase multimap = new MultimapDatabase();
        try(BufferedReader in = new BufferedReader(new FileReader(allEntitiesFileName))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] tokens = line.split("\t");
                Node createdNode = createNewNode(tokens[0]);

                mapNameToNode(createdNode, tokens[1], multimap);
                createdNode.setDefaultEntityName(tokens[1]);

                mapNameToNode(createdNode, tokens[2], multimap);

                setAlternateNames(createdNode, tokens[3], multimap);
            }
        }
        catch(IOException e) {
            log.error("IO exception at database creation: " + e.getMessage());
        }
        return multimap;
    }

    public void createGraph() {
        try(BufferedReader in = new BufferedReader(new FileReader(hierarchyFileName))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] ids = line.split("\t");
                connectNodes(ids[0], ids[1]);
            }
        }
        catch (IOException e) {
            log.error("IO exception at graph creation: " + e.getMessage());
        }
    }

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

    public void assignNodeTypes() {
        for (Node node : idToNodeMap.values()) {
            assignType(node);
        }
    }

    @Override
    public void run(String... args) {
        createGraph();
        assignNodeTypes();
        log.info("Initialized!");
        idToNodeMap = null;
    }
}
