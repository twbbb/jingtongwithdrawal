package com.twb.utils;

public class MathUtils
{
	/**
	 * 
	 * @Title: string2Int
	 * @Description: String转int类型
	 * @param @param str
	 * @param @param defaultVal
	 * @param @return
	 * @return int
	 * @throws
	 */
	public static int string2Int(String str, int defaultVal)
	{
		int i;
		try
		{
			i = Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
			
			i = defaultVal;
			e.printStackTrace();
		}
		return i;
	}
	
	public static double string2Double(String str, double defaultVal)
	{
		double i;
		try
		{
			i = Double.parseDouble(str);
		}
		catch (NumberFormatException e)
		{
			
			i = defaultVal;
			e.printStackTrace();
		}
		return i;
	}
}
