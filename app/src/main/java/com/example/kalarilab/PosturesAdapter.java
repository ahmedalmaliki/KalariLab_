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

import java.util.Arrays;
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
    private TextView description;
    private int NUM_ITEMS ;
    private SessionManagement sessionManagement ;
    private String curr_uri= "";
    public PosturesAdapter(FragmentManager fragmentManager, Activity context, List<String> posturesTags, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

        this.posturesTags = posturesTags;
        this.context = context;
        sessionManagement = new SessionManagement(context);

    }


    List<String> descs = Arrays.asList(
            "GAJA VADIVU\nPhysical: Strengthens Deep core muscles, improves the arch of the foot.\nSubtle: Earth element. Grounding posture for confidence and inner strength.",
            "MATSYA VADIVU\nPhysical: Helps in single leg balance, strengthens paraspinal muscles, Glutes and Hamstrings.\nSubtle: Fire element.",
            "ASHWA VADIVU\nPhysical: Strengthens Quadriceps. Lengthens lateral line.\nSubtle: Water element. Determination and steadfastness.",
            "SIMHA VADIVU\nPhysical: Strengthen the back and leg muscles. Stabilises the shoulder muscles.\nSubtle: Water element. Direct linear movement.",
            "MAYOORA VADIVU\nPhysical: Helps in single leg balance, strengthens the arms, shoulders and back.\nSubtle: Fire element. Increases focus and poise.",
            "KUKKUDA VADIVU\nPhysical: Builds explosive energy ready to attack.\nSubtle: Softens the mind and creates effortless.",
            "NAAGA VADIVU\nPhysical: Strengthens back muscles, Quads and posterior deltoid; lengthens hip muscles.\nSubtle: Water element. Effortlessness and release.",
            "VYAGHRA VADIVU\nPhysical: [Tiger stance description needed].\nSubtle: [Tiger stance subtle quality].",
            "VARAAHA VADIVU\nPhysical: Gain strength for the lower body.\nSubtle: Water element. Improve focus and determination."
    );


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
        description = rowView.findViewById(R.id.description);
        getPostureTitleFromTag(posturesTags.get(position));
        getPostureImageFromTag(posturesTags.get(position));
        description.setText(descs.get(position));

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
