package com.example.kalarilab;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.example.kalarilab.Fragments.PostureFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class PosturesAdapter extends  androidx.viewpager2.adapter.FragmentStateAdapter  {
    private List<String> posturesTags;
    private Activity context;

    private boolean doNotifyDataSetChangedOnce = false;
    private View rowView;
    private final static String TAG = "PostureAdapterDebug";
    private ImageView postureImage;
    private TextView title;
    private int NUM_ITEMS ;
    private SessionManagement sessionManagement ;
    private String curr_uri= "";
    public PosturesAdapter(FragmentManager fragmentManager, Activity context, List<String> posturesTags, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

        this.posturesTags = posturesTags;
        this.context = context;
        sessionManagement = new SessionManagement(context);

    }




    public void getPostureTitleFromTag(String keyTag){
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {

                if(!keyTag.contains("Silhouette")){
                FirebaseDatabase.getInstance().getReference("PosturesTitle").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            for(DataSnapshot ds: snapshot.getChildren()){
                                if(Objects.equals(ds.getKey(), keyTag)){
                                    title.setText((CharSequence) ds.getValue());
                                }
                            }
                        }catch (Exception e){
                            Log.d(TAG, "No Postures");
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else {
                    FirebaseDatabase.getInstance().getReference("PosturesSilhouetteTitles").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                for(DataSnapshot ds: snapshot.getChildren()){
                                    if(Objects.equals(ds.getKey(), keyTag)){
                                        title.setText((CharSequence) ds.getValue());
                                    }
                                }
                            }catch (Exception e){
                                Log.d(TAG, "No Postures");
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }
        //////


    }
    public void getPostureImageFromTag(String keyTag) {
        getImageFromDB(keyTag);

    }

    private void getImageFromDB(String keyTag) {

        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!keyTag.contains("Silhouette")){
                    FirebaseDatabase.getInstance().getReference("Postures").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                for(DataSnapshot ds: snapshot.getChildren()){
                                    if(Objects.equals(ds.getKey(), keyTag)){
                                        Glide
                                                .with(context)
                                                .load(ds.getValue()) // pass the image url
                                                .into(postureImage);
                                    }
                                }
                            }catch (Exception e){
                                Log.d(TAG, "No Postures");
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {

                    FirebaseDatabase.getInstance().getReference("PosturesSilhouette").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {

                                for(DataSnapshot ds: snapshot.getChildren()){

                                    if(Objects.equals(ds.getKey(), keyTag)){
                                        Glide
                                                .with(context)
                                                .load(ds.getValue()) // pass the image url
                                                .into(postureImage);
                                    }

                                }
                            }catch (Exception e){
                                Log.d(TAG, "No Postures");
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }


    }



    private void setUpFragmentLayout(int position) {
        Log.d(TAG, posturesTags.toString());
        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.fragment_posture, null, true);
        title = rowView.findViewById(R.id.PostureTitle);
        postureImage = rowView.findViewById(R.id.PostureImage);
        getPostureTitleFromTag(posturesTags.get(position));
        getPostureImageFromTag(posturesTags.get(position));
    }








    public void setNUM_ITEMS(int NUM_ITEMS) {
        doNotifyDataSetChangedOnce = true;
        this.NUM_ITEMS = NUM_ITEMS;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        setUpFragmentLayout(position);
        return PostureFragment.newInstance("0", "postureInstance", rowView);

    }

    @Override
    public int getItemCount() {
        if (doNotifyDataSetChangedOnce) {
            doNotifyDataSetChangedOnce = false;
            notifyDataSetChanged();
        }

        return NUM_ITEMS;
    }
}
