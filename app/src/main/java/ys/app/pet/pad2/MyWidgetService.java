package ys.app.pet.pad2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/*
 * @author : skywang <wangkuiwu@gmail.com>
 * description : 周期性更新AppWidget的服务
 */

public class MyWidgetService extends Service {
	
	private static final String TAG="CashierWidgetService";


	@Override
	public void onCreate() {


		super.onCreate();
	}
	
	@Override
	public void onDestroy(){
		// 中断线程，即结束线程。

		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * 服务开始时，即调用startService()时，onStartCommand()被执行。
	 * onStartCommand() 这里的主要作用：
	 * (01) 将 appWidgetIds 添加到队列sAppWidgetIds中
	 * (02) 启动线程
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		super.onStartCommand(intent, flags, startId);
		Intent updateIntent=new Intent(MyWidgetProvider.ACTION_UPDATE_ALL);
		sendBroadcast(updateIntent);
	    return START_STICKY;
	}

}
