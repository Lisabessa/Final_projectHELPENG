package ru.samsung.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity { // Активность теста для модулей

    long userId = 0; // ID текущего пользователя
    String moduleCode; // Код текущего модуля

    Button btn1, btn2, btn3, btn4, btn_exitFromTest;
    TextView num, question, wordQuestion, tv_moduleName;

    private List<Word> words = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tv_moduleName = findViewById(R.id.textView_ModuleNameTest);
        Bundle extras = getIntent().getExtras();
        if(extras.get("id") != null && extras.get("moduleCode") != null){ // Распаковка id текущего пользователя и код текущего модуля
            userId = extras.getLong("id");
            moduleCode = extras.getString("moduleCode");
            if(moduleCode.equals("software")){ // Установка названия текущего модуля в активности
                tv_moduleName.append(" Software");
            }
            else if (moduleCode.equals("hardware")){
                tv_moduleName.append(" Hardware");
            }
            else if(moduleCode.equals("genverbs")){
                tv_moduleName.append(" General Verbs");
            }
            else if(moduleCode.equals("internet")){
                tv_moduleName.append(" Internet");
            }
        }

        btn_exitFromTest = findViewById(R.id.btn_exitFromTest);
        btn1 = findViewById(R.id.btn_answer1);
        btn2 = findViewById(R.id.btn_answer2);
        btn3 = findViewById(R.id.btn_answer3);
        btn4 = findViewById(R.id.btn_answer4);
        num = findViewById(R.id.textView_numOfQuestion);
        question = findViewById(R.id.textView_question);
        wordQuestion = findViewById(R.id.textView_wordForQuestion);
        setInitData();

        //создание объекта тестироващика
        final Tester tester = new Tester(words, btn1, btn2, btn3, btn4, num, question, wordQuestion);

        boolean resOfQuery = tester.Change();
        if (!resOfQuery){
            EndOfTheTestwithResults(tester);
        }
        // Обработка нажатия соответствующих кнопок
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tester.CheckAnswer(1);
                boolean resOfQuery = tester.Change();
                if (!resOfQuery){
                    EndOfTheTestwithResults(tester);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tester.CheckAnswer(2);
                boolean resOfQuery = tester.Change();
                if (!resOfQuery){
                    EndOfTheTestwithResults(tester);
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tester.CheckAnswer(3);
                boolean resOfQuery = tester.Change();
                if (!resOfQuery){
                    EndOfTheTestwithResults(tester);
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tester.CheckAnswer(4);
                boolean resOfQuery = tester.Change();
                if (!resOfQuery){
                    EndOfTheTestwithResults(tester);
                }
            }
        });

        btn_exitFromTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Возвращение на главную вкладку модуля
                Intent intent = new Intent(TestActivity.this, ModuleMainActivity.class);
                intent.putExtra("id", userId); // id выбранного пользователя
                intent.putExtra("moduleCode", moduleCode); // какой модуль загружать
                startActivity(intent);
            }
        });
    }


    public void EndOfTheTestwithResults(Tester tester){  // Метод, осуществляющий переход в ModuleMainActivity с данными о результатах
        Toast.makeText(getApplicationContext(), "Подсчёт результата...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(TestActivity.this, ModuleMainActivity.class);
        intent.putExtra("RESULT" ,tester.getPoints());
        intent.putExtra("AT_ALL", tester.getWordsSize());
        intent.putExtra("MISTAKES" , (Serializable)tester.getMistakes());
        intent.putExtra("id", userId); // id выбранного пользователя
        intent.putExtra("moduleCode", moduleCode); // какой модуль загружать
        startActivity(intent);
    }

    protected void setInitData(){  // Метод, подбирающий нужный учебный материал в зависимости от текущего модуля
        WordList wl = new WordList();
        if(moduleCode.equals("software")){
            wl.ForTestSoftware();
        }
        else if (moduleCode.equals("hardware")){
            wl.ForTestHardware();
        }
        else if(moduleCode.equals("genverbs")){
            wl.ForTestGenVerbs();
        }
        else if(moduleCode.equals("internet")){
            wl.ForTestInternet();
        }
        words = wl.getWordList();
    }
}