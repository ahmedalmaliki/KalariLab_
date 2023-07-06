package com.example.kalarilab;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GenderAdapter extends BaseAdapter {
    private String[] array;

    public GenderAdapter(String[] array) {
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int position) {
        return array[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
