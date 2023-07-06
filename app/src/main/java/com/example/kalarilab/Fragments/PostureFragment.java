package com.example.kalarilab.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.kalarilab.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostureFragment extends Fragment {
    private ImageView postureImage;
    private TextView postureTitle;
    private TextView postureDescription;
    private View rowView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostureFragment() {
        // Required empty public constructor
    }
    public PostureFragment(View rowView) {
        // Required empty public constructor
        this.rowView = rowView;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostureFragment newInstance(String param1, String param2, View rowView) {
        PostureFragment fragment = new PostureFragment(rowView);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = rowView;
        postureImage = (ImageView) view.findViewById(R.id.PostureImage);
        postureTitle = (TextView) view.findViewById(R.id.PostureTitle);
        postureDescription = (TextView) view.findViewById(R.id.description);
        return view;
    }
}