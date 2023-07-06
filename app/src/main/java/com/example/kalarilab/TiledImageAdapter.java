package com.example.kalarilab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.C;

import java.util.List;

public class TiledImageAdapter extends RecyclerView.Adapter<TiledImageAdapter.ViewHolder> {

    private List<Drawable> tiles;
    private Activity activity;
    private final static String TAG = "TiledImageAdapterDebug";
    private int screenHeight;
    private Drawable drawable;

    public TiledImageAdapter(List<Drawable> tiles, Activity activity) {
        this.tiles = tiles;
        this.activity = activity;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;


        public ViewHolder(View itemView, int screenHeight) {
            super(itemView);

            imageView = itemView.findViewById(R.id.tile_image_view);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tile_image_view, parent, false);
        return new ViewHolder(itemView, screenHeight);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {
        drawable = tiles.get(position);
        holder.imageView.setImageDrawable(drawable);

}

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        if (drawable != null) {
            drawable.setCallback(null);
            drawable = null;
        }
    }
}
