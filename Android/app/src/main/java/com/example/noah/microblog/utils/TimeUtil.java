package com.example.noah.microblog.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//时间格式管理类
public class TimeUtil {
    private static DateFormat HM = new SimpleDateFormat("HH:mm", Locale.CHINA);

    public static String hmFromDate(Date date) {
        return HM.format(date);
    }
}
