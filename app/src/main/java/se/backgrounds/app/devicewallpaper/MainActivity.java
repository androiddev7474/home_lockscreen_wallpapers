package se.backgrounds.app.devicewallpaper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.IMAGE_THUMB_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.KEY_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.N_COLUMNS;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.PENDING_REMOVE_COL_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.POSITION;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TABLE_IMAGE_DATA;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TYPE;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.IMAGE_ID_COL_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.KEY_ID_COL_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.PENDING_REMOVE;

public class MainActivity extends AppCompatActivity implements TrashcanFragment.ClickInterface, ServiceChangeNotifier {

    public static final String MY_PREFS_NAME = "serviceState";
    public static final String MY_PREFS_NAME_TIMER = "timer";
    public static final String MY_PREFS_TIMER_VALUE = "timer_value";
    public static final String MY_PREFS_TIMER_BUTTON_VALUE = "timer_button_value";
    public static final String N_FAVOS_TO_BE_SAVED = "nToBeSaved";

    private SaveFragment saveFragment;
    private TrashcanFragment trashcanFragment;
    private HelpFragment helpFragment;
    private TimerFragment timerFragment;
    private FragmentManager fragmentManager;
    private IntroFragment introFragment;
    private FragmentTransaction fragmentTransaction;

    private ArrayList<CheckedFavorites> checkedImagesList = new ArrayList<>();
    private ArrayList <ImageData> imageDataList = new ArrayList<>();

    Intent mServiceIntent;
    private BackgroundLoaderService mBackgroundService;

    //private MyDataBaseHandler dBhandler;
    private SQLiteDBhandler dBhandler;
    BackgroundsPageAdapter backgroundsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //visas endast första gången
        introFragment = new IntroFragment();
        if (!getIntroFragmentShownState()) {

            introFragment.show(getSupportFragmentManager(), "introFragment");
            saveIntroFragmentShownState();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //dBhandler = new MyDataBaseHandler(this);
        dBhandler = new SQLiteDBhandler(this);
        backgroundsPageAdapter = new BackgroundsPageAdapter(getSupportFragmentManager(), this, checkedImagesList, imageDataList);
        ViewPager pager = findViewById(R.id.viewPager);
        pager.setAdapter(backgroundsPageAdapter);
        //pager.setOffscreenPageLimit(DeviceInfoAdapter.N_PAGES);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        mBackgroundService = new BackgroundLoaderService(this);
        mServiceIntent = new Intent(this, mBackgroundService.getClass());


        saveFragment = new SaveFragment();

        trashcanFragment = new TrashcanFragment();
        trashcanFragment.setClickInterface(this);
        fragmentManager = getSupportFragmentManager();

        helpFragment = new HelpFragment();

        timerFragment = new TimerFragment();
        timerFragment.setSwitchInterface(this);

        //dBhandler.dropTable("image_data_table");
        //dBhandler.clearTable(MyDataBaseHandler.TABLE_IMAGE_DATA);
        //SQLqueries.clearTable(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);

        /*for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setIcon(R.drawable.gallery);

            //tab.setText("");
        }*/

    }


    /**
     * Sparar tillståndet hos introfragment i form av en boolean
     */
    private void saveIntroFragmentShownState() {

        SharedPreferences.Editor editor = getSharedPreferences("IntroFragmentStatus", MODE_PRIVATE).edit();
        editor.putBoolean("introFragmentShown", true);
        editor.apply();
    }


    /**
     * Hämtar tillståndet hos introfragment - har det visats eller inte?
     * @return
     */
    private boolean getIntroFragmentShownState() {

        SharedPreferences prefs = getSharedPreferences("IntroFragmentStatus", MODE_PRIVATE);
        boolean hasBeenShown = prefs.getBoolean("introFragmentShown", false);

        return hasBeenShown;
    }

    @Override
    public void onConfirmTrash(boolean removeSelectedGallery, boolean removeSelectedFavos, boolean removeAllFavos) {

        Toast.makeText(this, "trash confirmed", Toast.LENGTH_SHORT).show();
        trashcanFragment.dismiss();

        if (removeSelectedGallery)
            removeSelectedGallery();
        if (removeSelectedFavos)
            removeFavorites();

    }

    private void removeSelectedGallery() {

        backgroundsPageAdapter.getImageGridBaseAdapter().onRemoveSelectedViews();
    }



    private void removeFavorites() {

        backgroundsPageAdapter.getImageGridBaseAdapter().notifyFavoritesChanged();

        int nRows = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        int[][] tableData = new int[nRows][N_COLUMNS];
        SQLqueries.getTableData(dBhandler.getReadableDatabase(), tableData, TABLE_IMAGE_DATA);

        for (int i = 0; i < tableData.length; i++) {

            if (tableData[i][PENDING_REMOVE_COL_ID] == 1)
                SQLqueries.deleteTableRow(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA, IMAGE_THUMB_ID, tableData[i][IMAGE_ID_COL_ID]);

        }
        backgroundsPageAdapter.getFavoImageGridBaseAdapter().notifyFavoriteChange();

    }


