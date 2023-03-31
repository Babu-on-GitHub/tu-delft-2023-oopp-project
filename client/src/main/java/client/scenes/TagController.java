package client.scenes;

import client.model.CardModel;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class TagController {

    @FXML
    private Text tagName;

    private ServerUtils server;
    private CardModel parent;
    public TagController(CardModel parent, ServerUtils server) {
        this.parent = parent;
        this.server = server;
    }
}

