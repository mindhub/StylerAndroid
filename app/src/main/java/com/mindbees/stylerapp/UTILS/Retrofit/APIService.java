package com.mindbees.stylerapp.UTILS.Retrofit;





import com.mindbees.stylerapp.UI.Models.Ethnicity.ModelEthnicity;
import com.mindbees.stylerapp.UI.Models.Tribes.ModelTribes;
import com.mindbees.stylerapp.UTILS.Urls;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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


}
