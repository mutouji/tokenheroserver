package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    @Autowired
    IUserRepository userRepository;

    @Test
    public void getRanking() {
        List<User> users = userRepository.findTop10ByTotalDpyGreaterThanOrderByTotalDpyDesc(0.0);

        for (User user : users) {
            System.out.println(user);
        }
    }
}
