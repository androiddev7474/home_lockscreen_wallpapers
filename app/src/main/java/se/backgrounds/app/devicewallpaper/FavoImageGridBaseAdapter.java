package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.IMAGE_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.IMAGE_THUMB_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.IMAGE_ID_COL_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.KEY_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.N_COLUMNS;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.PENDING_REMOVE;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.PENDING_REMOVE_COL_ID;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.POSITION;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TABLE_IMAGE_DATA;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TYPE;
import static se.backgrounds.app.devicewallpaper.ImageBackgroundsContract.ImageDataTable.TYPE_COL_ID;


/**
 * Björn Hallström
 * 2018-04-14
 * Version 1
 * Adapter för favoriter - precis som i galleriet för alla bilder visas favoriter i en GridView
 *
 */

public class FavoImageGridBaseAdapter extends BaseAdapter {

    //primitiver
    private int[][] tableData;

    //egna klasser
    //private MyDataBaseHandler dBhandler;
    private SQLiteDBhandler dBhandler;

    //Android API klasser
    private LayoutInflater inflater;
    private Context context;

    public FavoImageGridBaseAdapter(Context context) {

        this.context = context;
        //this.dBhandler = new MyDataBaseHandler(context);
        this.dBhandler = new SQLiteDBhandler(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //tableData = dBhandler.getSqlTableData(TABLE_IMAGE_DATA);
        int nRows = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        tableData = new int[nRows][N_COLUMNS];
        SQLqueries.getTableData(dBhandler.getReadableDatabase(), tableData, TABLE_IMAGE_DATA);

    }


    @Override
    public int getCount() {

        //int count = dBhandler.getRowCount(TABLE_IMAGE_DATA);
        int count = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }


    /**
     * Klass som håller vyer samt vyposition
     */
    private static class ViewHolder {
        protected TextView text;
        protected ImageView imageV, lockedV, homeV;
        protected CheckBox traschCB;
        protected int viewPosition;
    }



    /**
     * Uppdatering av viewer och förändrat tabelldata
     */
    public void notifyFavoriteChange() {

        //tableData = dBhandler.getSqlTableData(TABLE_IMAGE_DATA);
        int nRows = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        tableData = new int[nRows][N_COLUMNS];
        SQLqueries.getTableData(dBhandler.getReadableDatabase(), tableData, TABLE_IMAGE_DATA);
        notifyDataSetChanged();
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.favo_image_grid_content_layout, parent, false);
            holder = new ViewHolder();
            holder.imageV = (ImageView)convertView.findViewById(R.id.thumb_favo_image_id);
            holder.traschCB = (CheckBox)convertView.findViewById(R.id.trashcan_favo_id);
            holder.lockedV = (ImageView)convertView.findViewById(R.id.locked_img_id);
            holder.homeV = (ImageView)convertView.findViewById(R.id.home_img_id);
            convertView.setTag(holder);
            initListeners(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.viewPosition = position;

        int resID = tableData[position][IMAGE_ID_COL_ID];
        if (resID > 0) {
            holder.imageV.setImageResource(resID);
        } else {
            //holder.imageView.setImageResource(R.drawable.cities1_thumb);
        }

        //kontrollerar om den aktuella favoriten är föremål för att tas bort
        boolean checked = false;
        if (tableData[position][PENDING_REMOVE_COL_ID] == 1)
            checked = true;
        holder.traschCB.setChecked(checked);

        //sätter vy för checkboxarna beroende på val av låstskärm/hemskärm eller båda
        setTypeView(holder, position);

        return convertView;
    }


    /**
     * sätter rätt view för hemskärm/låstskärm
     * @param holder
     * @param position
     */
    private void setTypeView(ViewHolder holder, int position) {

        switch (tableData[position][TYPE_COL_ID]) {

            case 0:
                holder.lockedV.setImageResource(R.drawable.locked);
                holder.lockedV.setVisibility(View.VISIBLE);
                holder.homeV.setVisibility(View.GONE);
                break;

            case 1:
                holder.homeV.setImageResource(R.drawable.home);
                holder.homeV.setVisibility(View.VISIBLE);
                holder.lockedV.setVisibility(View.GONE);
                break;

            case 2:
                holder.lockedV.setImageResource(R.drawable.locked);
                holder.homeV.setImageResource(R.drawable.home);
                holder.lockedV.setVisibility(View.VISIBLE);
                holder.homeV.setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     * lyssnare för checkbox där användaren markerar de bilder densamme vill ta bort
     * @param holder
     */
    private void initListeners(final FavoImageGridBaseAdapter.ViewHolder holder) {

        holder.traschCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (holder.traschCB.isShown()) {
                    if (isChecked) {

                        setSqlChecked(holder, 1);
                        print();

                    } else {

                        setSqlChecked(holder, 0);
                        print();
                    }
                }
            }
        });
    }


    /**
     * hjälpmetod - markerad eller avmarkerad checkbox sparas i sqlite
     * @param holder
     * @param checked
     */
    private void setSqlChecked(ViewHolder holder, int checked) {

        int imageID = tableData[holder.viewPosition][3];
        //dBhandler.substituteTableRowCol(TABLE_IMAGE_DATA, checked, PENDING_REMOVE, IMAGE_ID, imageID);
        SQLqueries.substituteTableRowCol(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA, checked, PENDING_REMOVE, IMAGE_THUMB_ID, imageID);

        notifyFavoriteChange();
    }


    /**
     * Utskrift av sqlite-data
     */
    private void print() {

        //int[][] tableData = dBhandler.getSqlTableData(MyDataBaseHandler.TABLE_IMAGE_DATA);
        int nRows = SQLqueries.getRowCount(dBhandler.getReadableDatabase(), TABLE_IMAGE_DATA);
        tableData = new int[nRows][N_COLUMNS];
        SQLqueries.getTableData(dBhandler.getReadableDatabase(), tableData, TABLE_IMAGE_DATA);
        String[] TableColumnNames = {KEY_ID, TYPE, POSITION, IMAGE_THUMB_ID, IMAGE_ID, PENDING_REMOVE};
        StdOutTable.printTable(tableData, TableColumnNames);
    }

}
