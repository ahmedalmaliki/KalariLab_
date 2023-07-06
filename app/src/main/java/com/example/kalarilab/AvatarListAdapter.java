package com.example.kalarilab;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

public class AvatarListAdapter  {
    private Context mContext;
    private View view;
    private int resource;
    public AvatarListAdapter(@NonNull Context context, int resource, @NonNull int[] objects) {
        //super(context, resource, Collections.singletonList(objects));
        mContext = context;
        this.resource = resource;

    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        int picture =  getItem(position);
//        LayoutInflater inflater= LayoutInflater.from(mContext);
//        convertView  = inflater.inflate(resource, parent, false);
//        ImageButton row1 = (ImageButton) convertView.findViewById(R.id.leftMenu);
//        ImageButton row2 = (ImageButton) convertView.findViewById(R.id.rightMenu);
//        if (position % 2 > 0) {
//            row1.setImageResource(picture);
//        }
//        else row2.setImageResource(picture);
//
//        return view;
//    }
//
//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Nullable
//    @Override
////    public int getItem(int position) {
////
////    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
}
