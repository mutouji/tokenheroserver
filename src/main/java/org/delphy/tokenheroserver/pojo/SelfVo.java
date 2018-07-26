package org.delphy.tokenheroserver.pojo;

import lombok.Data;
import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.entity.User;

@Data
public class SelfVo {
    private String id;
    private String name;
    private String openId;
    private String avatar;
    private Long gender;
    private Double dpy;
    private Double totalDpy;
    private Long victories;
    private Long participates;
    private String phone;
    private Long create;
    private Long delete;
    private Double fee;

    public SelfVo(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.openId = user.getOpenId();
        this.avatar = user.getAvatar();
        this.gender = user.getGender();
        this.dpy = user.getDpy();
        this.totalDpy = user.getTotalDpy();
        this.victories = user.getVictories();
        this.participates = user.getParticipates();
        this.phone = user.getPhone();
        this.create = user.getCreate();
        this.delete = user.getDelete();
        this.fee = Constant.TX_FEE;
    }
}
