package org.delphy.tokenheroserver.exception;

import org.delphy.tokenheroserver.common.EnumError;

/**
 * @author mutouji
 */
public class DefaultException extends RuntimeException {
    private EnumError errorCode;

    public DefaultException(EnumError errorCode) {
        this.errorCode = errorCode;
    }

    public EnumError getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(EnumError errorCode) {
        this.errorCode = errorCode;
    }
}
