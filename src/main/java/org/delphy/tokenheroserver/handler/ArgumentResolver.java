package org.delphy.tokenheroserver.handler;

import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.pojo.UserVo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author mutouji
 */
public class ArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserVo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterType().equals(UserVo.class)) {
            return webRequest.getAttribute(Constant.TOKEN_VO, RequestAttributes.SCOPE_REQUEST);
        }
        return null;
    }
}
