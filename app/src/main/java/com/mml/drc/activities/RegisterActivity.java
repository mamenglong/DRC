package com.mml.drc.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mml.drc.Model.User;
import com.mml.drc.R;
import com.mml.drc.retrofit_interface.UserResult;
import com.mml.drc.utils.DeviceUtil;
import com.mml.drc.utils.LogUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BaseActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mAuthTask = null;
    private UserCheckTask mUserCheckTask=null;
    // UI references.

    private AutoCompleteTextView mUsernamelView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the register form.
        mActionBar = initActioBar();
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setSubtitle("注册");
        mUsernamelView =  findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mUsernamelView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                   //输入完成点击软键盘上完成时执行
                    checkUsernameExist();
                    return true;
                }
                return false;
                //返回true，保留软键盘。false，隐藏软键盘
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //输入完成点击软键盘上完成时执行
                   attemptRegister();
                   return true;
                }
                return false;
                //返回true，保留软键盘。false，隐藏软键盘
            }
        });
        Button mUsernameSignUpButton = (Button) findViewById(R.id.username_sign_up_button);
        mUsernameSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    /**
     * 输入完成检查用户名
     */
    private void checkUsernameExist() {
        if (mUserCheckTask != null) {
            return;
        }

        // Reset errors.
        mUsernamelView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernamelView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernamelView.setError(getString(R.string.error_field_required));
            focusView = mUsernamelView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mUserCheckTask = new UserCheckTask(username);
            mUserCheckTask.execute((Void) null);
        }
    }

    /**
     * 调用禁用menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernamelView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernamelView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernamelView.setError(getString(R.string.error_field_required));
            focusView = mUsernamelView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserRegisterTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameExist(String username) {
        //存在为true 不存为false，异常为true
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(getResources().getString(R.string.baseURL))
                    .client(getOkHttpClient())
                    .build();
            UserResult.UserRegisterUsernameCheckInterface request = retrofit.create(UserResult.UserRegisterUsernameCheckInterface.class);

            Call<UserResult> call = request.getCall(username);

            Response<UserResult> response = call.execute();
            if (response.isSuccessful()) {
                LogUtils.i(response.body().toString());
                LogUtils.i("isUsernameExist", response.body().getMsg());
                if (response.body().getStatus() == 1) {
                    //1不存在，0存在
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 显示url的okhttp封装
     *
     * @return OkHttpClient
     */
    private OkHttpClient getOkHttpClient() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.i("url", "OkHttp====Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient
                .Builder();
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mPassword;

        UserRegisterTask(String userName, String password) {
            mUserName = userName;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Looper.prepare();
                // Simulate network access.
                User user = new User();
                user.setUserName(mUserName);
                user.setPassWord(mPassword);
                user.setUniqueCode(DeviceUtil.getUniqueId(RegisterActivity.this));
                //todo  服务器端返回  status0 1
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(getResources().getString(R.string.baseURL))
                        .client(getOkHttpClient())
                        .build();
                UserResult.UserRegisterInterface request = retrofit.create(UserResult.UserRegisterInterface.class);

                Call<UserResult> call = request.getCall(user.getUserName(), user.getPassWord(), user.getUniqueCode());

                Response<UserResult> response = call.execute();
                if (response.isSuccessful()) {
                    LogUtils.i(response.body().toString());
                    if (response.body().getStatus() == 1) {
                        user.save();
                        return true;
                    } else {
                        LogUtils.i("注册失败", response.body().getMsg());
                        return false;
                    }
                }else{
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Toast.makeText(RegisterActivity.this, "注册成功，即将跳转登陆界面！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_register_fail));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    public class UserCheckTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;

        UserCheckTask(String userName) {
            mUserName = userName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
                return isUsernameExist(mUserName);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(final Boolean success) {
            mUserCheckTask = null;
            if (success) {
                //true存在 false不存在
                mUsernamelView.setError(getString(R.string.error_register_check_exist));
                mUsernamelView.requestFocus();
            } else {
                mUsernamelView.setTextColor(R.color.green);
                mUsernamelView.setError(getString(R.string.error_register_check_no_exist));

            }
        }

        @Override
        protected void onCancelled() {
            mUserCheckTask = null;
        }
    }
}

