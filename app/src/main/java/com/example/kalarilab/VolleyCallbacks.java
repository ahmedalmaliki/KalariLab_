package com.example.kalarilab;

import com.example.kalarilab.Activities.Settings;
import com.example.kalarilab.Fragments.PremiumFragment;
import com.example.kalarilab.Fragments.ProfileFragment;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

public class VolleyCallbacks {
    private  VolleyCallbackMap volleyCallbackMap;
    private JSONObject response;
    private ProfileFragment profileFragment;
    private PremiumFragment premiumFragment;
    private Settings context;
    private SessionManagement sessionManagement ;

    public VolleyCallbacks( ProfileFragment ProfileFragment, VolleyCallbackMap volleyCallbackMap) {

        this.volleyCallbackMap = volleyCallbackMap;
        this.profileFragment = ProfileFragment;
        this.premiumFragment = premiumFragment;
    }

    public VolleyCallbacks(Settings context, VolleyCallbackMap volleyCallbackMap, SessionManagement sessionManagement) {

        this.volleyCallbackMap = volleyCallbackMap;
        this.context = context;
        this.sessionManagement = sessionManagement;
    }

    public VolleyCallbacks( ) {


    }
    public PremiumFragment getPremiumFragment() {
        return premiumFragment;
    }

    public void setPremiumFragment(PremiumFragment premiumFragment) {
        this.premiumFragment = premiumFragment;
    }

    public void onSuccess_ProfileFragment(JSONObject r) {

        response = r;


    }

    public static Map<String, String> ConvertJsonObject(JSONObject jsonObj) {
        return  new Gson().fromJson(jsonObj.toString(),  Map.class);
    }


    private void extractInfoFromResponse_Fragments_Stripe_costumerId(String response) {
        this.getPremiumFragment().getEphericalKey(response);


    }
    private void extractInfoFromResponse_Fragments_Stripe_ephericalId(String costumerId,String response) {

        this.getPremiumFragment().getClientSecret(costumerId,response);


    }
    public void onSuccess_EditInfoActivity(JSONObject r) {

        response = r;


        extractInfoFromResponse_Activities();
    }

    private void extractInfoFromResponse_Activities() {
        volleyCallbackMap.setMap(ConvertJsonObject(response));

        try {


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onSuccess_PremiumActivity(JSONObject r) {

        response = r;


        extractInfoFromResponse_Activities();
    }
    public void onSuccess_CostumerIdStripe(String response) {



        extractInfoFromResponse_Fragments_Stripe_costumerId(response);
    }
    public void onSuccess_EphericalIdStripe(String costumerID,String response) {



        extractInfoFromResponse_Fragments_Stripe_ephericalId(costumerID, response);
    }

}
