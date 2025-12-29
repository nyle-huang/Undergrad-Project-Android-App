package com.bignerdranch.android.enworld.Basic;

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
import android.widget.Toast;

import com.bignerdranch.android.enworld.Pager.PagerActivity;
import com.bignerdranch.android.enworld.R;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.math.BigInteger;
import java.security.MessageDigest;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Jeffrey on 2017/3/5.
 */

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog mDialog;
    private EditText mUsername, mPassword, mSecondPassword;
    private String username, password1, password2, encryptedPassword;
    private Button mRegister;
    private BmobUser bu;

    private int mEditTextHaveInputCount = 0;
    private final int EDITTEXT_AMOUNT = 3;
    private TextWatcher mTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        mUsername = (EditText) findViewById(R.id.register_username);
        mPassword = (EditText) findViewById(R.id.register_password);
        mSecondPassword = (EditText) findViewById(R.id.register_second_password);
        mRegister = (Button) findViewById(R.id.register_button);
        mRegister.setEnabled(false);
        mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mSecondPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                /** EditText最初内容为空 改变EditText内容时 个数加一*/
                if (TextUtils.isEmpty(charSequence)) {
                    mEditTextHaveInputCount++;
                    /** 判断个数是否到达要求*/
                    if (mEditTextHaveInputCount == EDITTEXT_AMOUNT)
                        mRegister.setEnabled(true);
                }
            }

            /** 内容改变*/
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                /** EditText内容改变之后 内容为空时 个数减一 按钮改为不可以的背景*/
                if (TextUtils.isEmpty(charSequence)) {
                    mEditTextHaveInputCount--;
                    mRegister.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        /** 需要监听的EditText add*/
        mUsername.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);
        mSecondPassword.addTextChangedListener(mTextWatcher);

        mRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username=mUsername.getText().toString();
                password1=mPassword.getText().toString();
                password2=mSecondPassword.getText().toString();

                if (!password1.equals(password2)){
                    Toast.makeText(RegisterActivity.this,
                            "两次输入的密码不一致",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                register();
            }
        });
    }

    private void register() {

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("注册中，请稍后...");
        mDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    encryptedPassword = md5Password(password1);
                    EMClient.getInstance().createAccount(username, encryptedPassword);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!RegisterActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            registerOnBmob();
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!RegisterActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }

                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            Log.d("Easemob", String.format("sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
                            switch (errorCode) {
                                // 网络错误
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(RegisterActivity.this, "网络错误，请检查网络链接", Toast.LENGTH_LONG).show();
                                    break;
                                // 用户已存在
                                case EMError.USER_ALREADY_EXIST:
                                    Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_LONG).show();
                                    break;
                                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                case EMError.USER_ILLEGAL_ARGUMENT:
                                    Toast.makeText(RegisterActivity.this, "参数不合法，请更换参数重试", Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器未知错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(RegisterActivity.this, "服务器未知错误", Toast.LENGTH_LONG).show();
                                    break;
                                case EMError.USER_REG_FAILED:
                                    Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void registerOnBmob() {

        bu = new BmobUser();
        bu.setUsername(username);
        bu.setPassword(encryptedPassword);
        bu.signUp(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser s, BmobException e){
                if (e==null){
                    //自动登录，跳转至app首页
                    bu.login(new SaveListener<BmobUser>() {

                        @Override
                        public void done(BmobUser s, BmobException e){
                            if (e==null){
                                //注册并登录成功
                                Intent i=new Intent(RegisterActivity.this, PagerActivity.class);
                                startActivity(i);
                            }else {
                                //注册成功，登录失败
                                Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }else {
                    //注册失败
                    Toast.makeText(RegisterActivity.this,
                            "注册失败，请稍后重试",
                            Toast.LENGTH_SHORT).show();
                }
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
            // digest()最后确定返回md5 hash值，返回值为8位字符串。
            // 因为md5 hash值是16位的hex值，实际上就是8位的字符
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
