package com.example.kalarilab.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.kalarilab.Models.AdminPanelModel;
import com.example.kalarilab.R;
import com.example.kalarilab.RecyclerViewAdapter;
import com.example.kalarilab.ViewModels.AdminPanelViewModel;
import com.r0adkll.slidr.Slidr;

import org.json.JSONException;

public class AdminPanelActivity extends AppCompatActivity  {
    private SwipeRefreshLayout swipeRefreshLayout;

    private AdminPanelViewModel adminPanelViewModel;
    private static final String TAG = "AdminPanelActivityDebug";
    private  RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        initHooks(); //To assign variables
        bindings();
        observeDate();
        Slidr.attach(this);

    }

    private void observeDate() {
        adminPanelViewModel.getPanelModel().observe(this, new Observer<AdminPanelModel>() {
            @Override
            public void onChanged(AdminPanelModel adminPanelModel) {

               recyclerView = findViewById(R.id.recyclerView);
                Log.d(TAG, adminPanelModel.getUrersIds().toString());
                recyclerView.setAdapter(new RecyclerViewAdapter(adminPanelModel, AdminPanelActivity.this));
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



            }
        });
    }

    private void initHooks() {

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        adminPanelViewModel = new AdminPanelViewModel();
        adminPanelViewModel.setActivity(this); //passing the activity context to the main view model
        try {
            adminPanelViewModel.init();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindings() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //swiping the top of the page to refresh the page
            @Override
            public void onRefresh() {
                refreshCurrActivity();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        }

    private void refreshCurrActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }



}