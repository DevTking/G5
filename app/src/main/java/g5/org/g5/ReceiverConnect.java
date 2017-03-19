package g5.org.g5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ReceiverConnect extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainApp.instance().wl.acquire();
        MainApp.setupAlarm();
        BtUtil.startLeScanInternal();
    }
}
