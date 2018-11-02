package techease.com.shop4hunt.networking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import techease.com.shop4hunt.models.loginDataModel.LoginResponseModel;
import techease.com.shop4hunt.models.signupDatamodel.SignupResponseModel;

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
                                               @Field("password") String password);

}
