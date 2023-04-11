package client.scenes;

import client.model.BoardModel;
import client.model.ListModel;
import client.utils.SceneTools;
import client.utils.ServerUtils;
import client.utils.UserUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.BoardIdWithColors;
import commons.CardList;
import commons.ColorPair;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static client.utils.ColorTools.toHexString;
import static client.utils.ImageTools.recolorImage;


public class MainPageCtrl implements Initializable {

    static Logger log = Logger.getLogger(MainPageCtrl.class.getName());

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private final UserUtils userUtils;

    @FXML
    private TextField boardName;

    @FXML
    private BorderPane root;

    @FXML
    private HBox cardListsContainer;

    @FXML
    private BorderPane pageRoot;

    @FXML
    private ScrollPane boardScrollPane;


    private List<BoardIdWithColors> boardList;

    private List<BoardsListItemCtrl> boardListControllers;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ScrollPane boardListScrollPane;

    @FXML
    private VBox boardsListContainer;

    @FXML
    private AnchorPane boardIdPanel;

    @FXML
    private TextField boardIdLabel;

    @FXML
    private Button refreshBoardsListButton;

    @FXML
    private Button leaveBoardButton;

    @FXML
    private Button addListButton;

    private boolean admin = false;
    private Stage secondStage;

    @FXML
    private AnchorPane boardTop;
    @FXML
    private AnchorPane boardBottom;

    private Stage customizationStage;

    @FXML
    private Button customizeButton;

    @FXML
    private Button changeServerBtn;

    @FXML
    private Button tagsButton;

    @FXML
    private Label boardIdLabelText;

    @FXML
    private Button shareButton;

    @FXML
    private Button deleteButton;

    @FXML
    private AnchorPane boardsListAnchorPane;

    @FXML
    private MenuItem deleteBoardMenuItem;

    @FXML
    private MenuItem leaveBoardMenuItem;

    @FXML
    private Button addBoardButton;

    private BoardModel board;


    @Inject
    public MainPageCtrl(ServerUtils server, MainCtrl mainCtrl, UserUtils userUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.userUtils = userUtils;
        boardList = new ArrayList<>();
        userUtils.setMyId(1);

        boardList = new ArrayList<>();
        boardListControllers = new ArrayList<>();
    }

    private void setImage(ImageView img, String path) {
        File file = new File(path);
        Image image = recolorImage(file.toURI().toString(), Color.valueOf(getBoardColor().getFont()));
        img.setImage(image);
    }
    @FXML
    public void keyPressBoard(KeyEvent event) throws IOException {
        if (event.isShiftDown() && event.getCode() == KeyCode.SLASH) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpScreen.fxml"));

            Parent root = loader.load();

            secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.initOwner(boardName.getScene().getWindow());
            secondStage.show();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Default board");
        //makes board overview resize correctly
        SplitPane.setResizableWithParent(boardListScrollPane.getParent(), false);

        boardIdPanel.setVisible(false);
        boardIdLabel.setEditable(false);

        updateBoardColors(getBoardColor());
    }

    public BoardIdWithColors getColors() {
        return userUtils.getCurrentBoardColors();
    }

    @FXML
    void tagOverviewButton(ActionEvent event) {
        showTagOverview();
    }

    public void showTagOverview() {

    }

    public void updateTitleModel() {
        board.updateTitle(boardName.getText());
    }

    public Board getBoard() {
        return board.getBoard();
    }

