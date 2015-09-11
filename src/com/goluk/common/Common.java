package com.goluk.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;

public class Common {

	public final static String TAG = "GolukTest-";
	public final static String FindObject = "[Find Object]: ";
	public final static String NotFindObject = "[Not Find Object]: ";
	public final static String TestClick = "[Click Step]: ";
	public final static String TestInfo = "[Test Info]: ";
	public final static String FailedTestInfo = "[Test Step Failed]: ";
	public final static String TestResult = "[Test Result]: ";
	public final static String FailReason = "[Fail Reason]: ";
	// 华为MT7
	// public final static int[][] al = { { 406, 396 }, { 815, 396 },
	// { 406, 843 }, { 815, 843 }, { 406, 923 }, { 815, 923 },
	// { 406, 1166 }, { 815, 1166 } };
	// public final static int[] idlelocation = { 958, 1360 };
	// public final static int[] pause = { 136, 960 };
	// 小米Note
	// public final static int[][] al = { { 390, 425 }, { 815, 425 },
	// { 390, 666 }, { 815, 666 }, { 390, 920 }, { 815, 920 },
	// { 390, 1160 }, { 815, 1160 } };
	// public final static int[] idlelocation = { 921, 1449 };
	// public final static int[] pause = { 118, 949 };
	// 三星
	// public final static int[][] al = { { 403, 497 }, { 804, 497 }, { 403, 742
	// }, { 804, 742 },
	// { 403, 988 }, { 804, 988 }, { 403, 1318 },{804,1318} };
	// public final static int[] idlelocation={928,1234};
	// public final static int[] pause={118,949};

	// 魅蓝Note
	// public final static int[][] al = { { 402, 456 }, { 814, 456 },
	// { 402, 814 }, { 814, 814 }, { 402, 1013 }, { 814, 1013 },
	// { 402, 1268 }, { 814, 1268 } };
	// public final static int[] idlelocation = { 908, 1282 };
	// public final static int[] pause = { 141, 969 };

	public final static String[] filterName = { "无", "清新淡雅", "黑白经典", "多彩夏日",
			"复古怀旧", "缤纷梦幻", "柔和静谧", "古典胶片" };
	public final static String[] typeName = { "#曝光台", "#事故爆料", "#美丽风景", "#随手拍" };

	// Open the activity
	public static void openActivity() {
		try {
			Runtime.getRuntime()
					.exec("am start -n cn.com.mobnote.golukmobile/cn.com.mobnote.golukmobile.GuideActivity");
		} catch (IOException e) {
			System.out.println("Not open");
			e.printStackTrace();
		}
	}

	public static int[] getAnyLocation(String runcase, UiDevice device) {
		int h = device.getDisplayHeight();
		int w = device.getDisplayWidth();
		int[] al = { w / 2, h / 3 };
		return al;
	}

	/**
	 * 通过坐标点击App
	 * 
	 * @param runcase
	 *            测试用例名称
	 * @param device
	 *            UiDevice.getInstance()实例
	 * @param resourceid
	 *            验证App打开的控件，比如行车记录仪控件
	 * @throws Exception
	 *             打开失败跑出异常
	 */
	public static void openActivity(String runcase, UiDevice device,
			String resourceid) throws Exception {
		Runtime.getRuntime()
				.exec("am start -n cn.com.mobnote.golukmobile/cn.com.mobnote.golukmobile.GuideActivity");
		waitTime(5);
		infoLog(runcase, TestInfo + "Goluk已经启动");
		UiObject object = findViewById2(device, resourceid);
		int i = 1;
		while (i < 30) {
			// 取消升级提醒
			if (findViewByText2(device, "稍后再说").exists()) {
				findViewByText2(device, "稍后再说").clickAndWaitForNewWindow();
				infoLog(runcase, TestInfo + "Goluk已经取消升级");
			}
			// 取消续播提示
			if (findViewByText2(device, "取消").exists()) {
				findViewByText2(device, "取消").clickAndWaitForNewWindow();
			}
			if (object.exists()) {
				infoLog(runcase, TestInfo + "Goluk可以正常工作");
				break;
			}
			if (findViewById2(device,
					"cn.com.mobnote.golukmobile:id/loading_bg").exists()) {
				infoLog(runcase, TestInfo + "Goluk正在加载 " + i + "s");
			}
			waitTime(1);
			i++;
		}
	}

	/**
	 * 强查找控件，不存在抛出异常，当前用例失败
	 * 
	 * @param runcase
	 *            测试用例名称
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param resourceid
	 *            控件id
	 * @return 如果存在返回控件，不存在抛出控件不存在的异常
	 * @throws Exception
	 */
	public static UiObject findViewById(String runcase, UiDevice devices,
			String resourceid) throws Exception {
		UiObject object = new UiObject(
				new UiSelector().resourceIdMatches(resourceid));
		if (object.exists()) {
			infoLog(runcase, FindObject + resourceid);
			return object;
		} else {
			throw new Exception(NotFindObject + resourceid);
		}
	}

	public static String getViewTextById(String runcase, UiDevice devices,
			String resourceid) throws Exception {
		UiObject object = new UiObject(
				new UiSelector().resourceIdMatches(resourceid));
		if (object.exists()) {
			infoLog(runcase, FindObject + resourceid);
			return object.getText();
		} else {
			throw new Exception(NotFindObject + resourceid);
		}
	}

