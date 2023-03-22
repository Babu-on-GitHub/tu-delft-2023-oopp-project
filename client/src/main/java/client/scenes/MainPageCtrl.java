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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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

        if (board == null) {
            var res = server.getBoardById(1);
            if (res.isPresent()) {
                board = new BoardModel(res.get());
                board.setController(this);
            } else {
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
    public void optionsShowServerChoice(ActionEvent event) {
        mainCtrl.showServerChoice();
    }

    @FXML
    public void addListButton(ActionEvent event) throws IOException {
        ListModel model = new ListModel(new CardList(), board);
        addList(model); // important: keep order of these two the same
        board.addList(model);
    }

    public void recreateChildren(List<ListModel> arr) throws IOException {
        listOfLists.getChildren().clear();
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

    public HBox getListsContainer() {
        return listOfLists;
    }

    public BoardModel getModel() {
        return board;
    }
}
