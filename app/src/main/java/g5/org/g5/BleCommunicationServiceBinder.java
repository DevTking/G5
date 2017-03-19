package g5.org.g5;


import android.app.Notification;
import android.graphics.Bitmap;
import android.os.Binder;
import android.support.v7.app.NotificationCompat;

public class BleCommunicationServiceBinder extends Binder {
    final BleCommunicationService bleCommunicationService;

    public BleCommunicationServiceBinder(BleCommunicationService bleCommunicationService) {
        this.bleCommunicationService = bleCommunicationService;
    }


    public final void startCommunication() {
        bleCommunicationService.startConnectionController();
        MainApp.instance().m_notification = new NotificationCompat.Builder(MainApp.instance())
                .setContentTitle("BleCommunicationService")
                .setTicker("BleCommunicationService")
                .setContentText("BleCommunicationService")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true).build();
        this.bleCommunicationService.startForeground(1, MainApp.instance().m_notification);
    }
}
