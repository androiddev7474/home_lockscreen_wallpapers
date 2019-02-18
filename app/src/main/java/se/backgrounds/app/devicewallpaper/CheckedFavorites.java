package se.backgrounds.app.devicewallpaper;


/**
 * Created by stude on 2018-03-30.
 */

public class CheckedFavorites {

    private boolean locked;
    private boolean home;
    private boolean all;
    private boolean lockedEnabled, homeEnabled, allEnabled;
    private int position;
    private int resID_thumb;
    private int resID;

    public CheckedFavorites(boolean locked, boolean home, int position, int resID_thumb, int resID) {

        this.locked = locked;
        this.home = home;
        this.position = position;
        this.resID_thumb = resID_thumb;
        this.resID = resID;

        lockedEnabled = true;
        homeEnabled = true;
        allEnabled = true;
    }


    protected void setPosition(int position) {

        this.position = position;

    }

    protected void setLocked(boolean locked) {

        this.locked = locked;
    }

    protected void setHome(boolean home) {

        this.home = home;
    }

    protected void setAll(boolean all) {

        this.all = all;
    }

    protected void setLockedEnabled(boolean lockedEnabled) {

        this.lockedEnabled = lockedEnabled;
    }

    protected void setHomeEnabled(boolean homeEnabled) {

        this.homeEnabled = homeEnabled;
    }

    protected void setAllEnabled(boolean allEnabled) {

        this.allEnabled = allEnabled;
    }



    protected void setResID_thumb(int resID_thumb) {

        this.resID_thumb = resID_thumb;
    }

    protected void setResID(int resID) {

        this.resID = resID;
    }


    protected int getPosition() {

        return this.position;
    }

    protected boolean getLocked() {

        return this.locked;
    }

    protected boolean getHome() {

        return this.home;
    }

    protected boolean getAll() {

        return this.all;
    }

    protected boolean getLockedEnabled() {

        return this.lockedEnabled;
    }

    protected boolean getHomeEnabled() {

        return this.homeEnabled;
    }

    protected boolean getAllEnabled() {

        return this.allEnabled;
    }


    protected int getResID_thumb() {

        return this.resID_thumb;
    }

    protected int getResID() {

        return this.resID;
    }


}
