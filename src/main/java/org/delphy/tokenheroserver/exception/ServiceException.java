package org.delphy.tokenheroserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author mutouji
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ServiceException extends Exception {
    public ServiceException(String msg, Exception e) {
        super(msg + "\n" + e.getMessage());
    }

    public ServiceException(String msg) {
        super(msg);
    }
}

