package org.delphy.tokenheroserver.service;

import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.entity.User;
import org.delphy.tokenheroserver.repository.IUserRepository;
import org.delphy.tokenheroserver.util.RedisUtil;
import org.delphy.tokenheroserver.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mutouji
 */
@Service
public class UserService {
    private IUserRepository userRepository;
    private RedisUtil redisUtil;

    public UserService(@Autowired IUserRepository userRepository,
                       @Autowired RedisUtil redisUtil) {
        this.userRepository = userRepository;
        this.redisUtil = redisUtil;
    }

    public User getUser(String phone) {
        return userRepository.findByPhone(phone);
    }

    public List<User> getRanking() {
        return userRepository.findTop10ByTotalDpyGreaterThanOrderByTotalDpyDesc(0.0);
    }

    public Long getPosition(Double totalDpy) {
        if (totalDpy > 0) {
            return userRepository.countByTotalDpyGreaterThan(totalDpy) + 1;
        } else {
            return 0L;
        }
    }

    User generateUser(String phone){
        User user = new User(phone);

        userRepository.insert(user);
        return user;
    }
}
