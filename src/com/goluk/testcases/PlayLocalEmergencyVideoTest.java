package com.goluk.testcases;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.goluk.common.Common;

public class PlayLocalEmergencyVideoTest extends UiAutomatorTestCase {
	public final static UiDevice in=UiDevice.getInstance();
	public final static String runcase="PlayLocalEmergencyVideoTest";
	public void testcase() throws IOException{
		try{
			Common.startLog(runcase,"*****Start to run "+runcase+" *****");
			//通过是否有IPC按钮判断是否进入
			Common.openActivity(runcase,in,"cn.com.mobnote.golukmobile:id/index_carrecoder_btn");
			sleep(2000);
			//选择 我的 从下边状态栏
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/more_btn");
			sleep(2000);
			//选择 我的相册
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/video_item");
			sleep(2000);
			UiObject localVideoBtn=Common.findViewById(runcase, in, "cn.com.mobnote.golukmobile:id/mLocalVideoBtn");
			localVideoBtn.click();
			sleep(5);
			//等待列表刷新,刷新完成返回true
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/mEmergencyText");
			if(Common.waitForList(runcase, in,"cn.com.mobnote.golukmobile:id/loading_bg",30)){
				for(int nPlayCount=1;nPlayCount<3;nPlayCount++){
					Common.infoLog(runcase,"第 "+nPlayCount+" 页");
					Common.playVideo(runcase,in,"cn.com.mobnote.golukmobile:id/videoview",40);
					Common.scrollUp(runcase, in, "android.widget.ListView", 2);
				}
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
			Common.errorLog(runcase,s);
			Common.backToHome(runcase,in);
			Common.failcase(runcase);
			Common.startLog(runcase,"*****End to run "+runcase+" *****");
			
		}
	}

}
