//package com.example.gameecology;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class Quiz extends Activity {
//
//    private int currentQuestionIndex = 0;
//    private List<Question> questions;
//    private SharedPreferences sharedPreferences;
//    private TextView questionTextView;
//    private RadioGroup answersGroup;
//    private Button nextButton;
//    private Button prevButton;
//    private Button finishButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.quiz_act);
//
//        questionTextView = findViewById(R.id.questionTextView);
//        answersGroup = findViewById(R.id.answersGroup);
//        nextButton = findViewById(R.id.nextButton);
//        prevButton = findViewById(R.id.prevButton);
//        finishButton = findViewById(R.id.finishButton);
//
//        sharedPreferences = getSharedPreferences("QuizProgress", MODE_PRIVATE);
//        questions = QuizData.loadQuestions(this, "quiz1.json");
//
//        loadSavedProgress();
//        displayQuestion(currentQuestionIndex);
//
//        nextButton.setOnClickListener(v -> {
//            saveAnswer();
//            if (currentQuestionIndex < questions.size() - 1) {
//                currentQuestionIndex++;
//                displayQuestion(currentQuestionIndex);
//            }
//        });
//
//        prevButton.setOnClickListener(v -> {
//            saveAnswer();
//            if (currentQuestionIndex > 0) {
//                currentQuestionIndex--;
//                displayQuestion(currentQuestionIndex);
//            }
//        });
//
//        finishButton.setOnClickListener(v -> finishQuiz());
//    }
//
//    private void displayQuestion(int index) {
//        Question question = questions.get(index);
//        questionTextView.setText(question.getQuestion());
//        answersGroup.removeAllViews();
//
//        for (String answer : question.getAnswers()) {
//            RadioButton radioButton = new RadioButton(this);
//            radioButton.setText(answer);
//            answersGroup.addView(radioButton);
//        }
//
//        // Восстановление выбранного ответа, если он был сохранён
//        String savedAnswer = sharedPreferences.getString("answer_" + index, "");
//        for (int i = 0; i < answersGroup.getChildCount(); i++) {
//            RadioButton radioButton = (RadioButton) answersGroup.getChildAt(i);
//            if (radioButton.getText().toString().equals(savedAnswer)) {
//                radioButton.setChecked(true);
//                break;
//            }
//        }
//    }
//
//    private void saveAnswer() {
//        int selectedAnswerId = answersGroup.getCheckedRadioButtonId();
//        if (selectedAnswerId != -1) {
//            RadioButton selectedRadioButton = findViewById(selectedAnswerId);
//            String selectedAnswer = selectedRadioButton.getText().toString();
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("answer_" + currentQuestionIndex, selectedAnswer);
//            editor.apply();
//        }
//    }
//
//    private void loadSavedProgress() {
//        currentQuestionIndex = sharedPreferences.getInt("current_question_index", 0);
//    }
//
//    private void finishQuiz() {
//        // Здесь можно показать результат квиза или вернуться на главный экран
//        Intent intent = new Intent(this, Menu.class);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("current_question_index", currentQuestionIndex);
//        editor.apply();
//    }
//}
//


package com.example.gameecology;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Quiz extends Activity {

    private int currentQuestionIndex = 0;
    private List<Question> questions;
    private SharedPreferences sharedPreferences;
    private TextView questionTextView;
    private RadioGroup answersGroup;
    private Button nextButton;
    private Button prevButton;
    private Button finishButton;
    private int correctAnswersCount = 0;
    private int answeredQuestionsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_act);

        questionTextView = findViewById(R.id.questionTextView);
        answersGroup = findViewById(R.id.answersGroup);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        finishButton = findViewById(R.id.finishButton);

        sharedPreferences = getSharedPreferences("QuizProgress", MODE_PRIVATE);

        // Загрузка вопросов
        questions = QuizData.loadQuestions(this, "quiz1.json");

        // Восстановление сохранённого состояния
        loadSavedProgress();
        displayQuestion(currentQuestionIndex);

        // Обработчик нажатия кнопки "Далее"
        nextButton.setOnClickListener(v -> {
            saveAnswer();  // Сохраняем ответ перед переходом
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayQuestion(currentQuestionIndex);
            }
            updateButtonsState();
        });

        // Обработчик нажатия кнопки "Назад"
        prevButton.setOnClickListener(v -> {
            saveAnswer();  // Сохраняем ответ перед переходом
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion(currentQuestionIndex);
            }
            updateButtonsState();
        });

        // Обработчик нажатия кнопки "Завершить"
        finishButton.setOnClickListener(v -> finishQuiz());
    }

    private void displayQuestion(int index) {
        Question question = questions.get(index);
        questionTextView.setText(question.getQuestion());
        answersGroup.removeAllViews();

        // Создаем радиокнопки для каждого ответа
        for (String answer : question.getAnswers()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(answer);
            answersGroup.addView(radioButton);
        }

        // Восстановление выбранного ответа
        String savedAnswer = sharedPreferences.getString("answer_" + index, "");
        for (int i = 0; i < answersGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) answersGroup.getChildAt(i);
            if (radioButton.getText().toString().equals(savedAnswer)) {
                radioButton.setChecked(true);
                break;
            }
        }

        updateButtonsState(); // Обновление состояния кнопок
    }

    private void saveAnswer() {
        int selectedAnswerId = answersGroup.getCheckedRadioButtonId();
        if (selectedAnswerId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedAnswerId);
            String selectedAnswer = selectedRadioButton.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("answer_" + currentQuestionIndex, selectedAnswer);

            // Проверка правильности ответа
            Question currentQuestion = questions.get(currentQuestionIndex);
            if (selectedAnswer.equals(currentQuestion.getCorrect_answer())) {
                correctAnswersCount++;
            }

            // Увеличиваем счетчик отвеченных вопросов
            answeredQuestionsCount++;

            editor.apply();
        }
    }

    private void loadSavedProgress() {
        currentQuestionIndex = sharedPreferences.getInt("current_question_index", 0);
    }

    private void finishQuiz() {
        // Сохраняем ответ для последнего вопроса
        saveAnswer();

        // Показываем Toast с результатами
        Toast.makeText(this, "Викторина завершена! Правильных ответов: " + correctAnswersCount +
                " из " + answeredQuestionsCount + " отвеченных вопросов.", Toast.LENGTH_LONG).show();

        // Переход на экран завершения (можно оставить, если хотите переход на другой экран)
        Intent intent = new Intent(this, Menu.class); // Поменяйте на правильный класс меню или экран завершения
        startActivity(intent);
        finish();
    }

    private void updateButtonsState() {
        // Блокируем кнопку "Назад" на первом вопросе
        prevButton.setEnabled(currentQuestionIndex > 0);

        // Блокируем кнопку "Далее" на последнем вопросе
        nextButton.setEnabled(currentQuestionIndex < questions.size() - 1);

        // Блокируем кнопку "Далее", если ответ не выбран
        nextButton.setEnabled(answersGroup.getCheckedRadioButtonId() != -1);

        // Кнопка "Завершить" всегда доступна
        finishButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("current_question_index", currentQuestionIndex);
        editor.apply();
    }
}
