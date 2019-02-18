package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.os.Debug;
import android.os.Looper;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Björn Hallström
 * Version: 1
 * Loggar allokerat dynamiskt minne
 *
 */

public class MemoryLogger {

    private static DecimalFormat df = new DecimalFormat();
    private static final double ONE_MB = 1048576.0;

    public static void logHeap() {
        Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
        Double available = new Double(Debug.getNativeHeapSize())/ONE_MB;
        Double free = new Double(Debug.getNativeHeapFreeSize())/ONE_MB;

        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        String string1 = "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)";
        String string2 = "debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)";

        LogUtils.debug("MEM INFO",  "debug. =================================");
        LogUtils.debug("MEM INFO", string1);
        LogUtils.debug("MEM INFO", string2);

        //Toast.makeText(context, string1 + "\n" + string2, Toast.LENGTH_LONG).show();
    }

}