    public void lockCheckedListener(View v) {

        Toast.makeText(this, "locked", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.delete:

                Toast.makeText(this, "trashcan pressed", Toast.LENGTH_SHORT).show();
                trashcanFragment.show(fragmentManager, "trashcanFragment");

                return true;
            case R.id.save:

                saveFragment.show(fragmentManager, "saveFragment");

                return true;
            case R.id.help:

                Toast.makeText(this, "help pressed", Toast.LENGTH_SHORT).show();
                helpFragment.show(getSupportFragmentManager(), "helpFragment");
                return true;
            case R.id.rate:

                Toast.makeText(this, "rate app pressed", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.about:

                Toast.makeText(this, "about app pressed", Toast.LENGTH_SHORT).show();
                introFragment.show(getSupportFragmentManager(), "introFragment");
                return true;

            case R.id.timer:

                Bundle b = new Bundle();
                b.putBoolean("serviceState", getServiceState());
                timerFragment.setArguments(b);
                timerFragment.show(getSupportFragmentManager(), "timerFragment");
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback för SaveFragment
     * @param view
     */
    public void onClickOKbutton(View view) {

        setSQLitedata(); //spara undan favoritet i sqlite
        saveFragment.dismiss();
        backgroundsPageAdapter.getImageGridBaseAdapter().notifyFavoritesChanged(); //ändrade favoriter? uppdatera galleriet!!!

    }

    public void onOKImagesDisposal(View view) {


    }


    /**
     * Sparar undan valda bilder i sqlite - det som sparas undan är bildreferens, typ (låstskärm/hemskärm)
     */
    private void setSQLitedata() {

        //loopa listan och kontrollera om någon bild är favoriserad och på vilket sätt
        ArrayList <FavoriteHolder> favoList = new ArrayList<>();
        for (CheckedFavorites favorites: checkedImagesList) {

            int type = -1;
            if (favorites.getAll())
                type = 2;
            else if (favorites.getHome())
                type = 1;
            else if (favorites.getLocked())
                type = 0;

            if (type != -1) {
                int[] favoData = {type, favorites.getPosition(), favorites.getResID_thumb(), favorites.getResID()};
                favoList.add(new FavoriteHolder(favoData));
            }
        }

        int nRows = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        int[][] tableData = new int[nRows][N_COLUMNS];
        SQLqueries.getTableData(dBhandler.getReadableDatabase(), tableData, TABLE_IMAGE_DATA);

        if (tableData.length == 0) { // Om inga rader från början

            for (FavoriteHolder list : favoList) {

                SQLqueries.addSqlData(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA, list.getFavoriteData()[0], list.getFavoriteData()[1], list.getFavoriteData()[2], list.getFavoriteData()[3], 0);
                backgroundsPageAdapter.getFavoImageGridBaseAdapter().notifyFavoriteChange();
            }

        } else { // om det finns befintliga tabellrader

            for (FavoriteHolder list : favoList) {

                int[] data = list.getFavoriteData();

                //kontrollera om resID finns
                boolean doesExist = false;
                for (int i = 0; i < tableData.length; i++) {

                    if (tableData[i][IMAGE_ID_COL_ID] == data[2]) { // om resID finns, ja testa då nästa villkor. Vi kanske behöver uppdatera?

                        if (tableData[i][1] != data[0]) { //om type har ändrat - uppdatera då tabellen

                            SQLqueries.substituteTableRowCol(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA, data[0], TYPE, KEY_ID, tableData[i][KEY_ID_COL_ID]);
                            backgroundsPageAdapter.getFavoImageGridBaseAdapter().notifyFavoriteChange();
                        }
                        doesExist = true;
                    }
                }

                if (!doesExist) {
                    SQLqueries.addSqlData(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA, list.getFavoriteData()[0], list.getFavoriteData()[1], list.getFavoriteData()[2], list.getFavoriteData()[3], 0);
                    backgroundsPageAdapter.getFavoImageGridBaseAdapter().notifyFavoriteChange();
                }
            }

        }

        //läs in och kolla

        nRows = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        tableData = new int[nRows][N_COLUMNS];
        SQLqueries.getTableData(dBhandler.getReadableDatabase(), tableData, TABLE_IMAGE_DATA);
        String[] TableColumnNames = {KEY_ID, TYPE, POSITION, IMAGE_THUMB_ID, PENDING_REMOVE};
        StdOutTable.printTable(tableData, TableColumnNames);

    }


    class FavoriteHolder {

        private int[] favoriteData;

        public FavoriteHolder(int[] favoriteData) {

            this.favoriteData = favoriteData;
        }

        public int[] getFavoriteData() {

            return this.favoriteData;
        }

    }


    public void onDismissFragment(View view) {

        helpFragment.dismiss();
    }


    public void startStopService(boolean serviceOn) {

        if (serviceOn) {
            if (!isMyServiceRunning(mBackgroundService.getClass())) {
                mBackgroundService.restartServiceOnDestroy = true;
                startService(mServiceIntent);
                saveServiceState(true);
            }
        } else {
            mBackgroundService.restartServiceOnDestroy = false;
            stopService(mServiceIntent);
            saveServiceState(false);
        }
    }

    private void saveServiceState(boolean serviceOn) {

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("serviceSwitchState", serviceOn);
        editor.apply();
    }

    private boolean getServiceState() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean restoredSwitchState = prefs.getBoolean("serviceSwitchState", false);

        return restoredSwitchState;
    }

    /**
     *
     * @param serviceClass
     * @return
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service körs?", true+"");
                return true;
            }
        }
        Log.i ("Service körs?", false+"");
        return false;
    }



}
