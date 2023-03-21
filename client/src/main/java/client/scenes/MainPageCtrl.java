package client.scenes;

import client.model.BoardModel;
import client.model.ListModel;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import jakarta.ws.rs.BadRequestException;
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
import java.util.List;
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

    @FXML VBox boardListContainer;

    @FXML ScrollPane boardListScrollPane;

    private List<Board> boardList;

    /**
     * Getter for board
     */
    public BoardModel getBoard() {
        return board;
    }

    /**
     * Setter for board
     *
     * @param board Value for board
     */
    public void setBoard(BoardModel board) {
        this.board = board;
    }

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
        listOfLists.setSpacing(15);

        boardListContainer.setSpacing(10);
        boardListScrollPane.setFitToWidth(true);

    }

    public void recreateChildren(List<ListModel> arr) throws IOException {
        clearBoard();
        for (ListModel model : arr)
            addList(model);
    }

    public void addList(ListModel model) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedList.fxml"));

        var controller = new ListController(model, this);
        loader.setController(controller);
        model.setController(controller);

        Node newList = loader.load();
        listOfLists.getChildren().add(newList);
    }

    private void clearBoard(){
        listOfLists.getChildren().clear();
    }

    public void refresh() {
        // do nothing
    }

    @FXML
    public void optionsShowServerChoice(ActionEvent event){
        mainCtrl.showServerChoice();
    }

    @FXML
    public void addListButton(ActionEvent event) throws IOException {
        ListModel model = new ListModel(new CardList(), board);
        addList(model); // important: keep order of these two the same
        board.addList(model);
    }

    @FXML
    public void addBoardButton(ActionEvent event) {
        //do nothing
    }


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
