package androiddev.milnaa.com.milnaa.service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by LiuXiaocong on 9/19/2016.
 */
public interface EntRegisterService {
    @POST("Service.svc/EnterPriseRegister")
    Observable<ResponseBody> register(@Body RequestBody params);
}
