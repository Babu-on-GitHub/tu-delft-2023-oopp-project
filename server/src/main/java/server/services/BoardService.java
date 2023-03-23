package server.services;

import commons.Board;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.List;

@Service
public class BoardService {

    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    public Board getBoardById(long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public void deleteBoardById(long id) {
        boardRepository.deleteById(id);
    }
}
