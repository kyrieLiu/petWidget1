package ys.app.pet.pad2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * @author : skywang <wangkuiwu@gmail.com>
 * description : 提供App Widget
 */

public class MyWidgetProvider extends AppWidgetProvider {
    public static final String pkgName = "ys.app.pet.widget";
    // 发送服务对应action
    public static final String MY_ERVICE_INTENT = pkgName + "_APP_WIDGET_SERVICE";
    // 更新 widget 的广播对应的action
    public static final String ACTION_UPDATE_ALL = pkgName + "_UPDATE_ALL";

    private static final String TAG = "MyWidgetProvider";

    private boolean DEBUG = false;
    // 启动ExampleAppWidgetService服务对应的action
    private final Intent CASHIER_SERVICE_INTENT = new Intent(MY_ERVICE_INTENT);

    // 保存 widget 的id的HashSet，每新建一个 widget 都会为该 widget 分配一个 id。
    private static Set idsSet = new HashSet();
    // 关闭点击页面请求
    private static final int REQUEST_CLICK_RL0_EVENT = 1;// 点击事件
    private static final int REQUEST_CLICK_RL1_EVENT = 2;// 点击事件
    private static final int REQUEST_CLICK_RL2_EVENT = 3;// 点击事件
    private static final int REQUEST_CLICK_RL3_EVENT = 4;// 点击事件
    private static final int REQUEST_CLICK_RL4_EVENT = 5;// 点击事件
    private static final int REQUEST_CLICK_RL5_EVENT = 6;// 点击事件
    private static final int REQUEST_CLICK_RL6_EVENT = 7;// 点击事件


