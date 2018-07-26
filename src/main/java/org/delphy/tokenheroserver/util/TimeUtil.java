package org.delphy.tokenheroserver.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author mutouji
 */
public class TimeUtil {
    public static Long getCurrentSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static Long getDayBeginSeconds() {
        long current = System.currentTimeMillis();
        return (current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset()) / 1000;
    }

    public static String generateId() {
        Random random = new Random();
        Integer s = random.nextInt(10000);
        String  index = String.format("%04d", s);
        Long time = System.currentTimeMillis();
        return  "" + time + index;
    }
    public static String generateVerifyCode() {
        Random random = new Random();
        Integer s = random.nextInt(1000000);
        return String.format("%06d", s);
    }

    public static Long getDateSeconds(String strDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = df.parse(strDate);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
