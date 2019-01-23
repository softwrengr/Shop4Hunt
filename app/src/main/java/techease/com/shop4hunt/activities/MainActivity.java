package techease.com.shop4hunt.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import techease.com.shop4hunt.R;
import techease.com.shop4hunt.fragments.ForgotPasswordFragment;
import techease.com.shop4hunt.fragments.HomeFragment;
import techease.com.shop4hunt.fragments.LoginFragment;
import techease.com.shop4hunt.fragments.LoginSignupFragment;
import techease.com.shop4hunt.utils.GeneralUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(GeneralUtils.getOTP(this)){
            GeneralUtils.connectFragment(this, new ForgotPasswordFragment());
        }
        else {
            GeneralUtils.connectFragment(MainActivity.this,new HomeFragment());
        }


    }
}
