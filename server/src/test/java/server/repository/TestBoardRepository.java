package server.repository;

import commons.Board;
import commons.Card;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestBoardRepository implements BoardRepository {

    public List<Board> boards = new ArrayList<>();

    public final List<String> calledMethods = new ArrayList<>();
    private void call(String name) {
        calledMethods.add(name);
    }

    private Optional<Board> find(Long id) {
        return boards.stream().filter(c -> c.getId() == id).findFirst();
    }

    public TestBoardRepository(){

    }

    public TestBoardRepository(List<Card> cards){
        this.boards=boards;
    }
    @Override
    public List<Board> findAll() {
        calledMethods.add("findAll");
        return boards;
    }

    @Override
    public List<Board> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Board> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long id) {
        var toDelete = find(id);
        if (toDelete.isEmpty())
            return;
        boolean deleted = boards.remove(toDelete.get());
        if(deleted) call("deleteById");
    }

    @Override
    public void delete(Board entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Board> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Board> S save(S entity) {
        // replace the entity if it exists
        var toReplace = find(entity.getId());
        if (toReplace.isPresent()) {
            boards.remove(toReplace.get());
            boards.add(entity);
            return entity;
        }
        // add it otherwise
        call("save");
        entity.setId((long)boards.size());
        boards.add(entity);

        return entity;
    }

    @Override
    public <S extends Board> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Board> findById(Long aLong) {
        try {
            return find(aLong);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return find(id).isPresent();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Board> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Board> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Board> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Board getOne(Long aLong) {
        return null;
    }

    @Override
    public Board getById(Long id) {
        call("getById");
        return find(id).get();
    }

    @Override
    public Board getReferenceById(Long id) {
        call("getReferenceById");
        return find(id).get();
    }

    @Override
    public <S extends Board> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Board> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Board> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Board> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Board, R> R findBy(Example<S> example,
                                         Function<FluentQuery.FetchableFluentQuery<S>,
                                                 R> queryFunction) {
        return null;
    }
}
