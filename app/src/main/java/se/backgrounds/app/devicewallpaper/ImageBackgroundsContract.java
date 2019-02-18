package se.backgrounds.app.devicewallpaper;

/**
 * Created by stude on 2018-04-14.
 */

public class ImageBackgroundsContract {

    private ImageBackgroundsContract() {}

    public static class ImageDataTable {

        public static final String TABLE_IMAGE_DATA = "image_data_table"; // tabellnamn
        public static final String KEY_ID = "id"; // primärnyckel
        public static final String TYPE = "type"; // 0 = låstskärm 1 = hemskärm 2 = båda
        public static final String POSITION = "position"; //position i gridview - används inte nu
        public static final String IMAGE_THUMB_ID = "res_id_thumb"; // bildreferens
        public static final String IMAGE_ID = "res_id"; // bildreferens
        public static final String PENDING_REMOVE = "pending_remove"; // initieras med 0, antar 1 om bilden markeras för borttagning

        public static final int IMAGE_ID_COL_ID = 3;
        public static final int IMAGE_BG_ID_COL_ID = 4;
        public static final int TYPE_COL_ID = 1;
        public static final int PENDING_REMOVE_COL_ID = 5;
        public static final int KEY_ID_COL_ID = 0;

        public static final int N_COLUMNS = 6;

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_IMAGE_DATA +
                "("+ KEY_ID + " INTEGER PRIMARY KEY," + TYPE + " INTEGER," +
                POSITION + " INTEGER," + IMAGE_THUMB_ID + " INTEGER," + IMAGE_ID + " INTEGER," + PENDING_REMOVE + " INTEGER" +")";

    }

}
