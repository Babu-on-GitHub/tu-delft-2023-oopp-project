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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainPageCtrl implements Initializable {

    static Logger log = Logger.getLogger(MainPageCtrl.class.getName());

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardName;

    @FXML
    private HBox cardListsContainer;

    @FXML
    private ScrollPane boardScrollPane;

    private BoardModel board;

    private List<Board> boardList;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ScrollPane boardListScrollPane;

    @FXML
    private VBox boardsListContainer;



    @Inject
    public MainPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        boardList = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Default board");

        //This makes the lists to fill the entire height of their parent
        boardScrollPane.setFitToHeight(true);
        cardListsContainer.setSpacing(20);

        initializeBoard();

        board.setController(this);
        board.update();
        board.updateChildren();

        //makes board overview resize correctly
        splitPane.setResizableWithParent(boardListScrollPane.getParent(), false);

        //makes the boards fill width of the board list
        boardListScrollPane.setFitToWidth(true);

        try {
            showAllBoards();
        } catch (IOException e) {
            log.warning("Something went wrong when showing existing boards");
        }

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
        ListModel model = new ListModel(new CardList(), board);
        addList(model); // important: keep order of these two the same
        board.addList(model);
    }

    public void recreateChildren(List<ListModel> arr) throws IOException {
        cardListsContainer.getChildren().clear();
        for (ListModel model : arr)
            addList(model);
    }

    public void addList(ListModel model) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedList.fxml"));

        var controller = new ListController(model, this);
        loader.setController(controller);
        model.setController(controller);

        Node newList = loader.load();
        cardListsContainer.getChildren().add(newList);
    }

    public HBox getListsContainer() {
        return cardListsContainer;
    }

    public void setBoardOverview(Board board) {
        ServerUtils utils = new ServerUtils();
        var res = utils.getBoardById(board.getId());
        if(res.isEmpty()){
            //TODO give message that board doesn't exist and delete board
            var defaultBoard = boardList.stream().filter(q -> q.getId() == 1).findFirst();
            if(defaultBoard.isEmpty()){
                initializeBoard();
                updateBoardList();
                setBoardOverview(this.board.getBoard());
            }else{
                setBoardOverview(defaultBoard.get());
            }
            return;
        }
        this.board = new BoardModel(res.get());
        cardListsContainer.getChildren().clear();
        this.board.setController(this);
        this.board.update();
        this.board.updateChildren();
    }

    public void initializeBoard(){
        ServerUtils utils = new ServerUtils();
        //if(board == null) {
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
        //}
    }

    public void updateBoardList(){
        //This will pe changed to fetch only the boards linked to the user
        ServerUtils utils = new ServerUtils();
        var newBoardList = utils.getBoards();
        if(newBoardList.isEmpty()){
            log.warning("Something went wrong fetching the boards");
        }else{
            boardList = newBoardList.get();
        }
    }

    public void showAllBoards() throws IOException {
        updateBoardList();
        boardsListContainer.getChildren().clear();
        for(var newBoard : boardList){
            addBoardListItemToList(newBoard);
        }

    }

    public void addBoardListItemToList(Board board) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardsListItem.fxml"));

        loader.setController(new BoardsListItemCtrl(board, this));

        AnchorPane toAdd = loader.load();

        boardsListContainer.getChildren().add(toAdd);
    }

    @FXML
    public void addBoardButton(ActionEvent event) throws IOException {
        ServerUtils utils = new ServerUtils();
        Board board = new Board();
        var added = utils.addBoard(board);
        if(added.isEmpty()){
            log.warning("Failed to add new board to server");
            return;
        }
        addBoardListItemToList(added.get());
    }

    @FXML
    public void deleteBoardButton(ActionEvent event) throws IOException {
        if(board.getBoard().getId()==1) return;
        removeBoard(board.getBoard());
        showAllBoards();
        initializeBoard();
        setBoardOverview(board.getBoard());
    }

    public void removeBoard(Board board){
        ServerUtils utils =  new ServerUtils();
        utils.deleteBoardById(board.getId());
        boardList.remove(board);
    }

}
