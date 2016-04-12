package com.youshangxiaozi.testweidge;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志管理
 * 说明：当LOG_ENABLE为false时，不打印日志，发布以前注意屏蔽日志
 * @author 
 *
 */
public class LogUtils
{
	// 日志开关
	public final static boolean	LOG_ENABLE	= true;

	public final static String TAG_SOCKET = "tag_socket";
	public final static String TAG_NSD = "tag_nsd";

	public static int v(String tag, String msg)
	{
		return LOG_ENABLE ? Log.v(tag, msg) : 0;
	}

	public static int v(String tag, String msg, Throwable tr)
	{
		return LOG_ENABLE ? Log.v(tag, msg, tr) : 0;
	}

	public static int d(String tag, String msg)
	{
		return LOG_ENABLE ? Log.d(tag, msg) : 0;
	}

	public static int d(String tag, String msg, Throwable tr)
	{
		return LOG_ENABLE ? Log.d(tag, msg, tr) : 0;
	}

	public static int i(String tag, String msg)
	{
		return LOG_ENABLE ? Log.i(tag, msg) : 0;
	}

	public static int i(String tag, String msg, Throwable tr)
	{
		return LOG_ENABLE ? Log.i(tag, msg, tr) : 0;
	}

	public static int w(String tag, String msg)
	{
		return LOG_ENABLE ? Log.w(tag, msg) : 0;
	}

	public static int w(String tag, String msg, Throwable tr)
	{
		return LOG_ENABLE ? Log.w(tag, msg, tr) : 0;
	}

	public static int w(String tag, Throwable tr)
	{
		return LOG_ENABLE ? Log.w(tag, tr) : 0;
	}

	public static int e(String tag, String msg)
	{
		return LOG_ENABLE ? Log.e(tag, msg) : 0;
	}

	public static int e(String tag, String msg, Throwable tr)
	{
		return LOG_ENABLE ? Log.e(tag, msg, tr) : 0;
	}

	/**
	 * 写文件日志
	 * @param logText
	 */
	public static void writeFile(String logText)
	{
		if (LOG_ENABLE)
		{
			String sDStateString = android.os.Environment.getExternalStorageState();
			// 拥有可读可写权限
			if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED))
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());
				String strDateTime = dateFormat.format(curDate);
				String strLog = strDateTime + "    " + logText;

				try
				{
					// 获取扩展存储设备的文件目录
					File sDFile = android.os.Environment.getExternalStorageDirectory();

					// 打开文件
					String logFilePath = "treasure_box.txt";
					File logFile = new File(sDFile.getAbsolutePath() + File.separator + logFilePath);

					// 判断是否存在,不存在则创建 ,存在则删除
					if (!logFile.exists())
					{
						logFile.createNewFile();
					}

					// 写数据
					FileOutputStream outputStream = new FileOutputStream(logFile, true);
					outputStream.write(strLog.getBytes());
					String endLine = "\n";
					outputStream.write(endLine.getBytes());

					outputStream.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}