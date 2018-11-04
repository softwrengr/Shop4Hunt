package techease.com.shop4hunt.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import techease.com.shop4hunt.R;
import techease.com.shop4hunt.models.loginDataModel.LoginResponseModel;
import techease.com.shop4hunt.models.signupDatamodel.SignupResponseModel;
import techease.com.shop4hunt.networking.ApiClient;
import techease.com.shop4hunt.networking.ApiInterface;
import techease.com.shop4hunt.utils.AlertUtils;
import techease.com.shop4hunt.utils.GeneralUtils;

public class SignupFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    View view;
    @BindView(R.id.tv_already_signin)
    TextView tvSignin;
    @BindView(R.id.et_userEmail)
    EditText etEmail;
    @BindView(R.id.et_UserPhone)
    EditText etPhone;
    @BindView(R.id.et_User_name)
    EditText etName;
    @BindView(R.id.et_userPassword)
    EditText etPassword;
    @BindView(R.id.btn_signup)
    Button btnSignup;
    String strName, strEmail, strPhone, strPassword;

    private boolean valid = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragmentWithBackStack(getActivity(), new LoginFragment());
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    userRegistration();
                }
            }
        });
    }

    private void userRegistration() {
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<SignupResponseModel> userLogin = services.userRegistration(strName,strPhone,strEmail, strPassword);
        userLogin.enqueue(new Callback<SignupResponseModel>() {
            @Override
            public void onResponse(Call<SignupResponseModel> call, Response<SignupResponseModel> response) {
                alertDialog.dismiss();
                if (response.body().getSuccess()) {
                    Toast.makeText(getActivity(), "signup done successfully", Toast.LENGTH_SHORT).show();
                    GeneralUtils.connectFragment(getActivity(), new LoginFragment());

                } else {
                    Toast.makeText(getActivity(), "you got some error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponseModel> call, Throwable t) {

            }
        });
    }

    private boolean validate() {
        valid = true;

        strName = etName.getText().toString();
        strPhone = etPhone.getText().toString();
        strEmail = etEmail.getText().toString();
        strPassword = etPassword.getText().toString();


        if (strName.isEmpty()) {
            etName.setError("enter a your name");
            valid = false;
        } else {
            etName.setError(null);
        }
        if (strPhone.isEmpty()) {
            etPhone.setError("enter your phone number");
            valid = false;
        } else {
            etPhone.setError(null);
        }
        if (strEmail.isEmpty()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }
        if (strPassword.isEmpty() || strPassword.length()<6) {
            etPassword.setError("Please enter a strong password");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }
}
