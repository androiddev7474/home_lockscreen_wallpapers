package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by stude on 2018-04-14.
 */

public class SQLiteDBhandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ImgDataStorage5";
    public static final int DATABASE_VERSION = 1;

    public SQLiteDBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(ImageBackgroundsContract.ImageDataTable.SQL_CREATE_TABLE);
    }


    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

}