package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.entity.AdminUser;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author mutouji
 */
public interface IAdminUserRepository extends MongoRepository<AdminUser, String> {
}
