package client.utils;

import commons.Board;
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

    @AfterEach
    public void tearDown() {
        File file = new File("file.json");
        if (!file.delete())
            throw new RuntimeException("Failed to delete file");
    }

//    @Test
//    void getUserBoardsIdsTest() {
//        var ids = userUtils.getUserBoardsIds();
//        assertEquals(ids.size(), 1);
//    }
//
//    @Test
//    void updateUserBoardsTest() {
//        var ids = userUtils.getUserBoardsIds();
//        assertEquals(ids.size(), 1);
//
//        userUtils.updateUserBoards(List.of(2L, 3L));
//        assertEquals(ids.size(), 1);
//    }
}
