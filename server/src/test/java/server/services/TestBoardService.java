package server.services;

import server.database.BoardRepository;

public class TestBoardService extends BoardService {

    public TestBoardService(BoardRepository boardRepository, SynchronizationService synchronizationService) {
        super(boardRepository, synchronizationService);
    }
}
