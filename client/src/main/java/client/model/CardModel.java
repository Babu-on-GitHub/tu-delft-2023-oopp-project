package client.model;

import client.scenes.CardController;
import client.utils.ServerUtils;
import commons.Card;

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

    public CardModel(Card card, ListModel parent, ServerUtils utils) {
        this.card = card;
        this.parent = parent;
        this.utils = utils;
    }

    public Card getCard() {
        return card;
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
            var newCard = newCardOpt.get();
            card = newCard;

            parent.updateChild(card);
            return;
        }

        var fetchedCard = res.get();

        if (fetchedCard.equals(card))
            return;

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
}
