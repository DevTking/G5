package g5.org.g5;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class BleComunicationService_ServiceConnection implements ServiceConnection {

    private BleCommunicationServiceBinder m_binder;
    public boolean m_isCommunicationLoopEnabled;

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        m_binder = (BleCommunicationServiceBinder) iBinder;
        m_binder.startCommunication();

        m_isCommunicationLoopEnabled = true;
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        m_binder = null;
        m_isCommunicationLoopEnabled = false;
    }
}
