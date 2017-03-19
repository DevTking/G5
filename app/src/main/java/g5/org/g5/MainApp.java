package g5.org.g5;

import android.app.*;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.support.v7.app.NotificationCompat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {
    static public Handler handler;
    private static Logger log = LoggerFactory.getLogger(MainApp.class);

    private static MainApp sInstance;

    private static TextViewLogger textViewLogger;

    static HandlerThread handlerThread;

    ScanCallback callback;
    BluetoothGattCallback gattCallback;
    PowerManager.WakeLock wl;
    public BleComunicationService_ServiceConnection ble_connection = new BleComunicationService_ServiceConnection();
    public BluetoothGatt m_gatt;

    Notification m_notification ;

    NotificationManager mNotificationManager;

    static void setupAlarm() {
        log.debug("setupAlarm ");
        long triggerTime = SystemClock.elapsedRealtime() + 298_000L;


        AlarmManager alarmSvc = (AlarmManager) instance().getApplicationContext().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent( "g5.org.g5.REFRESH_THIS" );
        PendingIntent pi = PendingIntent.getBroadcast( instance().getApplicationContext(), 0, intent, 0 );
        alarmSvc.cancel(pi);

        if (Build.VERSION.SDK_INT >= 23) {
            alarmSvc.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
        } else {
            alarmSvc.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
        }

//        alarmSvc.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime, pi);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        textViewLogger = new TextViewLogger();

        if (handler == null) {
            handlerThread = new HandlerThread(MainApp.class.getSimpleName() + "Handler");
            handlerThread.start();
            handler = new Handler(MainApp.handlerThread.getLooper());
        }
        wl = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"BT");

        callback = new BtScanCallback();
        gattCallback = new BtGattCallback();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static MainApp instance() {
        return sInstance;
    }

    public static TextViewLogger getTextViewLogger() {
        return textViewLogger;
    }

}
