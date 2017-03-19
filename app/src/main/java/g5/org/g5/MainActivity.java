package g5.org.g5;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.le.*;
import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MainActivity.class);

    static Handler uiHandler = new Handler(Looper.getMainLooper());

    private TextView mTextView;

    private ScrollView scrollview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scrollview = ((ScrollView) findViewById(R.id.scrollView));
        scrollview.setHorizontalScrollBarEnabled(true);

        mTextView = (TextView) findViewById(R.id.log);

        if(!MainApp.instance().ble_connection.m_isCommunicationLoopEnabled) {

            Intent intent = new Intent(getApplicationContext(), BleCommunicationService.class);
            getApplicationContext().startService(intent);
            getApplicationContext().bindService(intent, MainApp.instance().ble_connection, 0);
        }



    }


    public static String toHexString(byte[] value)
    {
        if(value==null) return "EMPTY_BYTES";

        StringBuffer sb = new StringBuffer();

        for (byte element : value)
        {
            sb.append(String.format("%02x", element));
            sb.append("-");
        }
        sb.setLength(sb.length()-1);

        return sb.toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainApp.getTextViewLogger().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApp.getTextViewLogger().resume(mTextView,uiHandler);
    }


    @Override
    protected void onDestroy() {
        log.debug(" onDestroy " );
        super.onDestroy();

        //stopLeScanInternal();
    }


}
