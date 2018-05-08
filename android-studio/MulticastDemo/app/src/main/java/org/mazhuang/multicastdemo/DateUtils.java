package org.mazhuang.multicastdemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期类工具方法合集。
 *
 * @author mazhuang
 * @date 2017/6/2
 */

public class DateUtils {

    private DateUtils() {}

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_ORIGIN_FORMAT = "yyyy-MM-dd hh:mm:ss:SSS";
    private static final String DATETIME_COMPAT_FORMAT = "yyyyMMddHHmmss";

    public static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
    public static final long TWO_MINUTES_MILLIS = 2 * 60 * 1000L;

    /**
     * 获取今天的日期，格式为 yyyy-MM-dd。
     *
     * @return 返回当前 yyyy-MM-dd 格式的日期
     */
    public static String getToday() {
        return DateUtils.getDate(0);
    }

    /**
     * 返回与当前相差 diff 天的日期。
     *
     * @param diff 相差天数，可为正负
     * @return 返回 yyyy-MM-dd 格式的日期
     */
    private static String getDate(int diff) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        if (diff != 0) {
            calendar.add(Calendar.DATE, diff);
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 返回 sourceDate 字符串对应的日期相差 diff 天的日期。
     *
     * @param sourceDate 日期字符串，格式为 yyyy-MM-dd
     * @param diff 相差天数
     * @return 返回 yyyy-MM-dd 格式的日期
     */
    public static String getDate(String sourceDate, int diff) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Date date = sdf.parse(sourceDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (diff != 0) {
            calendar.add(Calendar.DATE, diff);
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前时间，格式为 yyyyMMddHHmmss。
     *
     * @return 返回格式为 yyyyMMddHHmmss 的当前时间
     */
    public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_COMPAT_FORMAT, Locale.US);
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间，格式为 yyyy-MM-dd hh:mm:ss:SSS。
     *
     * @return 返回格式为 yyyy-MM-dd hh:mm:ss:SSS 的当前时间
     */
    public static String getFormattedDatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_ORIGIN_FORMAT, Locale.US);
        return sdf.format(new Date());
    }


    /**
     * 对日期、时间进行加、减操作。
     *
     * <pre>
     * DateUtils.add(date, Calendar.YEAR, -1); // date减一年
     * DateUtils.add(date, Calendar.HOUR, -4); // date减4个小时
     * DateUtils.add(date, Calendar.MONTH, 3); // date加3个月
     * </pre>
     *
     * @param date
     *            日期时间。
     * @param field
     *            执行加减操作的属性，参考{@link Calendar#YEAR}、{@link Calendar#MONTH}、
     *            {@link Calendar#HOUR}等。
     * @param amount
     *            加减数量。
     * @return 执行加减操作后的日期、时间。
     */
    public static Date add(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 去除时间（小时分秒）部分，保留日期（年月）部分＋－dateIndex。
     *
     * @param date
     *            要操作的日期时间对象。
     * @return 去除时间部分后的日期时间对象。
     */
    public static Date truncateTime(Date date, int dateIndex) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dateIndex);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 去除时间（时分秒）部分，保留日期（年月日）部分。
     *
     * @param date
     *            要操作的日期时间对象。
     * @return 去除时间部分后的日期时间对象。
     */
    public static Date truncateTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 去除日期（年月日）部分，保留时间（时分秒）部分。
     *
     * @param date
     *            要操作的日期时间对象。
     * @return 去除日期部分后的日期时间对象（返回的日期部分为0001-01-01，因为无法用{@link Date}表示0000-00-00）。
     */
    public static Date truncateDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime();
    }
}
