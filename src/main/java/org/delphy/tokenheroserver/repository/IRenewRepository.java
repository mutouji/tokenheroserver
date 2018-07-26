package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.entity.Renew;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author mutouji
 */
public interface IRenewRepository extends MongoRepository<Renew, String> {
}
