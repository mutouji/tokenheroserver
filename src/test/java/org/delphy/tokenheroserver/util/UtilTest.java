package org.delphy.tokenheroserver.util;

import org.junit.Test;

import java.sql.Timestamp;
import java.util.TimeZone;


public class UtilTest {
    @Test
    public void testDay() {
        long current=System.currentTimeMillis();//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long zero1 = TimeUtil.getDayBeginSeconds();
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        long yesterday=System.currentTimeMillis()-24*60*60*1000;//昨天的这一时间的毫秒数
        System.out.println(new Timestamp(current));//当前时间
        System.out.println(new Timestamp(yesterday));//昨天这一时间点
        System.out.println(new Timestamp(zero));//今天零点零分零秒
        System.out.println(new Timestamp(twelve));//今天23点59分59秒
        System.out.println(new Timestamp(zero1));//今天零点零分零秒
    }

    @Test
    public void testGetDateSeconds() {
        Long time = TimeUtil.getDateSeconds("2018-07-06");
        System.out.println(time);
    }
}
