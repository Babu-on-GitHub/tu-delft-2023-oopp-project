package client.scenes;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class BoardsListItemCtrl implements Initializable {

    @FXML
    private ImageView icon;

    private MainPageCtrl mainPageCtrl;

    private long boardId;

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
        if(boardId == 1){
            setImage(icon,"client/src/main/resources/client/icons/home_FILL0_wght400_GRAD0_opsz48.png");
        }else{
            setImage(icon,"client/src/main/resources/client/icons/layers_FILL0_wght400_GRAD0_opsz48.png");
        }
    }
}
