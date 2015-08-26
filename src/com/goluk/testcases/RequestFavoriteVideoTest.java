package com.goluk.testcases;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.goluk.common.Common;

public class RequestFavoriteVideoTest extends UiAutomatorTestCase {
	public final static UiDevice in=UiDevice.getInstance();
	public final static String runcase="RequestFavoriteVideoTest";
	public void testcase() throws IOException{
		try{
			Common.startLog(runcase,"*****Start to run "+runcase+" *****");
			//通过是否有IPC按钮判断是否进入
			Common.openActivity(runcase,in,"cn.com.mobnote.golukmobile:id/index_carrecoder_btn");
			sleep(2000);
			//选择 IPC按钮 从下边状态栏
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/index_carrecoder_btn");
			sleep(2000);
			//检查是否连接到IPC
			Common.connectWifi(runcase, in, "cn.com.mobnote.golukmobile:id/mConnectTip");
			sleep(2000);
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/mPlayBtn");
			sleep(2000);
			int i=1;
			UiObject waitingnote=Common.findViewById2(in, "cn.com.mobnote.golukmobile:id/mLoading");
			while(i<16){
				if(waitingnote.exists()){
					sleep(1000);
					Common.infoLog(runcase,"Loading Preview"+i+"sec");
					i++;
				}else{
					Common.infoLog(runcase,"Loading Finish");
					sleep(3000);
					break;
				}				
			}
			if(i==16){
				throw new Exception("Loading more than 15s");
			}
			sleep(2000);
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/m8sBtn");
			sleep(7000);
			int waitforshare=1;
			UiObject shareBtn=null;
			while(waitforshare<30){
				shareBtn=Common.findViewById2(in, "cn.com.mobnote.golukmobile:id/new1");
				if(shareBtn.exists()){
					Common.infoLog(runcase, "精彩视频拍摄保存成功");
					sleep(2000);
					Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/image1");
					UiObject btn=Common.findViewById2(in, "cn.com.mobnote.golukmobile:id/back_btn");
					if(btn.exists()){
						btn.click();
					}
					break;
				}
				sleep(5000);
				waitforshare++;
			}
			if(waitforshare==30){
				throw new Exception("精彩视频拍摄保存失败");
			}
			Common.backToHome(runcase,in);
			Common.passcase(runcase);
			Common.startLog(runcase,"*****End to run "+runcase+" *****");
		} catch (Exception e) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date curDate = new Date(System.currentTimeMillis());
			String str = formatter.format(curDate);
			String s=Common.checkFailReason2(runcase,in,str, e.getMessage());
			Common.infoLog(runcase,"截图存储在 in /sdcard/GolukTest/"+runcase+"/"+str+".png");
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
