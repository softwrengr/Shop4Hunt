package techease.com.shop4hunt.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import techease.com.shop4hunt.R;
import techease.com.shop4hunt.utils.AlertUtils;
import techease.com.shop4hunt.utils.Configuration;
import techease.com.shop4hunt.utils.GeneralUtils;
import techease.com.shop4hunt.utils.NetworkUtils;


public class HomeFragment extends Fragment {
    View view;
    WebView webView;
    Button btnPlay;
    android.support.v7.app.AlertDialog alertDialog;
    String url = "http://www.shop4hunt.com/";
    String strBtnText = "play contest";
    RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        apiCall();
        webView = view.findViewById(R.id.webView);
        btnPlay = view.findViewById(R.id.btn_play);
        relativeLayout = view.findViewById(R.id.background);
        initUI();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragment(getActivity(),new LoginSignupFragment());


            }
        });
        return view;
    }

    private void initUI() {


        if(NetworkUtils.isNetworkConnected(getActivity())){
            loadWebView();
        }
        else {
           showError();
        }

    }

    private void loadWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new HelloWebViewClient());
//        if (alertDialog == null) {
//            alertDialog = AlertUtils.createProgressDialog(getActivity());
//            alertDialog.show();
//        }
        webView.loadUrl(url);

        webView.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });
    }


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            if (alertDialog == null) {
//                alertDialog = AlertUtils.createProgressDialog(getActivity());
//                alertDialog.show();
//            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if (!NetworkUtils.isNetworkConnected(getActivity())) {
                view.setBackgroundColor(getActivity().getResources().getColor(R.color.black_overlay));
                showError();
            }

            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            super.onPageFinished(view, url);
        }
    }

    private void showError(){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setConfirmText("Refresh")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        GeneralUtils.connectFragment(getActivity(),new HomeFragment());
                        sweetAlertDialog.dismiss();
                    }
                })
                .setTitleText("Oops...")
                .setContentText("No internet connection!")
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return false;
                    }
                });

        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }


    public void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.ChangeText
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("true")){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject object = jsonArray.getJSONObject(0);
                        strBtnText = object.getString("text");
                        btnPlay.setText(strBtnText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    strBtnText = "play contest";
                    btnPlay.setText(strBtnText);
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

}
