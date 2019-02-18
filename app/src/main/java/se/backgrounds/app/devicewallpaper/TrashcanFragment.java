package se.backgrounds.app.devicewallpaper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by stude on 2018-04-09.
 */

public class TrashcanFragment extends DialogFragment {

    private ClickInterface clickInterface;

    private View trashFragView;
    private Button okButton;
    private CheckBox selectedGalleryImgsCB, selectedFavoImgsCB, allFavoImgsCB;
    private boolean removeGalleryImgs, removeFavoriteImgs, removeAllFavoriteImgs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        trashFragView = inflater.inflate(R.layout.trashcan_fragment_content_layout, container, false);
        okButton = (Button) trashFragView.findViewById(R.id.trashConfirmOK_id);
        selectedGalleryImgsCB = (CheckBox) trashFragView.findViewById(R.id.delete_marked_gallery_imgs);
        selectedFavoImgsCB = (CheckBox) trashFragView.findViewById(R.id.delete_marked_favorites);
        allFavoImgsCB = (CheckBox) trashFragView.findViewById(R.id.delete_all_favorites);

        initCheckBoxListeners();

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                clickInterface.onConfirmTrash(removeGalleryImgs, removeFavoriteImgs, removeAllFavoriteImgs);
            }

        });

        return trashFragView;
    }

    interface ClickInterface {

        public void onConfirmTrash(boolean removeGalleryImgs, boolean removeFavoImgs, boolean removeAllFavoImgs);

    }


    protected void setClickInterface(ClickInterface clickInterface) {

        this.clickInterface = clickInterface;
    }


    private void initCheckBoxListeners() {


        //if (selectedGalleryImgsCB.isShown()) {
            selectedGalleryImgsCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked)
                        removeGalleryImgs = true;
                    else
                        removeGalleryImgs = false;
                }
            });
        //}


        //if (selectedFavoImgsCB.isShown()) {
            selectedFavoImgsCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked)
                        removeFavoriteImgs = true;
                    else
                        removeFavoriteImgs = false;
                }
            });
        //}

        //if (allFavoImgsCB.isShown()) {
            allFavoImgsCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked)
                        removeAllFavoriteImgs = true;
                    else
                        removeAllFavoriteImgs = false;
                }
            });
        //}


    }

}
