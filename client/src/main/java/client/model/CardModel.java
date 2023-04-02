package client.model;

import client.scenes.CardController;
import client.scenes.DetailedCardController;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import commons.Task;

import java.util.ArrayList;
import java.util.HashSet;
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
        //controller.updateTitleModel();
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

    public static Logger getLog() {
        return log;
    }

    public ListModel getParent() {
        return parent;
    }

    public ServerUtils getUtils() {
        return utils;
    }

    public void updateCardDetails(boolean isUpdate, String newTitle, String newDescription,
                                  List<Task> newSubtasks, Set<Tag> newTags) {
        if (!isUpdate) {
            return;
        }
        updateTitle(newTitle);
        updateDescription(newDescription);
//        updateSubtasks(newSubtasks);
//        updateTags(newTags);
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

    private void updateDescription(String newDescription) {
        if (newDescription == null || newDescription.equals("")) {
            return;
        }
        //card.setDescription(newDescription);
        var res = utils.updateCardDescriptionById(card.getId(), newDescription);
        if (res.isEmpty())
            log.severe("Failed to update card description");
        else {
            if (res.get().equals(card.getDescription()))
                log.info("Successfully updated card description");
            else {
                log.severe("Failed to update card description, overwriting new description");
                card.setDescription(res.get());
            }
        }
    }

    private void updateSubtasks(List<Task> newSubtasks) {
        if (newSubtasks == null) {
            return;
        }
        card.setSubTasks((ArrayList<Task>) newSubtasks);

        var res = utils.updateCardSubtasksById(card.getId(), newSubtasks);
        if (res.isEmpty())
            log.severe("Failed to update card subtask");
        else {
            if (res.get().equals(card.getSubTasks()))
                log.info("Successfully updated card subtask");
            else {
                log.severe("Failed to update card subtask, overwriting new");
                card.setSubTasks((ArrayList<Task>) res.get());
            }
        }

    }

    private void updateTags(Set<Tag> newTags) {
        if (newTags == null) {
            return;
        }
        card.setTags((HashSet<Tag>) newTags);

        var res = utils.updateCardTagsById(card.getId(), newTags);
        if (res.isEmpty())
            log.severe("Failed to update card tags");
        else {
            if (res.get().equals(card.getTags()))
                log.info("Successfully updated card tags");
            else {
                log.severe("Failed to update card tags, overwriting new tags");
                card.setTitle(res.get());
            }
        }
    }
}
