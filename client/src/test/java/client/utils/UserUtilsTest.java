package client.utils;

import commons.Board;
import commons.BoardIdWithColors;
import commons.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserUtilsTest {
    private UserUtils userUtils;

    @BeforeEach
    public void setUp() {
        userUtils = new UserUtils(new TestServerUtils());
        userUtils.setUserFile("file.json");
    }

    @Test
    public void testUpdateSingleBoard() {
        var board = new BoardIdWithColors(1L);
        userUtils.updateSingleBoard(board);
        var boards = userUtils.getUserBoardsIds();
        assertEquals(1, boards.size());
    }

    @Test
    public void clearHighlightTest() {
        var board = new BoardIdWithColors(1L);
        userUtils.updateSingleBoard(board);
        userUtils.clearHighlight();
        var boards = userUtils.getUserBoardsIds();
        assertEquals(1, boards.size());
        assertEquals(0, boards.get(0).getCardHighlightColors().size());
    }
}