	/**
	 * 弱查找空间，不存在返回空，不会抛异常，当前用例正常执行
	 * 
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param resourceid
	 *            控件id
	 * @return 如果存在返回控件，不存在返回空
	 */
	public static UiObject findViewById2(UiDevice devices, String resourceid) {
		UiObject object = new UiObject(
				new UiSelector().resourceIdMatches(resourceid));
		return object;

	}

	/**
	 * 强查找控件，不存在抛出异常，当前用例失败
	 * 
	 * @param runcase
	 *            测试用例名称
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param text
	 *            控件text
	 * @return 如果存在返回控件，不存在抛出控件不存在的异常
	 * @throws Exception
	 */
	public static UiObject findViewByText(String runcase, UiDevice devices,
			String text) throws Exception {
		UiObject object = new UiObject(new UiSelector().resourceIdMatches(text));
		if (object.exists()) {
			infoLog(runcase, FindObject + text);
			return object;
		} else {
			infoLog(runcase, NotFindObject + text);
			throw new Exception("NotFindObject" + text);
		}
	}

	/**
	 * 弱查找控件，不存在返回空，不会抛异常，当前用例正常执行
	 * 
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param text
	 *            控件文本text
	 * @return 如果存在返回控件，不存在返回空
	 */
	public static UiObject findViewByText2(UiDevice devices, String text) {
		UiObject object = new UiObject(new UiSelector().textContains(text));
		return object;

	}

	/**
	 * 强查找控件通过控件id,之后点击
	 * 
	 * @param runcase
	 *            测试用例名称
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param resourceid
	 *            控件id
	 * @throws Exception
	 *             控件找不到抛出不存在异常，测试用例执行失败
	 */
	public static void clickViewById(String runcase, UiDevice devices,
			String resourceid) throws Exception {
		UiObject object = new UiObject(
				new UiSelector().resourceIdMatches(resourceid));
		if (object.exists()) {
			infoLog(runcase, FindObject + resourceid);
			object.clickAndWaitForNewWindow();
			infoLog(runcase, TestClick + resourceid);
		} else {
			infoLog(runcase, NotFindObject + resourceid);
			throw new Exception("NotFindObject" + resourceid);
		}
	}

	/**
	 * 强查找控件通过控件text,之后点击
	 * 
	 * @param runcase
	 *            测试用例名称
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param text
	 *            控件text
	 * @throws Exception
	 *             控件找不到抛出不存在异常，测试用例执行失败
	 */
	public static void clickViewByText(String runcase, UiDevice devices,
			String text) throws Exception {
		UiObject object = new UiObject(new UiSelector().resourceIdMatches(text));
		if (object.exists()) {
			infoLog(runcase, FindObject + text);
			object.clickAndWaitForNewWindow();
			infoLog(runcase, TestClick + text);
		} else {
			infoLog(runcase, NotFindObject + text);
			throw new Exception("NotFindObject" + text);
		}
	}

	/**
	 * 强查找可翻滚控件，存在返回控件对象，不存在抛异常，当前测试停止
	 * 
	 * @param runcase
	 *            测试用例名称
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param resourceid
	 *            控件id
	 * @return 返回控件对象
	 * @throws Exception
	 *             抛出不存在异常
	 */
	public static UiScrollable findScrollViewById(String runcase,
			UiDevice devices, String resourceid) throws Exception {
		UiScrollable object = new UiScrollable(
				new UiSelector().resourceIdMatches(resourceid));
		if (object.exists()) {
			infoLog(runcase, FindObject + resourceid);
			return object;
		} else {
			infoLog(runcase, NotFindObject + resourceid);
			throw new Exception("NotFindObject" + resourceid);
		}
	}

	/**
	 * 返回桌面
	 * 
	 * @param runcase
	 *            测试用例名称
	 * @param device
	 *            UiDevice.getInstance()实例
	 */
	public static void backToHome(String runcase, UiDevice device) {
		for (int i = 0; i < 10; i++) {
			device.pressBack();
		}
		waitTime(2);
		UiObject exitbtn = findViewByText2(device, "确认");
		if (exitbtn.exists()) {
			try {
				exitbtn.clickAndWaitForNewWindow();
				waitTime(1);
				infoLog(runcase, TestInfo + "点击 确认 按钮退出");
			} catch (UiObjectNotFoundException e) {
				e.printStackTrace();
			}
		}
		device.pressBack();
		waitTime(3);
		device.pressHome();
		infoLog(runcase, TestInfo + "返回Home主页");
	}

	/**
	 * 失败测试用例结果呈现
	 * 
	 * @param runcase
	 *            测试用例
	 */
	public static void failcase(String runcase) {
		waitTime(1);
		Log.d(TAG + runcase, TestResult + "The Test Case " + runcase
				+ " Failed");
		System.out.println("[" + TAG + runcase + "] " + TestResult
				+ "The Test Case " + runcase + " Failed");

	}

