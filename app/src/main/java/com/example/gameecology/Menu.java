package com.example.gameecology;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class Menu extends Activity {
    private String login;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_act);

        // Получаем Intent и извлекаем переданную строку
        login = getIntent().getStringExtra(String.valueOf(R.string.key_login));
    }

    public void Q1(View v){
        Intent intent=new Intent(this, Quiz.class);
        startActivity(intent);
    }
    public void Q2(View v){
        Intent intent=new Intent(this, Menu.class);
        startActivity(intent);
    }
    public void Q3(View v){
        Intent intent=new Intent(this, Menu.class);
        startActivity(intent);
    }
    public void Q4(View v){
        Intent intent=new Intent(this, Menu.class);
        startActivity(intent);
    }
    public void IQ5(View v){
        Intent intent=new Intent(this, Menu.class);
        startActivity(intent);
    }
    public void Sorting6 (View v){
        Intent intent=new Intent(this, Menu.class);
        startActivity(intent);
    }

}
