package com.goluk.testcases;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.goluk.common.Common;

public class RequestLiveTest extends UiAutomatorTestCase {
	public final static UiDevice in = UiDevice.getInstance();
	public final static String runcase = "RequestLiveTest";

	public void testcase() throws IOException {
		try {
			Common.startLog(runcase, "*****Start to run " + runcase + " *****");
			// 通过是否有IPC按钮判断是否进入
			Common.openActivity(runcase, in,
					"cn.com.mobnote.golukmobile:id/index_carrecoder_btn");
			sleep(2000);
			// 选择 IPC按钮 从下边状态栏
			Common.clickViewById(runcase, in,
					"cn.com.mobnote.golukmobile:id/index_carrecoder_btn");
			sleep(2000);
			// 检查是否连接到IPC
			Common.connectWifi(runcase, in,
					"cn.com.mobnote.golukmobile:id/mConnectTip");
			sleep(1000);
			Common.clickViewById(runcase, in,
					"cn.com.mobnote.golukmobile:id/mPlayBtn");
			sleep(2000);
			int i = 1;
			UiObject waitingnote = Common.findViewById2(in,
					"cn.com.mobnote.golukmobile:id/mLoading");
			while (i < 16) {
				if (waitingnote.exists()) {
					sleep(1000);
					Common.infoLog(runcase, "Loading Preview" + i + "sec");
					i++;
				} else {
					Common.infoLog(runcase, "Loading Finish");
					sleep(3000);
					break;
				}
			}
			if (i == 16) {
				throw new Exception("Loading more than 15s");
			}
			sleep(2000);
			Common.clickViewById(runcase, in,
					"cn.com.mobnote.golukmobile:id/fqzb");
			Common.clickViewById(runcase, in,
					"cn.com.mobnote.golukmobile:id/liveBtn");
			boolean livestate = false;
			int waitforlive = 1;
			while (waitforlive < 60) {
				
				for(int n=0;n<5;n++){
					Common.clickByLocation(runcase, in);
					sleep(1000);
				}
				if (Common.findViewById2(in,
						"cn.com.mobnote.golukmobile:id/live_vRtmpPlayVideo")
						.exists()) {
					Common.infoLog(runcase, "直播发起成功");
					livestate = true;
					break;
				} else {
					for(int n=0;n<5;n++){
						Common.clickByLocation(runcase, in);
						sleep(1000);
					}
					try{
					if (Common.findViewById2(in, "android:id/message")
							.getText().equalsIgnoreCase("很抱歉，直播创建不成功，再试一次吧。")) {
						Common.clickViewById(runcase, in, "android:id/button1");
						Common.infoLog(runcase, "直播创建失败，点击确定后继续发起直播");
					}
					}catch(Exception e){
						for(int n=0;n<5;n++){
							Common.clickByLocation(runcase, in);
							sleep(1000);
						}
					}
					Common.infoLog(runcase, "直播创建中 " + waitforlive + "秒");
					waitforlive = waitforlive + 1;
					sleep(1000);
				}
				for(int n=0;n<5;n++){
					Common.clickByLocation(runcase, in);
					sleep(100);
				};
			}
			if (waitforlive == 60) {
				throw new Exception("直播创建失败，超时 " + waitforlive + "秒");
			}
			if (livestate) {
				Common.infoLog(runcase, "发起直播20秒开始计时");
				int liveduration = 1;
				while (liveduration < 10) {
					for(int n=0;n<5;n++){
						Common.clickByLocation(runcase, in);
						sleep(100);
					}
					Common.infoLog(runcase, "已经直播 " + liveduration + "秒");
					liveduration = liveduration + 1;
					sleep(1000);
					}
				}
			sleep(4000);
			for(int n=0;n<5;n++){
				Common.clickByLocation(runcase, in);
				sleep(1000);
			}

			UiObject o=Common.findViewById2(in, "android:id/message");
			if(o.exists()){
				if (o.getText().equalsIgnoreCase("网络异常，直播结束")){
					Common.clickViewById(runcase, in,
					"android:id/button1");
					throw new Exception("直播失败，网络异常");
				}
			}
			Common.backToHome(runcase, in);
			Common.passcase(runcase);
			Common.startLog(runcase, "*****End to run " + runcase + " *****");
		} catch (Exception e) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd-HH-mm-ss");
			Date curDate = new Date(System.currentTimeMillis());
			String str = formatter.format(curDate);
			String s=Common.checkFailReason2(runcase,in,str, e.getMessage());
			Common.infoLog(runcase, "截图存储在 /sdcard/GolukTest/" + runcase + "/"
					+ str + ".png");
			Common.takeScreen(in, runcase, str);
			sleep(5000);
//			String s = null;
//			s = Common.checkFailReason(runcase, in, e.getMessage());
			Common.backToHome(runcase,in);
			Common.errorLog(runcase, s);
			Common.failcase(runcase);
			Common.startLog(runcase, "*****End to run " + runcase + " *****");
		}
	}
}
