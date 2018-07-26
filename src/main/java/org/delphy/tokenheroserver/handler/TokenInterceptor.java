package org.delphy.tokenheroserver.handler;

import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.exception.DefaultException;
import org.delphy.tokenheroserver.pojo.UserVo;
import org.delphy.tokenheroserver.service.IndexService;
import org.delphy.tokenheroserver.util.RequestUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mutouji
 */
public class TokenInterceptor implements HandlerInterceptor {
    private IndexService indexService;

    public TokenInterceptor(IndexService indexService) {
        this.indexService = indexService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RequestUtil.logRequestIpAndUrl(request);

        String sid  = request.getHeader(Constant.SID);
        UserVo userVo = indexService.getToken(sid);
        if (userVo == null) {
            throw new DefaultException(EnumError.INCORRECT_TOKEN);
        } else {
            request.setAttribute(Constant.TOKEN_VO,userVo);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
