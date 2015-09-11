package com.goluk.testcases;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.goluk.common.Common;

public class ShareFavoriteVideoTest extends UiAutomatorTestCase {
	public final static UiDevice in=UiDevice.getInstance();
	public final static String runcase="ShareFavoriteVideoTest";
	public void testcase()throws IOException{
		try{
			Common.startLog(runcase,"*****Start to run "+runcase+" *****");
			Common.openActivity(runcase,in,"cn.com.mobnote.golukmobile:id/index_carrecoder_btn");
			sleep(2000);
			Common.clickViewById(runcase, in,
					"cn.com.mobnote.golukmobile:id/index_carrecoder_btn");
			sleep(2000);
			//点击分享
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/image3");
			//点击紧急视频
			boolean IPCStatus=false;
			IPCStatus=Common.waitForList(runcase, in,"cn.com.mobnote.golukmobile:id/loading_bg",30);
			if(IPCStatus){
				Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/mWonderfulText");

				Common.selectVideoFilter(runcase, in);
			}
			
			
			Common.backToHome(runcase,in);
			Common.passcase(runcase);
			Common.startLog(runcase,"*****End to run "+runcase+" *****");
		}catch(Exception e){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date curDate = new Date(System.currentTimeMillis());
			String str = formatter.format(curDate);
			String s=Common.checkFailReason2(runcase,in,str, e.getMessage());
			Common.infoLog(runcase,"截图存储在 /sdcard/GolukTest/"+runcase+"/"+str+".png");
			Common.takeScreen(in, runcase,str);
			sleep(5000);
//			String s=null;
//			s=Common.checkFailReason(runcase,in, e.getMessage());
			Common.errorLog(runcase,s);
			Common.backToHome(runcase,in);
			Common.failcase(runcase);
			Common.backToHome(runcase,in);
			Common.startLog(runcase,"*****End to run "+runcase+" *****");
		}
	}	
}
