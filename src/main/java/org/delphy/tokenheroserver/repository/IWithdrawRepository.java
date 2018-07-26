package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.entity.Withdraw;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author mutouji
 */
public interface IWithdrawRepository extends MongoRepository<Withdraw, String> {
    /**
     * haha
     * @param userId userId
     * @return return
     */
    Long countByUserId(String userId);

    /**
     * haha
     * @param userId userId
     * @param pageable pageable
     * @return return
     */
    List<Withdraw> findAllByUserIdOrderByIdDesc(String userId, Pageable pageable);
    /*
     * insert
     * this is for create new user, will throw exception if with the same id
     */
}
