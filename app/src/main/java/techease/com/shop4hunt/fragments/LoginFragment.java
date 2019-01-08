package techease.com.shop4hunt.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
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

import org.json.JSONArray;
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
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;
    View view;
    private boolean valid = false;
    private String strEmail;
    private String strPassword;
    String userID, strMarks, contestID, strResultDate;
    public static String FACEBOOK_URL = "https://www.facebook.com/shop4hunt/";
    public static String FACEBOOK_PAGE_ID = "367426056714023";
    public static String INSTAGRAM_URL = "https://www.instagram.com/shop4hunt/";


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

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragment(getActivity(), new ForgotPasswordFragment());
            }
        });
    }

    private void userLogin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.LOGIN
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                Log.d("resp", response);

                if (response.contains("200")) {
                    try {
                        alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject object = jsonObject.getJSONObject("data");

                        int id = object.getInt("id");
                        String check_login = object.getString("check_login");
                        GeneralUtils.putIntegerValueInEditor(getActivity(), "id", id);

                        if (check_login.equals("true")) {
                            GeneralUtils.connectFragment(getActivity(), new QuizFragment());
                        } else {
                              showAlert();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog != null)
                    alertDialog.dismiss();
                Toast.makeText(getActivity(), "you got some error", Toast.LENGTH_SHORT).show();
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
                params.put("password", strPassword);
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

    private void showAlert() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_layout);
        LinearLayout layoutInsta, layoutFb;
        TextView tvContestID, tvContestantID, tvResultDate;
        layoutFb = dialog.findViewById(R.id.dialog_layoutFb);
        layoutInsta = dialog.findViewById(R.id.dialog_layoutInsta);
        tvContestID = dialog.findViewById(R.id.dialog_tv_contest_id);
        tvContestantID = dialog.findViewById(R.id.dialog_tv_contestant_id);
        tvResultDate = dialog.findViewById(R.id.dialog_tv_result_date);
        tvContestID.setText("Contest ID = " + GeneralUtils.getContestID(getActivity()));
        tvContestantID.setText("Contestant ID = " + String.valueOf(GeneralUtils.getUserID(getActivity())));
        tvResultDate.setText("Result Date = " + GeneralUtils.getResultDate(getActivity()));

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
                intagramPageLoad(INSTAGRAM_URL, getActivity());
            }
        });
        dialog.show();
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


}
