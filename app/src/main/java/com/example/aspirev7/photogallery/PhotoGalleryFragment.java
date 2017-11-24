package com.example.aspirev7.photogallery;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGalleryFragment extends Fragment {


    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    RecyclerView photoGalleryRecyclerView;
    MyAdapter adapter;
    private static final String TAG = "PhotoGalleryFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        new FetchItemsTask().execute();

    }
      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        photoGalleryRecyclerView = view.findViewById(R.id.f_photo_gallery_recyclerView);
        photoGalleryRecyclerView.setHasFixedSize(true);
        return view;
    }




    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<GalleryItem>>{
        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... voids) {
            return  new FlickrFetchr().fetchItems();
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            super.onPostExecute(items);
            Log.e(TAG, "Items " + items.get(0).getmUrl());
            adapter = new MyAdapter(getActivity(),items);
            photoGalleryRecyclerView.setAdapter(adapter);
            photoGalleryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }
}
