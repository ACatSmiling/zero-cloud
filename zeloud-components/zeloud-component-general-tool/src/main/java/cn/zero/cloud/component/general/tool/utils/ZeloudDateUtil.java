package cn.zero.cloud.component.general.tool.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author Xisun Wang
 * @since 2024/3/14 13:45
 */
public class ZeloudDateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZeloudDateUtil.class);

    private final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static DateTimeFormatter UTC_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private final static Set<String> AVAILABLE_ZONE_IDS = ZoneId.getAvailableZoneIds();

    private ZeloudDateUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        TimeZone timezone = calendar.getTimeZone();
        int offsetCompareWithGmt = timezone.getRawOffset() / 3600000;
        calendar.add(10, -1 * offsetCompareWithGmt);
        return calendar.getTime();
    }

    /**
     * 获取默认时区的当前时间
     *
     * @return 格式化后的当前时间
     */
    public static String getCurrentTimeDefaultTimeZone() {
        return LOCAL_DATE_TIME_FORMATTER.format(ZonedDateTime.now());
    }

    /**
     * 获取默认时区的当前时间
     *
     * @return 格式化后的当前时间，UTC 格式
     */
    public static String getCurrentTimeDefaultTimeZoneWithUtc() {
        return UTC_DATE_TIME_FORMATTER.format(ZonedDateTime.now());
    }

    /**
     * 获取指定时区的当前时间
     *
     * @param timeZone 指定时区
     * @return 格式化后的当前时间
     */
    public static String getCurrentTimeSpecifyTimeZone(String timeZone) {
        if (StringUtils.isBlank(timeZone) || !AVAILABLE_ZONE_IDS.contains(timeZone)) {
            throw new IllegalArgumentException("Illegal Argument for timeZone [" + timeZone + "]");
        }

        return LOCAL_DATE_TIME_FORMATTER.format(ZonedDateTime.now(ZoneId.of(timeZone)));
    }

    /**
     * 获取指定时区的当前时间
     *
     * @param timeZone 指定时区
     * @return 格式化后的当前时间，UTC 格式
     */
    public static String getCurrentTimeSpecifyTimeZoneWithUtc(String timeZone) {
        if (StringUtils.isBlank(timeZone) || !AVAILABLE_ZONE_IDS.contains(timeZone)) {
            throw new IllegalArgumentException("Illegal Argument for timeZone [" + timeZone + "]");
        }

        return UTC_DATE_TIME_FORMATTER.format(ZonedDateTime.now(ZoneId.of(timeZone)));
    }

    /**
     * 格式化指定时间为字符串
     *
     * @param zonedDateTime 指定时间
     * @return 格式化后的指定时间
     */
    public static String formatTime(ZonedDateTime zonedDateTime) {
        return LOCAL_DATE_TIME_FORMATTER.format(zonedDateTime);
    }

    /**
     * 格式化指定时间为字符串
     *
     * @param zonedDateTime 指定时间
     * @return 格式化后的指定时间，UTC 格式
     */
    public static String formatTimeWithUtc(ZonedDateTime zonedDateTime) {
        return UTC_DATE_TIME_FORMATTER.format(zonedDateTime);
    }

    /**
     * 格式化 java.util.Date 为字符串
     *
     * @param date 指定时间
     * @return 格式化后的指定时间
     */
    public static String formatDate(Date date) {
        // 转换 java.util.Date 为 java.time.ZonedDateTime
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        return formatTime(zonedDateTime);
    }

    /**
     * 格式化 java.util.Date 为字符串
     *
     * @param date 指定时间
     * @return 格式化后的指定时间，UTC 格式
     */
    public static String formatDateWithUtc(Date date) {
        // 转换 java.util.Date 为 java.time.ZonedDateTime
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        return formatTimeWithUtc(zonedDateTime);
    }

    /**
     * 格式化 java.time.Instant 为字符串
     *
     * @param instant 指定时间
     * @return 格式化后的指定时间
     */
    public static String formatInstant(Instant instant) {
        // 转换 java.time.Instant 为 java.time.ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return formatTime(zonedDateTime);
    }

    /**
     * 格式化 java.time.Instant 为字符串
     *
     * @param instant 指定时间
     * @return 格式化后的指定时间，UTC 格式
     */
    public static String formatInstantWithUtc(Instant instant) {
        // 转换 java.time.Instant 为 java.time.ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return formatTimeWithUtc(zonedDateTime);
    }

    /**
     * 格式化 java.time.LocalDateTime 为字符串
     *
     * @param localDateTime 指定时间
     * @return 格式化后的指定时间
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        // 转换 java.time.LocalDateTime 为 java.time.ZonedDateTime
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return formatTime(zonedDateTime);
    }

    /**
     * 格式化 java.time.LocalDateTime 为字符串
     *
     * @param localDateTime 指定时间
     * @return 格式化后的指定时间，UTC 格式
     */
    public static String formatLocalDateTimeWithUtc(LocalDateTime localDateTime) {
        // 转换 java.time.LocalDateTime 为 java.time.ZonedDateTime
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return formatTimeWithUtc(zonedDateTime);
    }

    /**
     * 解析字符串为时间，使用默认时区
     *
     * @param timeStr 时间字符串，格式为："yyyy-MM-dd HH:mm:ss"，示例："2024-04-07 10:34:50"
     * @return 解析后的时间
     */
    public static ZonedDateTime parseString(String timeStr) {
        if (StringUtils.isBlank(timeStr)) {
            throw new IllegalArgumentException("Illegal Argument for timeStr [" + timeStr + "]");
        }

        // 解析日期时间字符串为 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(timeStr, LOCAL_DATE_TIME_FORMATTER);
        // 将 LocalDateTime 转换为 ZonedDateTime
        return localDateTime.atZone(ZoneId.systemDefault());
    }

    /**
     * 解析字符串为时间，使用默认时区
     *
     * @param timeStrWithUtc 时间字符串
     * @return 解析后的时间
     */
    public static ZonedDateTime parseStringForUtc(String timeStrWithUtc) {
        if (StringUtils.isBlank(timeStrWithUtc)) {
            throw new IllegalArgumentException("Illegal Argument for timeStrWithUtc [" + timeStrWithUtc + "]");
        }
        try {
            // 格式为："yyyy-MM-dd'T'HH:mm:ss.sssZ"，示例："2024-04-07T10:34:50.123+0800"
            return ZonedDateTime.parse(timeStrWithUtc, UTC_DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            // 格式为："yyyy-MM-dd'T'HH:mm:ssZ"，示例："2024-04-07T10:34:50Z"
            return ZonedDateTime.parse(timeStrWithUtc);
        }
    }

    public static void main(String[] args) {
        System.out.println("-------------------Instant 和 ZonedDateTime 的对比-------------------");
        Instant now = Instant.now();// 返回当前的瞬间，精确到纳秒（取决于系统时钟的精度），并且以 UTC（协调世界时）为基准，是全球统一的时间标准，与时区无关
        ZonedDateTime currentNow = ZonedDateTime.now();// 返回当前时区的当前日期和时间，如果不指定时区，则使用系统默认时区（也可以指定时区），使用时需要考虑时区差异的场景
        System.out.println(now);// 2024-04-07T02:34:50.111432600Z
        System.out.println(currentNow);// 2024-04-07T10:34:50.114470400+08:00[Asia/Shanghai]
        System.out.println(now.toEpochMilli());// 1712457290111，时间戳
        System.out.println(currentNow.toInstant().toEpochMilli());// 1712457290114，时间戳，理论上与 Instant.now().toEpochMilli() 是相同的

        System.out.println("-------------------获取所有的时区-------------------");
        System.out.println(AVAILABLE_ZONE_IDS);

        System.out.println("-------------------获取默认时区当前时间-------------------");
        System.out.println(ZeloudDateUtil.getCurrentTimeDefaultTimeZone());// 2024-04-07 10:34:50
        System.out.println(ZeloudDateUtil.getCurrentTimeDefaultTimeZoneWithUtc());// 2024-04-07T10:34:50.116+0800

        System.out.println("-------------------获取指定时区当前时间-------------------");
        System.out.println(ZeloudDateUtil.getCurrentTimeSpecifyTimeZone("America/New_York"));// 2024-04-06 22:34:50
        System.out.println(ZeloudDateUtil.getCurrentTimeSpecifyTimeZone("UTC"));// 2024-04-07 02:34:50
        System.out.println(ZeloudDateUtil.getCurrentTimeSpecifyTimeZone("Europe/London"));// 2024-04-07 03:34:50
        System.out.println(ZeloudDateUtil.getCurrentTimeSpecifyTimeZone("Asia/Shanghai"));// 2024-04-07 10:34:50
        System.out.println(ZeloudDateUtil.getCurrentTimeSpecifyTimeZoneWithUtc("America/New_York"));// 2024-04-06T22:34:50.123-0400
        System.out.println(ZeloudDateUtil.getCurrentTimeSpecifyTimeZoneWithUtc("UTC"));// 2024-04-07T02:34:50.123+0000
        System.out.println(ZeloudDateUtil.getCurrentTimeSpecifyTimeZoneWithUtc("Europe/London"));// 2024-04-07T03:34:50.123+0100
        System.out.println(ZeloudDateUtil.getCurrentTimeSpecifyTimeZoneWithUtc("Asia/Shanghai"));// 2024-04-07T10:34:50.123+0800

        System.out.println("-------------------格式化时间-------------------");
        Date date = new Date(1712457290111L);
        System.out.println(formatDate(date));// 2024-04-07 10:34:50
        System.out.println(formatDateWithUtc(date));// 2024-04-07T10:34:50.111+0800
        Instant instant = Instant.ofEpochMilli(1712457290111L);
        System.out.println(formatInstant(instant));// 2024-04-07 10:34:50
        System.out.println(formatInstantWithUtc(instant));// 2024-04-07T10:34:50.111+0800
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        System.out.println(formatLocalDateTime(localDateTime));// 2024-04-07 10:34:50
        System.out.println(formatLocalDateTimeWithUtc(localDateTime));// 2024-04-07T10:34:50.111+0800

        System.out.println("-------------------解析时间-------------------");
        String timeStr = "2024-04-07 10:34:50";
        System.out.println(parseString(timeStr));// 2024-04-07T10:34:50+08:00[Asia/Shanghai]
        String timeStrWithUTC = "2024-04-07T10:34:50.123+0800";
        System.out.println(parseStringForUtc(timeStrWithUTC));// 2024-04-07T10:34:50.123+08:00
        String timeStrWithUTC2 = "2024-04-07T10:34:50Z";
        System.out.println(parseStringForUtc(timeStrWithUTC2));// 2024-04-07T10:34:50Z

        System.out.println("-------------------反向验证-------------------");
        ZonedDateTime zonedDateTime = parseString(timeStr);
        System.out.println(zonedDateTime);// 2024-04-07T10:34:50+08:00[Asia/Shanghai]
        System.out.println(zonedDateTime.toEpochSecond());// 1712457290
        System.out.println(formatTime(zonedDateTime));// 2024-04-07 10:34:50
        System.out.println(formatTimeWithUtc(zonedDateTime));// 2024-04-07T10:34:50.000+0800
        ZonedDateTime zonedDateTimeWithUTC = parseStringForUtc(timeStrWithUTC);
        System.out.println(zonedDateTimeWithUTC);// 2024-04-07T10:34:50.123+08:00
        System.out.println(zonedDateTimeWithUTC.toEpochSecond());// 1712457290
        System.out.println(formatTime(zonedDateTimeWithUTC));// 2024-04-07 10:34:50
        System.out.println(formatTimeWithUtc(zonedDateTimeWithUTC));// 2024-04-07T10:34:50.123+0800

        LOGGER.info("aabb");
    }
}
