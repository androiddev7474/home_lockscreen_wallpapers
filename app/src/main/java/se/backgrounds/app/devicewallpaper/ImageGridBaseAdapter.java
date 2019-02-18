package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.IMAGE_ID_COL_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.N_COLUMNS;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TABLE_IMAGE_DATA;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TYPE_COL_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.PENDING_REMOVE_COL_ID;

/**
 * Björn Hallström
 * 2018-04-14
 * Version 1
 * Adapter för galleriet (alla bilder) - bilderna i form av miniatyrer visas i en GridView
 *
 */

public class ImageGridBaseAdapter extends BaseAdapter {

    //Android API klasser
    private Context context;
    private LayoutInflater layoutInflater;

    //primitiver
    private static final int N_IMAGES = 23;
    private int[][] imageResArray;
    private static final int COL_ID_THUMBS = 0;
    private static final int COL_ID_IMAGES = 1;

    //egna klasser
    //private MyDataBaseHandler dBhandler;
    private SQLiteDBhandler dBhandler;

    //listor
    private ArrayList <CheckedFavorites> checkedImagesList;

    public ImageGridBaseAdapter(Context context, ArrayList <CheckedFavorites> checkedImagesList) {

        this.context = context;
        this.checkedImagesList = checkedImagesList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //dBhandler = new MyDataBaseHandler(context);
        dBhandler = new SQLiteDBhandler(context);
        //int nRows = dBhandler.getRowCount(TABLE_IMAGE_DATA);


        createImageData();

        initcheckedImages();

        onFavoriteDisableViews();
    }

    private void initcheckedImages() {

        for (int i = 0; i < N_IMAGES; i++) {

            checkedImagesList.add(new CheckedFavorites(false, false, 0, imageResArray[i][0], imageResArray[i][1]));

        }

    }



    private void createImageData() {

        TypedArray typedThumbsArray = context.getResources().obtainTypedArray(R.array.images_thumbs);
        int[] resIDthumbs = new int[N_IMAGES];
        for (int i = 0; i < N_IMAGES; i++) {
            resIDthumbs[i] = typedThumbsArray.getResourceId(i, 0);
        }
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.images);
        int[] resID = new int[N_IMAGES];
        for (int i = 0; i < N_IMAGES; i++) {
            resID[i] = typedArray.getResourceId(i, 0);
        }

