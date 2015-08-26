package com.goluk.testcases;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.goluk.common.Common;

public class DownloadLoopVideoTest extends UiAutomatorTestCase {
	public final static UiDevice in=UiDevice.getInstance();
	public final static String runcase="DownloadLoopVideoTest";
	public void testcase()throws IOException{
		try{
			Common.startLog(runcase,"*****Start to run "+runcase+" *****");
			//通过是否有IPC按钮判断是否进入
			Common.openActivity(runcase,in,"cn.com.mobnote.golukmobile:id/index_carrecoder_btn");
			sleep(3000);
			//选择 我的 从下边状态栏
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/more_btn");
			sleep(1000);
			//选择 我的相册
			Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/video_item");
			sleep(1000);
			UiObject IPCBtn=Common.findViewById(runcase, in, "cn.com.mobnote.golukmobile:id/mCloudText");
			System.out.println("=====>"+IPCBtn.getText());
			boolean IPCStatus=false;
			int waitconnecttime=1;
			while (waitconnecttime<120){
				if (IPCBtn.getText().equalsIgnoreCase("远程视频")){
					Common.infoLog(runcase, "IPC已经连接");
					IPCBtn.clickAndWaitForNewWindow();
					break;
				}else{
					Common.infoLog(runcase, "IPC正在连接 "+ waitconnecttime+ " 秒");
					sleep(1000);
					waitconnecttime=waitconnecttime+1;
				}
			}
			if (waitconnecttime==120){
				throw new Exception("IPC连接超时，"+waitconnecttime+'秒');
				}
			//等待列表刷新,刷新完成返回true
			IPCStatus=Common.waitForList(runcase, in,"cn.com.mobnote.golukmobile:id/loading_bg",30);
			if (IPCStatus){
				Common.clickViewById(runcase, in, "cn.com.mobnote.golukmobile:id/mLoopText");
				if(Common.waitForList(runcase, in,"cn.com.mobnote.golukmobile:id/loading_bg",30)){
					Common.downloadVideo(runcase, in, "cn.com.mobnote.golukmobile:id/mEditBtn");
					sleep(5000);
				}				
			}
			Common.backToHome(runcase,in);
			Common.passcase(runcase);
			Common.startLog(runcase,"*****End to run "+runcase+" *****");
		}catch(Exception e){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date curDate = new Date(System.currentTimeMillis());
			String str = formatter.format(curDate);
			Common.infoLog(runcase,"截图保存在 /sdcard/GolukTest/"+runcase+"/"+str+".png");
			Common.takeScreen(in, runcase,str);
			String s=null;
			s=Common.checkFailReason(runcase,in, e.getMessage());
			Common.backToHome(runcase,in);
			Common.errorLog(runcase,s);
			Common.failcase(runcase);
			Common.startLog(runcase,"*****End to run "+runcase+" *****");
		}
	}	
}
