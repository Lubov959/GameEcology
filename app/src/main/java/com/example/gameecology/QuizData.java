package com.example.gameecology;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QuizData {
    public static List<Question> loadQuestions(Context context, String fileName) {
        String json = loadJSONFromAsset(context, fileName);
        Type type = new TypeToken<List<Question>>(){}.getType();
        return new Gson().fromJson(json, type);
    }

    private static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}