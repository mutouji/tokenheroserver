package org.delphy.tokenheroserver.exception;

/**
 * @author mutouji
 */
public class RequestLimitException extends Exception {
    public RequestLimitException() {
        super("HTTP请求频率超出设定的限制");
    }
}
