package ro.uaic.info.AddressCorrector.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.uaic.info.AddressCorrector.models.NodeType;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Node {
    private String defaultEntityName;
    private NodeType type;
    private Node parentNode;
    private List<Node> childNodes;

    public Node() {
        childNodes = new ArrayList<>();
    }

    public boolean isValid() {
        return parentNode != null || !childNodes.isEmpty();
    }

    public void addChildNode(Node node) {
        childNodes.add(node);
    }
}
