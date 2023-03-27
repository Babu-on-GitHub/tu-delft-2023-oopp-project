package client.scenes;


import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class BoardsListItemCtrl implements Initializable {

    @FXML
    private ImageView icon;

    @FXML
    private Button boardItemButton;

    private MainPageCtrl mainPageCtrl;

    private long boardId;

    private String boardName;

    public BoardsListItemCtrl(long boardId,MainPageCtrl mainPageCtrl) {
        this.mainPageCtrl = mainPageCtrl;
        this.boardId = boardId;
    }

    public void setBoardButton() throws IOException {
        mainPageCtrl.setBoardOverview(boardId);
    }

    private void setImage(ImageView img, String path){
        File file = new File(path);
        Image image = new Image(file.toURI().toString());
        img.setImage(image);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServerUtils utils = mainPageCtrl.getServer();
        var board = utils.getBoardById(boardId);
        if(board.isEmpty()){
            boardName = "???";
        }
        else{
            boardName = board.get().getTitle();
        }
        boardItemButton.setText(boardName);
        if(boardId == 1){
            setImage(icon,"client/src/main/resources/client/icons/home_FILL0_wght400_GRAD0_opsz48.png");
        }else{
            setImage(icon,"client/src/main/resources/client/icons/layers_FILL0_wght400_GRAD0_opsz48.png");
        }
    }
}
