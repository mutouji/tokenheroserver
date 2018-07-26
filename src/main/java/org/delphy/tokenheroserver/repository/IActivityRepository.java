package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.entity.Activity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author mutouji
 */
public interface IActivityRepository extends MongoRepository<Activity, String> {
    /**
     * delete when the activity is settlement
     * @param id id
     * @return return
     */
    @Cacheable(value="activity", key="#p0", unless = "#result == null")
    Activity findActivityById(String id);

    /**
     * find one in
     * @param ids ids
     * @return return
     */
    List<Activity> findByIdIn(List<String> ids);

    /**
     * used for update
     * @param entity entity
     * @param <S> Activity
     * @return return
     */
    @CacheEvict(value = "activity", key="#p0.id")
    @Override
    <S extends Activity> S save(S entity);

    /**
     * should delete empty items
     * @param entity entity
     * @param <S> Activity
     * @return return
     */
    @CacheEvict(value = "activity", key="#p0.id")
    @Override
    <S extends Activity> S insert(S entity);
}
