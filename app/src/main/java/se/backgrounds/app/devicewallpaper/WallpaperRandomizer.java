package se.backgrounds.app.devicewallpaper;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.IMAGE_BG_ID_COL_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.N_COLUMNS;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TABLE_IMAGE_DATA;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TYPE_COL_ID;

/**
 * Björn Hallström
 * Version: 1
 * Slumpar fran en bild från listor med bildreferenser - hämtar 1 referens från respektive lista - om det finns data
 *
 */

public class WallpaperRandomizer {

    //primitiver
    private int locked, home; //resid för låst resp hemskärm
    private int[][] tableData; //fylls med information från sqlite

    //egna klasser
    private SQLiteDBhandler dBhandler;

    //listor
    private ArrayList <Integer> lockedList = new ArrayList<>(); //låstskärm
    private ArrayList <Integer> homeList = new ArrayList<>(); //hemskärm
    private ArrayList<Integer> allList = new ArrayList<>(); //båda
    private ArrayList<ImageDataHolder> wallpaperDataList = new ArrayList<>();

    public WallpaperRandomizer(Context context) {

        dBhandler = new SQLiteDBhandler(context);
        createImageData();
    }


    /**
     * Hämtar data från sqlite och skapar listorna. Tre listor - en för låst skärm, en för hemskärm
     * och en för båda
     */
    private void createImageData() {

        int nRows = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        tableData = new int[nRows][N_COLUMNS];
        SQLqueries.getTableData(dBhandler.getReadableDatabase(), tableData, TABLE_IMAGE_DATA);

        for (int i = 0; i < tableData.length; i++) {

            wallpaperDataList.add(new ImageDataHolder(tableData[i][IMAGE_BG_ID_COL_ID], tableData[i][TYPE_COL_ID]));

            switch (tableData[i][TYPE_COL_ID]) {

                case 0: //låst
                    lockedList.add(tableData[i][IMAGE_BG_ID_COL_ID]);
                    break;
                case 1: //hem
                    homeList.add(tableData[i][IMAGE_BG_ID_COL_ID]);
                    break;
                case 2: //båda
                    allList.add(tableData[i][IMAGE_BG_ID_COL_ID]);
                    break;
            }

        }

    }


    /**
     * Randomiserar i listan och väljer låstskärm/hemskärm beroende på användarens inställningar
     * Ska fundera på att förbättra koden - känns lite rörig just nu.
     * @return
     */
    public boolean randomize() {

        //nollställ
        locked = 0;
        home = 0;

        //sluma från den generella listan
        int randIndex = 0;
        if (wallpaperDataList.size() > 0) // endast om det finns objekt i listan
            randIndex = ThreadLocalRandom.current().nextInt(0, wallpaperDataList.size());
        else {
            createImageData(); // nytt försök med att hämta
            LogUtils.debug("WALLPAPER RANDOMIZER", "Nytt försök hämta från sqlite");
            return false;
        }

        int type = wallpaperDataList.get(randIndex).getType();
        switch (type) {
            case 0:

                locked = wallpaperDataList.get(randIndex).resID;

                //se om hem kan sättas
                if (homeList.size() > 0) {

                    if (allList.size() > 0) { // om uppfylls - ge allList en chans att vara med för den kan också användas som hemskärm
                        randIndex = ThreadLocalRandom.current().nextInt(0, 2);
                        switch (randIndex) {
                            case 0: //använd homeList
                                randIndex = ThreadLocalRandom.current().nextInt(0, homeList.size());
                                home = homeList.get(randIndex);
                                break;
                            case 1: // använd allList
                                randIndex = ThreadLocalRandom.current().nextInt(0, allList.size());
                                home = allList.get(randIndex);
                                break;
                        }
                    } else {
                        randIndex = ThreadLocalRandom.current().nextInt(0, homeList.size());
                        home = homeList.get(randIndex);
                    }

                }
                break;
            case 1: //hem

                home = wallpaperDataList.get(randIndex).resID;

                //se om låst kan sättas
                if (lockedList.size() > 0) {

                    if (allList.size() > 0) { // om uppfylls - ge allList en chans att vara med för den kan också användas som låstskärm

                        randIndex = ThreadLocalRandom.current().nextInt(0, 2);
                        switch (randIndex) {
                            case 0: //använd lockedList
                                randIndex = ThreadLocalRandom.current().nextInt(0, lockedList.size());
                                locked = lockedList.get(randIndex);
                                break;
                            case 1: // använd allList
                                randIndex = ThreadLocalRandom.current().nextInt(0, allList.size());
                                locked = allList.get(randIndex);
                                break;
                        }
                    } else {
                        randIndex = ThreadLocalRandom.current().nextInt(0, lockedList.size());
                        locked = lockedList.get(randIndex);
                    }
                }

                break;
            case 2: //låst = hem
                locked = wallpaperDataList.get(randIndex).resID;
                home = locked;
                break;
        }

        return true;
    }


    public int getLocked() {

        return locked;
    }

    public int getHome() {

        return home;
    }


    private class ImageDataHolder {

        private int resID, type;

        public ImageDataHolder(int resID, int type) {

            this.resID = resID;
            this.type = type;
        }


        public int getResID() {

            return resID;
        }


        public int getType() {

            return type;
        }
    }

}
