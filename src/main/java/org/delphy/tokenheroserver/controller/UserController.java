package org.delphy.tokenheroserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.common.RestResult;
import org.delphy.tokenheroserver.entity.User;
import org.delphy.tokenheroserver.pojo.RankingVo;
import org.delphy.tokenheroserver.pojo.SelfVo;
import org.delphy.tokenheroserver.pojo.UserVo;
import org.delphy.tokenheroserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author mutouji
 */
@Api(value="UserController",description = "获取用户自己",tags={"用户信息"})
@RestController
public class UserController {
    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value="获取自己的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true)
    })
    @PostMapping("/self")
    public RestResult<SelfVo> getSelf(@ApiIgnore UserVo userVo) {
        User user = userService.getUser(userVo.getPhone());
        SelfVo selfVo = new SelfVo(user);
        return new RestResult<>(EnumError.SUCCESS, selfVo);
    }

    @ApiOperation(value="获取排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true)
    })
    @PostMapping("/ranking")
    public RestResult<RankingVo> getRanking(@ApiIgnore UserVo userVo) {
        List<User> users = userService.getRanking();
        User user = userService.getUser(userVo.getPhone());
        Long myPosition = userService.getPosition(user.getTotalDpy());

        RankingVo rankingVo = new RankingVo();
        rankingVo.setUsers(users);
        rankingVo.setMyPosition(myPosition);
        rankingVo.setMySelf(user);
        return new RestResult<>(EnumError.SUCCESS, rankingVo);
    }
}
