package ru.samsung.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LearnActivity extends AppCompatActivity { // Активность для изучения материала по модулям

    long userId = 0; // ID текущего пользователя
    String moduleCode; // Код текущего модуля

    ListView list;
    private List<Word> words = new ArrayList<>();
    TextView tv_moduleName;
    Button btn_exitFromLearn;
    EditText searchWord;
    WordAdapter wordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        tv_moduleName = findViewById(R.id.textView_ModuleNameLearn);
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

        setInitData();
        list = findViewById(R.id.Wordlist);
        wordAdapter = new WordAdapter(this, R.layout.list_item, words);
        //listSoftware.setAdapter(wordAdapter);
        btn_exitFromLearn = findViewById(R.id.btn_exitFromLearn);
        searchWord = findViewById(R.id.SearchWord);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word selectedWord = (Word)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "" + selectedWord.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_exitFromLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnActivity.this, ModuleMainActivity.class);
                intent.putExtra("id", userId); // id выбранного пользователя
                intent.putExtra("moduleCode", moduleCode); // Код текущего модуля
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!searchWord.getText().toString().isEmpty()){
            wordAdapter.getFilter().filter(searchWord.getText().toString());
        }
        searchWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        list.setAdapter(wordAdapter);
    }

    void setInitData(){ // Метод, подбирающий нужный учебный материал в зависимости от текущего модуля
        WordList wl = new WordList();
        if(moduleCode.equals("software")){
            wl.ForLearnSoftware();
        }
        else if (moduleCode.equals("hardware")){
            wl.ForLearnHardware();
        }
        else if(moduleCode.equals("genverbs")){
            wl.ForLearnGenVerbs();
        }
        else if(moduleCode.equals("internet")){
            wl.ForLearnInternet();
        }
        words = wl.getWordList();
    }
}