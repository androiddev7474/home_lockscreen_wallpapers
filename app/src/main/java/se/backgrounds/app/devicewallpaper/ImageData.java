package se.backgrounds.app.devicewallpaper;

/**
 * Created by stude on 2018-04-09.
 */

public class ImageData {

    private int[] tableData;
    private boolean readyToBeRemoved;

    public ImageData(int[] tableData) {

        this.tableData = tableData;
    }


    public int getImageResID() {

        return this.tableData[3];
    }



    public void setReadyToBeRemoved(boolean readyToBeRemoved) {

        this.readyToBeRemoved = readyToBeRemoved;
    }

    public boolean getReadyToBeRemoved() {

        return this.readyToBeRemoved;
    }
}
