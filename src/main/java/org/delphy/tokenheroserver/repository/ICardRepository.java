package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.entity.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author mutouji
 */
public interface ICardRepository extends MongoRepository<Card, String> {
    /**
     * findCardsByUserIdOrderByIdDesc
     * @param userId user id
     * @param pageable pageable
     * @return return
     */
    List<Card> findCardsByUserIdOrderByIdDesc(String userId, Pageable pageable);
}
