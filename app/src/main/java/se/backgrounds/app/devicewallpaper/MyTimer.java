package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static se.backgrounds.app.devicewallpaper.MainActivity.MY_PREFS_NAME;
import static se.backgrounds.app.devicewallpaper.MainActivity.MY_PREFS_NAME_TIMER;
import static se.backgrounds.app.devicewallpaper.MainActivity.MY_PREFS_TIMER_VALUE;

/**
 * Björn Hallström
 * Version: 1 (2018-04-26)
 * Klassen ansvarar för uppdatering av bakgrundsbild till låstskärm/hemskärm
 *
 */

public class MyTimer {

    private Timer timer;
    private TimerTask timerTask;
    private int timeLimit = TimerFragment.ONE_DAY; //default

    private Context context;

    private WallpaperRandomizer wallpaperRandomizer;
    private MyWallpaperManager myWallpaperManager;

    public MyTimer(Context context) {

        this.context = context;

    }

    public void startTimer() {

        wallpaperRandomizer = new WallpaperRandomizer(context);
        myWallpaperManager = new MyWallpaperManager(context);


        //hämta timervärde från sharedpreferences
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME_TIMER, MODE_PRIVATE);
            timeLimit = prefs.getInt(MY_PREFS_TIMER_VALUE, TimerFragment.ONE_DAY);
            LogUtils.debug("TIMERVALUE IN STARTIMER SET TO", "" + timeLimit);
        }

        timer = new Timer();
        initializeTimerTask();

        timer.schedule(timerTask, timeLimit, timeLimit);

    }

    public void setContext(Context context) {

        this.context = context;
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {

            public void run() {

                if (wallpaperRandomizer.randomize()) {

                    LogUtils.debug("TIMERVALUE IN BACKGROUNDTHREAD", "" + timeLimit);
                    int lockedID = wallpaperRandomizer.getLocked();
                    int homeID = wallpaperRandomizer.getHome();
                    LogUtils.debug("resID (locked home)", "" + lockedID + " " + homeID);
                    LogUtils.debug("CONTEXT IN TIMER", "" + context);
                    if (lockedID != 0)
                        myWallpaperManager.setLockedScreen(lockedID);
                    if (homeID != 0)
                        myWallpaperManager.setHomeScreen(homeID);

                } else {
                    LogUtils.debug("MYTIMER", "Wallpaper-listan = 0");
                }
            }
        };
    }

    public void stoptimertask() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


}
