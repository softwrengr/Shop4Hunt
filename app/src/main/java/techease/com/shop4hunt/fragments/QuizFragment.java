package techease.com.shop4hunt.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import techease.com.shop4hunt.R;
import techease.com.shop4hunt.utils.AlertUtils;
import techease.com.shop4hunt.utils.Configuration;
import techease.com.shop4hunt.utils.GeneralUtils;

public class QuizFragment extends Fragment implements RewardedVideoAdListener {
    android.support.v7.app.AlertDialog alertDialog;
    View view;
    @BindView(R.id.btnNext)
    Button btnNext;

    @BindView(R.id.question)
    TextView tvQuestion;
    @BindView(R.id.tv_answerOne)
    TextView tvAnswerOne;
    @BindView(R.id.tv_answerTwo)
    TextView tvAnswerTwo;
    @BindView(R.id.tv_answerThree)
    TextView tvAnswerThree;
    @BindView(R.id.tv_answerFour)
    TextView tvAnswerFour;
    @BindView(R.id.radio_one)
    RadioButton radioButtonOne;
    @BindView(R.id.radio_two)
    RadioButton radioButtonTwo;
    @BindView(R.id.radio_three)
    RadioButton radioButtonThree;
    @BindView(R.id.radio_four)
    RadioButton radioButtonFour;
    RewardedVideoAd mRewardedVideoAd;
    int check = 0;
    int next = 1;
    public static int count = 0;
    String strCorrectAnswer, strUserAnswer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quiz, container, false);

        MobileAds.initialize(getActivity(), "ca-app-pub-3940256099942544~3347511713");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        showAlertDialog();
        apiCall(check);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonOne.setChecked(false);
                radioButtonTwo.setChecked(false);
                radioButtonThree.setChecked(false);
                radioButtonFour.setChecked(false);

                if (next == 2) {
                    loadVideo();
                    showAlertDialog();
                    apiCall(check);
                    radioButtonEnabled();
                } else if (next == 3) {
                    loadVideo();
                    showAlertDialog();
                    apiCall(check);
                    radioButtonEnabled();
                } else if (next == 4) {
                    loadVideo();
                    showAlertDialog();
                    apiCall(check);
                    radioButtonEnabled();
                } else if (next == 5) {
                    loadVideo();
                    showAlertDialog();
                    apiCall(check);
                    radioButtonEnabled();
                } else if (next == 6) {
                    GeneralUtils.connectFragment(getActivity(), new ThanksFragment());
                }
            }
        });
    }

    private void loadVideo() {
        loadRewardedVideoAd();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    public void apiCall(final int checking) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.MCQ
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("200")) {
                    alertDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (checking == 0) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            String question = jsonObject1.getString("question");
                            String strOptionA = jsonObject1.getString("option_a");
                            String strOptionB = jsonObject1.getString("option_b");
                            String strOptionC = jsonObject1.getString("option_c");
                            String strOptionD = jsonObject1.getString("option_q");
                            strCorrectAnswer = jsonObject1.getString("answer");

                            setQuestions(question, strOptionA, strOptionB, strOptionC, strOptionD, strCorrectAnswer);
                            check++;
                            next++;
                        } else if (checking == 1) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                            String question = jsonObject1.getString("question");
                            String strOptionA = jsonObject1.getString("option_a");
                            String strOptionB = jsonObject1.getString("option_b");
                            String strOptionC = jsonObject1.getString("option_c");
                            String strOptionD = jsonObject1.getString("option_q");
                            strCorrectAnswer = jsonObject1.getString("answer");

                            setQuestions(question, strOptionA, strOptionB, strOptionC, strOptionD, strCorrectAnswer);
                            check++;
                            next++;
                        } else if (checking == 2) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(2);
                            String question = jsonObject1.getString("question");
                            String strOptionA = jsonObject1.getString("option_a");
                            String strOptionB = jsonObject1.getString("option_b");
                            String strOptionC = jsonObject1.getString("option_c");
                            String strOptionD = jsonObject1.getString("option_q");
                            strCorrectAnswer = jsonObject1.getString("answer");

                            setQuestions(question, strOptionA, strOptionB, strOptionC, strOptionD, strCorrectAnswer);
                            check++;
                            next++;
                        } else if (checking == 3) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(3);
                            String question = jsonObject1.getString("question");
                            String strOptionA = jsonObject1.getString("option_a");
                            String strOptionB = jsonObject1.getString("option_b");
                            String strOptionC = jsonObject1.getString("option_c");
                            String strOptionD = jsonObject1.getString("option_q");
                            strCorrectAnswer = jsonObject1.getString("answer");

                            setQuestions(question, strOptionA, strOptionB, strOptionC, strOptionD, strCorrectAnswer);
                            check++;
                            next++;
                        } else if (checking == 4) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(4);
                            String question = jsonObject1.getString("question");
                            String strOptionA = jsonObject1.getString("option_a");
                            String strOptionB = jsonObject1.getString("option_b");
                            String strOptionC = jsonObject1.getString("option_c");
                            String strOptionD = jsonObject1.getString("option_q");
                            strCorrectAnswer = jsonObject1.getString("answer");

                            setQuestions(question, strOptionA, strOptionB, strOptionC, strOptionD, strCorrectAnswer);
                            check++;
                            next++;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    private void setQuestions(String question, final String option_a, final String option_b, final String option_c, final String option_d, String correctAnswer) {
        tvQuestion.setText(question);
        tvAnswerOne.setText(option_a);
        tvAnswerTwo.setText(option_b);
        tvAnswerThree.setText(option_c);
        tvAnswerFour.setText(option_d);
        final String realAnswer = correctAnswer;


        radioButtonOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strUserAnswer = "option_a";
                    radioButtonTwo.setEnabled(false);
                    radioButtonThree.setEnabled(false);
                    radioButtonFour.setEnabled(false);
                    contestResult(realAnswer, strUserAnswer);
                }
            }
        });
        radioButtonTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strUserAnswer = "option_b";
                    radioButtonOne.setEnabled(false);
                    radioButtonThree.setEnabled(false);
                    radioButtonFour.setEnabled(false);
                    contestResult(realAnswer, strUserAnswer);
                }
            }
        });
        radioButtonThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strUserAnswer = "option_c";
                    radioButtonTwo.setEnabled(false);
                    radioButtonOne.setEnabled(false);
                    radioButtonFour.setEnabled(false);
                    contestResult(realAnswer, strUserAnswer);
                }
            }
        });
        radioButtonFour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strUserAnswer = "option_q";
                    radioButtonTwo.setEnabled(false);
                    radioButtonThree.setEnabled(false);
                    radioButtonOne.setEnabled(false);
                    contestResult(realAnswer, strUserAnswer);

                }
            }
        });
    }

    private void showAlertDialog() {
        if (alertDialog == null) {
            alertDialog = AlertUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
    }

    private void contestResult(String answer, String userAnswer) {
        Log.d("resultError", answer);
        Log.d("resultError", userAnswer);

        if (userAnswer.equals(answer)) {
            count += 2;
            Toast.makeText(getActivity(), String.valueOf(count), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), String.valueOf(count), Toast.LENGTH_SHORT).show();
        }
    }

    private void radioButtonEnabled() {
        radioButtonOne.setEnabled(true);
        radioButtonTwo.setEnabled(true);
        radioButtonThree.setEnabled(true);
        radioButtonFour.setEnabled(true);
    }


}
