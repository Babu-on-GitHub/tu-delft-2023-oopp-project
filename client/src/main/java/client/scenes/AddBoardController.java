package client.scenes;

import client.utils.ServerUtils;
import client.utils.UserUtils;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AddBoardController {

    @FXML
    private TextField idBar;
    @FXML
    private TextField titleBar;

    private ServerUtils utils;

    private MainPageCtrl parent;

    private UserUtils userUtils;



    public AddBoardController(MainPageCtrl parent){
        this.parent = parent;
        utils = parent.getServer();
        userUtils = parent.getUserUtils();
    }

    @FXML
    public void joinButton(ActionEvent event) throws IOException {
        try{
            long id = Long.parseLong(idBar.getText());
            Optional<Board> added = utils.getBoardById(id);
            if (added.isEmpty()) {
                idBar.setStyle("-fx-border-color: red;");
                return;
            }
            List<Long> boards = parent.getBoardList();
            if(boards.stream().filter(q->q == added.get().getId()).findAny().isEmpty()){
                boards.add(added.get().getId());
                userUtils.updateUserBoards(parent.getBoardList());
                parent.addBoardListItemToList(added.get().getId());
            }
            closeWindow(event);
        } catch (NumberFormatException | IOException e) {
            idBar.setStyle("-fx-border-color: red;");
        }
    }

    public void createButton(ActionEvent event) throws IOException {
        if(titleBar.getText().isEmpty()){
            titleBar.setStyle("-fx-border-color: red;");
            return;
        }
        Board board = new Board();
        board.setTitle(titleBar.getText());
        var added = utils.addBoard(board);
        if (added.isEmpty()) {
            titleBar.setStyle("-fx-border-color: red;");
            return;
        }
        parent.getBoardList().add(added.get().getId());
        userUtils.updateUserBoards(parent.getBoardList());
        parent.addBoardListItemToList(added.get().getId());
        closeWindow(event);
    }

    public void closeWindow(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }



}
