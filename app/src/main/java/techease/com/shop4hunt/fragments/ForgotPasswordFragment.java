package techease.com.shop4hunt.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import techease.com.shop4hunt.R;
import techease.com.shop4hunt.models.verifyCodeDataModel.NewPasswordDataModel;
import techease.com.shop4hunt.models.verifyCodeDataModel.ResetPasswordDataModel;
import techease.com.shop4hunt.models.verifyCodeDataModel.VerifyCode;
import techease.com.shop4hunt.models.verifyCodeDataModel.VerifyDetailModel;
import techease.com.shop4hunt.networking.ApiClient;
import techease.com.shop4hunt.networking.ApiInterface;
import techease.com.shop4hunt.utils.AlertUtils;
import techease.com.shop4hunt.utils.Configuration;
import techease.com.shop4hunt.utils.GeneralUtils;


public class ForgotPasswordFragment extends Fragment {
    @BindView(R.id.et_sendEmail)
    EditText etSendEmail;
    @BindView(R.id.btn_sendEmail)
    Button btnSendEmail;
    @BindView(R.id.et_verifyCode)
    EditText etVerifyCode;
    @BindView(R.id.btn_verifyCode)
    Button btnVerify;
    @BindView(R.id.et_newPassword)
    EditText etSetNewPassword;
    @BindView(R.id.btn_changePassword)
    Button btnChangePassword;
    @BindView(R.id.layoutVerification)
    LinearLayout layoutVerify;
    @BindView(R.id.layoutSendEmail)
    LinearLayout layoutSendEmail;
    @BindView(R.id.layoutChangePassword)
    LinearLayout layoutChangePassword;

    String strResetPassword, strVerifycode,strNewPassword,email;
    Boolean valid = false, verify = false,check =false;
    android.support.v7.app.AlertDialog alertDialog;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
        initUI();
        return  view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendEmail()) {
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    resetPassword();

                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()) {
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    verifyCode();
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changePassword()) {
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    setNewPassword();
                }
            }
        });

    }

    private void resetPassword() {
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        final Call<ResetPasswordDataModel> resetPassword = services.resetPassword(strResetPassword);
        resetPassword.enqueue(new Callback<ResetPasswordDataModel>() {
            @Override
            public void onResponse(Call<ResetPasswordDataModel> call, Response<ResetPasswordDataModel> response) {
                alertDialog.dismiss();
                if (response.body().getSuccess()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    layoutSendEmail.setVisibility(View.GONE);
                    layoutVerify.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getActivity(), "something went wrong please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordDataModel> call, Throwable t) {
                Toast.makeText(getActivity(), "something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //networking call for verify code
    private void verifyCode() {
        email = GeneralUtils.getEmail(getActivity());
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        final Call<VerifyCode> verifyCode = services.verifyCode(email,strVerifycode);
        verifyCode.enqueue(new Callback<VerifyCode>() {
            @Override
            public void onResponse(Call<VerifyCode> call, Response<VerifyCode> response) {
                alertDialog.dismiss();
                if (response.body().getResponseCode()==200) {
                    String token = response.body().getData().getApiToken();
                    GeneralUtils.putStringValueInEditor(getActivity(),"token",token);
                    layoutVerify.setVisibility(View.GONE);
                    layoutChangePassword.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getActivity(), "something went wrong please try again", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<VerifyCode> call, Throwable t) {
                Toast.makeText(getActivity(), "something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //networking call for set new Password
    private void setNewPassword() {
        String apiToken = GeneralUtils.getToken(getActivity());
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<NewPasswordDataModel> setNewPassword = services.changePassword(strNewPassword,apiToken, strNewPassword);
        setNewPassword.enqueue(new Callback<NewPasswordDataModel>() {
            @Override
            public void onResponse(Call<NewPasswordDataModel> call, Response<NewPasswordDataModel> response) {
                alertDialog.dismiss();
                if(response.body().getSuccess()){
                    GeneralUtils.connectFragment(getActivity(),new LoginFragment());
                }
                else {
                    Toast.makeText(getActivity(), "something went wrong please try again", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<NewPasswordDataModel> call, Throwable t) {
                Toast.makeText(getActivity(), "something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean sendEmail() {
        valid = true;
        strResetPassword = etSendEmail.getText().toString().trim();
        GeneralUtils.putStringValueInEditor(getActivity(),"email",strResetPassword);

        if (strResetPassword.isEmpty()) {
            etSendEmail.setError("you enter a wrong email");
            valid = false;
        }
        return valid;
    }

    private boolean verify() {
        verify = true;
        strVerifycode = etVerifyCode.getText().toString().trim();

        if (strVerifycode.isEmpty()) {
            etSendEmail.setError("you enter a wrong code");
            verify = false;
        }
        return verify;
    }

    private boolean changePassword(){
        check = true;
        strNewPassword = etSetNewPassword.getText().toString().trim();

        if (strNewPassword.isEmpty()) {
            etSetNewPassword.setError("please set a strong password");
            check = false;
        }
        return check;
    }
}
