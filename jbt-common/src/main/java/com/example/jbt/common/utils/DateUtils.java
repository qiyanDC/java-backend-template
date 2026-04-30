package com.example.jbt.common.utils;

import java.lang.management.ManagementFactory;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.jbt.common.constants.DatePattern;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author qiyan
 * @version 1.0
 * @since 2024-04-28
 */
public class DateUtils {

	private DateUtils() {}
	
	static String[] parsePatterns = {"yyyy[/M][/d][ ]['T'][HH][:][时][mm][:][分][ss][秒][,][.]"};
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN_FULL = "yyyyMMddHHmmss";
	
	/**
	 * 解析字符串为日期格式的配置
		 * 允许的格式为：yyyy,yyyy-MM-dd,yyyy-MM-dd HH:mm:ss.SSSSSSSSS
		 * 以及：yyyy/MM/dd，yyyy年MM月dd日等
	 * 只有yyyy是必选项
	 */
	private static DateTimeFormatter DEFAULT_FORMATTER = new DateTimeFormatterBuilder()
	    .appendPattern("yyyy[[-][/][.][年]MM][[-][/][.][月]dd[日]][ ]['T'][HH][:][时][mm][:][分][ss][秒][,][.]")
		.optionalStart()
		.appendValue(ChronoField.NANO_OF_SECOND, 1, 10, SignStyle.NORMAL)
		.optionalEnd()
	    .parseDefaulting(ChronoField.YEAR_OF_ERA, 0)
	    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
	    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
	    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
	    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
	    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
	    .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
	    .toFormatter()
	    .withZone(DateUtils.getDefaultZoneId());
	
	/**
	 * 解析字符串为日期格式的配置
		 * 允许的格式为：yyyyMMddHHmmss.SSSSSSSSS
	 * 只有yyyy是必选项
	 */
	private static DateTimeFormatter SMALL_FORMATTER = new DateTimeFormatterBuilder()
		.appendValue(ChronoField.YEAR_OF_ERA, 4, 4, SignStyle.EXCEEDS_PAD)
	    .optionalStart().appendValue(ChronoField.MONTH_OF_YEAR, 2, 2, SignStyle.EXCEEDS_PAD).optionalEnd()
	    .optionalStart().appendValue(ChronoField.DAY_OF_MONTH, 2, 2, SignStyle.EXCEEDS_PAD).optionalEnd()
	    .optionalStart().appendValue(ChronoField.HOUR_OF_DAY, 2, 2, SignStyle.EXCEEDS_PAD).optionalEnd()
	    .optionalStart().appendValue(ChronoField.MINUTE_OF_HOUR, 2, 2, SignStyle.EXCEEDS_PAD).optionalEnd()
	    .optionalStart().appendValue(ChronoField.SECOND_OF_MINUTE, 2, 2, SignStyle.EXCEEDS_PAD).optionalEnd()
	    .optionalStart().appendLiteral('.').appendValue(ChronoField.NANO_OF_SECOND, 1, 10, SignStyle.NORMAL).optionalEnd()
	    .parseDefaulting(ChronoField.YEAR_OF_ERA, 0)
	    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
	    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
	    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
	    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
	    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
	    .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
	    .toFormatter()
	    .withZone(DateUtils.getDefaultZoneId());
	
	/**
	 * 系统要用的默认时区
	 * @return
	 */
	public static ZoneId getDefaultZoneId() {
		return ZoneId.systemDefault();
	}

