package g5.org.g5;

import android.os.Handler;
import android.widget.TextView;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class TextViewLogger extends AppenderBase<ILoggingEvent> {
    private TextView textViewLog;
    private Handler uiHandler;

    private PatternLayout layout;
    private LinkedList<ILoggingEvent> iLoggingEvents = new LinkedList<>();
    private int maxErrorLines = 400;
    private boolean isDisplayed = false;

    private void addToLog(ILoggingEvent str) {
        if (iLoggingEvents.size() >= maxErrorLines) {
            iLoggingEvents.removeFirst();
        }

        iLoggingEvents.add(str);



        updateLog();
    }

    private void updateLog() {
        if (!isDisplayed) return;
        StringBuilder log = new StringBuilder();

        for (ILoggingEvent eventObject : iLoggingEvents) {
            log.append(layout.doLayout(eventObject));
        }
        if(uiHandler!=null && textViewLog!=null && isDisplayed) {
            uiHandler.post(new UpdateUiRunnable(log.toString()));
        }
    }


    public TextViewLogger() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.getLoggerList();
        Logger logger = (Logger) LoggerFactory.getLogger("ROOT");

//        SLF4JBridgeHandler.install();
//        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
//        rootLogger.setLevel(java.util.logging.Level.ALL);

        layout = new PatternLayout();
        layout.setContext(lc);
        layout.setPattern("%d{HH:mm:ss} %-1.-1level %logger{0} %msg%n");
        layout.start();

        setContext(lc);
        start();
        logger.addAppender(this);

        logger.setLevel(Level.TRACE);
        logger.setAdditive(true);
    }

    public void clearLog() {
        iLoggingEvents.clear();
        updateLog();
    }

    @Override
    protected void append(final ILoggingEvent eventObject) {
        addToLog(eventObject);
    }

    public void pause() {
        isDisplayed = false;
        textViewLog = null;
        this.uiHandler = null;
    }

    public void resume(TextView textView, Handler uiHandler) {
        textViewLog = textView;
        this.uiHandler = uiHandler;
        isDisplayed = true;
        updateLog();
    }

    private class UpdateUiRunnable implements Runnable {
        private final String log;

        public UpdateUiRunnable(String log) {
            this.log = log;
        }

        @Override
        public void run() {
            if(uiHandler!=null && textViewLog!=null && isDisplayed) {
                textViewLog.setText(log);
            }
//                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(MainApp.instance().getApplicationContext());
//                if (SP.getBoolean("nsAutoScroll", true)) {
//                    mainActivity.scrollview.fullScroll(ScrollView.FOCUS_DOWN);
//                }
        }
    }
}
