package com.example.aspirev7.photogallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AspireV7 on 11/24/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    ArrayList<GalleryItem> items = new ArrayList<>();
    Context context;

    public MyAdapter(Context context, ArrayList<GalleryItem> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //holder.galleryImage.setImageResource(); TODO:GET IMAGE
        Picasso.with(context).load(items.get(position).getmUrl()).into(holder.galleryImage);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView galleryImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            galleryImage = itemView.findViewById(R.id.galleryListImage);
        }
    }


}
