package com.mindbees.stylerapp.UTILS.Retrofit;





import com.mindbees.stylerapp.UI.Models.Ethnicity.ModelEthnicity;
import com.mindbees.stylerapp.UI.Models.Fblogin.ModelFblogin;
import com.mindbees.stylerapp.UI.Models.Login.ModelLogin;
import com.mindbees.stylerapp.UI.Models.ModelForgotpassword.Modelforgot;
import com.mindbees.stylerapp.UI.Models.Register.ModelRegister;
import com.mindbees.stylerapp.UI.Models.Tribes.ModelTribes;
import com.mindbees.stylerapp.UI.Models.update_profile.ModelUpdateProfile;
import com.mindbees.stylerapp.UTILS.Urls;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Created by tony on 14/06/2016.
 */
public interface APIService {
    @Headers("Oakey:stylapp@XYZ")
    @GET(Urls.ETHNICITY_URL)
    Call<ModelEthnicity>getethnic(@QueryMap Map<String,String>params);
    @Headers("Oakey:stylapp@XYZ")
    @FormUrlEncoded
    @POST(Urls.TRIBES_URL)
    Call<ModelTribes>gettribes(@FieldMap HashMap<String,String>params);
    @Headers("Oakey:stylapp@XYZ")
    @FormUrlEncoded
    @POST(Urls.REGISTER_URL)
    Call<ModelRegister>register(@FieldMap HashMap<String,String>params);
    @Headers(("Oakey:stylapp@XYZ"))
    @FormUrlEncoded
    @POST(Urls.FBREGISTRATION)
    Call<ModelRegister>fbregister(@FieldMap HashMap<String,String>params);
    @Headers("Oakey:stylapp@XYZ")
    @FormUrlEncoded
    @POST(Urls.FB_SIGN_IN_URL)
    Call<ModelFblogin>fblogin(@FieldMap HashMap<String,String>params);
    @Multipart
    @POST(Urls.FILE_UPLOAD)
    Call<ResponseBody>postImage(@Part MultipartBody.Part image, @Part("user_id")RequestBody userid);
    @Headers("Oakey:stylapp@XYZ")
    @FormUrlEncoded
    @POST(Urls.UPDATE_PROFILE)
    Call<ModelUpdateProfile>updateprofile(@FieldMap HashMap<String,String>params);
    @Headers("Oakey:stylapp@XYZ")
    @FormUrlEncoded
    @POST(Urls.LOGIN_URL)
    Call<ModelLogin>login(@FieldMap HashMap<String,String>params);
    @Headers("Oakey:stylapp@XYZ")
    @FormUrlEncoded
    @POST(Urls.FORGOT_PASSWORD)
    Call<Modelforgot>forgotpass(@FieldMap HashMap<String,String>params);
}
