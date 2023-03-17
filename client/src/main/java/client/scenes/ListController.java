package client.scenes;


import client.model.CardModel;
import client.model.ListModel;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.Node;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ListController implements Initializable {

    @FXML
    private VBox cardListContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField listTitle;

    private ListModel listModel;


    @SuppressWarnings("unused")
    public ListController(){}

    public ListController(ListModel cardList){
        this.listModel = cardList;
    }

    public void addCardButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedCard.fxml"));

        CardModel newChild = new CardModel(new Card(),listModel);

        ServerUtils utils = new ServerUtils();

        listModel.addCard(newChild);

        loader.setController(new CardController(newChild));
        AnchorPane newCard = loader.load();

        cardListContainer.getChildren().add(newCard);
    }

    @FXML
    public void deleteListButton(ActionEvent event) {
        Button pressed = (Button) event.getSource();

        var toDelete = pressed.getParent().getParent();
        var listOfLists = (HBox) toDelete.getParent();

        listModel.deleteList();

        listOfLists.getChildren().remove(toDelete);

    }

    public void updateTitle(){
//        cardList.setTitle(listTitle.getText());
//        ServerUtils utils = new ServerUtils();
//        cardList = utils.updateCardListById(cardList.getId(),cardList);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardListContainer.setSpacing(10);
        scrollPane.setFitToWidth(true);

        listTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateTitle();
            }
        });
    }
}