	/**
	 * 获取当前时间，便于绑定时钟或者统一切换
	 * @return
	 */
	public static LocalDateTime now() {
		return LocalDateTime.now();//SystemDefaultZone在需要时重新指定，或者使用特定时钟
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), DateUtils.getDefaultZoneId());
	}

	public static LocalDateTime toLocalDateTime(long epochMilli) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), getDefaultZoneId());
	}

	public static Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(DateUtils.getDefaultZoneId()).toInstant());
	}

	public static String format(long dateTime, String pattern) {
		return DateUtils.format(toLocalDateTime(dateTime), pattern);
	}

	/**
		 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String format(LocalDateTime date) {
		return DateUtils.format(date, DateUtils.DATE_TIME_PATTERN);
	}

	public static String format(LocalDateTime localDateTime, String pattern) {
		Objects.requireNonNull(localDateTime, "date must not be null");

		if (ObjectUtils.isEmpty(pattern)) {
			pattern = DateUtils.DATE_PATTERN;
		}
		return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalDateTime parse(String dateString, String... patterns) {
		Objects.requireNonNull(dateString, "date must not be null");

		TemporalAccessor temporalAccessor = null;
		if(patterns.length == 0) {
			temporalAccessor = safeParse(DEFAULT_FORMATTER, dateString);

			if(null == temporalAccessor) temporalAccessor = safeParse(SMALL_FORMATTER, dateString);

			if(null == temporalAccessor) {
				for(String pattern: DateUtils.parsePatterns) {
					temporalAccessor = safeParse(DateTimeFormatter.ofPattern(pattern), dateString);

					if(null != temporalAccessor) break;
				}
			}
		} else {
			for(String pattern: patterns) {
				temporalAccessor = safeParse(DateTimeFormatter.ofPattern(pattern), dateString);
				if(null != temporalAccessor) break;
			}
		}
		return DateUtils.parse(temporalAccessor);
	}

	private static TemporalAccessor safeParse(DateTimeFormatter dtf, String dateString) {
		try {
			return dtf.parse(dateString);
		} catch (Exception ex2) {}
		return null;
	}

	private static LocalDateTime parse(TemporalAccessor temporalAccessor) {
		if(null == temporalAccessor) return null;

		LocalDateTime localDateTime = LocalDateTime.of(
		    safeGet(temporalAccessor, ChronoField.YEAR),
		    safeGet(temporalAccessor, ChronoField.MONTH_OF_YEAR),
		    safeGet(temporalAccessor, ChronoField.DAY_OF_MONTH),
		    safeGet(temporalAccessor, ChronoField.HOUR_OF_DAY),
		    safeGet(temporalAccessor, ChronoField.MINUTE_OF_HOUR),
		    safeGet(temporalAccessor, ChronoField.SECOND_OF_MINUTE),
		    safeGet(temporalAccessor, ChronoField.NANO_OF_SECOND)
		);
		return localDateTime;
	}

	private static int safeGet(TemporalAccessor temporalAccessor, ChronoField chronoField) {
		if (temporalAccessor.isSupported(chronoField)) {
	        return temporalAccessor.get(chronoField);
	    }
		if(chronoField == ChronoField.MONTH_OF_YEAR || chronoField == ChronoField.DAY_OF_MONTH) {
			return 1;
		}
		if(chronoField == ChronoField.YEAR) {
			return 1970;
		}
	    return 0;//如果返回0,会导致日期不正确
	}

	public static boolean between(LocalDateTime one, LocalDateTime begin, LocalDateTime end) {
		return (DateUtils.equals(one, begin) || DateUtils.greaterThen(one, begin))
				&& (DateUtils.lessThen(one, end) || DateUtils.equals(one, end));
	}

	public static boolean equals(LocalDateTime one, LocalDateTime target) {
		if(null == one || null == target) return false;

		return one.isEqual(target);
	}

	public static boolean equals(LocalDate one, LocalDate target) {
		if(null == one || null == target) return false;

		return one.isEqual(target);
	}

	public static boolean greaterThen(LocalDateTime one, LocalDateTime target) {
		if(null == one || null == target) return false;

		return one.isAfter(target);
	}

	public static boolean lessThen(LocalDateTime one, LocalDateTime target) {
		if(null == one || null == target) return false;

		return one.isBefore(target);
	}

	public static long toEpochSecond(LocalDateTime localDateTime) {
		return localDateTime.atZone(getDefaultZoneId()).toEpochSecond();
	}
	public static long toEpochMilli(LocalDateTime localDateTime) {
		return localDateTime.atZone(getDefaultZoneId()).toInstant().toEpochMilli();
	}

	// ------------------------------ 年处理start ------------------------------ //

	/**
	 * 根据提供日期获取所在年份
	 *
	 * @param date
	 * @return
	 */
	public static int getYearOfCurrentDay(LocalDateTime date) {
		return date.getYear();
	}

	// ------------------------------ 年处理end ------------------------------ //

	// ------------------------------ 月处理start ------------------------------ //

	/**
	 * 获取某月有几天
	 * @param localDateTime 日期
	 * @return 天数
	 */
	public static int getMonthHasDays(LocalDateTime localDateTime){
		LocalDate start = localDateTime.toLocalDate().withDayOfMonth(1);
		LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
		return Period.between(start, end).getDays() + 1;
	}

	/**
	 * 获取当月天数
	 * @return
	 */
	public static int getDaysOfCurrentMonth() {
		LocalDateTime now = DateUtils.now();
		return now.toLocalDate().lengthOfMonth();
	}

	/**
	 * 获取当月剩余天数
	 *
	 * @return
	 */
	public static int getDaysOfCurrentMonthResidue() {
		LocalDateTime now = DateUtils.now();
		int day = now.getDayOfMonth();    //获取当前天数
		int last = DateUtils.getMonthHasDays(now);
		return last - day;
	}

	/**
	 * 是否为月底
	 * */
	public static boolean isLastDayOfMonth(LocalDateTime localDateTime) {
		LocalDateTime lastDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth());
		return DateUtils.equals(localDateTime, lastDay);
	}

	/**
	 * 根据提供日期获取所在月份
	 *
	 * @param date
	 * @return
	 */
	public static int getMonthOfCurrentDay(LocalDateTime date) {
		return date.getMonthValue() + 1;
	}

	/**
	 * 获取某个月开始和结束时间
		 * 举例：2021-08-01 -- 2021-08-31
	 *
		 * @param amount -1：上个月  1：下个月
	 * @return
	 */
	public static Map<String, String> getMonth(int amount) {
		Map<String, String> map = new HashMap<>();
		LocalDateTime now = DateUtils.now();
		now = now.plusMonths(amount);
		int lastMonthMaxDay = DateUtils.getMonthHasDays(now);
		LocalDateTime startTime = LocalDateTime.of(LocalDate.of(now.getYear(), now.getMonthValue(), 1), LocalTime.MIN);
		map.put("startTime", DateUtils.format(startTime, DATE_PATTERN));
		LocalDateTime endTime = LocalDateTime.of(LocalDate.of(now.getYear(), now.getMonthValue(), lastMonthMaxDay), LocalTime.MAX);
		map.put("endTime", DateUtils.format(endTime, DATE_PATTERN));
		return map;
	}

	/**
	 * 获取某个月开始和结束时间
		 * 举例：2021-08-01 -- 2021-08-31
	 *
		 * @param amount -1：上个月  1：下个月
	 * @return
	 */
	public static Map<String, LocalDateTime> getMonthDate(int amount) {
		Map<String, LocalDateTime> map = new HashMap<>();
		LocalDateTime now = DateUtils.now();
		now = now.plusMonths(amount);
		int lastMonthMaxDay = DateUtils.getMonthHasDays(now);
		map.put("startTime", LocalDateTime.of(LocalDate.of(now.getYear(), now.getMonthValue(), 1), LocalTime.MIN));
		map.put("endTime", LocalDateTime.of(LocalDate.of(now.getYear(), now.getMonthValue(), lastMonthMaxDay), LocalTime.MAX));
		return map;
	}

	/**
	 * 根据日期获取本月第一天
	 *
	 * @param date
	 * @return
	 */
	public static LocalDateTime getMonthStart(LocalDateTime date) {
		return date.with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * 根据日期获取本月最后一天
	 *
	 * @param date
	 * @return
	 */
	public static LocalDateTime getMonthEnd(LocalDateTime date) {
		return date.with(TemporalAdjusters.lastDayOfMonth());
	}

	/**
	 * 根据日期获得所在月-第一天的日期
	 * */
	public static String getMonthFirstDayByDate(String date) {
		LocalDate localDate = DateUtils.parse(date).toLocalDate();
		LocalDate monthDay = localDate.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime localDateTime = LocalDateTime.of(monthDay.getYear(), monthDay.getMonthValue(), monthDay.getDayOfMonth(), 0, 0, 0);
		return DateUtils.format(localDateTime, DATE_PATTERN);
	}

	/**
	 * 根据日期获得所在月-最后一天的日期
	 * */
	public static String getMonthLastDayByDate(String date) {
		LocalDate localDate = DateUtils.parse(date).toLocalDate();
		LocalDate monthDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
		LocalDateTime localDateTime = LocalDateTime.of(monthDay.getYear(), monthDay.getMonthValue(), monthDay.getDayOfMonth(), 0, 0, 0);
		return DateUtils.format(localDateTime, DATE_PATTERN);
	}

	/**
	 * 获取两个日期间的月份列表
	 *
	 * @param startDate yyyy-MM-dd
	 * @param endDate   yyyy-MM-dd
	 * @return List<yyyyMM>
	 */
	public static List<Long> monthBetweenList(String startDate, String endDate) {
		List<Long> list = new ArrayList<>();
		int num = monthsBetween(startDate, endDate);
		for (int i = 0; i < num; i++) {
			list.add(Long.parseLong(plusMonthFirstDay(startDate, i).substring(0, 7).replace("-", "")));
		}
		return list;
	}

	/**
	 * 获取两个日期间的月份数
	 *
	 * @param startDate yyyy-MM-dd
	 * @param endDate   yyyy-MM-dd
	 * @return int
	 */
	public static int monthsBetween(String startDate, String endDate) {
		LocalDate sDate = LocalDate.parse(startDate);
		LocalDate eDate = LocalDate.parse(endDate);
		int months = 0;
		if (sDate.getYear() == eDate.getYear()) {
			months = eDate.getMonthValue() - sDate.getMonthValue() + 1;
		} else if (eDate.getYear() > sDate.getYear()) {
			months = (eDate.getYear() - sDate.getYear()) * 12 - sDate.getMonthValue() + eDate.getMonthValue() + 1;
		}
		return months;
	}

	/**
	 * 获取指定日期加上固定月份的开始日期
	 *
	 * @param date  日期
	 * @param month 月
	 * @return String
	 */
	public static String plusMonthFirstDay(String date, int month) {
		return LocalDate.parse(date).with(TemporalAdjusters.firstDayOfMonth()).plusMonths(month).toString();
	}

	/**
	 * 获取起始时间到结束时间的月末时间
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getTotalMonthEnd(LocalDateTime startDate, LocalDateTime endDate) {
		List<String> monthEnd = new ArrayList<>();
		if(ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate)) {
			return monthEnd;
		}
		LocalDate endOfMonth = startDate.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());
		while (!endOfMonth.isAfter(endDate.toLocalDate())) {
			monthEnd.add(DateUtils.format(endOfMonth.atStartOfDay(), DatePattern.PURE_DATE_PATTERN));
			endOfMonth = endOfMonth.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		}
		return monthEnd;
	}

	/**
	 * 获取上个月的最后一天
	 *
	 * @return
	 */
	public static LocalDateTime lastMonthLastDay() {
		LocalDateTime now = DateUtils.now();
		LocalDateTime date = now.plusMonths(-1);
		date = date.with(TemporalAdjusters.lastDayOfMonth());
		return date;
	}

	// ------------------------------ 月处理end ------------------------------ //

	// ------------------------------ 周处理start ------------------------------ //

	/**
	 * 获取日期是当年的第几周
	 * @param localDateTime
	 * @return
	 */
	public static int getWeekOfYear(LocalDateTime localDateTime){
		return localDateTime.get(WeekFields.ISO.weekOfYear());
	}

	/**
	 * 根据日期获得所在周-周几的日期
	 * */
	public static String getWeekDayByDate(String date, long time) {
		LocalDate localDate = DateUtils.parse(date).toLocalDate();
		LocalDate weekDay = localDate.with(WeekFields.ISO.dayOfWeek(), time);
		LocalDateTime localDateTime = LocalDateTime.of(weekDay.getYear(), weekDay.getMonthValue(), weekDay.getDayOfMonth(), 0, 0, 0);
		return DateUtils.format(localDateTime, DATE_PATTERN);
	}

	/**
	 * @param start 开始日期对应的周一
	 * @param end   结束日期对应的周日
	 * @return List<Integer>  yyyyMMdd
	 * @Description 获取两个时间段当前所在周
	 */
	public static ArrayList<Integer> getWeekBetween(Integer start, Integer end) {
		ArrayList<Integer> result = new ArrayList<>();
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyyMMdd");//格式化为年月
		try {
			LocalDate c_begin = LocalDate.parse(String.valueOf(start), sdf);
			LocalDate c_end = LocalDate.parse(String.valueOf(end), sdf);
			while (c_begin.isBefore(c_end)) {
				int week = c_begin.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
				int year = c_begin.get(IsoFields.WEEK_BASED_YEAR);
				result.add(year * 100 + week);
				c_begin = c_begin.plusDays(7);
			}
		} catch (DateTimeParseException p) {
			p.printStackTrace();
		}
		return result;
	}

	/**
	 * 获得本周的第一天，周一
	 *
	 * @return Date
	 */
	public static LocalDateTime getCurrentWeekMonday(LocalDateTime date) {
		LocalDate monday = date.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		return LocalDateTime.of(monday, LocalTime.MIN);
	}

	/**
	 * 获得本周的最后一天，周日
	 *
	 * @return Date
	 */
	public static LocalDateTime getCurrentWeekSunday(LocalDateTime date) {
		LocalDate monday = date.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
		return LocalDateTime.of(monday, LocalTime.MIN);
	}

	/**
	 * 获取某周 周一和周日 时间
	 *
		 * @param week -1：上周周一周日 0：本周周一周日  1：下个周周一周日
	 * @return Map
	 */
	public static Map<String, LocalDateTime> getWeekDate(int week) {
		Map<String, LocalDateTime> map = new HashMap<>();
		LocalDateTime now = DateUtils.now();
		LocalDateTime date = now.plusDays(7L * week);
		map.put("startTime", DateUtils.getCurrentWeekMonday(date));
		map.put("endTime", DateUtils.getCurrentWeekSunday(date));
		return map;
	}

	// ------------------------------ 周处理end ------------------------------ //

	// ------------------------------ 日处理start ------------------------------ //

	/**
	 * 当天开始时间
	 * */
	public static LocalDateTime startOfDay(LocalDateTime localDateTime) {
		return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
	}

	/**
	 * 当天结束时间
	 * */
	public static LocalDateTime endOfDay(LocalDateTime localDateTime) {
		return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
	}

	/**
	 * 增加天数
	 * */
	public static LocalDateTime plusDays(LocalDateTime localDateTime, int days) {
		return localDateTime.plusDays(days);
	}


	/**
	 * 格式化为日期范围字符串
	 * @param beginDate 2018-01-01
	 * @param endDate 2018-01-31
	 * @return 2018-01-01 ~ 2018-01-31
	 */
	public static String formatDateBetweenString(LocalDateTime beginDate, LocalDateTime endDate){
		if(null == beginDate || null == endDate) return null;
		return beginDate.toLocalDate().toString() + " ~ " + endDate.toLocalDate().toString();
	}

	/**
	 * 解析日期范围字符串为日期对象
	 * @param dateString 2018-01-01 ~ 2018-01-31
	 * @return new Date[]{2018-01-01, 2018-01-31}
	 */
	public static LocalDateTime[] parseDateBetweenString(String dateString){
		LocalDateTime beginDate = null; LocalDateTime endDate = null;
		if (!ObjectUtils.isEmpty(dateString)){
			String[] ss = StringUtils.split(dateString, "~");
			if (ss != null && ss.length == 2){
				String begin = StringUtils.trim(ss[0]);
				String end = StringUtils.trim(ss[1]);
				if (StringUtils.isNoneBlank(begin, end)){
					beginDate = DateUtils.parse(begin);
					endDate = DateUtils.parse(end);
				}
			}
		}
		return new LocalDateTime[]{beginDate, endDate};
	}

	/**
		 * 将时间转换为字符串（xx天，xx时，xx分，xx秒，大于360天显示日期时间）
	 */
	public static String formatDateAgo(long dateTime) {
		StringBuilder sb = new StringBuilder();
		if (dateTime < 1000){
			sb.append(dateTime).append("毫秒");
		} else {
			LocalDateTime localDateTime = toLocalDateTime(dateTime);
			int day = localDateTime.getDayOfMonth();
			int hour = localDateTime.getHour();
			int minute = localDateTime.getMinute();
			int second = localDateTime.getSecond();
			if (day > 365){
				return DateUtils.format(localDateTime, "yyyy年MM月dd日 HH时mm分ss秒");
			}
			if (day > 0){
				sb.append(day).append("天");
			}
			if (hour > 0){
				sb.append(hour).append("时");
			}
			if (minute > 0){
				sb.append(minute).append("分");
			}
			if (second > 0){
				sb.append(second).append("秒");
			}
		}
		return sb.toString();
	}

	/**
	 * 将过去的时间转为为，刚刚，xx秒，xx分钟，xx小时前、xx天前，大于3天的显示日期
	 */
	public static String formatTimeAgo(String dateTime) {
		return formatTimeAgo(DateUtils.parse(dateTime));
	}

	/**
	 * 将过去的时间转为为，刚刚，xx秒，xx分钟，xx小时前、xx天前，大于3天的显示日期
	 */
	public static String formatTimeAgo(LocalDateTime dateTime) {
		String interval = null;
		;
		// 得出的时间间隔是毫秒
		long time = System.currentTimeMillis() - DateUtils.toEpochMilli(dateTime);
		// 如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
		if (time / 1000 < 10 && time / 1000 >= 0) {
			interval = "刚刚";
		}
		// 如果时间间隔大于24小时则显示多少天前
		else if (time / 3600000 < 24*4 && time / 3600000 >= 24) {
			int d = (int) (time / (3600000*24));// 得出的时间间隔的单位是天
			interval = d + "天前";
		}
		// 如果时间间隔小于24小时则显示多少小时前
		else if (time / 3600000 < 24 && time / 3600000 >= 1) {
			int h = (int) (time / 3600000);// 得出的时间间隔的单位是小时
			interval = h + "小时前";
		}
		// 如果时间间隔小于60分钟则显示多少分钟前
		else if (time / 60000 < 60 && time / 60000 >=1) {
			int m = (int) ((time % 3600000) / 60000);// 得出的时间间隔的单位是分钟
			interval = m + "分钟前";
		}
		// 如果时间间隔小于60秒则显示多少秒前
		else if (time / 1000 < 60 && time / 1000 >=10) {
			int se = (int) ((time % 60000) / 1000);
			interval = se + "秒前";
		}
		// 大于3天的，则显示正常的时间，但是不显示秒
		else {
			interval = DateUtils.format(dateTime,"yyyy-MM-dd");
		}
		return interval;
	}

	/**
	 * 根据日期获取前一天日期
	 * */
	public static LocalDateTime getPreDayByDate(String date) {
		LocalDate localDate = DateUtils.parse(date).toLocalDate();
		LocalDate beforeDay = localDate.minusDays(1);
		LocalTime localTime = DateUtils.parse(date).toLocalTime();
		return LocalDateTime.of(beforeDay, localTime);
	}

	/**
	 * 获取时间间隔
	 *
	 * @return {@link Duration}
	 */
	public static Duration getTimeConsuming(LocalDateTime beginTime, LocalDateTime endTime) {
		return Duration.between(beginTime, endTime);
	}

	/**
	 * 获取两个日期间的日期列表
	 *
	 * @param startDate yyyy-MM-dd
	 * @param endDate   yyyy-MM-dd
	 * @return List<Long>
	 */
	public static List<Long> dayBetweenList(String startDate, String endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		List<Long> list = new ArrayList<>();
		LocalDate sDate = LocalDate.parse(startDate);
		int num = daysBetween(startDate, endDate);
		for (int i = 0; i < num; i++) {
			list.add(Long.parseLong(sDate.plusDays(i).format(formatter)));
		}

		return list;
	}

	/**
	 * 获取两个日期之间的天数
	 *
	 * @param startDate yyyy-MM-dd
	 * @param endDate   yyyy-MM-dd
	 * @return int
	 */
	public static int daysBetween(String startDate, String endDate) {
		LocalDate sDate = DateUtils.parse(startDate).toLocalDate();
		LocalDate eDate = DateUtils.parse(endDate).toLocalDate();
		int days = 0;
		if (sDate.getYear() == eDate.getYear()) {
			days = eDate.getDayOfYear() - sDate.getDayOfYear() + 1;
		} else if (sDate.getYear() < eDate.getYear()) {
			int num = eDate.getYear() - sDate.getYear();
			days = eDate.getDayOfYear() - sDate.getDayOfYear() + 1;
			for (int i = 0; i < num; i++) {
				sDate = sDate.plusYears(i);
				days += sDate.lengthOfYear();
			}
		}
		return days;
	}

	// ------------------------------ 日处理end ------------------------------ //

	/**
	 * 获取服务器启动时间
	 * @param
	 * @return
	 */
	public static LocalDateTime getServerStartDate() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return toLocalDateTime(time);
	}

	/**
	 * 获取上个月月末时间
	 * @return
	 */
	public static LocalDateTime getLastMonthEnd() {
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).minusDays(1).with(TemporalAdjusters.lastDayOfMonth());
	}
}