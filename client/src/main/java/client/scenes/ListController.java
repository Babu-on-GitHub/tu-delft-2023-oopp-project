package client.scenes;


import client.model.CardModel;
import client.model.ListModel;
import client.utils.ServerUtils;
import commons.Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ListController implements Initializable {

    @FXML
    private VBox cardListContainer;

    @FXML
    private BorderPane listContainer;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField listTitle;
    private ListModel listModel;
    private MainPageCtrl parent;



    @SuppressWarnings("unused")
    public ListController(){}

    public ListController(ListModel cardList){
        this.listModel = cardList;
    }

    public ListController(ListModel cardList, MainPageCtrl parent) {
        this.listModel = cardList;
        listModel.setController(this);
        this.parent = parent;
    }

    public void recreateChildren(ArrayList<CardModel> temp) throws IOException {
        cardListContainer.getChildren().clear();
        for (CardModel card : temp)
            addCard(card);
    }

    public void addCard(CardModel model) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedCard.fxml"));
        var controller = new CardController(model, this);
        model.setController(controller);
        loader.setController(controller);

        AnchorPane newCard = loader.load();
        cardListContainer.getChildren().add(newCard);
    }

    public void addCardButton(ActionEvent event) throws IOException {
        CardModel newCard = new CardModel(new Card(), listModel);
        addCard(newCard); // keep this order of add card and listModel.addCard
        listModel.addCard(newCard);
    }


    @FXML
    public void deleteListButton(ActionEvent event) {
        deleteList();
    }

    public void deleteList() {
        listModel.deleteList();
        parent.getListsContainer().getChildren().remove(listContainer);
    }

    public void updateTitle(){
        // not implemented
        throw new NotImplementedException();
    }

    public VBox getCardsContainer() {
        return cardListContainer;
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