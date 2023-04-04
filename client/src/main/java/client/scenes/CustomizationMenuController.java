package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;



public class CustomizationMenuController {

    private MainPageCtrl mainPageCtrl;

    @FXML
    private ColorPicker boardBackground;

    @FXML
    private ColorPicker listBackground;

    @FXML
    private ColorPicker cardBackground;


    public CustomizationMenuController(MainPageCtrl mainPageCtrl) {
        this.mainPageCtrl = mainPageCtrl;
    }



    public void setBoardBackground(ActionEvent event) {



        //mainPageCtrl.getBoard().setBoardColor(convertColor(boardBackground.getValue()));
//        var fxColor = boardBackground.getValue();
//        var color =  convertColor(boardBackground.getValue());
//        mainPageCtrl.getBoard().setBoardColor(color);
//        boardBackground.setValue(fxColor); //to keep it with that color?
//        mainPageCtrl.getBoardTop().setBackground(new Background(new BackgroundFill(fxColor, null, null)));
//        mainPageCtrl.getBoardBottom().setBackground(new Background(new BackgroundFill(fxColor, null, null)));
    }

    public void resetBoardBackground(ActionEvent event) {
        boardBackground.getValue().getBlue();
        //mainPageCtrl.getBoardTop().setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        //mainPageCtrl.getBoardBottom().setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }

    public void setListBackground(ActionEvent event) {
        var color = listBackground.getValue();
        var lists = mainPageCtrl.getCardsListContainer().getChildren();

        for (Node list : lists) {
            var listBorderPane = (BorderPane) list;

            var listTop = (TextField) ((AnchorPane) listBorderPane.getTop()).getChildren().get(0);
            listTop.setBackground(new Background(new BackgroundFill(color, null, null)));

            var listBottom = (AnchorPane) listBorderPane.getBottom();
            listBottom.setBackground(new Background(new BackgroundFill(color, null, null)));
        }
    }

    public void resetListBackground(ActionEvent event) {
        var lists = mainPageCtrl.getCardsListContainer().getChildren();

        for (Node list : lists) {
            var listBorderPane = (BorderPane) list;

            var listTop = (TextField) ((AnchorPane) listBorderPane.getTop()).getChildren().get(0);
            //listTop.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

            var listBottom = (AnchorPane) listBorderPane.getBottom();
            //listBottom.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        }
    }

    public void setCardBackground(ActionEvent event) {
        var color = cardBackground.getValue();
        var lists = mainPageCtrl.getCardsListContainer().getChildren();
        for (Node list : lists) {
            var listVBox = ((VBox) ((ScrollPane) ((BorderPane) list).getCenter()).getContent()).getChildren();

            for (Node card : listVBox) {
                var cardAnchorPane = (AnchorPane) card;
                cardAnchorPane.setBackground(new Background(new BackgroundFill(color, null, null)));
            }
        }
    }

    public void resetCardBackground(ActionEvent event) {
        var lists = mainPageCtrl.getCardsListContainer().getChildren();
        for (Node list : lists) {
            var listVBox = ((VBox) ((ScrollPane) ((BorderPane) list).getCenter()).getContent()).getChildren();

            for (Node card : listVBox) {
                var cardAnchorPane = (AnchorPane) card;
                //cardAnchorPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            }
        }
    }
}
