package com.example.kalarilab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kalarilab.Activities.PostViewer;
import com.example.kalarilab.Models.AdminPanelModel;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private AdminPanelModel adminPanelModel;
    private Context context;
    private ViewHolder mHolder;
    private static final String TAG = "RecyclerAdapterDebug";




    public RecyclerViewAdapter(AdminPanelModel adminPanelModel, Context context) {
        this.adminPanelModel = adminPanelModel;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView fullName, LevelAndChallenge;
        LinearLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.userFullName);
            LevelAndChallenge = itemView.findViewById(R.id.challenge);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int pos = getAdapterPosition();

                        // check if item still exists
                        if (pos != RecyclerView.NO_POSITION) {
                            moveToPostViewerActivity(pos);
                        }
                    }catch (Exception e){
                        Log.d(TAG, e.getMessage());

                    }
                }
            });

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mHolder = holder;
        holder.fullName.setText(adminPanelModel.getUsersFullNames().get(position));
        holder.LevelAndChallenge.setText(adminPanelModel.getLevels().get(position)+"/"+ adminPanelModel.getChallenges().get(position));
//        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                moveToPostViewerActivity();
//            }
//        });
    }

    private void moveToPostViewerActivity(int pos) {
        Intent intent = new Intent(context, PostViewer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("adminPanelModel", adminPanelModel);
        intent.putExtra("position", pos);
        context.startActivity(intent);
        ((Activity)context).finish();

    }

    @Override
    public int getItemCount() {
        return adminPanelModel.getUsersFullNames().size();
    }
}
