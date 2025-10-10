package top.teek.utils;

import cn.hutool.core.date.DatePattern;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Teeker
 * @date 2024/4/15 23:19:12
 * @note
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DateTimeUtil {

    public static String now() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 基于当前日期获取前面的日期
     */
    public static String minus(Integer minusCount, ChronoUnit chronoUnit) {
        return formatDateTime(LocalDateTime.now().minus(minusCount, chronoUnit));
    }

    /**
     * 基于当前日期获取后面的日期
     */
    public static String plus(Integer minusCount, ChronoUnit chronoUnit) {
        return formatDateTime(LocalDateTime.now().plus(minusCount, chronoUnit));
    }

    /**
     * 格式化日期
     */
    public static String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
    }

    /**
     * 格式化日期
     */
    public static String formatDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
    }

    /**
     * 格式化日期
     */
    public static String formatDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
    }

    /**
     * 格式化日期
     */
    public static String format(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 格式化日期
     */
    public static String format(LocalDate localDate, DateTimeFormatter dateTimeFormatter) {
        return localDate.format(dateTimeFormatter);
    }

    /**
     * 获取当天的最小时间 * * @param localDate * @return
     */
    public static String minTimeInDate(LocalDate localDate) {
        return formatDateTime(LocalDateTime.of(localDate, LocalTime.MIN));
    }

    /**
     * 获取 localDate 所在天的最大时间 * * @param localDate * @return
     */
    public static String maxTimeInDate(LocalDate localDate) {
        return formatDateTime(LocalDateTime.of(localDate, LocalTime.MAX));
    }

    /**
     * 获取当月的最小日期，月初 * * @param localDate * @return
     */
    public static String minDateInMonth(LocalDate localDate) {
        return formatDate(localDate.with(TemporalAdjusters.firstDayOfMonth()));
    }

    /**
     * 获取当月的最大日期，月底 * * @param localDate * @return
     */
    public static String maxDateInMonth(LocalDate localDate) {
        return formatDate(localDate.with(TemporalAdjusters.lastDayOfMonth()));
    }

    /**
     * 获取时间范围集合
     */
    public static List<String> getDateRange(LocalDate start, LocalDate end) {
        List<String> dateRange = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            dateRange.add(formatDate(date));
        }
        return dateRange;
    }

    /**
     * 获取时间范围集合
     */
    public static List<String> getDateRange(LocalDate start, LocalDate end, Function<LocalDate, String> function) {
        List<String> dateRange = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            dateRange.add(function.apply(date));
        }
        return dateRange;
    }
}
