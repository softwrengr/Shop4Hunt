package techease.com.shop4hunt.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import techease.com.shop4hunt.R;
import techease.com.shop4hunt.fragments.QuizFragment;
import techease.com.shop4hunt.utils.AlertUtils;
import techease.com.shop4hunt.utils.GeneralUtils;
import techease.com.shop4hunt.utils.NetworkUtils;

public class HomeActivity extends AppCompatActivity {
    WebView webView;
    Button btnPlay;
    android.support.v7.app.AlertDialog alertDialog;
    String url = "http://www.shop4hunt.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        webView = findViewById(R.id.webView);
        btnPlay = findViewById(R.id.btn_play);
        initUI();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initUI() {

        if (NetworkUtils.isNetworkConnected(this)) {
            loadWebView();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            alertDialog = AlertUtils.createProgressDialog(this);
            alertDialog.show();
        }
        webView.loadUrl(url);
    }


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (alertDialog == null) {
                alertDialog = AlertUtils.createProgressDialog(HomeActivity.this);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

