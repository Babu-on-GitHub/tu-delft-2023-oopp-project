import client.scenes.CardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DetailedCardController {
    //TODO make detailed card model

    @FXML
    private TextArea cardDescription;

    @FXML
    private TextField cardTitle;

    @FXML
    private VBox detailedCardBox;

    @FXML
    private VBox subtaskArea;

    @FXML
    private Button subtaskButton;

    @FXML
    private HBox tagArea;

    @FXML
    private Button tagButton;

    @FXML
    void addSubtask(ActionEvent event) {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("Subtask.fxml"));

        // TODO creates new subtask, displays it
    }

    @FXML
    void addTag(ActionEvent event) {
        // TODO creates new tag, displays it OR adds existing tag
    }

}
