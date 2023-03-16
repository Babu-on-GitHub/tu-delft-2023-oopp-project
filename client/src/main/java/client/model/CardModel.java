package client.model;

import commons.Card;

public class CardModel {
    private Card card;
    private ListModel parent;
    public CardModel(Card card,ListModel parent){
        this.card = card;
        this.parent = parent;
    }
    public void update(){

    }

    public void delete(){

    }

    public Card getCard(){
        return card;
    }

    public void setCard(Card newCard){
        this.card = newCard;
    }
}
