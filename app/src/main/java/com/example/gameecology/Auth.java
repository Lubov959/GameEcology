package com.example.gameecology;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;


public class Auth extends Activity {
    private String login, password;
    private EditText etLogin, etPassword;
    private Button bOpen;
    private SharedPreferences preferences;  //ссылка на объект-настройку


    @Override
    protected void onResume() { //обработчик события, которое помещает активность на передний план
        super.onResume();              //вызов обработчика базового класса
     //Загружаем данные из SharedPreferences
    login = preferences.getString(getString(R.string.key_login), "");
    password = preferences.getString(getString(R.string.key_password), "");
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_act);
        // Инициализация элементов
        etLogin = findViewById(R.id.login_text);
        etPassword = findViewById(R.id.password_text);
        bOpen = findViewById(R.id.bOpen);


        //метод getSharedPreferences возвращает объект-настройку
        preferences = getSharedPreferences(
                getString(R.string.name_preferences), //имя настройки здесь берется из строкового ресурса
                MODE_PRIVATE //скрытый режим – только наше приложение может читать
        );

        login = preferences.getString(getString(R.string.key_login), "");
        password = preferences.getString(getString(R.string.key_password), "");

        //заполнение полей через из памяти
        etLogin.setText(login);
        etPassword.setText(password);

        // Добавляем слушателей для полей ввода
        etLogin.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
    }


        // Создаем TextWatcher для проверки наличия текста в обоих полях
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            // Проверка, что оба поля не пустые
            if (!etLogin.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                bOpen.setEnabled(true);  // Включаем кнопку
            } else {
                bOpen.setEnabled(false); // Выключаем кнопку
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {}
    };

    //Метод аутентификации
    public void Auth(View v){
        //Auth(etLogin.getText(), etPassword.getText());
        Intent intent=new Intent(this, Menu.class);  //создаем Intent
        intent.putExtra(getString(R.string.key_login), etLogin.getText().toString());
        // Запускаем вторую активность
        startActivity(intent);
    }


    //обработчик события, которое убирает активность с переднего плана
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit(); //с помощью метода edit объекта-активности
        //получаем объект-редактор editor
        editor.putString(getString(R.string.key_login), etLogin.getText().toString()); //с помощью редактора помещаем в настройку элемент
        editor.putString(getString(R.string.key_password), etPassword.getText().toString());
        editor.apply();
    }
}
