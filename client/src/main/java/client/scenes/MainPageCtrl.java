package client.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageCtrl implements Initializable {
//    private final ServerUtils server;
//
//    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardName;

    @FXML
    private VBox list;

    @FXML
    private HBox listOfLists;

    @FXML
    private HBox listButtons;

    private int listButtonCounter;

    private int listIdCounter;

    //for card

    private int cardIdCounter;

    private int deleteCardButtonCounter;

    private int detailsButtonCounter;


//    @Inject
//    public MainPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
//        this.server = server;
//        this.mainCtrl = mainCtrl;
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Default board");

        //deleteListButton.setGraphic(new FontIcon(Feather.TRASH));
        //deleteCardButton.setGraphic(new FontIcon(Feather.TRASH));

        //addCardButton.setGraphic(new FontIcon(Feather.PLUS));
        //addListButton.setGraphic(new FontIcon(Feather.PLUS));
    }

    public void refresh() {
        // do nothing
    }

    public VBox getList() {
        return list;
    }

    @FXML
    public void addListButton(ActionEvent event) throws IOException {
        System.out.println("test button click");

        FXMLLoader loader = new FXMLLoader(MainPageCtrl.class.getResource("List.fxml"));
        loader.setController(this);
        VBox newList = loader.load();
        listIdCounter++;
        newList.setId("list" + listIdCounter);

        listOfLists.getChildren().add(newList);

    }

    @FXML
    public void addCardButtonPress(ActionEvent event) throws IOException {
        System.out.println("test button click add card");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/Card.fxml"));
        loader.setController(this);
        System.out.println(loader.getController().toString());
        StackPane newCard = loader.load();

        cardIdCounter++;
        newCard.lookup("#card").setId("card" + cardIdCounter);

        deleteCardButtonCounter++;
        newCard.lookup("#deleteButton").setId("deleteButton" + deleteCardButtonCounter);

        detailsButtonCounter++;
        newCard.lookup("#detailsButton").setId("detailsButton" + detailsButtonCounter);

        list.getChildren().add(newCard);
    }

    @FXML
    public void deleteCardButton(ActionEvent event) {
//        if(!list.getChildren().isEmpty()) {
//
//            list.lookup()
//            list.getChildren().get(list.lookup("#"))
//
//
//            list.getChildren().remove(list.getChildren().size() - 1);
//        }
        Button delete = (Button) event.getSource();
        StackPane remove = (StackPane) delete.getParent().getParent().getParent();
        list.getChildren().remove(remove);
    }

    @FXML
    public void deleteListButton(ActionEvent event) {

        if(!listOfLists.getChildren().isEmpty()) {
//            String listId = list.getId();
//            System.out.println(listId);


//            ObservableList<Node> list = listOfLists.getChildren();
//            while (node != null){
//                node = node.getParent();
//            }
//            Node parentNode = node;
//            Node toBeDeleted = mainCtrl.getMainPage().lookup("#");
            listOfLists.getChildren().remove(listOfLists.getChildren().size() - 1);

//            Button delete = (Button) event.getSource();
//            ScrollPane fullList = (ScrollPane) delete.getParent().getParent().getParent();
//            fullList.getContent().remove(fullList);
        }
    }



}
