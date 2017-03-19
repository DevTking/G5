package g5.org.g5;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BtUtil {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(BtUtil.class);

    static void startLeScanInternal() {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner scanner = ba.getBluetoothLeScanner();

        ScanFilter scanFilter = new ScanFilter.Builder()
                .setServiceUuid(new ParcelUuid(UUID.fromString("0000febc-0000-1000-8000-00805f9b34fb")))
                //.setDeviceName("Dexcom25")
                .build();//.setDeviceName(a(this.c)).build();

        List<ScanFilter> scanFilterList = Collections.singletonList(scanFilter);
//        scanFilterList = null;

        scanner.startScan(scanFilterList,
                new ScanSettings.Builder()
                        .setReportDelay(0)
                        .setMatchMode(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build(),
                MainApp.instance().callback);
        //scanner.flushPendingScanResults(callback);

        //flushScanResult();

        log.debug(" startLeScanInternal " );
    }

    private static void flushScanResult() {
        MainApp.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                log.debug(" flushScanResult " );
                BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
                BluetoothLeScanner scanner = ba.getBluetoothLeScanner();
                scanner.flushPendingScanResults(MainApp.instance().callback);
                flushScanResult();
            }
        },1000);
    }

    static void stopLeScanInternal() {
        log.debug(" stopLeScanInternal " );
        BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().stopScan(MainApp.instance().callback);
    }
}
