package com.example.kalarilab.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.VolleyCallbacks;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PremiumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PremiumFragment extends Fragment implements View.OnClickListener {
    SessionManagement sessionManagement;
    BottomSheetDialog bottomSheetDialog;
    //declare stripe
    private com.stripe.android.Stripe stripe;
    private PaymentSheet paymentSheet;
    private OkHttpClient httpClient;
    String costumerID = "";
    String ephericalKey = "";
    String clientSecret;
    String amount;
    Button monthlyActivateButton;
    Button annualActivateBtn;
    String chosenPlan;
    ProgressBar progressBar;
    Map<String, String> map = new HashMap<>();
    VolleyCallbacks volleyCallbacks;
    View view;
    //    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PremiumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PremiumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PremiumFragment newInstance(String param1, String param2) {
        PremiumFragment fragment = new PremiumFragment();
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
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_premium, container, false);
        initHooks();
        bindings();
        return view;
    }

    private void bindings() {
        monthlyActivateButton.setOnClickListener(this);
        annualActivateBtn.setOnClickListener(this);

    }

    private void initHooks() {
        volleyCallbacks = new VolleyCallbacks();
        volleyCallbacks.setPremiumFragment(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        monthlyActivateButton = (Button) view.findViewById(R.id.monthlyActivateBtn);
        annualActivateBtn = (Button) view.findViewById(R.id.annualActivateBtn);
        sessionManagement = new SessionManagement(getActivity());
        PaymentConfiguration.init(getActivity(), sessionManagement.returnPublished_Key());
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResults(paymentSheetResult);
        });


    }

    private void pickPaymentMethod() {

         bottomSheetDialog = new BottomSheetDialog(
                getActivity(), R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet,
                (ConstraintLayout) view.findViewById(R.id.bottomSheetContainer)
        );
        bottomSheetView.findViewById(R.id.stripeOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                runStripePayment();
            }
        });
        bottomSheetView.findViewById(R.id.googleOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                runStripePayment();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void runStripePayment() {
        bottomSheetDialog.hide();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    costumerID = object.getString("id");
                    volleyCallbacks.onSuccess_CostumerIdStripe(costumerID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer "+ sessionManagement.returnSecret_Key());
                return header;
            }
        }
                ;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void onPaymentResults(PaymentSheetResult paymentSheetResult) {
        progressBar.setVisibility(View.GONE);
        if (paymentSheetResult instanceof  PaymentSheetResult.Completed){
            switch (chosenPlan){
                case "m":
                    monthlyActivateButton.setText(R.string.current_plan);
                    break;
                case  "a":
                    annualActivateBtn.setText(R.string.current_plan);
            }
        }else {
            Toast.makeText(getActivity(), "Payment has failed!", Toast.LENGTH_SHORT).show();
        }
    }
    public void getEphericalKey(String costumerID) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    ephericalKey = object.getString("id");

                    volleyCallbacks.onSuccess_EphericalIdStripe(costumerID, ephericalKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer "+ sessionManagement.returnSecret_Key());
                header.put("Stripe-Version", "2019-10-08");

                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", costumerID);
                return params;
            }
        }
                ;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void getClientSecret(String costumerID, String ephericalKey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    clientSecret = object.getString("client_secret");
                    paymentFlow();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer "+ sessionManagement.returnSecret_Key());

                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", costumerID);
                params.put("amount", amount+"00");
                params.put("currency", "usd");
                params.put("automatic_payment_methods[enabled]", "true");

                return params;
            }
        }
                ;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void paymentFlow() {
        try {

            paymentSheet.presentWithPaymentIntent(clientSecret, new PaymentSheet.Configuration("KalariLab"
                    , new PaymentSheet.CustomerConfiguration(costumerID, ephericalKey)));
        }catch (Exception e){
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.monthlyActivateBtn:
                amount = "10";
                chosenPlan = "m";

                break;
            case  R.id.annualActivateBtn:
                amount = "100";
                chosenPlan = "a";
                break;
        }

        pickPaymentMethod();
    }
}