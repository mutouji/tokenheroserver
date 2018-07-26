package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.entity.Forecast;
import org.delphy.tokenheroserver.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mutouji
 */
@Component
public class ComplexActivityRepository {
    private MongoTemplate mongoTemplate;

    public ComplexActivityRepository(@Autowired MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Activity getMainActivity(Long mode) {
        Query query = new Query();
        query.addCriteria(where("delete").is(Constant.SURVIVAL).and("status").ne(Constant.ACTIVITY_END).and("mode").is(mode));
        query.with(new Sort(Sort.Direction.ASC, "start"));
        query.limit(1);
        List<Activity> activityList = mongoTemplate.find(query, Activity.class);
        if (activityList.size() > 0) {
            return activityList.get(0);
        } else {
            return null;
        }
    }

    /**
     * delete cache when create new activities
     * @param mode mode
     * @param begin begin
     * @param end end
     * @return return
     */
    @Cacheable(value="todayActivities", key="#mode + '_' + #begin")
    public List<Activity> getTodayActivities(Long mode, Long begin, Long end) {
        Query query = new Query();
        query.addCriteria(
                where("delete").is(Constant.SURVIVAL)
                .and("mode").is(mode)
                .and("start").gte(begin).lt(end)
        );
        query.with(new Sort(Sort.Direction.ASC, "start"));
        return mongoTemplate.find(query, Activity.class);
    }

    @Cacheable(value="winnerCount", key="#activityId")
    public long getWinnerCount(String activityId, Double min, Double max) {
        Query query = new Query();
        query.addCriteria(
                where("activityId").is(activityId)
                        .and("last.price").gte(min).lte(max)
        );
        return mongoTemplate.count(query, Forecast.class);
    }

    public void updateForecasts(List<Forecast> winners) {
        for (Forecast forecast : winners) {
            Update update = new Update();
            update.set("reward", forecast.getReward());
            update.set("rewardTime", forecast.getRewardTime());
            mongoTemplate.findAndModify(Query.query(where("id").is(forecast.getId())), update, Forecast.class);
        }
    }

    public void updateUsers(List<Forecast> winners, boolean isWin) {
        for (Forecast forecast : winners) {
            Update update = new Update();
            if (isWin) {
                update.inc("dpy", forecast.getReward());
                update.inc("totalDpy", forecast.getReward());
                update.inc("victories", 1);
            }
            if (forecast.getLast() != null) {
                update.inc("participates", 1);
            }
            mongoTemplate.findAndModify(Query.query(where("id").is(forecast.getUserId())), update, User.class);
        }
    }

}
