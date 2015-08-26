# -*- coding: UTF-8 -*-  
import subprocess
import time
import datetime
import os
deviceid="64308251"

def foo():
    print("**********Start To Run Stability Test***********")
def activelogcat():
    print("===Start to logcat===")
    s1=subprocess.Popen("adb -s "+deviceid+" shell logcat -v time >./GolukTest/logcat.txt", shell=True)
def activeGoluklog(f):
    print("===Start "+f+" to Goluklog===")
    subprocess.Popen("adb -s "+deviceid+" shell logcat -v time -s "+"GolukTest-"+f+" >./GolukTest/"+f+"/Goluklog.txt", shell=True)
def endadb():
    killadb=subprocess.Popen("adb -s "+deviceid+"  kill-server", shell=True)
    killadb.communicate()[0]
    time.sleep(2)
    startadb=subprocess.Popen("adb devices", shell=True)
    startadb.communicate()[0]
def rmfolder():
    print("===Delete Phone Goluklog Folder===")
    rf=subprocess.Popen("adb -s "+deviceid+" shell rm -r /sdcard/GolukTest", shell=True)
    rf.communicate()[0]
    time.sleep(2)
    print("===Delete Goluklog Folder===")
    rf2=subprocess.Popen("rm -r ./GolukTest", shell=True)
    rf2.communicate()[0]
    time.sleep(2)
    
def rootfolder():
    print("===Start to Create Goluklog Folder===")
    mf=subprocess.Popen("mkdir ./GolukTest", shell=True)
    mf.communicate()[0]
    time.sleep(2)
    print("===Start to Create Phone Goluklog Folder===")
    mf2=subprocess.Popen("adb -s "+deviceid+" shell mkdir /sdcard/GolukTest", shell=True)
    mf2.communicate()[0]
    time.sleep(2)
def mkfolder(f):
    print("===Start to Create "+f+" Folder===")
    mf=subprocess.Popen("mkdir -p ./GolukTest/"+f+"/screen", shell=True)
    mf.communicate()[0]
    time.sleep(2)
def pullscreen(f):
    print("===Start To Pull Screen From the Phone===")
    time.sleep(3)
    pull=subprocess.Popen("adb -s "+deviceid+" pull /sdcard/GolukTest/"+f+"/ ./GolukTest/"+f+"/screen/", shell=True)
    pull.communicate()[0]
def execute(case,round):
    print("===Start To Run "+case+"===")
    time.sleep(2)
    mkfolder(case)
    #activelogcat()
    t1=datetime.datetime.now()
    print("Start time: "+bytes(t1))
    activeGoluklog(case)
    for i in range(round):
        print("***"+case+" Excution "+bytes(i+1) +" times===")
        run=subprocess.Popen("adb -s "+deviceid+" shell uiautomator runtest GolukTest.jar -c com.goluk.testcases."+case+" |tee -a ./GolukTest/output.txt", shell=True)
        run.communicate()[0]
    time.sleep(5)
    t2=datetime.datetime.now()
    print("End time: "+bytes(t2))
    t=(t2-t1).seconds
    print("Run time is: "+bytes(t))
    pullscreen(case)
    #endadb()
    time.sleep(2)
    
    startadb=subprocess.Popen("adb -s "+deviceid+" logcat -c", shell=True)
    print("======End "+case+" ========")
    
if __name__=="__main__":
    round=5
    Case={'ShareFavoriteVideoTest':5,\
          'ShareEmergencyVideoTest':5,\
          'RequestLiveTest':6,\
          'RequestFavoriteVideoTest':6,\
          'PreviewTest':8,\
          'PlayLoopVideoTest':5,\
          'PlayLocalFavoriteVideoTest':5,\
          'PlayLocalEmergencyVideoTest':5,\
          'PlayFavoriteVideoTest':5,\
          'PlayEmergencyVideoTest':5,\
          'DownloadFavoriteVideoTest':8,\
          'DownloadEmergencyVideoTest':8}
    #Case={'PlayLocalFavoriteVideoTest':5}
    rmfolder()
    rootfolder()
    activelogcat()
    for key in Case:
        execute(key,Case[key])
    time.sleep(5)
    #endadb()
    #print("======Monkey ========")
    #m=subprocess.Popen("sh monkey", shell=True)
    #m.communicate()[0]
