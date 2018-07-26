package org.delphy.tokenheroserver.common;

/**
 * @author mutouji
 */
public class Constant {
    public static final String SID = "sid";
    public static final String TOKEN_VO = "tokenvo";

    public static final Long ACTIVITY_INIT = 1L;
    public static final Long ACTIVITY_PROCESSING = 2L;
    public static final Long ACTIVITY_LOCKED = 3L;
    public static final Long ACTIVITY_CLEARING = 4L;
    public static final Long ACTIVITY_END = 5L;

    public static final Long DELETE = 1L;
    public static final Long SURVIVAL = 0L;

    public static final Long ACTIVITY_MODE_WX = 0L;
    public static final Long ACTIVITY_MODE_H5 = 1L;

    public static final String CACHE_TOKEN = "token";
    public static final String CACHE_VERIFYCODE = "verifycode";
    public static final String CACHE_USER = "user";
    public static final String CACHE_MAINACTIVITY = "mainactivity";

    public static final Long FREENUM_DEFUALT = 2L;

    public static final Long WITHDRAW_UNSEND = 0L;

    public static final int ETH_ADDRESS_LENGTH = 42;

    public static final int WITHDRAW_MIN = 10;
    public static final Double TX_FEE = 0.0;

    public static final Long LONG_TRUE = 1L;
    public static final Long LONG_FALSE = 0L;

    /**
     * 60 minites
     * 3 days = 4320
     */
    public static final long TIME_TOKE = 4320;
    public static final long TIME_VERIFYCODE = 5;
    public static final long TIME_USER = 50;

    public static final Long TIME_ADAY_SECONDS = 86400L;
}
