package org.delphy.tokenheroserver.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.delphy.tokenheroserver.annotation.RequestLimit;
import org.delphy.tokenheroserver.exception.RequestLimitException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mutouji
 */
@Slf4j
public class RequestLimitContract extends HandlerInterceptorAdapter {
    private Map<String, Integer> redisTemplate = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(8,
            new BasicThreadFactory.Builder().namingPattern("request-limit-pool-%d").daemon(true).build());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();

        RequestLimit limit = method.getAnnotation(RequestLimit.class);
        if (limit == null) {
            return true;
        }

        String ip = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String key = "req_limit_".concat(url).concat(ip);
        if (redisTemplate.get(key) == null || redisTemplate.get(key) == 0) {
            redisTemplate.put(key, 1);
        } else {
            redisTemplate.put(key, redisTemplate.get(key) + 1);
        }
        int count = redisTemplate.get(key);
        if (count > 0) {
            scheduledExecutorService.schedule(() -> redisTemplate.remove(key), limit.time(), TimeUnit.MILLISECONDS);
        }
        if (count > limit.count()) {
            log.error("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
            throw new RequestLimitException();
        }
        return true;
    }

    /*
    @Before("within(org.delphy.tokenheroserver.controller.*) && @annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) throws RequestLimitException {
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = null;
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
                break;
            }
        }
        if (request == null) {
            throw new RequestLimitException("方法中缺失HttpServletRequest参数");
        }
        String ip = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String key = "req_limit_".concat(url).concat(ip);
        if (redisTemplate.get(key) == null || redisTemplate.get(key) == 0) {
            redisTemplate.put(key, 1);
        } else {
            redisTemplate.put(key, redisTemplate.get(key) + 1);
        }
        int count = redisTemplate.get(key);
        if (count > 0) {
            ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("request-limit-pool-%d").daemon(true).build());
            scheduledExecutorService.schedule(() -> redisTemplate.remove(key), limit.time(), TimeUnit.MILLISECONDS);
        }
        if (count > limit.count()) {
            log.error("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
            throw new RequestLimitException();
        }
    }
    */
}
