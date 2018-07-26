package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author mutouji
 */
public interface IUserRepository extends MongoRepository<User, String> {
    /**
     *  delete cache when user info changed
     * @param phone phone
     * @return user
     */
    @Cacheable(value = "user", key="#p0", unless = "#result == null")
    User findByPhone(String phone);

    /**
     * delete cache when an activity settlement
     * @param dpy dpy
     * @return return
     */
    @Cacheable(value = "userTop10", key="'top10'")
    List<User> findTop10ByTotalDpyGreaterThanOrderByTotalDpyDesc(Double dpy);

    /**
     * delete cache when an activity settlement
     * @param totalDpy totalDpy
     * @return return
     */
    @Cacheable(value = "userPosition", key="#p0")
    Long countByTotalDpyGreaterThan(Double totalDpy);

    /**
     * this is for update
     * @param entity entity
     * @param <S> User
     * @return return
     */
    @Override
    @CacheEvict(value = "user", key="#p0.phone")
    <S extends User> S save(S entity);

    /**
     * insert
     * this is for create new user, will throw exception if with the same id
     */
    @Override
    @CacheEvict(value = "user", key="#p0.phone")
    <S extends User> S insert(S entity);
}
