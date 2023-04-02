package server.services;

import server.repository.TestBoardRepository;

import static org.mockito.Mockito.mock;

public class TestSynchronizationService extends SynchronizationService {
    public TestSynchronizationService(BoardService boardService) {
        super(boardService);
    }

    public TestSynchronizationService() {
        super(mock(BoardService.class));

        this.boardService = new BoardService(new TestBoardRepository(), this);
    }
}