	/**
	 * 
	 * @param device
	 *            UiDevice.getInstance()实例
	 * @param eMessage
	 * @return 返回失败原因，ANR,APPCRASH或控件找不到等
	 * @throws IOException
	 */
	public static String checkFailReason(String runcase, UiDevice device,
			String eMessage) throws IOException {
		String s = null;
		Common.infoLog(runcase, "发生异常，等待30秒捕获");
		waitTime(8);
		UiObject AppCrash = findViewByText2(device, "Unfortunately");
		UiObject ANR = findViewByText2(device, "极路客 isn't responding.");
		if (AppCrash.exists()) {
			s = "App Crash happened";

		} else if (ANR.exists()) {
			s = "ANR happened";

		} else {

			s = eMessage;
		}
		waitTime(10);
		UiObject AppCrashBtn = Common.findViewByText2(device, "OK");
		if (AppCrashBtn.exists()) {
			try {
				waitTime(5);
				AppCrashBtn.clickAndWaitForNewWindow();
				waitTime(20);
				backToHome(runcase, device);
				waitTime(10);
				backToHome(runcase, device);
			} catch (UiObjectNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		return s;
	}

	public static String checkFailReason2(String runcase, UiDevice device,
			String currentTime, String eMessage) throws IOException {
		createFolder(runcase);
		String s = null;
		Common.infoLog(runcase, "发生异常，正在捕获异常");
		waitTime(30);
		UiObject AppCrash = findViewByText2(device, "Unfortunately");
		UiObject ANR = findViewByText2(device, "极路客 isn't responding.");
		if (AppCrash.exists()) {
			s = "App Crash happened";
			takeBugReport(device, runcase, "CRASH", currentTime);
		} else if (ANR.exists()) {
			s = "ANR happened";
			takeBugReport(device, runcase, "ANR", currentTime);
		} else {
			takeBugReport(device, runcase, "Except", currentTime);
			s = eMessage;
		}
		return s;
	}

	/**
	 * 失败测试用例失败原因
	 * 
	 * @param runcase
	 *            测试用例
	 * @param errlog
	 *            失败Trace
	 */
	public static void errorLog(String runcase, String errlog) {
		Log.d(TAG + runcase, FailReason + errlog);
		System.out.println("[" + TAG + runcase + "] " + FailReason + errlog);
	}

	/**
	 * 通过测试用例总结
	 * 
	 * @param runcase
	 *            测试用例
	 */
	public static void passcase(String runcase) {
		waitTime(1);
		Log.d(TAG + runcase, TestResult + "The Test case " + runcase + " Pass");
		System.out.println("[" + TAG + runcase + "] " + TestResult
				+ "The Test case " + runcase + " Pass");
	}

	/**
	 * 
	 * @param runcase
	 *            测试用例
	 * @param logmsg
	 *            需要打印的LOG
	 */
	public static void infoLog(String runcase, String logmsg) {
		Log.d(TAG + runcase, logmsg);
		System.out.println("[" + TAG + runcase + "] " + logmsg);
	}

	/**
	 * 
	 * @param runcase
	 *            测试用例
	 * @param logmsg
	 *            开始/结束执行用例提示
	 */
	public static void startLog(String runcase, String logmsg) {
		waitTime(1);
		System.out.println("[" + TAG + runcase + "] " + logmsg);
		Log.d(TAG + runcase, logmsg);
	}

	/**
	 * 选择进入某个列表
	 * 
	 * @param runcase
	 *            测试用例
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param resourceid
	 *            列表ID
	 * @throws Exception
	 *             抛出找不到异常
	 */
	public static void entryVideoList(String runcase, UiDevice devices,
			String resourceid) throws Exception {
		UiObject videoList = findViewById(runcase, devices, resourceid);
		waitTime(1);
		if (videoList.exists()) {
			infoLog(runcase, FindObject + resourceid);
			videoList.clickAndWaitForNewWindow();
			infoLog(runcase, TestClick + resourceid);
		} else {
			infoLog(runcase, NotFindObject + resourceid);
			throw new Exception("NotFindObject" + resourceid);
		}
	}

	/**
	 * 在手机路径/sdcard/GolukTest/创建对应用例的文件夹，用于存放失败截图等
	 * 
	 * @param folderName
	 * @throws IOException
	 */
	public static void createFolder(String runcase) throws IOException {
		waitTime(1);
		Runtime.getRuntime().exec("mkdir -p /sdcard/GolukTest/" + runcase);

	}

	/**
	 * 截取当前失败用例的图，并存放在对应的测试用例文件夹下
	 * 
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param runcase
	 *            测试用例
	 * @param currentTime
	 *            当前时间
	 * @throws IOException
	 */
	public static void takeScreen(UiDevice devices, String runcase,
			String currentTime) throws IOException {
		// createFolder(runcase);
		devices.takeScreenshot(new File("/sdcard/GolukTest/" + runcase + "/"
				+ currentTime + ".png"), 0, 50);
	}

	// Take Bug Report
	public static String takeBugReport(UiDevice device, String runcase,
			String crashType, String currentTime) {
		waitTime(10);
		UiObject AppCrashBtn = Common.findViewByText2(device, "OK");
		if (AppCrashBtn.exists()) {
			try {
				waitTime(5);
				AppCrashBtn.clickAndWaitForNewWindow();
				waitTime(20);
				backToHome(runcase, device);
				waitTime(10);
				backToHome(runcase, device);
			} catch (UiObjectNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		Common.infoLog(runcase, "Start to catch log");
		try {
			// Executes the command.
			String logname = "/sdcard/GolukTest/" + runcase + "/Crash_"
					+ crashType + "_" + currentTime + ".txt";
			File file = new File(logname);
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file, true);
			Process process = Runtime.getRuntime()
					.exec("/system/bin/bugreport");
			// Reads stdout.// NOTE: You can write to stdin of the command using
			// process.getOutputStream().
			waitTime(10);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			int read;
			char[] buffer = new char[4096];
			while ((read = reader.read(buffer)) > 0) {
				StringBuffer output = new StringBuffer();
				output.append(buffer, 0, read);
				System.out.println(output);
				out.write(output.toString().getBytes("utf-8"));
			}
			// Waits for the command to finish.
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			reader.close();
			out.close();
			Process process2 = Runtime.getRuntime().exec(
					"/system/bin/am force-stop cn.com.mobnote.golukmobile");
			try {
				process2.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return logname;
		} catch (IOException e) {
			try {
				Process process2 = Runtime.getRuntime().exec(
						"/system/bin/am force-stop cn.com.mobnote.golukmobile");
				try {
					process2.waitFor();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			Common.infoLog(runcase, "No LOG");
			return "fail";
		}

	}

	/**
	 * 获取视频列表的视频
	 * 
	 * @param runcase
	 * @param device
	 * @return 返回视频的UiObject列表
	 */

	public static List<UiObject> getObjectList(String runcase, UiDevice device) {
		waitTime(2);
		Common.infoLog(runcase, "进入获取list");
		List<UiObject> list = new ArrayList<UiObject>();
		UiScrollable object = new UiScrollable(
				new UiSelector()
						.resourceIdMatches("cn.com.mobnote.golukmobile:id/mStickyListHeadersListView"));
		if (object.exists()) {
			int nObject = object.getChildCount(new UiSelector()
					.className("android.widget.RelativeLayout"));
			infoLog(runcase, "找到控件: " + nObject);
			UiObject o1, o2;
			try {

				for (int i = 0; i < 10; i++) {
					waitTime(1);
					if (list.size() < 6) {
						o1 = object
								.getChild(new UiSelector()
										.className(
												"android.widget.RelativeLayout")
										.index(i)
										.childSelector(
												new UiSelector()
														.resourceIdMatches("cn.com.mobnote.golukmobile:id/mVideoLayout1")));
						if (o1.exists()) {
							list.add(o1);
							infoLog(runcase, "添加找到控件的索引为: " + i);
						}
						o2 = object
								.getChild(new UiSelector()
										.className(
												"android.widget.RelativeLayout")
										.index(i)
										.childSelector(
												new UiSelector()
														.resourceIdMatches("cn.com.mobnote.golukmobile:id/mVideoLayout2")));
						if (o2.exists()) {
							list.add(o2);
							System.out.println("添加找到控件的索引为: " + i);
						}
					} else {
						break;
					}
				}

				System.out.println("控件收集完成");

			} catch (UiObjectNotFoundException e) {
				System.out.println("异常");
				e.printStackTrace();
				return null;
			}
		} else {
			Common.infoLog(runcase, "此项目没有视频");
		}
		return list;
	}

	/**
	 * 下载视频
	 * 
	 * @param runcase
	 *            测试用例
	 * @param device
	 *            UiDevice.getInstance()实例
	 * @param editbtnid
	 *            编辑按钮id
	 * @throws Exception
	 */
	public static void downloadVideo(String runcase, UiDevice device,
			String editbtnid) throws Exception {
		// 点击 编辑 按钮
		clickViewById(runcase, device, editbtnid);
		// for (int n = 0; n < al.length; n++) {
		List<UiObject> objectlist = getObjectList(runcase, device);
		int llist = objectlist.size();
		Common.infoLog(runcase, "视频个数为：" + llist);
		if (llist > 0) {
			for (int i = 0; i < llist; i++) {
				Common.infoLog(runcase, "第 " + i + " 个视频被选择");
				objectlist.get(i).clickAndWaitForNewWindow();
				waitTime(1);
			}
		} else {
			Common.infoLog(runcase, "此处没有任何视频");
		}
		UiObject downloadbtn = findViewById(runcase, device,
				"cn.com.mobnote.golukmobile:id/mDownLoadIcon");
		if (downloadbtn.exists()) {
			infoLog(runcase, TestInfo + "准备下载的视频已经被选中");
			downloadbtn.clickAndWaitForNewWindow();
			infoLog(runcase, TestClick
					+ "cn.com.mobnote.golukmobile:id/mDownLoadIcon");
		} else {
			throw new Exception("编辑按钮选择失败");
		}
	}

	/**
	 * 播放本地/远程视频
	 * 
	 * @param runcase
	 *            测试用例
	 * @param device
	 *            UiDevice.getInstance()实例
	 * @param resourceid
	 *            播放器id
	 * @param loadtime
	 *            等待时间
	 * @throws Exception
	 */
	public static void playVideo(String runcase, UiDevice device,
			String resourceid, int loadtime) throws Exception {
		int al[] = getAnyLocation(runcase, device);
		List<UiObject> objectlist = getObjectList(runcase, device);
		int llist = objectlist.size();
		Common.infoLog(runcase, "视频个数为：" + llist);
		for (int n = 0; n < llist; n++) {
			if (llist == 0) {
				Common.infoLog(runcase, "此处列表没视频");
				break;
			}
			infoLog(runcase, TestInfo + "第 " + (n + 1) + " 个视频位置被点击");
			if (objectlist.get(n).exists()) {
				try {
					objectlist.get(n).clickAndWaitForNewWindow();
				} catch (UiObjectNotFoundException e) {
					infoLog(runcase, "视频点击失败");
					continue;
				}
			}
			waitTime(2);
			UiObject btn = findViewById2(device,
					"cn.com.mobnote.golukmobile:id/leftButton");
			if (btn.exists()) {
				try {
					infoLog(runcase, TestInfo + "视频 " + (n + 1) + " 损坏");
					btn.clickAndWaitForNewWindow();
				} catch (UiObjectNotFoundException e) {
					e.printStackTrace();
				}
				continue;
			}
			UiObject loadingNote;
			waitTime(3);
			UiObject playView = findViewById2(device, resourceid);
			waitTime(2);
			// 判断播放器是否打开
			if (playView.exists()) {
				infoLog(runcase, TestInfo + "视频已经打开");
				loadingNote = findViewById2(device,
						"cn.com.mobnote.golukmobile:id/mLoading");
				// 如果出现等待提示
				if (loadingNote.exists()) {
					infoLog(runcase, TestInfo + "正在加载视频...");
					int nloadtime = 1;
					while (loadingNote.exists() && nloadtime < loadtime) {
						waitTime(1);
						infoLog(runcase, TestInfo + "视频加载 " + nloadtime + "s");
						nloadtime++;
					}
					// 如果视频异常，无法播放，提示出错，退出
					if (findViewByText2(device, "视频出错，请重试！").exists()) {
						findViewByText2(device, "确定")
								.clickAndWaitForNewWindow();
						infoLog(runcase, TestInfo + "此视频出错，无法播放");
					}
					// 如果等待超时，提示播放失败
					if (nloadtime == loadtime) {
						infoLog(runcase, TestInfo + "加载超时，播放失败");
						device.pressBack();
						waitTime(2);
						throw new Exception("视频加载超时，播放失败");
					} else {
						infoLog(runcase, TestInfo + "开始播放视频，播放10秒钟");
						waitTime(8);
						Common.clickByLocation(runcase, device);
						waitTime(3);
					}
					device.pressBack();
					waitTime(2);
				} else {
					infoLog(runcase, TestInfo + "正在播放视频,播放10秒");
					infoLog(runcase, TestInfo + "开始播放视频，播放10秒钟");
					waitTime(8);
					device.click(al[0], al[1]);
					waitTime(3);
					device.pressBack();
					infoLog(runcase, TestInfo + "视频播放结束");
					checkIPCConnect(runcase, device);
					waitTime(5);
				}
			} else {
				// 如果播放器没有打开，判断如果是出现OK,表示出现CRASH,抛异常，否则判断为此处没有视频
				UiObject AppCrashBtn = Common.findViewByText2(device, "OK");
				if (AppCrashBtn.exists()) {
					throw new Exception();
				} else {
					infoLog(runcase, TestInfo + "点击此处没有视频");
					System.out.println("点击此处没有视频");
				}
			}
		}

	}

	/**
	 * 播放精选视频
	 * 
	 * @param runcase
	 *            测试用例
	 * @param device
	 *            UiDevice.getInstance()实例
	 * @throws Exception
	 */
	public static void playSquareVide(String runcase, UiDevice device)
			throws Exception {
		waitTime(5);
		// 选择任意字符
		clickViewById(runcase, device, "cn.com.mobnote.golukmobile:id/imlayout");
		waitTime(5);
		// 处理退广
		if (findViewById2(device, "cn.com.mobnote.golukmobile:id/my_webview")
				.exists()) {
			Common.infoLog(runcase, "进入推广页面");
			waitTime(5);
			findViewById2(device,
					"cn.com.mobnote.golukmobile:id/user_title_right").click();
		}
		// 处理单视频
		if (findViewById2(device, "cn.com.mobnote.golukmobile:id/shareText")
				.exists()) {
			Common.infoLog(runcase, "进入单视频");
			waitTime(5);
			int i = 0;
			while (i < 60) {
				if (Common.findViewById2(device,
						"cn.com.mobnote.golukmobile:id/mLoadingLayout")
						.exists()) {
					Common.infoLog(runcase, "单视频加载 " + i + "秒");
					waitTime(1);
					i++;
				} else {
					Common.infoLog(runcase, "单视频加载完成");
					waitTime(3);
					Common.clickViewById(runcase, device,
							"cn.com.mobnote.golukmobile:id/play_btn");
					Common.infoLog(runcase, "单视频播放10秒");
					waitTime(10);
					if (Common.findViewById2(device, "android:id/button1")
							.exists()) {
						Common.findViewById2(device, "android:id/button1")
								.clickAndWaitForNewWindow();
					}
					waitTime(2);
					if (Common.findViewById2(device,
							"cn.com.mobnote.golukmobile:id/leftButton")
							.exists()) {
						Common.findViewById2(device,
								"cn.com.mobnote.golukmobile:id/leftButton")
								.clickAndWaitForNewWindow();
					}
					break;
				}
			}
			if (i == 60) {
				Common.infoLog(runcase, "单视频加载超时，60秒");
			}

			if (Common.findViewById2(device,
					"cn.com.mobnote.golukmobile:id/comment_noinput").exists()) {
				Common.infoLog(runcase, "评论已关闭");
				device.pressBack();
			} else {
				if (Common.findViewById2(device,
						"cn.com.mobnote.golukmobile:id/commentText").exists()) {
					Common.clickViewById(runcase, device,
							"cn.com.mobnote.golukmobile:id/commentText");
					waitTime(5);
				}
			}
			if (Common.findViewById2(device,
					"cn.com.mobnote.golukmobile:id/comment_title").exists()) {
				Common.infoLog(runcase, "进入评论页");
				UiObject input = Common.findViewById(runcase, device,
						"cn.com.mobnote.golukmobile:id/comment_input");
				input.clickAndWaitForNewWindow();
				input.setText("Cool");
				waitTime(3);
				Common.clickViewById(runcase, device,
						"cn.com.mobnote.golukmobile:id/comment_send");
				waitTime(5);
				Common.clickViewById(runcase, device,
						"cn.com.mobnote.golukmobile:id/comment_back");
				device.pressBack();
			} else {
				Common.infoLog(runcase, "无法进入评论页");
			}
		}
		// 处理专题视频
		if (Common.findViewById2(device,
				"cn.com.mobnote.golukmobile:id/title_share").exists()) {
			Common.infoLog(runcase, "进入专题");
			Common.scrollUp(runcase, device, "android.widget.ListView", 1);
			Common.clickViewById(runcase, device,
					"cn.com.mobnote.golukmobile:id/mPlayBigBtn");
			waitTime(5);
			if (Common.findViewById2(device,
					"cn.com.mobnote.golukmobile:id/videoview").exists()) {
				Common.infoLog(runcase, "视频已经打开");
			} else {
				Common.clickViewById(runcase, device,
						"cn.com.mobnote.golukmobile:id/mPlayBigBtn");
			}
			waitTime(20);
			UiObject btn = Common.findViewById2(device,
					"cn.com.mobnote.golukmobile:id/leftButton");
			if (btn.exists()) {
				btn.clickAndWaitForNewWindow();
				waitTime(3);
			} else {
				device.pressBack();
				waitTime(3);
			}

			UiObject comments = Common.findViewById2(device,
					"cn.com.mobnote.golukmobile:id/push_comment");
			int i = 0;
			while (i < 10) {
				if (comments.exists()) {
					break;
				} else {
					Common.scrollUp(runcase, device, "android.widget.ListView",
							1);
					i = i + 1;
				}
			}
			if (i == 10) {
				Common.infoLog(runcase, "没有找到评论按钮");
				device.pressBack();
				waitTime(4);
			} else {
				waitTime(4);
				comments.clickAndWaitForNewWindow();
				if (Common.findViewById(runcase, device,
						"cn.com.mobnote.golukmobile:id/comment_title").exists()) {
					waitTime(5);
					Common.infoLog(runcase, "进入评论页");
					UiObject input = Common.findViewById(runcase, device,
							"cn.com.mobnote.golukmobile:id/comment_input");
					waitTime(4);
					input.clickAndWaitForNewWindow();
					waitTime(4);
					input.setText("Cool");
					waitTime(3);
					Common.clickViewById(runcase, device,
							"cn.com.mobnote.golukmobile:id/comment_send");
					waitTime(5);
					Common.clickViewById(runcase, device,
							"cn.com.mobnote.golukmobile:id/comment_back");
					if (Common.findViewById2(device,
							"cn.com.mobnote.golukmobile:id/title_share")
							.exists()) {
						device.pressBack();
					}
				}
			}
		}
	}

	/**
	 * 播放最新中具体栏目视频
	 * 
	 * @param runcase
	 * @param devices
	 * @throws Exception
	 */
	public static void playTypeVideo(String runcase, UiDevice devices)
			throws Exception {

		for (int n = 0; n < typeName.length; n++) {
			waitTime(10);
			UiObject type = findViewByText2(devices, typeName[n]);
			int loading = 1;
			if (type.exists()) {
				Common.infoLog(runcase, "找到类型" + typeName[n]);
				type.clickAndWaitForNewWindow();
				while (loading < 60) {
					if (Common.findViewById2(devices,
							"cn.com.mobnote.golukmobile:id/loading_bg")
							.exists()) {
						Common.infoLog(runcase, "类型 " + typeName[n] + "正在加载 "
								+ loading + " 秒");
						waitTime(1);
						loading++;
					} else {
						Common.infoLog(runcase, "类型 " + typeName[n] + "正在加载完成");
						waitTime(2);
						Common.scrollUp(runcase, devices,
								"android.widget.ListView", 2);
						waitTime(2);
						for (int i = 0; i < 5; i++) {
							UiObject playbtn = Common
									.findViewById2(devices,
											"cn.com.mobnote.golukmobile:id/mPlayBigBtn");
							if (playbtn.exists()) {
								playbtn.clickAndWaitForNewWindow();
								Common.infoLog(runcase, "播放10秒");
								waitTime(10);
								break;
							} else {
								scrollDown(runcase, devices,
										"android.widget.ListView", 2);
							}

						}
						if (Common.findViewById2(devices, "android:id/button1")
								.exists()) {
							Common.findViewById2(devices, "android:id/button1")
									.clickAndWaitForNewWindow();
							waitTime(2);
							devices.pressBack();
							break;
						} else {
							if (Common.findViewById2(devices,
									"cn.com.mobnote.golukmobile:id/leftButton")
									.exists()) {
								Common.findViewById2(devices,
										"cn.com.mobnote.golukmobile:id/leftButton")
										.clickAndWaitForNewWindow();
								waitTime(2);
								devices.pressBack();
								break;
							} else {
								devices.pressBack();
								waitTime(2);
								devices.pressBack();
								break;
							}

						}

					}
				}

			} else {
				Common.infoLog(runcase, "没有找到类型" + typeName[n]);
			}
		}

	}

	public static void playlatestVideo(String runcase, UiDevice devices)
			throws Exception {
		// 下拉刷新
		scrollDown(runcase, devices, "android.widget.ListView", 5);
		waitTime(5);
		// 播放最新视频
		for (int i = 1; i < 6; i++) {
			infoLog(runcase, "第 " + i + "个最新视频");
			UiObject plybtn = findViewById2(devices,
					"cn.com.mobnote.golukmobile:id/mPlayBigBtn");
			if (plybtn.exists()) {
				plybtn.clickAndWaitForNewWindow();
				infoLog(runcase, "最新视频被点击，播放10秒钟");
				waitTime(10);
				devices.pressBack();
			}
			// 评论
			Common.infoLog(runcase, "进入评论");
			waitTime(5);
			UiObject commentsbtn = findViewById2(devices,
					"cn.com.mobnote.golukmobile:id/commentText");
			if (commentsbtn.exists()) {
				commentsbtn.clickAndWaitForNewWindow();
				waitTime(3);
				if (Common.findViewById2(devices,
						"cn.com.mobnote.golukmobile:id/comment_noinput")
						.exists()) {
					Common.infoLog(runcase, "禁止评论");
					waitTime(3);
					devices.pressBack();
				} else {
					UiObject input = findViewById2(devices,
							"cn.com.mobnote.golukmobile:id/comment_input");
					input.clickAndWaitForNewWindow();
					waitTime(5);
					input.setText("Cool");
					waitTime(3);
					clickViewById(runcase, devices,
							"cn.com.mobnote.golukmobile:id/comment_send");
					waitTime(5);
					if (findViewById2(devices,
							"cn.com.mobnote.golukmobile:id/comment_title_layout")
							.exists()) {
						clickViewById(runcase, devices,
								"cn.com.mobnote.golukmobile:id/comment_back");
					} else {
						waitTime(5);
						clickViewById(runcase, devices,
								"cn.com.mobnote.golukmobile:id/comment_back");
					}
					waitTime(2);
					devices.pressBack();
				}
			}
			// 点赞
			Common.infoLog(runcase, "执行点赞");
			waitTime(5);
			UiObject zanbtn = findViewById2(devices,
					"cn.com.mobnote.golukmobile:id/zanIcon");
			if (zanbtn.exists()) {
				for (int k = 0; k < 10; k++) {
					zanbtn.click();
				}
			}
			scrollUp(runcase, devices, "android.widget.ListView", 3);
			waitTime(10);
		}
	}

	/**
	 * 判断是否已经连接IPC
	 * 
	 * @param runcase
	 *            测试用例
	 * @param device
	 *            UiDevice.getInstance()实例
	 * @param resourceid
	 *            IPC界面的时候显示IPC名字，未连接显示未连接到极路客
	 * @throws Exception
	 */
	public static void connectWifi(String runcase, UiDevice device,
			String resourceid) throws Exception {
		waitTime(2);
		UiObject ipcname = null;

		int waitIPCConnect = 1;
		while (waitIPCConnect < 121) {
			ipcname = Common.findViewById2(device, resourceid);
			String name = ipcname.getText();
			if (name.equalsIgnoreCase("未连接到极路客")) {
				infoLog(runcase, TestInfo + "IPC正在连接 " + waitIPCConnect + "秒");
				waitTime(1);
				waitIPCConnect++;
			} else {
				infoLog(runcase, TestInfo + "IPC已经连接到 " + name);
				break;
			}
		}
		if (waitIPCConnect == 121) {
			throw new Exception("IPC连接超过" + waitIPCConnect + "秒，失败");
		}
	}

	/**
	 * 
	 * @param runcase
	 *            测试用例
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param hanldtimeout
	 *            滤镜处理耗时
	 * @throws Exception
	 */
	public static void selectVideoFilter(String runcase, UiDevice devices)
			throws Exception {
		List<UiObject> objectlist = getObjectList(runcase, devices);
		int llist = objectlist.size();
		Common.infoLog(runcase, "视频个数为：" + llist);
		for (int n = 0; n < llist; n++) {
			infoLog(runcase, TestInfo + "第 " + (n + 1) + " 个视频被点击");
			objectlist.get(n).clickAndWaitForNewWindow();
			waitTime(3);
			UiObject filterbtn = Common.findViewById2(devices,
					"cn.com.mobnote.golukmobile:id/startshare_prompt");
			if (filterbtn.exists()) {
				findViewById2(devices,
						"cn.com.mobnote.golukmobile:id/share_switch_filter")
						.click();
				infoLog(runcase, TestInfo + "进入滤镜处理界面");
				waitTime(1);
				handleVideoByFilter(runcase, devices);
				waitTime(3);
			} else {
				infoLog(runcase, TestInfo + "此处没有视频");
			}
		}
	}

	/**
	 * 随机选择滤镜处理视频
	 * 
	 * @param runcase
	 *            测试用例
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @param hanldtimeout
	 *            滤镜处理超时
	 * @throws Exception
	 */
	public static void handleVideoByFilter(String runcase, UiDevice devices)
			throws Exception {
		for (int n = 0; n < filterName.length; n++) {
			UiObject filter = findViewByText2(devices, filterName[n]);
			if (filter.exists()) {
				filter.clickAndWaitForNewWindow();
				infoLog(runcase, TestInfo + "滤镜 " + filterName[n] + " 被选择");
			} else {
				throw new Exception(filterName[n] + " 滤镜不存在");
			}
		}
		Random rand = new Random(47);
		int randInt = rand.nextInt(filterName.length);
		UiObject filter = findViewByText2(devices, filterName[randInt]);
		filter.clickAndWaitForNewWindow();
		UiObject nextStepBtn = findViewById2(devices,
				"cn.com.mobnote.golukmobile:id/wechat_circle");
		nextStepBtn.clickAndWaitForNewWindow();
		waitTime(10);
		devices.pressBack();
		waitTime(2);
	}

	/**
	 * 判断IPC是否断开
	 * 
	 * @param runcase
	 *            测试用例
	 * @param devices
	 *            UiDevice.getInstance()实例
	 * @throws Exception
	 */
	public static void checkIPCConnect(String runcase, UiDevice devices)
			throws Exception {
		waitTime(4);
		UiObject waitConnectIPC = Common.findViewByText2(devices,
				"摄像头断开，正在为您重连…");
		int waitConnect = 1;
		while (waitConnect < 40) {
			if (!waitConnectIPC.exists()) {
				break;
			}
			waitConnect++;
		}
		UiObject disconnectIPC = Common
				.findViewByText2(devices, "您好像没有连接摄像头哦。");
		if (disconnectIPC.exists()) {
			UiObject oo2 = findViewById2(devices,
					"cn.com.mobnote.golukmobile:id/leftButton");
			oo2.clickAndWaitForNewWindow();
			infoLog(runcase, TestInfo + "IPC 断开连接");
			throw new Exception("IPC 断开连接");
		}
	}

	/**
	 * 等待时间设置
	 * 
	 * @param n
	 *            等待时间，单位为秒
	 */
	public static void waitTime(int n) {
		long time = n * 1000;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下拉
	 * 
	 * @param runcase
	 *            测试用例
	 * @param device
	 *            UiDevice.getInstance()实例
	 * @param className
	 *            列表的classType
	 * @param scrollTime
	 *            下拉次数
	 * @throws UiObjectNotFoundException
	 */
	public static void scrollDown(String runcase, UiDevice device,
			String className, int scrollTime) throws UiObjectNotFoundException {
		UiScrollable object = new UiScrollable(
				new UiSelector().className(className));

		for (int i = 0; i < scrollTime; i++) {
			object.scrollBackward(8);
		}

		infoLog(runcase, TestInfo + "向下拉动");
	}

	/**
	 * 上翻
	 * 
	 * @param runcase
	 *            测试用例
	 * @param device
	 *            UiDevice.getInstance()实例
	 * @param className
	 *            列表的classType
	 * @param scrollTime
	 *            上翻次数
	 * @throws UiObjectNotFoundException
	 */
	public static void scrollUp(String runcase, UiDevice device,
			String className, int scrollTime) throws UiObjectNotFoundException {
		UiScrollable object = new UiScrollable(
				new UiSelector().className(className));
		if (object.exists()) {
			for (int i = 0; i < scrollTime; i++) {
				object.scrollForward();
				infoLog(runcase, TestInfo + "向上翻动");
			}
		} else {
			infoLog(runcase, "列表不存在");
		}

	}

	public static void selectMyItem(String runcase, UiDevice device) {
		int height = device.getDisplayHeight();
		int width = device.getDisplayWidth();
		device.click((width / 6) * 5, height - 10);
		infoLog(runcase, TestInfo + "点击 我的 选项");
	}

	public static void selectIPCItem(String runcase, UiDevice device) {
		int height = device.getDisplayHeight();
		int width = device.getDisplayWidth();
		device.click(width / 2, (height / 50) * 49);
		infoLog(runcase, TestInfo + "点击 IPC 选项");
	}

	/**
	 * 等待视频列表加载
	 * 
	 * @param runcase
	 *            测试用例名字
	 * @param device
	 * @param resouceId
	 *            判断某个id是不是存在，如果存在就等待，不存在表示加载完成
	 * @param n
	 *            等待时间
	 * @return 是否加载完成，加载超时抛出异常
	 * @throws Exception
	 */
	public static boolean waitForList(String runcase, UiDevice device,
			String resouceId, int n) throws Exception {
		int waitwondfullisttime = 1;
		while (waitwondfullisttime < n) {
			if (findViewById2(device, resouceId).exists()) {
				infoLog(runcase, "正在加载视频列表 " + waitwondfullisttime + "秒");
				waitTime(1);
				waitwondfullisttime = waitwondfullisttime + 1;
			} else {
				infoLog(runcase, "视频列表加载完成");
				break;
			}
		}
		if (waitwondfullisttime == n) {
			throw new Exception("IPC视频列表加载超时, " + waitwondfullisttime + "秒");
		} else {
			return true;
		}

	}

	public static void clickByLocation(String runcase, UiDevice device) {
		int[] al = getAnyLocation(runcase, device);
		device.click(al[0] / 2, al[1] / 2);
		// infoLog(runcase, "随机点击屏幕");
	}
}