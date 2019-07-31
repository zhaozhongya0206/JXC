package com.mf.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 * @author Administrator
 *
 */
public class DateUtil {

	/**
     * format pattern is "yyyy-MM-dd"
     */
    public static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";
    /**
     * format pattern is "HH:mm:ss"
     */
    public static final String HOUR_MINUTE_SECOND_PATTERN = "HH:mm:ss";
    /**
     * format pattern is "HH:mm:ss"
     */
    public static final String YMDHMS_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * format pattern is "yyyyMMddHHmmss"
     */
    public static final String YMDHMS_LIST_PATTERN = "yyyyMMddHHmmss";
    
	/**
	 * 获取当年年月日字符串
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentDateStr()throws Exception{
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	
	 /**
     * 字符串形式的日期，格式为yyyyMMddHHmmss
     * @return 字符串类型的日期
     * @throws java.sql.SQLException
     */
    public static String currentDateIDString() throws Exception {
        return formatDate(new Date(), YMDHMS_LIST_PATTERN);
    }
    
	/**
	 * 把日期字符串格式化成日期对象
	 * @param str
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Date formatString(String str,String format)throws Exception{
		if(StringUtil.isEmpty(str)){
			return null;
		}
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		return sdf.parse(str);
	}
	
	/**
	 * 把日期对象格式化成字符串
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String formatDate(Date date,String format)throws Exception{
		String result="";
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		if(date!=null){
			result=sdf.format(date);
		}
		return result;
	}
	
	/**
	 * 获取指定范围内的日期集合
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public static List<String> getRangeDates(String begin,String end)throws Exception{
		List<String> datas=new ArrayList<String>();
		Calendar cb=Calendar.getInstance();
		Calendar ce=Calendar.getInstance();
		cb.setTime(formatString(begin,"yyyy-MM-dd"));
		ce.setTime(formatString(end,"yyyy-MM-dd"));
		datas.add(begin);
		while(cb.before(ce)){
			cb.add(Calendar.DAY_OF_MONTH, 1);
			datas.add(formatDate(cb.getTime(),"yyyy-MM-dd"));
		}
		return datas;
	}
	
	/**
	 * 计算两个日期相减，返回秒
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public static String beginEndTime(Date beginDate, Date endDate) throws Exception {
		//计算时间差
        long diff = endDate.getTime() - beginDate.getTime();
        //计算秒
        long seconds = (diff % (1000 * 60)) / 1000;
        //输出
        System.out.println("日期时间秒差："+seconds+"秒");
        return String.valueOf(seconds);
	}
	
	/**
	 * 获取指定范围内的月份集合
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public static List<String> getRangeMonths(String begin,String end)throws Exception{
		List<String> months=new ArrayList<String>();
		Calendar cb=Calendar.getInstance();
		Calendar ce=Calendar.getInstance();
		cb.setTime(formatString(begin,"yyyy-MM"));
		ce.setTime(formatString(end,"yyyy-MM"));
		months.add(begin);
		while(cb.before(ce)){
			cb.add(Calendar.MONTH, 1);
			months.add(formatDate(cb.getTime(),"yyyy-MM"));
		}
		return months;
	}
	
	public static void main(String[] args) throws Exception {
		List<String> dates=getRangeMonths("2016-10","2017-12");
		for(String date:dates){
			System.out.println(date);
		}
	}
}
