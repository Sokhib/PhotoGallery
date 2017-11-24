package com.example.aspirev7.photogallery;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    Fragment createFragment() {
        return new PhotoGalleryFragment();
    }
}
