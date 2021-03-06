package techease.com.shop4hunt.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    String userID, strMarks,contestID,strResultDate;
    public static String FACEBOOK_URL = "https://www.facebook.com/shop4hunt/";
    public static String FACEBOOK_PAGE_ID = "367426056714023";
    public static String INSTAGRAM_URL = "https://www.instagram.com/shop4hunt/";
    LinearLayout layoutInsta, layoutFb;
    TextView tvContestID,tvContestantID,tvResultDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_thanks, container, false);
        layoutFb = view.findViewById(R.id.layoutFb);
        layoutInsta = view.findViewById(R.id.layoutInsta);
        tvContestantID = view.findViewById(R.id.tv_contestant_id);
        tvContestID = view.findViewById(R.id.tv_contest_id);
        tvResultDate = view.findViewById(R.id.tv_result_date);

        contestID = GeneralUtils.getContestID(getActivity());
        userID = String.valueOf(GeneralUtils.getUserID(getActivity()));
        strMarks = String.valueOf(GeneralUtils.getUserScore(getActivity()));
        tvContestantID.setText("ContestantID = "+userID);
        tvContestID.setText("Contest ID = "+contestID);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        getContestResult();
        apiCallForResult();
        apiCall();

        Log.d("count",String.valueOf(GeneralUtils.getUserScore(getActivity())));


        layoutFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getActivity());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });

        layoutInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                //Intent facebookUrl = intagramPageLoad(INSTAGRAM_URL,getActivity());
                intagramPageLoad(INSTAGRAM_URL, getActivity());

            }
        });
        return view;
    }

    public void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.CHECK + userID
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                if (response.contains("true")) {
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

    public void apiCallForResult() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.Result
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                if (response.contains("true")) {
                    Toast.makeText(getActivity(), "Result will be announce by going live on facebook or instagrame pages", Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "you got some error");
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("contest_id", contestID);
                params.put("user_id", userID);
                params.put("result", strMarks+"/10");
                return params;

            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    public String getFacebookPageURL(Context context) {

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

    public void intagramPageLoad(String url, Context context) {
        PackageManager pm = context.getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                final String username = url.substring(url.lastIndexOf("/") + 1);
                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void showSweetDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
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

    private void getContestResult(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.ResultDate
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("200")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        strResultDate =  jsonObject1.getString("result_date");
                        tvResultDate.setText("Result Date = "+strResultDate);
                        GeneralUtils.putStringValueInEditor(getActivity(),"date",strResultDate);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error","you got some error");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
}
