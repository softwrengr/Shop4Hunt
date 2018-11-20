package techease.com.shop4hunt.networking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import techease.com.shop4hunt.models.loginDataModel.LoginResponseModel;
import techease.com.shop4hunt.models.signupDatamodel.SignupResponseModel;
import techease.com.shop4hunt.models.verifyCodeDataModel.NewPasswordDataModel;
import techease.com.shop4hunt.models.verifyCodeDataModel.ResetPasswordDataModel;
import techease.com.shop4hunt.models.verifyCodeDataModel.VerifyCode;
import techease.com.shop4hunt.models.verifyCodeDataModel.VerifyDetailModel;

/**
 * Created by eapple on 29/08/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> userLogin(@Field("email") String email,
                                       @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<SignupResponseModel> userRegistration( @Field("name") String name,
                                                @Field("phone") String phone,
                                                @Field("email") String email,
                                                @Field("address") String address,
                                               @Field("password") String password);


    @FormUrlEncoded
    @POST("forgot-password")
    Call<ResetPasswordDataModel> resetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("verify")
    Call<VerifyCode> verifyCode(@Field("email") String email,
                                @Field("verification_code") String code);

    @FormUrlEncoded
    @POST("change-password")
    Call<NewPasswordDataModel> changePassword(@Field("password") String password,
                                              @Field("api_token") String token,
                                              @Field("password_confirmation") String confirm);

}
