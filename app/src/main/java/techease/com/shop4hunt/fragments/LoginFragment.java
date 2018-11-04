package techease.com.shop4hunt.fragments;

import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import techease.com.shop4hunt.R;
import techease.com.shop4hunt.models.loginDataModel.LoginResponseModel;
import techease.com.shop4hunt.networking.ApiClient;
import techease.com.shop4hunt.networking.ApiInterface;
import techease.com.shop4hunt.utils.AlertUtils;
import techease.com.shop4hunt.utils.Configuration;
import techease.com.shop4hunt.utils.GeneralUtils;

public class LoginFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_new_user)
    TextView tvNewUser;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    View view;
    private boolean valid = false;
    private String strEmail;
    private String strPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    userLogin();
                }
            }
        });

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragmentWithBackStack(getActivity(), new SignupFragment());
            }
        });
    }

    private void userLogin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://choicegeek.com/mcqs/public/api/login"
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                Log.d("resp",response);
                if (response.contains("true")) {

                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject object = jsonObject.getJSONObject("data");

                            int id = object.getInt("id");
                            Log.d("resp",String.valueOf(id));
                            GeneralUtils.connectFragment(getActivity(),new QuizFragment());
                            GeneralUtils.putIntegerValueInEditor(getActivity(),"id",id);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else  {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    Toast.makeText(getActivity(), "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog != null)
                    alertDialog.dismiss();
                Toast.makeText(getActivity(), "Email or password is incorrect", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", strEmail);
                params.put("password",strPassword);
                return params;

            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }


    private boolean validate() {
        valid = true;

        strEmail = etEmail.getText().toString();
        strPassword = etPassword.getText().toString();

        if (strEmail.isEmpty()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }
        if (strPassword.isEmpty()) {
            etPassword.setError("Please enter a valid password");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }


}
