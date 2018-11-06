package techease.com.shop4hunt.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import techease.com.shop4hunt.R;
import techease.com.shop4hunt.utils.AlertUtils;
import techease.com.shop4hunt.utils.Configuration;
import techease.com.shop4hunt.utils.GeneralUtils;

public class ThanksFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    View view;
    String userID;
    Button btnFollowFB;
    public static String FACEBOOK_URL = "https://www.facebook.com/PyarBareyLamhy/";
    public static String FACEBOOK_PAGE_ID = "PyarBareyLamhy";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_thanks, container, false);
        btnFollowFB = view.findViewById(R.id.followFB);
        userID = String.valueOf(GeneralUtils.getUserID(getActivity()));
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        apiCall();

        btnFollowFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getActivity());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        return view;
    }

    public void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.CHECK+userID
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                if(response.contains("true")){
                    showSweetDialog();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("check", "true");
                return params;

            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    private void showSweetDialog(){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setConfirmText("Back")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                      //  GeneralUtils.connectFragment(getActivity(),new HomeFragment());
                        sweetAlertDialog.dismiss();
                    }
                })
                .setTitleText("Message")
                .setContentText("You have successfully play the contest")
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                        return false;
                    }
                });

        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    public String  getFacebookPageURL(Context context) {

        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL;
        }

    }
}
