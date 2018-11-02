package techease.com.shop4hunt.fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
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

import techease.com.shop4hunt.R;
import techease.com.shop4hunt.utils.AlertUtils;
import techease.com.shop4hunt.utils.GeneralUtils;
import techease.com.shop4hunt.utils.NetworkUtils;


public class HomeFragment extends Fragment {
    WebView webView;
    Button btnPlay;
    android.support.v7.app.AlertDialog alertDialog;
    String url = "http://www.shop4hunt.com/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        webView = view.findViewById(R.id.webView);
        btnPlay = view.findViewById(R.id.btn_play);
        initUI();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragmentWithBackStack(getActivity(),new LoginSignupFragment());


            }
        });
        return view;
    }

    private void initUI() {

        if (NetworkUtils.isNetworkConnected(getActivity())) {
            loadWebView();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Internet Issue");
            builder.setMessage("you have lost your connection please try again");
            builder.setCancelable(false);
            builder.show();
        }

    }

    private void loadWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new HelloWebViewClient());
        if (alertDialog == null) {
            alertDialog = AlertUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
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
            if (alertDialog == null) {
                alertDialog = AlertUtils.createProgressDialog(getActivity());
                alertDialog.show();
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
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
}
