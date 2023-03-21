package client.model;

import client.scenes.ListController;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ListModel {
    static Logger log = Logger.getLogger(ListModel.class.getName());

    private CardList cardList;
    private BoardModel parent;
    private List<CardModel> children = new ArrayList<>();
    private ListController controller;

    public ListController getController() {
        return controller;
    }

    public void setController(ListController controller) {
        this.controller = controller;
    }


    public ListModel(CardList cardList, BoardModel parent) {
        this.cardList = cardList;
        this.parent = parent;
    }

    public CardList getCardList() {
        return cardList;
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }

    public BoardModel getParent() {
        return parent;
    }

    public void setParent(BoardModel parent) {
        this.parent = parent;
    }

    public List<CardModel> getChildren() {
        return children;
    }

    public void setChildren(List<CardModel> children) {
        this.children = children;
    }

    public void addCard(CardModel cardModel) {
        ServerUtils utils = new ServerUtils();
        if (this.getChildren() == null) {
            this.setChildren(new ArrayList<>());
        }
        if (cardList.getCards() == null) {
            cardList.setCards(new ArrayList<>());
        }
        var newCard = cardModel.getCard();

        var req = utils.addCard(newCard, cardList);
        if (req.isEmpty()) {
            log.warning("Failed to add card to server");
            return;
        }

        newCard = req.get();
        cardModel.setCard(newCard);

        cardList.add(newCard);
        children.add(cardModel);

        update(false);
        quietTest();
    }

    public void insertCard(CardModel card, int position) {
        cardList.getCards().add(position, card.getCard());
        children.add(position, card);
        var utils = new ServerUtils();
        var res = utils.updateCardListById(cardList.getId(), cardList);
        if (res.isEmpty()) {
            log.warning("Failed to update card list with id " + cardList.getId() + " in local model");
            return;
        }

        update(false);
        quietTest();
    }

    public void moveCard(int from, int to) {
        var utils = new ServerUtils();
        var card = cardList.getCards().get(from);
        cardList.getCards().remove(from);
        cardList.getCards().add(to, card);
        var res = utils.deleteCardById(card.getId(), cardList.getId());
        if (res.isEmpty()) {
            log.warning("Failed to delete card with id " + card.getId() + " in local model");
            return;
        }

        var opt = utils.insertCard(card, to, cardList);
        if (opt.isEmpty()) {
            log.severe("During move card, failed to insert");
            return;
        }


        update(true);
        quietTest();
    }

    public void deleteList() {
        parent.deleteList(this);
    }

    public void deleteCard(CardModel cardModel) {
        ServerUtils utils = new ServerUtils();
        long id = cardModel.getCard().getId();
        boolean deletedSuccessfully = false;
        for (var card : cardList.getCards()) {
            if (card.getId() == id) {
                cardList.getCards().remove(card);
                deletedSuccessfully = true;
                break;
            }
        }
        if (!deletedSuccessfully)
            log.warning("Failed to delete card with id " + id + " in local model");

        children.remove(cardModel);

        utils.deleteCardById(id, cardList.getId());

        update(false);
        quietTest();
    }

    public void deleteCardById(long id) {
        ServerUtils utils = new ServerUtils();
        boolean deletedSuccessfully = false;
        for (var card : cardList.getCards()) {
            if (card.getId() == id) {
                cardList.getCards().remove(card);
                deletedSuccessfully = true;
                break;
            }
        }
        if (!deletedSuccessfully)
            log.warning("Failed to delete card with id " + id + " in local model");

        children.removeIf(cardModel -> cardModel.getCard().getId() == id);

        utils.deleteCardById(id, cardList.getId());

        update(false);
        quietTest();
    }

    public boolean update(boolean forced) {
        ServerUtils utils = new ServerUtils();
        var res = utils.getCardListById(cardList.getId());
        if (res.isEmpty()) {
            log.info("Adding new card list..");
            var newCardList = utils.addCardList(cardList, parent.getBoard());
            if (newCardList.isEmpty()) {
                log.warning("Failed to add card list");
                return false;
            }

            cardList = newCardList.get();

            updateChildren();
            parent.updateChild(cardList);
            return false;
        }

        var fetchedCardList = res.get();

        if (!forced && fetchedCardList.equals(cardList)) return false;

        var serverTimestamp = fetchedCardList.getTimestamp();
        var localTimestamp = cardList.getTimestamp();

        if (serverTimestamp.after(localTimestamp)) {
            log.info("Server timestamp is newer, overwriting local list");
            cardList = fetchedCardList;

            updateChildren();
            parent.updateChild(cardList);
            return true;
        }

        log.info("Overwriting server-stored list...");

        var updatedCardList = utils.updateCardListById(cardList.getId(), cardList);
        if (updatedCardList.isEmpty()) {
            log.warning("Failed to update, trying to revert");
            return false;
        }

        cardList = updatedCardList.get();

        updateChildren();
        parent.updateChild(this.getCardList());
        return false;
    }

    public boolean tryToUpdateChildrenNaively() {
        if (cardList.getCards().size() != children.size()) return false;

        for (int i = 0; i < cardList.getCards().size(); i++) {
            var list = cardList.getCards().get(i);
            var child = children.get(i);

            if (list.getId() != child.getCard().getId()) return false;
        }

        for (int i = 0; i < cardList.getCards().size(); i++) {
            var card = cardList.getCards().get(i);
            var child = children.get(i);

            child.setCard(card);
            child.updateChildren();
        }

        return true;
    }

    public void updateChildren() {
        if (cardList.getCards() == null) return;

        if (tryToUpdateChildrenNaively()) return;

        log.info("Updating children naively failed, rebuilding the list..");

        var temp = new ArrayList<CardModel>();
        for (int i = 0; i < cardList.getCards().size(); i++)
            temp.add(null);

        for (int i = 0; i < cardList.getCards().size(); i++) {
            var model = new CardModel(cardList.getCards().get(i), this);
            temp.set(i, model);
        }

        children = temp;
        try {
            controller.recreateChildren(temp);
        } catch (Exception e) {
            log.warning("Problems during list children recreation..");
        }

        for (var child : children)
            child.updateChildren();
    }

    public void updateChild(Card card) {
        for (int i = 0; i < cardList.getCards().size(); i++) {
            var toUpdate = cardList.getCards().get(i);
            if (toUpdate.getId() == card.getId()) {
                cardList.getCards().set(i, card);
            }
        }
        parent.updateChild(this.getCardList());
    }

    private boolean coherencyTest() {
        if (children == null && cardList.getCards() == null) return true;
        if (children == null || cardList.getCards() == null) return false;
        if (children.size() != cardList.getCards().size()) return false;

        for (int i = 0; i < children.size(); i++) {
            var first = children.get(i).getCard();
            var second = cardList.getCards().get(i);
            if (first != second)
                return false;
        }

        return true;
    }

    public void quietTest() {
        var isCoherent = coherencyTest();
        if (!isCoherent)
            log.severe("Coherency test failed on the list!");
    }
}

