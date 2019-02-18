package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by stude on 2018-03-30.
 * Björn Hallström
 * Version 1
 * Adapter för de olika tabbarna eller rättare sagt sidorna. Varje sida är ett Fragment. I Denna version finns
 * två sidor/Fragment (1) Galleri för samtliga bilder (2) galleri för favoriter
 *
 */

public class BackgroundsPageAdapter extends FragmentPagerAdapter {

    private ImageGridFragment imageGridFragment;
    private FavoriteImagesFragment favoriteImagesFragment;
    private Context context;


    public BackgroundsPageAdapter(FragmentManager fm, Context context, ArrayList<CheckedFavorites> checkedImagesList, ArrayList <ImageData> imageDataList) {

        super(fm);
        this.context = context;

        try {
            favoriteImagesFragment = FavoriteImagesFragment.newInstance("themes", checkedImagesList);
            favoriteImagesFragment.initFavoAdapter(context);

            imageGridFragment = ImageGridFragment.newInstance("imagegallery");
            imageGridFragment.initGalleryAdapter(context, checkedImagesList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected FavoImageGridBaseAdapter getFavoImageGridBaseAdapter() {

        return favoriteImagesFragment.getFavoAdapter();
    }

    protected ImageGridBaseAdapter getImageGridBaseAdapter() {

        return imageGridFragment.getImageGridAdapter();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return imageGridFragment;
            case 1:
                return favoriteImagesFragment;
            default:
                return imageGridFragment;

        }
    }

    @Override
    public int getCount() {

        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return context.getString(R.string.gallery);
                //return context.getString(R.string.device_tab);
            case 1:
                return context.getString(R.string.favorites);
                //return context.getString(R.string.os_tab);
            default:
                return null;
        }
    }
}
