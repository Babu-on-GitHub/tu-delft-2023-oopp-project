package client.model;

import client.scenes.CardController;
import client.scenes.DetailedCardController;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class CardModel {
    static Logger log = Logger.getLogger(CardModel.class.getName());

    private Card card;
    private ListModel parent;
    private CardController controller;

    private ServerUtils utils;

    public CardController getController() {
        return controller;
    }

    public void setController(CardController controller) {
        this.controller = controller;
    }

    public void setDetailedController(DetailedCardController detailedController) {
        this.controller = controller;
    }

    public CardModel(Card card, ListModel parent, ServerUtils utils) {
        this.card = card;
        this.parent = parent;
        this.utils = utils;
    }


    public void setCard(Card newCard) {
        this.card = newCard;
    }

    public void deleteCard() {
        parent.deleteCard(this);
    }

    public void update() {
        var res = utils.getCardById(card.getId());
        if (res.isEmpty()) {
            log.info("Adding new card..");

            var newCardOpt = utils.addCard(card, parent.getCardList());
            if (newCardOpt.isEmpty()) {
                log.warning("Failed to add card");
                return;
            }
            card = newCardOpt.get();

            parent.updateChild(card);
            return;
        }

        var fetchedCard = res.get();

        if (fetchedCard.equals(card)) {
            log.info("Card is up to date");
            return;
        }

        var newCard = utils.updateCardById(card.getId(), card);
        if (newCard.isEmpty()) {
            log.warning("Failed to update card");
            return;
        }
        card = newCard.get();

        this.updateChildren();
        parent.updateChild(this.getCard());
    }

    public void updateChildren() {
        controller.overwriteTitleNode(card.getTitle());
    }

    public void disown() {
        this.parent.getCardList().getCards().remove(this.card);
        this.parent.getChildren().remove(this);
        this.parent = null;
    }

    public void fosterBy(ListModel parent, int index) {
        this.parent = parent;
        this.parent.getChildren().add(index, this);
        this.parent.getCardList().getCards().add(index, this.card);
    }

    public Card getCard() {
        return card;
    }

    public ListModel getParent() {
        return parent;
    }

    public ServerUtils getUtils() {
        return utils;
    }


    public void updateTitle(String newTitle) {
        if (newTitle == null || newTitle.equals("")) {
            log.info("Card doesn't have title anymore");
            return;
        }
        card.setTitle(newTitle);
        controller.overwriteTitleNode(newTitle);

        var res = utils.updateCardTitleById(card.getId(), newTitle);
        if (res.isEmpty())
            log.severe("Failed to update card title");
        else {
            if (res.get().equals(card.getTitle()))
                log.info("Successfully updated card title!!!");
            else {
                log.severe("Failed to update card title, overwriting new name");
                card.setTitle(res.get());
                controller.overwriteTitleNode(card.getTitle());
            }
        }
    }

    public void overwriteWith(Card newCard) {
        card = newCard;

        var res = utils.updateCardById(card.getId(), card);
        if (res.isEmpty()) {
            log.severe("Failed to update card");
            return;
        }
        card = res.get();
        controller.overwriteTitleNode(card.getTitle());

        update();
    }

    public Set<Tag> getAllTags() {
        return parent.getParent().getBoard().getTags();
    }

    public void assignTag(Tag tag) {
        var newTag = utils.addTagToCard(card.getId(), tag);
        if (newTag.isEmpty()) {
            log.warning("Failed to add tag to card");
            return;
        }
        card.getTags().add(newTag.get());

        update();
    }

    public void unassignTag(Tag tag) {
        var res = utils.deleteTagFromCard(card.getId(), tag.getId());
        if (res.isEmpty()) {
            log.warning("Failed to remove tag from card");
            return;
        }
        card.getTags().remove(tag);

        update();
    }

    public void deleteTag(Tag tag) {
        unassignTag(tag);

        // but we also want to delete it from the board
        var res = utils.deleteTagFromBoard(parent.getParent().getBoard().getId(), tag.getId());
        if (res.isEmpty()) {
            log.warning("Failed to delete tag from board");
            return;
        }
        parent.getParent().getBoard().getTags().remove(tag);

        update();
    }
}
