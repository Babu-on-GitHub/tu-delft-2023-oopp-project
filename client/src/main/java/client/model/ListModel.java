package client.model;

import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;

import java.util.ArrayList;
import java.util.List;

public class ListModel {
    private CardList cardList;
    private BoardModel parent;
    private List<CardModel> children;

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

    public void addCard(CardModel cardModel){
        if (this.getChildren() == null) {
            this.setChildren(new ArrayList<>());
        }
        if(cardList.getCards() == null){
            cardList.setCards(new ArrayList<>());
        }

        cardModel.update();

        cardList.getCards().add(cardModel.getCard());

        children.add(cardModel);

        this.update();
    }

    public void deleteList(){
        parent.deleteList(this);
    }

    public void deleteCard(CardModel cardModel){
        ServerUtils utils = new ServerUtils();
        long id = cardModel.getCard().getId();
        for(int i = 0; i<cardList.getCards().size(); i++){
            Card card = cardList.getCards().get(i);
            if(card.getId()==id){
                cardList.getCards().remove(card);
            }
        }
        children.remove(cardModel);

        this.update();
    }

    public void update(){
        ServerUtils utils = new ServerUtils();
        if(cardList.getId()==0) cardList = utils.addCardList(cardList, parent.getBoard()).get();
        else cardList = utils.updateCardListById(cardList.getId(),cardList).get();
        this.updateChildren();
        parent.updateChild(this.getCardList());
    }

    public void updateChildren() {
        if(cardList.getCards()==null||children==null) return;
        for(var card: cardList.getCards()){
            for(var child: children){
                if(child.getCard().getId() == card.getId()){
                    child.setCard(card);
                    child.updateChildren();
                }
            }
        }
    }

    public void updateChild(Card card){
        for(int i=0;i<cardList.getCards().size();i++){
            var toUpdate = cardList.getCards().get(i);
            if( toUpdate.getId() == card.getId()){
                cardList.getCards().set(i, card);
            }
        }
        this.update();
        parent.updateChild(this.getCardList());
    }
}
