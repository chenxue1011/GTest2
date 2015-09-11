package com.goluk.testcases;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.goluk.common.Common;

public class NewVideoTest extends UiAutomatorTestCase {
	public final static UiDevice in=UiDevice.getInstance();
	public final static String runcase="NewVideoTest";
	public final static String[] item={"cn.com.mobnote.golukmobile:id/category_btn_one",
		"cn.com.mobnote.golukmobile:id/category_btn_three","cn.com.mobnote.golukmobile:id/category_btn_five",
		"cn.com.mobnote.golukmobile:id/category_btn_four"};
	public void testcase() throws IOException{
		try{
			Common.startLog(runcase,"*****Start to run "+runcase+" *****");
			Common.openActivity(runcase,in,"cn.com.mobnote.golukmobile:id/index_carrecoder_btn");
			sleep(2000);
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/square_title");
			Common.scrollDown(runcase, in, "android.widget.ListView", 5);
			Common.playTypeVideo(runcase, in);
			sleep(2);
			Common.infoLog(runcase, "开始执行最新视频");
			Common.playlatestVideo(runcase, in);
			UiObject AppCrash = Common.findViewByText2(in, "Unfortunately");
			UiObject ANR = Common.findViewByText2(in, "极路客 isn't responding.");
			if (AppCrash.exists()) {
				throw new Exception("AppCrash");
			} else if (ANR.exists()) {
				throw new Exception("ANR");
			}
			Common.backToHome(runcase,in);
			Common.passcase(runcase);
			Common.startLog(runcase,"*****End to run "+runcase+" *****");
		} catch (Exception e) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date curDate = new Date(System.currentTimeMillis());
			String str = formatter.format(curDate);
			String s=Common.checkFailReason2(runcase,in,str, e.getMessage());
			Common.infoLog(runcase,"截图存储在 /sdcard/GolukTest/"+runcase+"/"+str+".png");
			Common.takeScreen(in, runcase,str);
//			String s=null;
//			s=Common.checkFailReason(runcase,in, e.getMessage());
			Common.backToHome(runcase,in);
			Common.errorLog(runcase,s);
			Common.failcase(runcase);
			Common.startLog(runcase,"*****End to run "+runcase+" *****");
		}
	}
}

