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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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

    private BoardModel board;

    private List<Board> boardList;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ScrollPane boardListScrollPane;

    @FXML
    private VBox listOfBoards;



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

        if(board == null){
            try {
                board = new BoardModel( server.getBoardById(1L));
            }catch(BadRequestException e){
                Board toAdd = new Board();
                toAdd = server.addBoard(toAdd);
                board = new BoardModel(toAdd);
            }

        }

        //makes board overview resize correctly
        splitPane.setResizableWithParent(boardListScrollPane.getParent(), false);

        //makes the boards fill the width of the board list
        boardListScrollPane.setFitToWidth(true);

        try {
            showAllBoards();
        } catch (IOException e) {
            e.printStackTrace();
        }

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



    public void setBoardOverview(BoardModel boardModel) {
        this.board = boardModel;
        //TODO clear overview and show new board
    }

    public void showAllBoards() throws IOException {
        ServerUtils utils = new ServerUtils();
        boardList = utils.getBoards(); //this will be changed to fetching the boards of the users.
        for(var newBoard : boardList){
            var newModel = new BoardModel(newBoard);
            addBoardListItemToList(newModel);
        }

    }

    public void addBoardListItemToList(BoardModel model) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardsListItem.fxml"));

        loader.setController(new BoardsListItemCtrl(model, this));

        AnchorPane toAdd = loader.load();

        listOfBoards.getChildren().add(toAdd);
    }

    @FXML
    public void addBoardButton(ActionEvent event) throws IOException {
        BoardModel newBoardModel = new BoardModel(new Board());
        newBoardModel.update();
        addBoardListItemToList(newBoardModel);
    }

}
