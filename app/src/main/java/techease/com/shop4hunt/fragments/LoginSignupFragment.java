package techease.com.shop4hunt.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import techease.com.shop4hunt.R;
import techease.com.shop4hunt.utils.GeneralUtils;


public class LoginSignupFragment extends Fragment {
    Button btnGoLogin,btnGoSignup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_signup, container, false);
        btnGoLogin = view.findViewById(R.id.btn_go_login);
        btnGoSignup = view.findViewById(R.id.btn_go_signup);

        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragment(getActivity(),new LoginFragment());
            }
        });
        btnGoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragment(getActivity(),new SignupFragment());
            }
        });
        return view;
    }
}
