package se.backgrounds.app.devicewallpaper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.IMAGE_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.IMAGE_THUMB_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.N_COLUMNS;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.PENDING_REMOVE;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.POSITION;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TYPE;

/**
 * Created by stude on 2018-04-14.
 */

public class SQLqueries {

    private Context context;

    private SQLqueries() { }

    public static void addSqlData(SQLiteDatabase db, String table, int type, int position, int image_thumb_id, int image_id, int pending) {

        ContentValues values = new ContentValues();
        values.put(TYPE, type);
        values.put(POSITION, position);
        values.put(IMAGE_THUMB_ID, image_thumb_id);
        values.put(IMAGE_ID, image_id);
        values.put(PENDING_REMOVE, pending);

        // skapa tabellrad
        db.insert(table, null, values);
        db.close(); // stäng anslutning

    }


    /**
     * Räknar antalet rader
     * @return
     */
    public static int getRowCount(SQLiteDatabase db, String table) {
        String countQuery = "SELECT  * FROM " + table;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    /**
     * Hämta sql-data
     * @return
     */
    public static void getTableData(SQLiteDatabase db, int[][] tableData, String table) {

        String query = "SELECT * FROM " + table;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null) {
                int row = 0;
                cursor.moveToFirst();
                do {
                    for (int col = 0; col < N_COLUMNS; col++) {
                        tableData[row][col] = cursor.getInt(col);
                    }
                    row++;

                } while (cursor.moveToNext());
            }
        }
        catch (Exception e) {}
    }


    /**
     *
     * @param db
     * @param table
     */
    public static void dropTable( SQLiteDatabase db, String table) {

        db.execSQL("DROP TABLE IF EXISTS " + table);
    }


    /**
     *
     * @param db
     * @param table
     */
    public static void clearTable(SQLiteDatabase db, String table) {

        db.execSQL("delete from " + table);

    }


    /**
     *
     * @param db
     * @param table
     * @param value
     * @param column
     * @param column_key
     * @param key
     */
    public static void substituteTableRowCol(SQLiteDatabase db, String table, int value, String column, String column_key, int key) {

        ContentValues cv = new ContentValues();
        cv.put(column,"" + value);
        db.update(table, cv, column_key + "=" + key, null);
    }


    /**
     *
     * @param db
     * @param table
     * @param columnName
     * @param id
     */
    public static void deleteTableRow(SQLiteDatabase db, String table, String columnName, int id) {

        db.execSQL("delete from " + table + " WHERE " + columnName + "=" + id);
    }

}
