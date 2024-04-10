package trungndph39729.fpoly.lab5.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {

    private ApiServices requestInterface;

    public HttpRequest(){
        requestInterface = new Retrofit.Builder()
                .baseUrl(ApiServices.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiServices.class);
    }

    public ApiServices callAPI(){
        return requestInterface;
    }
}
