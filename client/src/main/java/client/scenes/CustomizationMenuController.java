package client.scenes;

import client.model.BoardModel;
import client.model.ListModel;
import client.utils.UserUtils;
import commons.Board;
import commons.BoardIdWithColors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.List;


public class CustomizationMenuController implements Initializable {

    private MainPageCtrl mainPageCtrl;

    private UserUtils userUtils;

    @FXML
    private ColorPicker boardBackground;

    @FXML
    private ColorPicker listBackground;

    @FXML
    private ColorPicker cardBackground;

    @FXML
    private ColorPicker boardFont;

    @FXML
    private ColorPicker listFont;

    @FXML
    private ColorPicker cardFont;

    private BoardIdWithColors colorState;

    public CustomizationMenuController(MainPageCtrl mainPageCtrl) {
        this.mainPageCtrl = mainPageCtrl;
        try {
            // find the board with the id
            var thisBoardColors = mainPageCtrl.getColors();

            if (thisBoardColors == null)
                throw new Exception("Board not found, impossible");

            colorState = thisBoardColors.clone();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeStage(ActionEvent event) {
        mainPageCtrl.getCustomizationStage().close();
    }

    public void applyChanges(ActionEvent event) {
        mainPageCtrl.getUserUtils().updateSingleBoard(colorState);
        mainPageCtrl.globalColorUpdate();
    }

    public void done(ActionEvent event) {
        applyChanges(event);
        closeStage(event);
    }

    public String makeColorString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));
        return String.format("0x%08X", (r + g + b + a));
    }

    public String makeColorStringWithHashtag(Color color) {
        return "#" + makeColorString(color).substring(2);
    }

    public void setBoardBackground(ActionEvent event) {
        Color color = boardBackground.getValue();
        String colorString = this.makeColorString(color);

        colorState.getBoardPair().setBackground(colorString);
    }

    public void resetBoardBackground(ActionEvent event) {
        try {
            colorState.setBoardPair(mainPageCtrl.getDefaultBoardColor().clone());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        syncUIWithLocalState();
    }

    public void setListBackground(ActionEvent event) {
        Color color = listBackground.getValue();
        String colorString = this.makeColorString(color);

        colorState.getListPair().setBackground(colorString);
        syncUIWithLocalState();
    }

    public void resetListBackground(ActionEvent event) {
        try {
            colorState.setListPair(mainPageCtrl.getDefaultListColor().clone());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        syncUIWithLocalState();
    }

    public void setCardBackground() {
        Color color = cardBackground.getValue();
        String colorString = this.makeColorString(color);

        colorState.getCardPair().setBackground(colorString);
        syncUIWithLocalState();
    }

    public void resetCardBackground(ActionEvent event) {
        try {
            colorState.setCardPair(mainPageCtrl.getDefaultCardColor().clone());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        syncUIWithLocalState();
    }

    public void setBoardFont(ActionEvent event) {
        Color color = boardFont.getValue();
        String colorString = this.makeColorString(color);

        colorState.getBoardPair().setFont(colorString);
        syncUIWithLocalState();
    }

    public void setListFont(ActionEvent event) {
        Color color = listFont.getValue();
        String colorString = this.makeColorString(color);

        colorState.getListPair().setFont(colorString);
        syncUIWithLocalState();
    }

    public void setCardFont(ActionEvent event) {
        Color color = cardFont.getValue();
        String colorString = this.makeColorString(color);

        colorState.getCardPair().setFont(colorString);
        syncUIWithLocalState();
    }

    public void syncUIWithLocalState() {
        boardBackground.setValue(Color.web(colorState.getBoardPair().getBackground()));
        listBackground.setValue(Color.web(colorState.getListPair().getBackground()));
        cardBackground.setValue(Color.web(colorState.getCardPair().getBackground()));
        boardFont.setValue(Color.web(colorState.getBoardPair().getFont()));
        listFont.setValue(Color.web(colorState.getListPair().getFont()));
        cardFont.setValue(Color.web(colorState.getCardPair().getFont()));
    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        syncUIWithLocalState();
    }
}
