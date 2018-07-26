package org.delphy.tokenheroserver.service;

import org.delphy.tokenheroserver.entity.Card;
import org.delphy.tokenheroserver.repository.ICardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mutouji
 */
@Service
public class CardService {
    private ICardRepository cardRepository;

    public CardService(@Autowired ICardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getCard(String userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.cardRepository.findCardsByUserIdOrderByIdDesc(userId, pageable);
    }
}