    // onUpdate() 在更新 widget 时，被执行，
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, " ------ onUpdate(): appWidgetIds.length=" + appWidgetIds.length);

        // 每次 widget 被创建时，对应的将widget的id添加到set中
        for (int appWidgetId : appWidgetIds) {
            idsSet.add(Integer.valueOf(appWidgetId));
        }
        prtSet(context);

        // 解决CashierWidgetProvider没有定时刷新问题（不刷新则不会变化layout 已经按钮触发事件）
        Log.d(TAG, " ------ onUpdate(): CashierWidgetService");
        Intent intent = new Intent(context, MyWidgetService.class);
        PendingIntent refreshIntent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC, 0, 1000, refreshIntent);
        context.startService(intent);
    }

    // 当 widget 被初次添加 或者 当 widget 的大小被改变时，被调用
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                                          Bundle newOptions) {
        Log.d(TAG, " ------ onAppWidgetOptionsChanged");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    // widget被删除时调用
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, " ------ onDeleted(): appWidgetIds.length=" + appWidgetIds.length);

        // 当 widget 被删除时，对应的删除set中保存的widget的id
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId));
        }
        prtSet(context);

        super.onDeleted(context, appWidgetIds);
    }

    // 第一个widget被创建时调用
    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, " ------ onEnabled");
        // 在第一个 widget 被创建时，开启服务
        Intent intent=new Intent(MY_ERVICE_INTENT);
        intent.setPackage(context.getPackageName());
        context.startService(intent);

        super.onEnabled(context);
    }

    // 最后一个widget被删除时调用
    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, " ------ onDisabled");

        // 在最后一个 widget 被删除时，终止服务
        Intent intent=new Intent(MY_ERVICE_INTENT);
        intent.setPackage(context.getPackageName());
        context.stopService(intent);

        super.onDisabled(context);
    }

    // 接收广播的回调函数
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            return;
        }
        final String action = intent.getAction();
        Log.d(TAG, " ------ OnReceive:Action: " + action);
        if (ACTION_UPDATE_ALL.equals(action)) {
            // “更新”广播,当覆盖安装时：AppWidgetManager.getInstance(context)会报空指针
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);

        } else if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            // “按钮点击”广播
            Uri data = intent.getData();
            int clickId = Integer.parseInt(data.getSchemeSpecificPart());
            Log.d(TAG, " ------ OnReceive:clickId: " + clickId);
            switch (clickId) {
                case REQUEST_CLICK_RL0_EVENT:
                    startPetApp(context, Activity_One.class);
                    break;
                case REQUEST_CLICK_RL1_EVENT:
                    startPetApp(context, Activity_Two.class);

                    break;
                case REQUEST_CLICK_RL2_EVENT:
                    startPetApp(context, Activity_One.class);
                    break;
                case REQUEST_CLICK_RL3_EVENT:
                    startPetApp(context, Activity_Two.class);

                    break;
                case REQUEST_CLICK_RL4_EVENT:
                    startPetApp(context, Activity_One.class);

                    break;
                case REQUEST_CLICK_RL5_EVENT:
                    startPetApp(context, Activity_Two.class);

                    break;
                case REQUEST_CLICK_RL6_EVENT:
                    startPetApp(context, Activity_One.class);

                    break;

                default:
                    break;
            }
        }

        super.onReceive(context, intent);
    }

    private void startPetApp(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }

    // 更新所有的 widget
    private void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, Set set) {

        Log.d(TAG, "CashierWidgetProvider ------ updateAllAppWidgets(): size=" + set.size());

        // widget 的id
        int appID;
        // 迭代器，用于遍历所有保存的widget的id
        Iterator it = set.iterator();

        while (it.hasNext()) {
            appID = ((Integer) it.next()).intValue();

            // 获取 example_appwidget.xml 对应的RemoteViews
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.my_widget_layout);

            // 设置点击按钮对应的PendingIntent：即点击按钮时，发送广播。
            remoteView.setOnClickPendingIntent(R.id.rl0, getPendingIntent(context, appID, REQUEST_CLICK_RL0_EVENT));
            remoteView.setOnClickPendingIntent(R.id.rl1, getPendingIntent(context, appID, REQUEST_CLICK_RL1_EVENT));
            remoteView.setOnClickPendingIntent(R.id.rl2, getPendingIntent(context, appID, REQUEST_CLICK_RL2_EVENT));
            remoteView.setOnClickPendingIntent(R.id.rl3, getPendingIntent(context, appID, REQUEST_CLICK_RL3_EVENT));
            remoteView.setOnClickPendingIntent(R.id.rl4, getPendingIntent(context, appID, REQUEST_CLICK_RL4_EVENT));
            remoteView.setOnClickPendingIntent(R.id.rl5, getPendingIntent(context, appID, REQUEST_CLICK_RL5_EVENT));
            remoteView.setOnClickPendingIntent(R.id.rl6, getPendingIntent(context, appID, REQUEST_CLICK_RL6_EVENT));
//            remoteView.setOnClickPendingIntent(R.id.rl7, getPendingIntent(context, appID, REQUEST_CLICK_RL7_EVENT));


            // 更新 widget
            appWidgetManager.updateAppWidget(appID, remoteView);
        }
    }

    // 发送点击事件广播
    private PendingIntent getPendingIntent(Context context, int appID, int clickId) {
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MyWidgetProvider.class);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setData(Uri.parse("custom:" + clickId));
        // PendingIntent.FLAG_CANCEL_CURRENT
        PendingIntent pi = PendingIntent.getBroadcast(context, appID, intent, 0);
        return pi;
    }

    // 调试用：遍历set
    private void prtSet(Context context) {
        if (DEBUG) {
            int index = 0;
            int size = idsSet.size();
            Iterator it = idsSet.iterator();
            Log.d(TAG, "total:" + size);
            while (it.hasNext()) {
                Log.d(TAG, index + " -- " + ((Integer) it.next()).intValue());
            }
        }
        // context.startService(CASHIER_SERVICE_INTENT);
    }


    private void startApkByPkgName(Context context, String pkgName, String errorInfo) {
        if (!isPkgInstall(context, pkgName)) {
            Toast.makeText(context.getApplicationContext(), errorInfo, Toast.LENGTH_SHORT).show();
            return;
        }
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(pkgName);
        context.startActivity(intent);
    }

    private boolean isPkgInstall(Context context, String pkgName) {
        if (pkgName == null || pkgName.length() == 0) {
            return false;
        }
        PackageManager pm = context.getApplicationContext().getPackageManager();
        try {
            PackageInfo pkgInfo = pm.getPackageInfo(pkgName, 0);
            if (pkgInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
