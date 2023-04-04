package server.services;

import commons.Board;
import commons.CardList;
import commons.Tag;
import org.springframework.stereotype.Service;
import server.api.BoardController;
import server.database.BoardRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class BoardService {
    private static final Logger log = Logger.getLogger(BoardController.class.getName());

    private BoardRepository boardRepository;
    private SynchronizationService synchronizationService;

    public BoardService(BoardRepository boardRepository, SynchronizationService synchronizationService) {
        this.boardRepository = boardRepository;
        this.synchronizationService = synchronizationService;
    }

    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    public Board getBoardById(long id) {
        if (id < 0 || boardRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Invalid board id:" + id);
        }
        var board = boardRepository.findById(id).get();
        return board;
    }

    public Board saveBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }

        var b = boardRepository.save(board);
        synchronizationService.addBoardToUpdate(b.getId());
        return b;
    }

    public CardList saveCardList(CardList list, long boardId) {
        if (list == null) {
            throw new IllegalArgumentException("Trying to add null list");
        }

        try {
            getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to add list into non-existent board");
        }

        var board = this.getBoardById(boardId);
        list.sync();
        board.getLists().add(list);

        board.sync();
        this.saveBoard(board);
        var ret = board.getLists().get(board.getLists().size() - 1);

        return ret;
    }

    public void deleteBoardById(long id) {
        if (id < 0 || boardRepository.findById(id).orElse(null) == null) {
            throw new IllegalArgumentException("Invalid board id:" + id);
        }

        if (id == 1)
            throw new IllegalArgumentException("Deleting default board is prohibited");

        boardRepository.deleteById(id);
    }

    public void deleteCardListById(long listId, long boardId) {
        try {
            var boardOptional = this.getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to delete list from non-existent board");
        }

        var board = this.getBoardById(boardId);
        var lists = board.getLists();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId() == listId) {
                lists.remove(i);
                board.sync();
                this.saveBoard(board);
                return;
            }
        }
        throw new IllegalArgumentException("Trying to delete non-existent list");
    }

    public void moveCard(long cardId, long listId, int index, long boardId) {
        try {
            var boardOptional = this.getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to move card in non-existent board");
        }

        var board = this.getBoardById(boardId);

        var lists = board.getLists();
        var targetListOpt = lists.stream()
                .filter(list -> list.getId() == listId).findFirst();
        if (targetListOpt.isEmpty()) {
            throw new IllegalArgumentException("Trying to move card to non-existent list");
        }
        var targetList = targetListOpt.get();

        // find the card with correct ID anywhere
        var cardOpt = lists.stream()
                .flatMap(list -> list.getCards().stream())
                .filter(card -> card.getId() == cardId).findFirst();
        if (cardOpt.isEmpty()) {
            throw new IllegalArgumentException("Trying to move non-existent card");
        }

        var card = cardOpt.get();

        // remove the card from the list it is currently in
        var sourceListOpt = lists.stream()
                .filter(list -> list.getCards().contains(card)).findFirst();
        if (sourceListOpt.isEmpty()) {
            throw new IllegalArgumentException("Something went wrong while moving card");
        }

        var sourceList = sourceListOpt.get();
        sourceList.getCards().remove(card);

        if (index > targetList.getCards().size()) {
            log.info("Index out of bounds, moving to the end of list");
            index = targetList.getCards().size();
        }

        targetList.getCards().add(index, card);
        board.sync();
        sourceList.sync();
        targetList.sync();
        saveBoard(board);
    }

    public Board update(Board board, long id) {
        if (board == null) {
            throw new IllegalArgumentException("Board is null");
        }
        log.info("Updating board: " + board.getId());
        try {
            var boardOptional = this.getBoardById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to update non existing board");
        }

        if (id != board.getId()) {
            throw new IllegalArgumentException("Ids are not coherent in board update");
        }

        try {
            var stored = this.getBoardById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Something went wrong while updating board");
        }

        board.sync();
        return this.saveBoard(board);
    }

    public String updateTitle(String title, long id) {
        if (title == null) {
            throw new IllegalArgumentException("Title is null");
        }
        log.info("Updating board title: " + title);

        Board board;
        try {
            board = this.getBoardById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to update non existing board");
        }

        board.setTitle(title);
        board.sync();
        this.saveBoard(board);
        return title;
    }

    public Tag addTag(Tag tag, long boardId) {
        if (tag == null) {
            throw new IllegalArgumentException("Trying to add null tag");
        }

        try {
            getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to add tag into non-existent board");
        }

        var board = this.getBoardById(boardId);

        // if the tag with this ID already exists, throw an exception
        if (board.getTags().stream().anyMatch(t -> t.getId() == tag.getId())) {
            throw new IllegalArgumentException("Trying to add tag with duplicate ID");
        }

        // save the tags
        var oldTags = Set.copyOf(board.getTags());

        board.getTags().add(tag);
        board.sync();

        var boardCopy = this.saveBoard(board);
        var newTags = new HashSet<>(Set.copyOf(boardCopy.getTags()));

        // subtract old tags from new tags
        newTags.removeAll(oldTags);

        // the remaining tag is the one we just added
        if (newTags.size() != 1)
            throw new IllegalArgumentException("Something went wrong while adding tag, most likely duplicate," +
                    " there are " + newTags.size() + " tags in the list");

        return newTags.iterator().next();
    }

    public void deleteTag(long tagId, long boardId) {
        try {
            getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to delete tag from non-existent board");
        }

        var board = this.getBoardById(boardId);

        var tags = board.getTags();
        tags.removeIf(tag -> tag.getId() == tagId);
        board.setTags(tags);

        // also remove tag from all the cards, just in case JPA has issues (btw, it does)
        // functional programming is fun of course
        board.getLists().forEach(list -> list.getCards().
                forEach(card -> card.getTags().removeIf(tag -> tag.getId() == tagId)));

        board.sync();
        this.saveBoard(board);
    }

    public Set<Tag> getTags(long boardId) {
        try {
            getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to get tags from non-existent board");
        }

        var board = this.getBoardById(boardId);
        return board.getTags();
    }

    public Tag updateTag(long boardId, Tag tag) {
        try {
            getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to edit tag from non-existent board");
        }

        var board = this.getBoardById(boardId);

        var tags = board.getTags();
        // check if tag exists
        if (tags.stream().noneMatch(t -> t.getId() == tag.getId())) {
            throw new IllegalArgumentException("Trying to edit non-existent tag");
        }

        // set the new title for tag
        var tagOpt = tags.stream().filter(t -> t.getId() == tag.getId()).findFirst();
        if (tagOpt.isEmpty()) {
            throw new IllegalArgumentException("Something went wrong while editing tag");
        }

        var tagToEdit = tagOpt.get();
        tagToEdit.setTitle(tag.getTitle());

        board.sync();
        this.saveBoard(board);
        return tag;
    }
}
