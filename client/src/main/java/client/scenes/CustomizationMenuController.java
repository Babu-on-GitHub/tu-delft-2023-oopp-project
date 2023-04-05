package client.scenes;

import client.model.BoardModel;
import client.model.ListModel;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;


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

    public void closeStage(ActionEvent event) {
        mainPageCtrl.getModel().update();
        mainPageCtrl.getCustomizationStage().close();
        boardBackground.setValue(Color.WHITE);
    }

    public void applyChanges(ActionEvent event) {
        // display updated board
        mainPageCtrl.showBoard();
        // we need this because changing the colour twice breaks the colours
        boardBackground.setValue(Color.WHITE);
    }

    public String makeColorString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));
        return String.format("0x%08X", (r + g + b + a));
    }


    public void setBoardBackground(ActionEvent event) {
        BoardModel model = mainPageCtrl.getModel();
        Board board = model.getBoard();

        Color color = boardBackground.getValue();
        String colorString = this.makeColorString(color);
        board.setBoardColor(colorString);

        board.sync(); // make client board's timestamp newer
        model.update(); // hence update board on the database
    }

    public void resetBoardBackground(ActionEvent event) {
        BoardModel model = mainPageCtrl.getModel();
        Board board = model.getBoard();

        String colorString = "0x91B7BF";
        board.setBoardColor(colorString);

        board.sync(); //make client board's timestamp newer
        model.update(); // hence update board on the database
        mainPageCtrl.showBoard(); // display updated board
    }

    public void boardPreset1(ActionEvent event) {
        BoardModel model = mainPageCtrl.getModel();
        Board board = model.getBoard();

        String colorString = "0x22ee33";
        //also have ist and card colours
        board.setBoardColor(colorString);

        board.sync(); //make client board's timestamp newer
        model.update(); //hence update board on the database
        mainPageCtrl.showBoard(); //display updated board
    }

    public void setListBackground(ActionEvent event) {
        BoardModel model = mainPageCtrl.getModel();
        Board board = model.getBoard();

        Color color = listBackground.getValue();
        String colorString = this.makeColorString(color);
        board.setListColor(colorString);

        board.sync();
        model.update();
    }

    public void resetListBackground(ActionEvent event) {
        BoardModel model = mainPageCtrl.getModel();
        Board board = model.getBoard();

        String colorString = "0xD2A295";
        board.setListColor(colorString);

        board.sync(); //make client board's timestamp newer
        model.update(); // hence update board on the database
        mainPageCtrl.showBoard(); // display updated board
    }

    public void setCardBackground(ActionEvent event) {
        BoardModel model = mainPageCtrl.getModel();
        List<ListModel> listModels = model.getChildren();
        Board board = model.getBoard();

        Color color = cardBackground.getValue();
        String colorString = this.makeColorString(color);
        board.setCardColor(colorString);

        board.sync();
        model.update();
        //todo: apply card color when drag and drop is used
    }

    public void resetCardBackground(ActionEvent event) {
        BoardModel model = mainPageCtrl.getModel();
        Board board = model.getBoard();

        String colorString = "0xF7EFD2";
        board.setCardColor(colorString);

        board.sync(); //make client board's timestamp newer
        model.update(); // hence update board on the database
        mainPageCtrl.showBoard(); // display updated board
    }
}
