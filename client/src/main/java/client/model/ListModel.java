package client.model;

import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import org.checkerframework.checker.units.qual.C;

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

    public void addCard(CardModel card){
        if (this.getChildren() == null) {
            this.setChildren(new ArrayList<>());
        }
        if(cardList.getCards() == null){
            cardList.setCards(new ArrayList<>());
        }
        cardList.getCards().add(card.getCard());

        ServerUtils utils = new ServerUtils();
        cardList = utils.updateCardListById(cardList.getId(), cardList);

        card.setCard(cardList.getCards().get(cardList.getCards().size()-1));

        children.add(card);
        parent.updateCardListById(cardList.getId(), cardList);

        for(var newCard : cardList.getCards()){
            boolean flag = true;
            for(var child : children){
                if (child.getCard().getId() == newCard.getId()){
                    child.setCard(newCard);
                    flag = false;
                }
            }
            if (flag){
                //TODO fix inconsistencies
            }
        }
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
        cardList = utils.updateCardListById(cardList.getId(),cardList);
        parent.updateCardListById(cardList.getId(),cardList);
        children.remove(cardModel);

    }

}