    public void initializeServerStuff() {
        try {
            initializeBoard();

            server.getSocketUtils().connect();

            showBoard();

            if (admin) {
                getAllBoardsIds();
            } else {
                boardList = userUtils.getUserBoardsIds();
            }

            showBoardsList();
        } catch (Exception e) {
            log.warning("Something wrong in main page init: " + e.getMessage());
        }

        boardName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateTitleModel();
                try {
                    showBoardsList();
                } catch (IOException e) {
                    log.warning("Failed to show the board list on title update");
                }
            }
        });

        try {
            server.getPollingUtils().longPoll("/long/status", (status) -> {
                if (status.isEmpty() || !status.get().equals("OK")) {
                    log.warning("Lost connection to the server");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            mainCtrl.showServerChoice();
                        }
                    });
                }
            });
        } catch (Exception e) {
            log.warning("Polling failure");
        }
    }

    public void refresh() {
        if (admin) {
            refreshBoardsListButton.setVisible(true);
            leaveBoardButton.setVisible(false);
        } else {
            refreshBoardsListButton.setVisible(false);
            leaveBoardButton.setVisible(true);
        }

        try {
            showBoardsList();
        } catch (IOException e) {
            log.warning("Couldn't show boards on refresh");
        }
        if (board == null) {
            initializeBoard();
        }
        board.update();
        board.updateChildren();
    }

    public void refreshWithBoard(Board b) {
        if (b.getId() == board.getBoard().getId())
            board.updateWithNewBoard(b);
    }

    public void overWriteWithModel() {
        boardName.setText(board.getBoard().getTitle());
        updateBoardColors(getBoardColor());
    }

    @FXML
    public void optionsShowServerChoice(ActionEvent event) {
        mainCtrl.showServerChoice();
    }

    @FXML
    public void optionsShowCustomizationMenu(ActionEvent event)  {
        showCustomizationMenu();
    }

    public void showCustomizationMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomizationMenu.fxml"));
            loader.setController(new CustomizationMenuController(this));
            Parent root = loader.load();

            customizationStage = new Stage();
            customizationStage.setScene(new Scene(root));
            customizationStage.initModality(Modality.APPLICATION_MODAL);
            customizationStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getCustomizationStage() {
        return customizationStage;
    }

    public HBox getCardsListContainer() {
        return cardListsContainer;
    }

    public void setBoardColorFXML(String color) {
        var colorCode = Color.valueOf(color);
        var fill = new Background(new BackgroundFill(colorCode, null, null));
        boardTop.setBackground(fill);
        boardBottom.setBackground(fill);
        boardsListAnchorPane.setBackground(fill);

        var desaturated = colorCode.desaturate().desaturate();
        var fillDesaturated = new Background(new BackgroundFill(desaturated, null, null));
        cardListsContainer.setBackground(fillDesaturated);
        boardScrollPane.setBackground(fillDesaturated);
        boardListScrollPane.setBackground(fillDesaturated);
    }

    public void setBoardFontFXML(String color) {
        var colorCode = Color.valueOf(color);

        var styleStr = "-fx-text-fill: " + toHexString(colorCode)
                + " !important; -fx-background-color: inherit !important;";
        boardName.setStyle(styleStr);
        addListButton.setStyle(styleStr);
        refreshBoardsListButton.setStyle(styleStr);
        tagsButton.setStyle(styleStr);
        boardIdLabelText.setStyle(styleStr);
        shareButton.setStyle(styleStr);
        boardListScrollPane.setStyle(styleStr);
        boardsListAnchorPane.setStyle(styleStr);
        addBoardButton.setStyle(styleStr);
    }

    @FXML
    public void addListButton(ActionEvent event) throws IOException {
        if (board.getBoard().getLists().isEmpty() && !cardListsContainer.getChildren().isEmpty()) {
            cardListsContainer.getChildren().clear();
        }
        ListModel model = new ListModel(new CardList("New List"), board, server);
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

    public List<BoardIdWithColors> getBoardList() {
        return boardList;
    }

    public UserUtils getUserUtils() {
        return userUtils;
    }

    public BoardModel getModel() {
        return board;
    }

    public void showEmptyBoardPrompt() {
        cardListsContainer.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EmptyBoardPrompt.fxml"));
        Node prompt = null;
        try {
            prompt = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cardListsContainer.getChildren().add(prompt);
    }

    public void showBoard() {
        if (board.getBoard().getLists().isEmpty() && !cardListsContainer.getChildren().isEmpty()) {
            cardListsContainer.getChildren().clear();
        }
        if (board == null) return;
        boardName.setText(board.getBoard().getTitle());
        boardIdPanel.setVisible(false);
        this.board.setController(this);

        userUtils.setMyId((int) board.getBoard().getId());

        board.update();
        board.updateChildren();

        if (this.board.getChildren().isEmpty()) {
            showEmptyBoardPrompt();
        }
        try {
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
        } catch (Exception e) {
            log.warning("Websockets failure");
        }
    }

    public void setBoardOverview(long id) throws IOException {
        var res = server.getBoardById(id);
        if (res.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("This board no longer exists");
            alert.showAndWait();

            boardList.removeIf((board) -> board.getBoardId() == id);
            userUtils.updateUserBoards(boardList);

            initializeBoard();
            showBoardsList();
            return;
        }
        board.updateWithNewBoard(res.get());

        showBoard();
    }

    public void initializeBoard() {
        var res = server.getBoardById(1);
        if (res.isPresent()) {
            board = new BoardModel(res.get(), server);
            board.setController(this);
        } else {
            Board toAdd = new Board();
            toAdd.setTitle("Default Board");
            var added = server.addBoard(toAdd);
            if (added.isEmpty())
                throw new RuntimeException("Server Request failed");
            board = new BoardModel(added.get(), server);
            board.setController(this);
        }
    }

    public void getAllBoardsIds() {
        var newBoardList = server.getBoards();
        if (newBoardList.isEmpty()) {
            log.warning("Something went wrong fetching the boards");
        } else {
            var boardListIds = newBoardList.get().stream().map(Board::getId).toList();
            boardList.clear();
            for(Long id: boardListIds) {
                boardList.add(new BoardIdWithColors(id));
            }
        }
    }

    //call getAllBoardsIds or userUtils.getUserBoardsIds() before this one
    public void showBoardsList() throws IOException {
        if (boardList == null) return;
        if (admin) {
            getAllBoardsIds();
        }
        boardsListContainer.getChildren().clear();
        boardListControllers.clear();
        for (var newBoard : boardList) {
            addBoardListItemToList(newBoard);
        }

    }

    public void addBoardListItemToList(BoardIdWithColors props) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardsListItem.fxml"));

        var controller = new BoardsListItemCtrl(props, this);
        loader.setController(controller);

        AnchorPane toAdd = loader.load();

        boardsListContainer.getChildren().add(toAdd);
        boardListControllers.add(controller);
        boardListControllers.add(controller);
    }

    @FXML
    public void addBoardButton(ActionEvent event) throws IOException {
        if (admin) {
            var b = server.addBoard(new Board());
            if (b.isEmpty()) {
                log.warning("Failed to add board");
            } else {
                setBoardOverview(b.get().getId());
                showBoardsList();
            }
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewBoardScene.fxml"));
                fxmlLoader.setController(new AddBoardController(this));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void deleteBoardButton(ActionEvent event) throws IOException {
        //TODO make custom alerts. Default ones are ugly.
        if (board.getBoard().getId() == 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Default board cannot be deleted");

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
            if(!admin){
                boardList.removeIf(b -> b.getBoardId() == board.getBoard().getId());
                userUtils.updateUserBoards(boardList);
            }
            showBoardsList();
            initializeBoard();
            setBoardOverview(board.getBoard().getId());
        } else {
            return;
        }
    }

    @FXML
    public void leaveBoardButton(ActionEvent event) throws IOException {
        if (board.getBoard().getId() == 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Cannot leave default board");

            alert.showAndWait();
            return;
        }
        boardList.removeIf(b -> b.getBoardId() == board.getBoard().getId());
        userUtils.updateUserBoards(boardList);
        showBoardsList();
        initializeBoard();
        setBoardOverview(board.getBoard().getId());
    }

    @FXML
    public void shareButton(ActionEvent event) {
        boolean visibility = boardIdPanel.isVisible();
        boardIdPanel.setVisible(!visibility);
        boardIdLabel.setText(Long.toString(board.getBoard().getId()));
    }

    public void copyIdButton(ActionEvent event) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(boardIdLabel.getText());
        clipboard.setContents(selection, selection);
    }


    public ServerUtils getServer() {
        return server;
    }

    public void setAdmin(boolean b) {
        admin = b;
    }

    public boolean getAdmin() {
        return admin;
    }

    @FXML
    public void showTagsButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TagOverview.fxml"));
        fxmlLoader.setController(new TagOverviewCtrl(this));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void shutDown() {
        server.getSocketUtils().disconnect();
        server.getPollingUtils().stopLongPolling();
    }


    public ColorPair getCardColor(long id) {
        var cardData = getColors().getCardHighlightColors().get(id);

        if (cardData == null) {
            return getColors().getCardPair();
        }

        return cardData;
    }

    public ColorPair getListColor() {
        var color = getColors();
        if (color == null)
            return new ColorPair();
        return color.getListPair();
    }

    public ColorPair getBoardColor() {
        var color = getColors();
        if (color == null)
            return new ColorPair();
        return color.getBoardPair();
    }

    public ColorPair getDefaultBoardColor() {
        return new BoardIdWithColors(1).getBoardPair();
    }

    public ColorPair getDefaultListColor() {
        return new BoardIdWithColors(1).getListPair();
    }

    public ColorPair getDefaultCardColor() {
        return new BoardIdWithColors(1).getCardPair();
    }

    public void globalColorUpdate() {
        updateBoardColors(getColors().getBoardPair());

        for (var list : board.getChildren()) {
            var ctrl = (ListController) list.getController();
            ctrl.updateListColors(getColors().getListPair());

            for (var card : list.getChildren()) {
                var cardCtrl = (CardController) card.getController();

                cardCtrl.updateCardColors(getCardColor(card.getCard().getId()));
            }
        }
    }

    public void updateBoardOverviewColors() {
        try {
            boardListControllers.forEach(x -> {
                x.updateProps(userUtils.getUserBoardsIds().stream()
                        .filter(y -> y.getBoardId() == x.getProps().getBoardId())
                        .findFirst().get());
            });
        } catch (Exception e) {
            log.severe("Failed to update board overview colors");
        }
    }

    public void updateBoardColors(ColorPair pair) {
        setBoardColorFXML(pair.getBackground());
        setBoardFontFXML(pair.getFont());
        updateIcons();

        updateBoardOverviewColors();
    }

    public void updateIcons() {
        SceneTools.applyToEveryNode(pageRoot, (Node x) -> {
            if (x instanceof ImageView settable) {
                var color = getBoardColor().getFont();
                settable.setImage(recolorImage(settable.getImage(), Color.valueOf(color)));
            }
        });
    }

    public void destroy() {
        cardListsContainer.getChildren().clear();
    }
}
