package com.example.aspirev7.photogallery;


import android.support.v4.app.Fragment;

/**
 * Created by AspireV7 on 12/2/2017.
 */

public class PhotoPageActivity extends SingleFragmentActivity {
    @Override
    Fragment createFragment() {
        return new PhotoPageFragment();
    }

}
