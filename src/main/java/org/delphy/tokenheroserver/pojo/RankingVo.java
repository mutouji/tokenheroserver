package org.delphy.tokenheroserver.pojo;

import lombok.Data;
import org.delphy.tokenheroserver.entity.User;

import java.util.List;

/**
 * @author mutouji
 */
@Data
public class RankingVo {
    private List<User> users;
    private Long myPosition;
    private User mySelf;
}
