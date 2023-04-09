package client.scenes;

import client.utils.UserUtils;
import commons.BoardIdWithColors;
import commons.ColorPair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static client.utils.ColorTools.makeColorString;
import static client.utils.ColorTools.toHexString;

public class CustomizationMenuController implements Initializable {

    private MainPageCtrl mainPageCtrl;

    private UserUtils userUtils;

    @FXML
    private AnchorPane customizationMenuAnchorPane;

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

    @FXML
    private Label backgroundLabel;

    @FXML
    private Label fontLabel;

    @FXML
    private Label boardLabel;

    @FXML
    private Label listLabel;

    @FXML
    private Label cardLabel;

    @FXML
    private Label customizationLabel;

    @FXML
    private Button boardColorReset;

    @FXML
    private Button listColorReset;

    @FXML
    private Button cardColorReset;

    @FXML
    private Button doneButton;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;


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


    public String makeColorStringWithHashtag(Color color) {
        return "#" + makeColorString(color).substring(2);
    }

    public void setBoardBackground(ActionEvent event) {
        Color color = boardBackground.getValue();
        String colorString = makeColorString(color);

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
        String colorString = makeColorString(color);

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
        String colorString = makeColorString(color);

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
        String colorString = makeColorString(color);

        colorState.getBoardPair().setFont(colorString);
        syncUIWithLocalState();
    }

    public void setListFont(ActionEvent event) {
        Color color = listFont.getValue();
        String colorString = makeColorString(color);

        colorState.getListPair().setFont(colorString);
        syncUIWithLocalState();
    }

    public void setCardFont(ActionEvent event) {
        Color color = cardFont.getValue();
        String colorString = makeColorString(color);

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

        updateColors(mainPageCtrl.getBoardColor());
    }

    private void updateColors(ColorPair colorPair) {
        setCustomizationMenuBackgroundFXML(colorPair.getBackground());
        setCustomizationMenuFontFXML(colorPair.getFont());
    }

    private void setCustomizationMenuFontFXML(String color) {
        var colorCode = Color.valueOf(color);

        var styleStr = "-fx-text-fill: " + toHexString(colorCode) + " !important; " +
                "-fx-background-color: inherit !important;" +
                "-fx-border-radius: 10px !important; " ;
        var styleStrWithoutBorder = "-fx-text-fill: " + toHexString(colorCode) + " !important; " +
                "-fx-background-color: inherit !important;" ;
        boardLabel.setStyle(styleStrWithoutBorder);
        cardLabel.setStyle(styleStrWithoutBorder);
        listLabel.setStyle(styleStrWithoutBorder);
        backgroundLabel.setStyle(styleStrWithoutBorder);
        fontLabel.setStyle(styleStrWithoutBorder);
        customizationLabel.setStyle(styleStrWithoutBorder);
        applyButton.setStyle(styleStr);
        doneButton.setStyle(styleStr);
        cancelButton.setStyle(styleStr);
        boardColorReset.setStyle(styleStr);
        listColorReset.setStyle(styleStr);
        cardColorReset.setStyle(styleStr);
    }

    public void setCustomizationMenuBackgroundFXML(String color) {
        var colorCode = Color.valueOf(color);
        var darker = colorCode.darker();
        var fill = new Background(new BackgroundFill(colorCode, null, null));
        var darkerFill = new Background(new BackgroundFill(darker, new CornerRadii(10), null));
        customizationMenuAnchorPane.setBackground(fill);
        boardLabel.setBackground(fill);
        cardLabel.setBackground(fill);
        listLabel.setBackground(fill);
        backgroundLabel.setBackground(fill);
        fontLabel.setBackground(fill);
        customizationLabel.setBackground(fill);
        applyButton.setBackground(darkerFill);
        doneButton.setBackground(darkerFill);
        cancelButton.setBackground(darkerFill);
        boardColorReset.setBackground(darkerFill);
        listColorReset.setBackground(darkerFill);
        cardColorReset.setBackground(darkerFill);
        var styleStrWithoutBorder = "-fx-text-fill: " + toHexString(colorCode) + " !important; " +
                "-fx-background-color: "+ toHexString(darker) +" !important;" ;
        boardBackground.setBackground(darkerFill);
        boardFont.setBackground(darkerFill);
        listBackground.setBackground(darkerFill);
        listFont.setBackground(darkerFill);
        cardBackground.setBackground(darkerFill);
        cardFont.setBackground(darkerFill);
    }
}
