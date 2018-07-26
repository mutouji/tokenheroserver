package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.entity.Forecast;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author mutouji
 */
public interface IForecastRepository extends MongoRepository<Forecast, String> {
    /**
     * get one use's forecasts
     * delete cache when forecast a new activity
     * @Cacheable(value="forecasts",key="#p0 + ':' + #p2.toString()")
     * @param userId one user
     * @param time time
     * @param page pageIndex and pageSize
     * @return one use's forecasts
     */
    List<Forecast> findByUserIdAndRewardTimeGreaterThanOrderByIdDesc(String userId, Long time, Pageable page);

    /**
     * delete cache when make forecast and when activity is settlement
     * @param userId userId
     * @param activityId activityId
     * @return return
     */
    @Cacheable(value = "forecast", key="#p0 + '.' + #p1", unless = "#result == null")
    Forecast findByUserIdAndActivityId(String userId, String activityId);

    /**
     * 获取所有参与某activity的预测
     * @param activityId activityId
     * @return return
     */
    List<Forecast> findByActivityId(String activityId);

    @Override
    @CacheEvict(value = "forecast", key="#p0.userId + '.' + #p0.activityId")
    <S extends Forecast> S save(S entity);

    @Override
    <S extends Forecast> S insert(S entity);
}
