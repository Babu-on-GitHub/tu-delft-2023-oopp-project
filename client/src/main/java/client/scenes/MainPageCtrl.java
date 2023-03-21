package client.scenes;

import client.model.BoardModel;
import client.model.ListModel;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageCtrl implements Initializable {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardName;

    @FXML
    private HBox listOfLists;

    @FXML
    private ScrollPane boardScrollPane;

    private BoardModel board;


    @Inject
    public MainPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Default board");

        //This makes the lists to fill the entire height of their parent
        boardScrollPane.setFitToHeight(true);
        listOfLists.setSpacing(20);

        if(board == null) {
            var res = server.getBoardById(1);
            if (res.isPresent()) {
                board = new BoardModel(res.get());
                board.setController(this);
            }
            else {
                Board toAdd = new Board();
                var added = server.addBoard(toAdd);
                if (added.isEmpty())
                    throw new RuntimeException("Server Request failed");
                board = new BoardModel(added.get());
            }
        }
        board.setController(this);
        board.update();
        board.updateChildren();
    }

    public void refresh() {
        board.update();
        //TODO: sockets
        board.updateChildren();
    }

    @FXML
    public void optionsShowServerChoice(ActionEvent event){
        mainCtrl.showServerChoice();
    }

    @FXML
    public void addListButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedList.fxml"));

        ListModel newChild = new ListModel(new CardList(),board);

        board.addList(newChild);

        loader.setController(new ListController(newChild));

        Node newList = loader.load();
        listOfLists.getChildren().add(newList);
    }
//    @FXML
//    public void addListButton(ActionEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("List.fxml"));
//        loader.setController(this);
//        VBox newList = loader.load();
//
//        listOfLists.getChildren().add(newList);
//    }

    @FXML
    public void addCardButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        loader.setController(this);
        StackPane newCard = loader.load();

        Button pressed = (Button) event.getSource();
        VBox wholeList = (VBox) pressed.getParent().getParent();
        VBox listVbox = (VBox) ((ScrollPane) wholeList.getChildren().get(2)).getContent();

        listVbox.getChildren().add(newCard);
    }

    @FXML
    public void deleteCardButton(ActionEvent event) {
        Button pressed = (Button) event.getSource();

        StackPane toDelete = (StackPane) pressed.getParent().getParent().getParent();
        VBox listOfToDelete = (VBox) toDelete.getParent();

        listOfToDelete.getChildren().remove(toDelete);
    }

    @FXML
    public void deleteListButton(ActionEvent event) {
        Button pressed = (Button) event.getSource();

        var toDelete = (VBox) pressed.getParent().getParent();
        var listOfLists = (HBox) toDelete.getParent();

        listOfLists.getChildren().remove(toDelete);
    }



}
