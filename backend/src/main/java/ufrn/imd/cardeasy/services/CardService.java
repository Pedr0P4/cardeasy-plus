package ufrn.imd.cardeasy.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.cardeasy.errors.CardListNotFound;
import ufrn.imd.cardeasy.errors.CardNotFound;
import ufrn.imd.cardeasy.models.Card;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.repositories.CardListsRepository;
import ufrn.imd.cardeasy.repositories.CardsRepository;

@Service
public class CardService {

    private CardListsRepository cardListsRepo;
    private CardsRepository cardRepo;
    
    @Autowired
    public CardService(
        CardListsRepository cardListsRepo,
        CardsRepository cardRepo
    ) {
        this.cardListsRepo = cardListsRepo;
        this.cardRepo = cardRepo;
    }

    public Card create(
        Integer cardListId,
        String title,
        String description
    ) {
        CardList list = cardListsRepo.findById(cardListId)
            .orElseThrow(CardListNotFound::new);

        Card card = new Card();
        card.setIndex(0l);
        card.setTitle(title);
        card.setList(list);
        card.setDescription(description);

        this.cardRepo.save(card);

        return card;
    }

    public Card update(
        Integer cardId,
        Integer cardListId,
        String title,
        String description
    ) {
        Card card = cardRepo.findById(cardId)
            .orElseThrow(CardNotFound::new);
        
        CardList list = cardListsRepo.findById(cardListId)
            .orElseThrow(CardListNotFound::new);
        
        card.setList(list);
        card.setTitle(title);
        card.setDescription(description);
        
        cardRepo.save(card);

        return card;
    }

    public void deleteById(Integer id) {
        this.cardRepo.deleteById(id);
    }

    public List<Card> findAllByCardList(Integer cardListId) {
        return this.cardRepo.findAllByCardList(cardListId);
    }

    public Card findById(Integer cardId) {
        return this.cardRepo.findById(cardId)
            .orElseThrow(CardNotFound::new);
    }

    public void existsById(Integer id) {
        if(!cardRepo.existsById(id))
            throw new CardNotFound();
    }
}
