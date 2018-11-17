package techease.com.shop4hunt.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
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
    @BindView(R.id.et_userAddress)
    EditText etAddress;
    @BindView(R.id.btn_signup)
    Button btnSignup;

    String strName, strEmail, strPhone, strPassword, strResponse,strAddress;

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
        Call<SignupResponseModel> userLogin = services.userRegistration(strName, strPhone, strEmail,strAddress, strPassword);
        userLogin.enqueue(new Callback<SignupResponseModel>() {
            @Override
            public void onResponse(Call<SignupResponseModel> call, Response<SignupResponseModel> response) {
                alertDialog.dismiss();
                strResponse = response.body().getMessage();
                Log.d("user", response.body().getMessage());
                if (response.body().getMessage().equals("User successfully registered")) {

                    Toast.makeText(getActivity(), "User successfully Registered", Toast.LENGTH_SHORT).show();
                    GeneralUtils.connectFragment(getActivity(), new LoginFragment());

                } else if (response.body().getMessage().equals("Email Already Exist")) {
                    Toast.makeText(getActivity(), "User already exist with this email or phone", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "you got some error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponseModel> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(getActivity(), "User already exist with this email or phone", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        valid = true;

        strName = etName.getText().toString();
        strPhone = etPhone.getText().toString();
        strEmail = etEmail.getText().toString();
        strAddress = etAddress.getText().toString();
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
        if (strAddress.isEmpty()) {
            etAddress.setError("enter a your address");
            valid = false;
        } else {
            etEmail.setError(null);
        }
        if (strPassword.isEmpty() || strPassword.length() < 6) {
            etPassword.setError("Please enter a strong password");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }
}
