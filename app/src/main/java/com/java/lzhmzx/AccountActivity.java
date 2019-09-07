package com.java.lzhmzx;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class AccountActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.edit_text_user_name);
        userPassword = findViewById(R.id.edit_text_user_password);
        setUpStatusBar();
        setUpButtons();
    }

    private void setUpStatusBar(){
        getWindow().setStatusBarColor(getColor(R.color.colorBackground));
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(mode == Configuration.UI_MODE_NIGHT_NO)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void setUpButtons(){
        Button confirm = findViewById(R.id.button_confirm);
        Button cancel = findViewById(R.id.button_cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtilities.hideKeyboard(userPassword);

                String userNameString = userName.getText().toString();
                String userPasswordString = userPassword.getText().toString();
                processLog(userNameString, userPasswordString);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtilities.hideKeyboard(userPassword);
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void processLog(final String userNameString, String userPasswordString){
        int logInStatus = DataHelper.logIn(userNameString, userPasswordString);
        switch (logInStatus){
            case 0:
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 1:
                Snackbar.make(getWindow().getDecorView().findViewById(R.id.edit_text_user_password), "密码错误", Snackbar.LENGTH_LONG).show();
                break;
            case 2:
                Snackbar.make(getWindow().getDecorView().findViewById(R.id.edit_text_user_password), "用户名不存在 是否建立新的账户", Snackbar.LENGTH_LONG)
                        .setAction("是的", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Boolean registerStatus = DataHelper.register(userNameString);
                                if (registerStatus){
                                    DataHelper.changeUser(userNameString);
                                    Snackbar.make(getWindow().getDecorView().findViewById(R.id.edit_text_user_password), "注册成功 已为您登录", Snackbar.LENGTH_LONG).show();
                                    Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }else{
                                    Snackbar.make(getWindow().getDecorView().findViewById(R.id.edit_text_user_password), "操作失败", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }).show();
                break;
        }
    }
}
