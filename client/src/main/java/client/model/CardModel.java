package client.model;

import client.utils.ServerUtils;
import commons.Card;

public class CardModel {
    private Card card;
    private ListModel parent;
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
        if(card.getId() == 0) card = utils.addCard(card, parent.getCardList()).get();
        else card = utils.updateCardById(card.getId(),card).get();
        this.updateChildren();
        parent.updateChild(this.getCard());
    }

    public void updateChildren() {
    }
}
