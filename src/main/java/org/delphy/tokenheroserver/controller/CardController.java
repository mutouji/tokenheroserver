package org.delphy.tokenheroserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.common.RestResult;
import org.delphy.tokenheroserver.entity.Card;
import org.delphy.tokenheroserver.pojo.UserVo;
import org.delphy.tokenheroserver.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author mutouji
 */
@Api(value = "CardController", description = "卡片接口", tags={"卡片"})
@RestController
public class CardController {
    private CardService cardService;

    public CardController(@Autowired CardService cardService) {
        this.cardService = cardService;
    }

    @ApiOperation(value="获取自己的卡片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name = "page", value = "分页索引", paramType = "query", dataType = "int", required = true, defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "分页大小", paramType = "query", dataType = "int", required = true, defaultValue = "2")
    })
    @GetMapping("/cards")
    public RestResult<List<Card>> getCards(@ApiIgnore UserVo userVo, Integer page, Integer size) {
        if (userVo == null) {
            return new RestResult<>(EnumError.USER_ERROR);
        }
        List<Card> cards = cardService.getCard(userVo.getId(), page, size);
        return new RestResult<>(EnumError.SUCCESS, cards);
    }
}
