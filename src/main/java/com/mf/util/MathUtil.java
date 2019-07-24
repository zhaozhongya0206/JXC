package com.mf.util;

/**
 * 数学工具类
 * @author Administrator
 *
 */
public class MathUtil {

	/**
	 * 格式化数字  保留两位
	 * @param n
	 * @return
	 */
	public static float format2Bit(float n){
		return (float)(Math.round(n*100))/100;
	}
	
	public static void main(String[] args) {
		System.out.println(format2Bit(2.53532f));
	}
}
