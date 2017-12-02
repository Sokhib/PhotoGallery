package com.example.aspirev7.photogallery;


import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        setHasOptionsMenu(true);
        updateItems();
        /*Intent intent = new Intent(getActivity(), PollService.class);
            getActivity().startService(intent);
        */
        // PollService.setServiceAlarm(getActivity(),true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        photoGalleryRecyclerView = view.findViewById(R.id.f_photo_gallery_recyclerView);
        photoGalleryRecyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photo_gallery_menu, menu);

            MenuItem searchItem = menu.findItem(R.id.menu_item_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            ComponentName name = getActivity().getComponentName();
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(name);
            searchView.setSearchableInfo(searchableInfo);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(getActivity()))
            toggleItem.setTitle(R.string.stop_polling);
        else toggleItem.setTitle(R.string.start_polling);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                //getActivity().onSearchRequested();
                getActivity().startSearch("nature", true, null, false);
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(FlickrFetchr.PREF_SEARCH_QUERY, null)
                        .commit();
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
                getActivity().invalidateOptionsMenu();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>> {
        /*
            *  About 100 pages of the book is left, aimed to finish it tonight,
            *  but got a toothache, time is about 2.30+ AM,
            *  have no idea WTF[rea]k I'm coding
        * */
        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... voids) {
            Activity activity = getActivity();
            if (activity == null) {
                return new ArrayList<>();
            }
            String query = PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
            if (query != null)
                return new FlickrFetchr().search(query);
            else return new FlickrFetchr().fetchItems();
        }
        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            super.onPostExecute(items);
            Toast.makeText(getActivity(), FlickrFetchr.total, Toast.LENGTH_SHORT).show();
            adapter = new MyAdapter(getActivity(), items);
            photoGalleryRecyclerView.setAdapter(adapter);
            photoGalleryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }
    public void updateItems() {
        new FetchItemsTask().execute();
    }

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "canceling notification");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, intentFilter, PollService.PERM_PRIVATE, null);
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mOnShowNotification);
    }
}