package com.mml.drc.retrofit_interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/6/25.
 * 修改时间：2018/6/25 10:48
 */

public class UserResult {
    private int status;//状态0失败，1成功
    private String msg;//说明
    private String exception;//异常
    private String timestamp;//时间

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Deprecated
    public interface UserInterface1 {
        //@GET("Users/GetHasUser?userName={name}&pwd={pw}")
        @GET("Users/GetHasUser")
        Call<UserResult> getCall(@Query("userName") String name, @Query("pwd") String pw);
        // 注解里传入 网络请求 的部分URL地址
        // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
        // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
        // getCall()是接受网络请求数据的方法

    }
    public interface UserLoginInterface {
        @POST("login")
        Call<UserResult> getCall(@Query("userName") String name, @Query("passWord") String pw,@Query("uniqueCode") String uniqueCode);
        // 注解里传入 网络请求 的部分URL地址
        // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
        // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
        // getCall()是接受网络请求数据的方法

    }
    public interface UserRegisterInterface {
        @POST("register")
        Call<UserResult> getCall(@Query("userName") String name, @Query("passWord") String pw,@Query("uniqueCode") String uniqueCode);
        // 注解里传入 网络请求 的部分URL地址
        // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
        // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
        // getCall()是接受网络请求数据的方法

    }
    public interface UserRegisterUsernameCheckInterface {
        @POST("isUsernameExist")
        Call<UserResult> getCall(@Query("userName") String name);
        // 注解里传入 网络请求 的部分URL地址
        // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
        // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
        // getCall()是接受网络请求数据的方法

    }
    public interface InsertInterface {
        //@GET("Users/GetHasUser?userName={name}&pwd={pw}")
        @GET("Users/InsertUser")
        Call<UserResult> getCall(@Query("userName") String name, @Query("pwd") String pw);
        // 注解里传入 网络请求 的部分URL地址
        // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
        // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
        // getCall()是接受网络请求数据的方法

    }
}

