package client.scenes;

import client.model.BoardModel;
import client.model.ListModel;
import client.utils.ServerUtils;
import client.utils.UserUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainPageCtrl implements Initializable {

    static Logger log = Logger.getLogger(MainPageCtrl.class.getName());

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private final UserUtils userUtils;

    @FXML
    private TextField boardName;

    @FXML
    private HBox cardListsContainer;

    @FXML
    private ScrollPane boardScrollPane;

    private BoardModel board;

    private List<Long> boardList;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ScrollPane boardListScrollPane;

    @FXML
    private VBox boardsListContainer;

    @FXML
    private ImageView deleteBoardImage;
    @FXML
    private ImageView addListImage;
    @FXML
    private ImageView addBoardImage;
    @FXML
    private ImageView settingsImage;
    @FXML
    private ImageView shareImage;


    @Inject
    public MainPageCtrl(ServerUtils server, MainCtrl mainCtrl, UserUtils userUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.userUtils = userUtils;
        boardList = new ArrayList<>();
    }

    private void setImage(ImageView img, String path){
        File file = new File(path);
        Image image = new Image(file.toURI().toString());
        img.setImage(image);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Default board");

        setImage(deleteBoardImage,"client/src/main/resources/client/icons/delete_FILL0_wght400_GRAD0_opsz48.png");
        setImage(shareImage,"client/src/main/resources/client/icons/share_FILL0_wght400_GRAD0_opsz48.png");
        setImage(settingsImage,"client/src/main/resources/client/icons/settings_FILL0_wght400_GRAD0_opsz48.png");
        setImage(addListImage,"client/src/main/resources/client/icons/add_notes_FILL0_wght400_GRAD0_opsz48.png");
        setImage(addBoardImage,"client/src/main/resources/client/icons/new_window_FILL0_wght400_GRAD0_opsz48.png");

        //This makes the lists to fill the entire height of their parent
        boardScrollPane.setFitToHeight(true);
        cardListsContainer.setSpacing(20);

        if (board == null) {
            var res = server.getBoardById(1);
            if (res.isPresent()) {
                board = new BoardModel(res.get(), server);
                board.setController(this);
            } else {
                Board toAdd = new Board();
                var added = server.addBoard(toAdd);
                if (added.isEmpty())
                    throw new RuntimeException("Server Request failed");
                board = new BoardModel(added.get(), server);
            }
        }
        board.setController(this);
        board.update();
        board.updateChildren();

        server.getSocketUtils().connect();

        showBoard();

        //makes board overview resize correctly
        SplitPane.setResizableWithParent(boardListScrollPane.getParent(), false);

        //makes the boards fill width of the board list
        boardListScrollPane.setFitToWidth(true);

        boardList = userUtils.getUserBoardsIds();
        try {
            showBoardsList();
        } catch (IOException e) {
            log.warning("Couldn't show boards");
        }

    }


    public void refresh() {
        boardList = userUtils.getUserBoardsIds();
        try {
            showBoardsList();
        } catch (IOException e) {
            log.warning("Couldn't show boards on refresh");
        }
        board.update();
        board.updateChildren();
    }

    public void refreshWithBoard(Board b) {
        board.updateWithNewBoard(b);
    }

    @FXML
    public void optionsShowServerChoice(ActionEvent event) {
        mainCtrl.showServerChoice();
    }

    @FXML
    public void addListButton(ActionEvent event) throws IOException {
        ListModel model = new ListModel(new CardList(), board, server);
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

    public BoardModel getModel() {
        return board;
    }

    public void showBoard() {
        if (board == null) return;
        this.board.setController(this);
        this.board.update();
        this.board.updateChildren();
        server.getSocketUtils().registerForMessages(
                "/topic/board/" + board.getBoard().getId(),
                board.getBoard().getClass(),
                (board) -> {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            refreshWithBoard(board);
                        }
                    });
                });
    }

    public void setBoardOverview(long id) throws IOException {
        var res = server.getBoardById(id);
        if (res.isEmpty()) {
            initializeBoard();
            //getAllBoardsIds();
            showBoardsList();
            return;
        }
        this.board = new BoardModel(res.get(), server);
        cardListsContainer.getChildren().clear();
        showBoard();
    }

    public void initializeBoard() {
        var res = server.getBoardById(1);
        if (res.isPresent()) {
            board = new BoardModel(res.get(), server);
            board.setController(this);
        } else {
            Board toAdd = new Board();
            var added = server.addBoard(toAdd);
            if (added.isEmpty())
                throw new RuntimeException("Server Request failed");
            board = new BoardModel(added.get(), server);
            board.setController(this);
        }
    }

    public void getAllBoardsIds() {
        //This will pe changed to fetch only the boards linked to the user
        var newBoardList = server.getBoards();
        if (newBoardList.isEmpty()) {
            log.warning("Something went wrong fetching the boards");
        } else {
            boardList = newBoardList.get().stream().map(Board::getId).toList();
        }
    }

    //call getAllBoardsIds or userUtils.getUserBoardsIds() before this one
    public void showBoardsList() throws IOException {
        if (boardList == null) return;
        boardsListContainer.getChildren().clear();
        for (var newBoard : boardList) {
            addBoardListItemToList(newBoard);
        }

    }

    public void addBoardListItemToList(long boardId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardsListItem.fxml"));

        loader.setController(new BoardsListItemCtrl(boardId, this));

        AnchorPane toAdd = loader.load();

        boardsListContainer.getChildren().add(toAdd);
    }

    @FXML
    public void addBoardButton(ActionEvent event) throws IOException {
        ServerUtils utils = new ServerUtils();
        Board board = new Board();
        var added = utils.addBoard(board);
        if (added.isEmpty()) {
            log.warning("Failed to add new board to server");
            return;
        }
        boardList.add(added.get().getId());
        userUtils.updateUserBoards(boardList);
        addBoardListItemToList(added.get().getId());
    }

    @FXML
    public void deleteBoardButton(ActionEvent event) throws IOException {
        if (board.getBoard().getId() == 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Default board cannot be deleted");
            //alert.setContentText("Default board cannot be deleted");

            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Dialog");
        alert.setHeaderText("Are you sure you want to delete the board?");
        alert.setContentText("Click OK to proceed or Cancel to abort.");

        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOK) {
            server.deleteBoardById(board.getBoard().getId());
            boardList.remove(board.getBoard().getId());
            userUtils.updateUserBoards(boardList);
            showBoardsList();
            initializeBoard();
            setBoardOverview(board.getBoard().getId());
        } else {
            return;
        }
    }
}
