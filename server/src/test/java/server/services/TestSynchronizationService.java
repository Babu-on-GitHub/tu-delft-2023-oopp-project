package server.services;

import server.repository.TestBoardRepository;

public class TestSynchronizationService extends SynchronizationService {
    public TestSynchronizationService() {
        boardService = new BoardService(new TestBoardRepository(), this);
    }

}