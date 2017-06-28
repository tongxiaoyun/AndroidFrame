package com.risenb.expand.imagepick.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/5/27
 * 描    述：时间处理工具
 * 修订历史：
 * ================================================
 */
public class TimeUtils {

    public static String timeFormat(long timeMillis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time) {
        return timeFormat(time, "yyyy-MM-dd");
    }

    public static String formatPhotoDate(String path) {
        File file = new File(path);
        if (file.exists()) {
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }
}