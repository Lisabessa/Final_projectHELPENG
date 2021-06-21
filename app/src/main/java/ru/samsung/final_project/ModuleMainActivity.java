package ru.samsung.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ModuleMainActivity extends AppCompatActivity { // Главная активность для модулей

    long userId = 0; // ID текущего пользователя
    String moduleCode; // Код текущего модуля

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    Button btn_learn, btn_test, btn_backToListOfModuls;

    ListView mistakes;
    TextView tv_mistakes, tv_total, tv_moduleName;
    TextView tv_results;
    double Percentage = 0;
    int Result = 0;
    int General_count = 0;
    ArrayList<Word> listOfMistakes = new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_main);

        btn_learn = findViewById(R.id.btn_learn);
        btn_test = findViewById(R.id.btn_test);
        tv_results = findViewById(R.id.textView_Res);
        tv_total = findViewById(R.id.textView_Total);
        tv_mistakes = findViewById(R.id.textView_Mistakes);
        btn_backToListOfModuls = findViewById(R.id.btn_exitFromModuleMain);
        tv_moduleName = findViewById(R.id.textView_ModuleNameMain);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.open();

        Bundle extras = getIntent().getExtras();
        if(extras.get("id") != null && extras.get("moduleCode") != null){ // Распаковка id текущего пользователя и код текущего модуля
            userId = extras.getLong("id");
            moduleCode = extras.getString("moduleCode");
            if(moduleCode.equals("software")){
               tv_moduleName.append(" Software"); // Установка названия текущего модуля в активности
                tv_total.append(" Software");
            }
            else if (moduleCode.equals("hardware")){
                tv_moduleName.append(" Hardware");
                tv_total.append(" Hardware");
            }
            else if(moduleCode.equals("genverbs")){
                tv_moduleName.append(" General Verbs");
                tv_total.append(" General Verbs");
            }
            else if(moduleCode.equals("internet")){
                tv_moduleName.append(" Internet");
                tv_total.append(" Internet");
            }
        }

        if(extras.get("RESULT") != null && extras.get("AT_ALL") != null && extras.get("MISTAKES") != null){
            try { // В случае, если переход в данную активность осуществился из TestActivity и содержит результаты теста
                listOfMistakes = (ArrayList<Word>)extras.getSerializable("MISTAKES");
                Result = extras.getInt("RESULT");
                General_count = extras.getInt("AT_ALL");

            }
            catch (Exception e){}
        }
        mistakes = findViewById(R.id.listOfMistakes);
        WordAdapter mistakesAdapter = new WordAdapter(this, R.layout.list_item, listOfMistakes);
        mistakes.setAdapter(mistakesAdapter);

        if(General_count != 0 && userId > 0){
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where "+DatabaseHelper.COLUMN_ID + " =?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();

            tv_results.setText("Ваши результаты:\n"+ Result + " из "+General_count+ " ");
            Percentage = Double.parseDouble(String.valueOf(Result)) / Double.parseDouble(String.valueOf(General_count)) * 100;
            tv_results.append("(" + Math.round(Percentage)+"%)");

            ContentValues cv = new ContentValues();

            if (moduleCode.equals("software")){
                cv.put(DatabaseHelper.COLUMN_NAME, userCursor.getString(1)); // заполнение полей с результатами
                cv.put(DatabaseHelper.COLUMN_RES_SOFTWARE, Math.round(Percentage));
                cv.put(DatabaseHelper.COLUMN_RES_HARDWARE, userCursor.getFloat(3));
                cv.put(DatabaseHelper.COLUMN_RES_GENVERBS, userCursor.getFloat(4));
                cv.put(DatabaseHelper.COLUMN_RES_INTERNET, userCursor.getFloat(5));
            }
            else if (moduleCode.equals("hardware")){
                cv.put(DatabaseHelper.COLUMN_NAME, userCursor.getString(1)); // заполнение полей
                cv.put(DatabaseHelper.COLUMN_RES_SOFTWARE, userCursor.getFloat(2));
                cv.put(DatabaseHelper.COLUMN_RES_HARDWARE, Math.round(Percentage));
                cv.put(DatabaseHelper.COLUMN_RES_GENVERBS, userCursor.getFloat(4));
                cv.put(DatabaseHelper.COLUMN_RES_INTERNET, userCursor.getFloat(5));
            }
            else if(moduleCode.equals("genverbs")){
                cv.put(DatabaseHelper.COLUMN_NAME, userCursor.getString(1)); // заполнение полей
                cv.put(DatabaseHelper.COLUMN_RES_SOFTWARE, userCursor.getFloat(2));
                cv.put(DatabaseHelper.COLUMN_RES_HARDWARE, userCursor.getFloat(3));
                cv.put(DatabaseHelper.COLUMN_RES_GENVERBS, Math.round(Percentage));
                cv.put(DatabaseHelper.COLUMN_RES_INTERNET, userCursor.getFloat(5));
            }
            else if(moduleCode.equals("internet")){
                cv.put(DatabaseHelper.COLUMN_NAME, userCursor.getString(1)); // заполнение полей
                cv.put(DatabaseHelper.COLUMN_RES_SOFTWARE, userCursor.getFloat(2));
                cv.put(DatabaseHelper.COLUMN_RES_HARDWARE, userCursor.getFloat(3));
                cv.put(DatabaseHelper.COLUMN_RES_GENVERBS, userCursor.getFloat(4));
                cv.put(DatabaseHelper.COLUMN_RES_INTERNET, Math.round(Percentage));
            }

            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + " = " + String.valueOf(userId), null);

            if(Math.round(Percentage) == 100){ // Определение комментария к результатам пользователя
                tv_total.setBackgroundResource(R.color.ColMAIN);
                tv_total.setText("Отлично! Тема полностью усвоена!");
            }
            else {
                if(Math.round(Percentage) >= 95){
                    tv_total.setText("Хорошая работа! Так держать!");
                    tv_total.setBackgroundResource(R.color.ColMAIN3);
                }
                else{
                    tv_total.setText("Попробуйте ещё раз!");
                    tv_total.setBackgroundResource(R.color.ColMAIN2);
                }
                tv_mistakes.setText("Слова, которые Вам нужно повторить:");
            }


        }
        else if(General_count != 0 && userId == 0) {
            tv_results.setText("Ваши результаты:\n" + Result + " из " + General_count + " ");
            Percentage = Double.parseDouble(String.valueOf(Result)) / Double.parseDouble(String.valueOf(General_count)) * 100;
            tv_results.append("(" + Math.round(Percentage) + "%)");

            if (Math.round(Percentage) == 100) { // Определение комментария к результатам пользователя
                tv_total.setBackgroundResource(R.color.ColMAIN);
                tv_total.setText("Отлично! Тема полностью усвоена!");
            } else {
                if (Math.round(Percentage) >= 95) {
                    tv_total.setText("Хорошая работа! Так держать!");
                    tv_total.setBackgroundResource(R.color.ColMAIN3);
                } else {
                    tv_total.setText("Попробуйте ещё раз!");
                    tv_total.setBackgroundResource(R.color.ColMAIN2);
                }
                tv_mistakes.setText("Слова, которые Вам нужно повторить:");
            }
        }

        btn_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModuleMainActivity.this, LearnActivity.class);
                intent.putExtra("id", userId); // id выбранного пользователя
                intent.putExtra("moduleCode", moduleCode); // какой модуль загружать
                startActivity(intent);
            }
        });

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModuleMainActivity.this, TestActivity.class);
                intent.putExtra("id", userId); // id выбранного пользователя
                intent.putExtra("moduleCode", moduleCode); // какой модуль загружать
                startActivity(intent);
            }
        });

        btn_backToListOfModuls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModuleMainActivity.this, ListActivity.class);
                intent.putExtra("id", userId); // id выбранного пользователя
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        userCursor.close();
    }
}