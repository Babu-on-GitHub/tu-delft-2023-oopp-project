package server.api;

import commons.CardList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.CardListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestCardListRepository implements CardListRepository {

    public List<CardList> lists = new ArrayList<>();

    public final List<String> calledMethods = new ArrayList<>();
    private void call(String name) {
        calledMethods.add(name);
    }

    private Optional<CardList> find(Long id) {
        return lists.stream().filter(c -> c.getId() == id).findFirst();
    }

    public TestCardListRepository(){

    }

    public TestCardListRepository(List<CardList> lists){
        this.lists=lists;
    }
    @Override
    public List<CardList> findAll() {
        calledMethods.add("findAll");
        return lists;
    }

    @Override
    public List<CardList> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<CardList> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<CardList> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(CardList entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends CardList> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends CardList> S save(S entity) {
        call("save");
        entity.setId((long)lists.size());
        lists.add(entity);
        return entity;
    }

    @Override
    public <S extends CardList> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CardList> findById(Long aLong) {
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
    public <S extends CardList> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends CardList> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<CardList> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public CardList getOne(Long aLong) {
        return null;
    }

    @Override
    public CardList getById(Long id) {
        call("getById");
        return find(id).get();
    }

    @Override
    public <S extends CardList> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CardList> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends CardList> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends CardList> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CardList> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CardList> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends CardList, R> R findBy(Example<S> example,
                                            Function<FluentQuery.FetchableFluentQuery<S>,
                                                    R> queryFunction) {
        return null;
    }
}
