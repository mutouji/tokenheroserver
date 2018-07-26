package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.entity.Subscribe;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author mutouji
 */
public interface ISubscribeRepository extends MongoRepository<Subscribe, String> {
}
