package se.backgrounds.app.devicewallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

//egna bibliotek
import se.test.app.bitmapdecoder.BitmapDecoder;

/**
 * Björn Hallström
 * Version: 1 (2018-04-24)
 *
 * Tar hand om att slutgiltigt sätta låstskärm-bakgrund och/eller hemskärm-bakgrund - beroende på användaren preferenser
 * Kan tänkas att bygga ut klassen så att den kan beskära en bild och vilja olika vyer av en större bild.
 */

public class MyWallpaperManager {

    private int wallpaperHeight, wallpaperWidth;
    private final static int QUALITY = 100; // kvalitet i procent

    //Android API klasser
    private Context context;
    final BitmapFactory.Options options = new BitmapFactory.Options();

    public MyWallpaperManager(Context context) {

        this.context = context;
    }


    /**
     * Anpassar dimensionerna efter telefonens dimensioner - ja eg. höjden. Tveksamt om denna metod fyller någon
     * annnat än att spara minne. Kanske nog så viktigt i minneshanteringssynpunkt!
     * @param resID
     */
    private void requestBitmapDimen(int resID) {

        //Ingen optimal lösning i så mening att detta ev kan lyftas in i BitmapDecoder-biblioteket - men koden helt ok
        wallpaperWidth = context.getResources().getDisplayMetrics().widthPixels;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resID, options);
        final int height = options.outHeight; // ta reda på höjd/bredd utan att ladda in hela bilden i primärminnet
        final int width = options.outWidth;
        float bitmapRatio = (float)height / width;
        wallpaperHeight = (int)(wallpaperWidth * bitmapRatio); //anpassa höjden tll en bildhöjd som matchar bildkvoten (bitmapRatio)

        LogUtils.debug("IMAGE RAW WIDTH", "" + width);
        LogUtils.debug("IMAGE RAW HEIGHT", "" + height);
        LogUtils.debug("REQUESTED WALLPAPERWIDTH = DEVICE SCREENWIDTH", "" + wallpaperWidth);
        LogUtils.debug("REQUESTED WALLPAPERHEIGHT = ADJUSTED TO REAL IMAGE ASPECT RATIO", "" + wallpaperHeight);

    }


    /**
     * Låstskärm
     * @param lockedID
     */
    public void setLockedScreen(int lockedID) {

        LogUtils.debug("MYWALLPAPERMANAGER", ".. setting locked screen widht resource id" + lockedID);
        requestBitmapDimen(lockedID);
        //skapa en bitmap anpassad till skärmen. Höjden dock anpassad till den ursprungliga bildens dimensioner - bättre att beskära bilden - löser det senare
        Bitmap lockedScreenBitmap = BitmapDecoder.decodeSampledBitmapFromResource(context.getResources(), lockedID, wallpaperWidth, wallpaperHeight, Bitmap.Config.RGB_565);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        lockedScreenBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                WallpaperManager.getInstance(context).setStream(bs, null, true, WallpaperManager.FLAG_LOCK);
                MemoryLogger.logHeap();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


    /**
     * Hemskärm
     * @param homeID
     */
    public void setHomeScreen(int homeID) {

        requestBitmapDimen(homeID);
        Bitmap bitmap = BitmapDecoder.decodeSampledBitmapFromResource(context.getResources(), homeID, wallpaperWidth, wallpaperHeight, Bitmap.Config.RGB_565);
        final WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
        try {
            //myWallpaperManager.setResource(homeID);
            myWallpaperManager.setBitmap(bitmap);
            MemoryLogger.logHeap();

        } catch (IOException ioe) {

            ioe.printStackTrace();
        }
    }

}
