package com.zlb.http;

import com.zlb.http.result.StaffMsg;
import com.zlb.httplib.HttpResponse;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 *
 * 尽量分出去，可以核心的写在这里。
 *
 *
 *
 *
 * ApiService 对象最终会被在retrofit.create(ApiService.class)后被动态代理
 * 比如 HttpCall.getApiService().getWeather(url,"深圳")
 * <p>
 * Retrofit关心的就是method和它的参数args，接下去Retrofit就会用Java反射获取到getWeather方法的注解信息，
 * 配合args参数，创建一个ServiceMethod对象，ServiceMethod就像是一个中央处理器，传入Retrofit对象和
 * Method对象，调用各个接口和解析器，最终生成一个Request，包含api 的域名、path、http请求方法、请求头、
 * 是有body、是否是multipart等等。最后返回一个Call对象，Retrofit2中Call接口的默认实现是OkHttpCall，
 * 它默认使用OkHttp3作为底层http请求client。
 *
 *
 * Created by zenglb on 2017/3/17.
 */
@Deprecated
public interface ApiService {

//    @GET("https://q.qnmlgb.tech/w/api/articles")
//    Observable<HttpResponse<ArticlesResult>> getArticles(@Query("_sub_tab") String tabType);



}