        imageResArray = new int[N_IMAGES][2];
        for (int i = 0; i < N_IMAGES; i++) {

           imageResArray[i][COL_ID_THUMBS] = resIDthumbs[i];
           imageResArray[i][COL_ID_IMAGES] = resID[i];
        }

    }

    @Override
    public int getCount() {
        return N_IMAGES;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        protected TextView text;
        protected ImageView imageView;
        protected CheckBox lockedCB, homeCB, allCB;
        protected int viewPosition;

    }

    protected void savedStateNotifier() {

        Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.image_grid_content_layout, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.thumb_image_id);
            holder.lockedCB = (CheckBox)convertView.findViewById(R.id.locked_id);
            holder.homeCB = (CheckBox)convertView.findViewById(R.id.home_id);
            holder.allCB = (CheckBox)convertView.findViewById(R.id.all_id);
            initListeners(holder);
            convertView.setTag(holder);
            Log.d("new holderinstance", holder.toString());

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.viewPosition = position;

        holder.imageView.setImageResource(imageResArray[position][0]);

        holder.lockedCB.setChecked(checkedImagesList.get(position).getLocked());
        holder.homeCB.setChecked(checkedImagesList.get(position).getHome());
        holder.allCB.setChecked(checkedImagesList.get(position).getAll());

        holder.lockedCB.setEnabled(checkedImagesList.get(position).getLockedEnabled());
        holder.homeCB.setEnabled(checkedImagesList.get(position).getHomeEnabled());
        holder.allCB.setEnabled(checkedImagesList.get(position).getAllEnabled());

        return convertView;
    }


    /**
     * Om användaren vill avmarkera samtliga markerade bilder i galleriet
     */
    public void onRemoveSelectedViews() {

        //hämta tabell sqlite
        int nRows = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        int[][] tableData = new int[nRows][N_COLUMNS];
        SQLqueries.getTableData(dBhandler.getReadableDatabase(), tableData, TABLE_IMAGE_DATA);

        for (CheckedFavorites checkedFavorites : checkedImagesList) {

            int resIDgallery = checkedFavorites.getResID_thumb();
            boolean isFavorite = false;
            for (int row = 0; row < tableData.length; row++) {
                int resIDfavo = tableData[row][IMAGE_ID_COL_ID];

                if (resIDfavo == resIDgallery) {
                    isFavorite = true;
                }
            }

            if (!isFavorite) {
                checkedFavorites.setLockedEnabled(true);
                checkedFavorites.setHomeEnabled(true);
                checkedFavorites.setAllEnabled(true);
                checkedFavorites.setLocked(false);
                checkedFavorites.setHome(false);
                checkedFavorites.setAll(false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * När användaren valt favoriter eller tar bort favoriter ändrar checkboxarna utseende och sätts till
     * disabled/enabled, beroende på situation.
     */
    private void onFavoriteDisableViews() {

        //int[][] tableData = dBhandler.getSqlTableData(TABLE_IMAGE_DATA);
        int nRows = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        int[][] tableData = new int[nRows][N_COLUMNS];
        SQLqueries.getTableData(dBhandler.getReadableDatabase(), tableData, TABLE_IMAGE_DATA);

        for (int row = 0; row < tableData.length; row++) {

            int resIDsql = tableData[row][IMAGE_ID_COL_ID];
            int typeSql = tableData[row][TYPE_COL_ID];
            int pendingRemove = tableData[row][PENDING_REMOVE_COL_ID];

            for (CheckedFavorites checkedList: checkedImagesList) {

                int resIDthumb = checkedList.getResID_thumb();

                if (resIDthumb == resIDsql && pendingRemove == 0) { //om lägger till favorit

                    setCheckBoxesDisabled(checkedList, typeSql);

                } else if (resIDthumb == resIDsql && pendingRemove == 1) { //om tar bort favorit (checkboxarna åter klickbara)

                    setCheckBoxesEnabled(checkedList);
                }
            }

        }
    }


    /**
     *
     * @param checkedList aktuell lista som innehåller galleriets bild-id samt checkboxvärden
     * @param type (0 = låstskärm; 1 = hemskärm; 2 = bådadera
     */
    private void setCheckBoxesDisabled(CheckedFavorites checkedList, int type) {

        switch (type) {

            case 0:
                checkedList.setLocked(true);
                checkedList.setHome(false);
                checkedList.setAll(false);
                break;

            case 1:
                checkedList.setLocked(false);
                checkedList.setHome(true);
                checkedList.setAll(false);
                break;


            case 2:
                checkedList.setLocked(true);
                checkedList.setHome(true);
                checkedList.setAll(true);
                break;
        }

        checkedList.setLockedEnabled(false);
        checkedList.setHomeEnabled(false);
        checkedList.setAllEnabled(false);
    }


    /**
     *
     * @param checkedList checkedList aktuell lista som innehåller galleriets bild-id samt checkboxvärden
     */
    private void setCheckBoxesEnabled(CheckedFavorites checkedList) {

        checkedList.setLocked(false);
        checkedList.setHome(false);
        checkedList.setAll(false);

        checkedList.setLockedEnabled(true);
        checkedList.setHomeEnabled(true);
        checkedList.setAllEnabled(true);

    }


    /**
     * Anropas då bilder tas bort/sparas sanmt när appen startar
     */
    public void notifyFavoritesChanged() {

        onFavoriteDisableViews();
        notifyDataSetChanged();
    }


    /**
     *
     * @param holder
     */
    private void initListeners(final ViewHolder holder) {

        holder.lockedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

               if (holder.lockedCB.isShown()) {

                   if (buttonView.isChecked()) {

                       if (holder.homeCB.isChecked()) {
                           holder.allCB.setChecked(true);
                           holder.lockedCB.setEnabled(false);
                           holder.homeCB.setEnabled(false);
                           checkedImagesList.get(holder.viewPosition).setAll(true);
                           checkedImagesList.get(holder.viewPosition).setHomeEnabled(false);
                           checkedImagesList.get(holder.viewPosition).setLockedEnabled(false);
                       }
                       checkedImagesList.get(holder.viewPosition).setLocked(true);
                       checkedImagesList.get(holder.viewPosition).setResID_thumb(imageResArray[holder.viewPosition][0]);

                   } else {
                       checkedImagesList.get(holder.viewPosition).setLocked(false);
                       checkedImagesList.get(holder.viewPosition).setResID_thumb(-1);
                   }

               }
           }
       }
        );


        holder.homeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

               if (holder.homeCB.isShown()) {

                   if (buttonView.isChecked()) {

                       if (holder.lockedCB.isChecked()) {
                           holder.allCB.setChecked(true);
                           holder.lockedCB.setEnabled(false);
                           holder.homeCB.setEnabled(false);
                           checkedImagesList.get(holder.viewPosition).setAll(true);
                           checkedImagesList.get(holder.viewPosition).setHomeEnabled(false);
                           checkedImagesList.get(holder.viewPosition).setLockedEnabled(false);
                       }

                       checkedImagesList.get(holder.viewPosition).setHome(true);
                       checkedImagesList.get(holder.viewPosition).setResID_thumb(imageResArray[holder.viewPosition][0]);
                   } else {

                       checkedImagesList.get(holder.viewPosition).setHome(false);
                       checkedImagesList.get(holder.viewPosition).setResID_thumb(-1);
                   }
               }

           }
       }
        );


        holder.allCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

             @Override
             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                 if (holder.allCB.isShown()) {

                     if (buttonView.isChecked()) {

                         holder.lockedCB.setEnabled(false);
                         holder.lockedCB.setChecked(true);
                         holder.homeCB.setEnabled(false);
                         holder.homeCB.setChecked(true);
                         checkedImagesList.get(holder.viewPosition).setHomeEnabled(false);
                         checkedImagesList.get(holder.viewPosition).setLockedEnabled(false);
                         checkedImagesList.get(holder.viewPosition).setLocked(true);
                         checkedImagesList.get(holder.viewPosition).setHome(true);
                         checkedImagesList.get(holder.viewPosition).setAll(true);
                         checkedImagesList.get(holder.viewPosition).setResID_thumb(imageResArray[holder.viewPosition][0]);
                     } else {

                         holder.lockedCB.setEnabled(true);
                         holder.lockedCB.setChecked(false);
                         holder.homeCB.setEnabled(true);
                         holder.homeCB.setChecked(false);

                         checkedImagesList.get(holder.viewPosition).setAll(false);
                         checkedImagesList.get(holder.viewPosition).setHome(false);
                         checkedImagesList.get(holder.viewPosition).setLocked(false);
                         checkedImagesList.get(holder.viewPosition).setHomeEnabled(true);
                         checkedImagesList.get(holder.viewPosition).setLockedEnabled(true);
                         checkedImagesList.get(holder.viewPosition).setResID_thumb(-1);
                     }

                 }
             }
         }
        );

    }

}
