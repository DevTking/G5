package g5.org.g5;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import org.slf4j.LoggerFactory;

public class BtScanCallback extends ScanCallback {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(BtScanCallback.class);

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        ScanRecord scanRecord = result.getScanRecord();

        final BluetoothDevice device = result.getDevice();
        int rssi = result.getRssi();
        byte[] bytes = scanRecord.getBytes();

        byte[] manufacturerSpecificData = scanRecord.getManufacturerSpecificData(208);
        byte i3 = (manufacturerSpecificData == null || manufacturerSpecificData.length < 2) ? -1 : manufacturerSpecificData[1];


        log.debug(" " + device.getAddress() + " \n" + device.getName() + " " + rssi + " " + MainActivity.toHexString(bytes) + " " + MainActivity.toHexString(manufacturerSpecificData));

        if(manufacturerSpecificData !=null && manufacturerSpecificData.length == 2) {

            MainApp.handler.post(new Runnable() {
                @Override
                public void run() {
                    if (MainApp.instance().m_gatt == null) {
                        MainApp.instance().m_gatt =
                                device.connectGatt(MainApp.instance().getApplicationContext(), false,
                                        MainApp.instance().gattCallback, BluetoothDevice.TRANSPORT_LE);
                    } else {
                        MainApp.instance().m_gatt.connect();
                    }
                }
            });
            BtUtil.stopLeScanInternal();
            MainApp.setupAlarm();
        }
        //        MainApp.handler.postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                BluetoothManager bluetoothManager = (BluetoothManager) MainApp.instance().getSystemService(Context.BLUETOOTH_SERVICE);
        //                int connectionState = bluetoothManager.getConnectionState(device, BluetoothProfile.GATT);
        //                if(connectionState!=BluetoothProfile.STATE_CONNECTED) {
        //                    MainApp.instance().m_gatt.disconnect();
        //                    MainApp.instance().m_gatt.connect();
        //                }
        //
        //            }
        //        }, 100);

    }

    @Override
    public void onScanFailed(int errorCode) {

        log.debug(" onScanFailed " + errorCode);
        BtUtil.stopLeScanInternal();
        MainApp.handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                BtUtil.startLeScanInternal();
            }
        }, 100);
    }
}
