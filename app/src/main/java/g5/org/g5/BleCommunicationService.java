package g5.org.g5;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import org.slf4j.LoggerFactory;

public class BleCommunicationService extends Service {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(BleCommunicationService.class);

    private BleCommunicationServiceBinder m_binder = new BleCommunicationServiceBinder(this);

    @Override
    public IBinder onBind(Intent intent) {
        return this.m_binder;
    }


    public void startConnectionController() {
    }

    public int onStartCommand(Intent intent2, int i, int i2) {
        log.debug("onStartCommand ");
        if (intent2 == null) {
            //this.m_isServiceError = true;
        }
        super.onStartCommand(intent2, i, i2);
//        MainApp.instance().m_gatt = BluetoothAdapter.getDefaultAdapter()
//                .getRemoteDevice("D8:6E:14:95:26:C0")
//                .connectGatt(MainApp.instance().getApplicationContext(), true,
//                MainApp.instance().gattCallback, BluetoothDevice.TRANSPORT_LE);

        MainApp.instance().wl.acquire();
        BtUtil.startLeScanInternal();

        AlarmManager alarmSvc = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent( "g5.org.g5.REFRESH_THIS" );
        PendingIntent pi = PendingIntent.getBroadcast( getApplicationContext(), 0, intent, 0 );
        alarmSvc.cancel(pi);

        return Service.START_FLAG_REDELIVERY;
    }

    @Override
    public void onDestroy() {
        log.debug("onDestroy");

    }
}