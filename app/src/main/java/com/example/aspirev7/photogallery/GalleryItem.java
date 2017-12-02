package com.example.aspirev7.photogallery;

/**
 * Created by AspireV7 on 11/24/2017.
 */

public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;
    private String mOwner;

    public String getOwner() {
        return mOwner;
    }
    public void setOwner(String mOwner) {
        this.mOwner = mOwner;
    }
    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getPhotoPageURL(){
        return "https://www.flickr.com/photos/" + mOwner + "/" + mId;
    }

}
