package client.model;

import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class ListModel {
    private CardList cardList;
    private BoardModel parent;
    private List<CardModel> children;

    public void addCard(Card card){
        children.add(new CardModel(card, this));
        cardList.getCards().add(card);

        ServerUtils utils = new ServerUtils();
        cardList = utils.updateCardListById(cardList.getId(), cardList);
        parent.updateCardListById(cardList.getId(), cardList);

        for(var newCard : cardList.getCards()){
            boolean flag = true;
            for(var child : children){
                if (child.getCard().getId() == newCard.getId()){
                    child.setCard(newCard);
                    flag = false;
                }
            }
            if (flag == true){

            }
        }
    }

}
