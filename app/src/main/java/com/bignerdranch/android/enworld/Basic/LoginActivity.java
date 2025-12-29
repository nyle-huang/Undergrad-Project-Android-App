package com.bignerdranch.android.enworld.Basic;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.enworld.EWApplication;
import com.bignerdranch.android.enworld.Pager.PagerActivity;
import com.bignerdranch.android.enworld.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by Jeffrey on 2017/3/4.
 */

public class LoginActivity extends AppCompatActivity{

    private ProgressDialog mDialog;
    private EditText mUsername, mPassword;
    private TextView mRegister;
    private Button mLogin;
    private String username, password, encryptedPassword;
    private BmobUser bu;

    private int mEditTextHaveInputCount = 0;
    private final int EDITTEXT_AMOUNT = 2;
    private TextWatcher mTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "58500c9835693ad4669ede4526396c21");
        setContentView(R.layout.activity_login);

        if (autoLogin()){
            initView();
        } else {
            finish();
        }
    }

    private boolean autoLogin(){
        //判断本地缓存是否有已登录信息
        bu=BmobUser.getCurrentUser();
        if (bu!=null){
            //Easemob自动登录option已开启
            //有已登录帐号，直接开启首页
            Intent intent = new Intent(LoginActivity.this, PagerActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void initView(){
        mRegister=(TextView)findViewById(R.id.register_textview);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至注册页
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        mUsername = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_password);
        mLogin = (Button) findViewById(R.id.login);
        mLogin.setEnabled(false);
        mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                /** EditText最初内容为空 改变EditText内容时 个数加一*/
                if (TextUtils.isEmpty(charSequence)) {
                    mEditTextHaveInputCount++;
                    /** 判断个数是否到达要求*/
                    if (mEditTextHaveInputCount == EDITTEXT_AMOUNT)
                        mLogin.setEnabled(true);
                }
            }

            /** 内容改变*/
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                /** EditText内容改变之后 内容为空时 个数减一 按钮改为不可以的背景*/
                if (TextUtils.isEmpty(charSequence)) {
                    mEditTextHaveInputCount--;
                    mLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        /** 需要监听的EditText add*/
        mUsername.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);

        mLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();
                encryptedPassword = md5Password(password);
                bu = new BmobUser();
                bu.setUsername(username);
                bu.setPassword(encryptedPassword);
                bu.login(new SaveListener<BmobUser>() {

                    @Override
                    public void done(BmobUser s, BmobException e) {
                        if (e == null) {
                            //登录成功
                            loginToEasemob();
                        } else {
                            //登录失败
                            Toast.makeText(LoginActivity.this,
                                    "请输入正确的帐号与密码",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void loginToEasemob(){
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在登陆，请稍后...");
        mDialog.show();
        EMClient.getInstance().login(username, encryptedPassword, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();

                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        //EMClient.getInstance().groupManager().loadAllGroups();
                        // 登录成功跳转界面
                        Intent intent = new Intent(LoginActivity.this, PagerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Log.d("easemob", "登录失败 Error code:" + i + ", message:" + s);
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(LoginActivity.this, "网络错误，请检查网络链接", Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(LoginActivity.this, "无效的用户名", Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(LoginActivity.this, "无效的密码", Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(LoginActivity.this, "服务器无法访问，请稍后重试", Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(LoginActivity.this, "等待服务器响应超时", Toast.LENGTH_LONG).show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(LoginActivity.this, "服务器繁忙，请稍后重试", Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(LoginActivity.this, "未知的服务器异常", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, "登录出错，请稍后重试", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    private String md5Password(String password) {
        MessageDigest md;
        try {
            // 生成一个MD5加密计算摘要
            md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(password.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String pwd = new BigInteger(1, md.digest()).toString(16);
            System.err.println(pwd);
            return pwd;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return password;
    }
}