package client.tools;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.util.function.Consumer;

public interface SceneTools {
    static void applyToEveryNode(Node node, Consumer<Node> consumer) {
        if (node == null)
            return;

        consumer.accept(node);

        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                applyToEveryNode(child, consumer);
            }
        }

        if (node instanceof Button btn) {
            applyToEveryNode(btn.getGraphic(), consumer);
        }
    }
}
