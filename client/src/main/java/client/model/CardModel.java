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

    public CardController getController() {
        return controller;
    }

    public void setController(CardController controller) {
        this.controller = controller;
    }

    public CardModel(Card card,ListModel parent){
        this.card = card;
        this.parent = parent;
    }

    public Card getCard(){
        return card;
    }

    public void setCard(Card newCard){
        this.card = newCard;
    }

    public void deleteCard(){
        parent.deleteCard(this);
    }

    public void update(){
        ServerUtils utils = new ServerUtils();
        var res = utils.getCardById(card.getId());
        if (res == null) {
            log.info("Adding new card..");

            var newCard = utils.addCard(card);
            if (newCard == null) {
                log.warning("Failed to add card");
                return;
            }

            card = utils.getCardById(newCard.getId());

            parent.updateChild(card);
            return;
        }

        if (utils.getCardById(card.getId()).equals(card))
            return;

        var newCard = utils.updateCardById(card.getId(), card);
        if (newCard == null) {
            log.warning("Failed to update card");
            return;
        }

        card = utils.getCardById(newCard.getId());

        this.updateChildren();
        parent.updateChild(this.getCard());
    }

    public void updateChildren() {
    }
}
