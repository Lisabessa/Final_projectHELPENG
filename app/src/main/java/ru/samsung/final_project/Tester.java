package ru.samsung.final_project;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static ru.samsung.final_project.R.string.QuestionNumber;

public class Tester { // Класс, который реализует механику тестирования (использование в TestActivity)
    private int Points = 0;
    Random random = new Random();

    String alpha = "abcdefghijklmnopqrstuvwxyz- "; // алфавит, элементы из которого будут использованы в задании на правописание

    private Button btn1, btn2, btn3, btn4;
    private TextView num, question, wordQuestion;

    private List<Word> words;
    private List<Word> mistakes = new ArrayList<>();

    int btn;
    private UsingInfo spisok = new UsingInfo();
    int currentNum = -1; // Номер текущего слова из списка, с которым в данный момент происходит работа.

    // в конструкторе получаем элементы исходной разметки для взаимодействия с ними
    public Tester(List<Word> words, Button btn1, Button btn2, Button btn3, Button btn4, TextView num, TextView question, TextView wordQuestion) {
        this.words = words;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.btn4 = btn4;
        this.num = num;
        this.question = question;
        this.wordQuestion = wordQuestion;
        BigMix(); // Перемешка для того, чтоб каждый раз слова шли в другом порядке.
    }

    public void CheckAnswer(int btn_num){
        if (spisok.CheckButton(btn_num)){
            Points++;
        }
        else{
            mistakes.add(new Word(words.get(currentNum).getName(), words.get(currentNum).getTranscription(), words.get(currentNum).getTranslation()));
        }
    }

    public boolean Change(){  // Метод, генерирующий следующий вопрос для теста.
        btn1.setVisibility(View.VISIBLE);  // Делаем все кнопки видимыми.
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btn4.setVisibility(View.VISIBLE);
        btn1.setText("");
        btn2.setText("");
        btn3.setText("");
        btn4.setText("");
        currentNum ++; // Индекс текущего слова из списка
        if(currentNum < words.size()){
            spisok = new UsingInfo();
            num.setText(QuestionNumber);
            num.append(""+(currentNum +1));

            int taskCode = random.nextInt(4); // Рандомный выбор типа задания
            switch (taskCode){
                case 0: // Тип задания с переводом с английского на русский
                    question.setText(R.string.question1);
                    wordQuestion.setText(words.get(currentNum).getName());
                    btn = random.nextInt(4) + 1; // Рандомный выбор кнопки, которая будет содержать ВЕРНЫЙ ответ

                    spisok.AddWord(currentNum); // Пометка об использованном слове
                    spisok.AddButton(btn); // Пометка об использованной кнопке

                    switch (btn){
                        case 1: // Установка правильного перевода на выбранную кнопку
                            btn1.setText(words.get(currentNum).getTranslation());
                            break;
                        case 2:
                            btn2.setText(words.get(currentNum).getTranslation());
                            break;
                        case 3:
                            btn3.setText(words.get(currentNum).getTranslation());
                            break;
                        case 4:
                            btn4.setText(words.get(currentNum).getTranslation());
                            break;
                    }

                    spisok.setAnswerButton(btn); // Запись кнопки с правильным ответом

                    for(int i=0; i<4; i++){
                        if(!spisok.getUsingButtons().contains(i+1)){ // Установка неверных вариантов ответа для оставшихся кнопок
                            int numForIncorrect = random.nextInt(words.size()); // Рандомный выбор слова для неправильного ответа

                            while(spisok.getUsingWords().contains(numForIncorrect)){ // Исправление совпадений случайно выбранного слова с уже использованными
                                numForIncorrect = random.nextInt(words.size());
                            }
                            switch (i+1){ // Установка значения для данной кнопки
                                case 1:
                                    btn1.setText(words.get(numForIncorrect).getTranslation());
                                    break;
                                case 2:
                                    btn2.setText(words.get(numForIncorrect).getTranslation());
                                    break;
                                case 3:
                                    btn3.setText(words.get(numForIncorrect).getTranslation());
                                    break;
                                case 4:
                                    btn4.setText(words.get(numForIncorrect).getTranslation());
                                    break;
                            }
                            spisok.AddWord(numForIncorrect); // Добавление информации об использованных данных
                            spisok.AddButton(i+1);
                        }
                    }
                    break;

                case 1:  // Тип задания с переводом с русского на английский
                    question.setText(R.string.question1);
                    wordQuestion.setText(words.get(currentNum).getTranslation());
                    btn = random.nextInt(4) + 1;  // Рандомный выбор кнопки, которая будет содержать ВЕРНЫЙ ответ

                    spisok.AddWord(currentNum); // Пометка об использованной информации
                    spisok.AddButton(btn);
                    switch (btn){ // Установка правильного английского слова на выбранную кнопку
                        case 1:
                            btn1.setText(words.get(currentNum).getName());
                            break;
                        case 2:
                            btn2.setText(words.get(currentNum).getName());
                            break;
                        case 3:
                            btn3.setText(words.get(currentNum).getName());
                            break;
                        case 4:
                            btn4.setText(words.get(currentNum).getName());
                            break;
                    }

                    spisok.setAnswerButton(btn);

                    for(int i=0; i<4; i++){
                        if(!spisok.getUsingButtons().contains(i+1)){
                            int numForIncorrect = random.nextInt(words.size());
                            while(spisok.getUsingWords().contains(numForIncorrect)){
                                numForIncorrect = random.nextInt(words.size());
                            }
                            switch (i+1){
                                case 1:
                                    btn1.setText(words.get(numForIncorrect).getName());
                                    break;
                                case 2:
                                    btn2.setText(words.get(numForIncorrect).getName());
                                    break;
                                case 3:
                                    btn3.setText(words.get(numForIncorrect).getName());
                                    break;
                                case 4:
                                    btn4.setText(words.get(numForIncorrect).getName());
                                    break;
                            }
                            spisok.AddWord(numForIncorrect);
                            spisok.AddButton(i+1);
                        }
                    }
                    break;

                case 2: // Тип задания на правописание
                    question.setText(R.string.question2);

                    char []ar = words.get(currentNum).getName().toCharArray(); // Текущее слово превращаем в строку символов
                    int missed = random.nextInt(ar.length); // выбираем рандомно пропущенную букву
                    ar[missed] = '_'; // в слове заменяем её на прочерк

                    String resString = new String(ar); // получившийся массив превращаем в строку
                    wordQuestion.setText(resString); // эту строку записываем в поле со словом для задания

                    btn = random.nextInt(4) + 1; // Выбираем кнопку, котрая будет содержать правильный ответ

                    String SymbRight = String.valueOf(words.get(currentNum).getName().toCharArray()[missed]); // верный пропущенный символ
                    char [] alphabet = alpha.toCharArray();
                    spisok.AddWord(SymbRight.hashCode());
                    spisok.AddButton(btn);
                    switch (btn){
                        case 1:
                            btn1.setText(SymbRight.toLowerCase()); // приводим к нижнему регистру
                            break;
                        case 2:
                            btn2.setText(SymbRight.toLowerCase());
                            break;
                        case 3:
                            btn3.setText(SymbRight.toLowerCase());
                            break;
                        case 4:
                            btn4.setText(SymbRight.toLowerCase());
                            break;
                    }
                    spisok.setAnswerButton(btn); // Записываем кнопку с верным ответом

                    for(int i=0; i<4; i++){
                        if(!spisok.getUsingButtons().contains(i+1)){
                            int numForIncorrect = random.nextInt(alpha.length()); // Рандомный выбор неверного символа без совпадений с предыдущими выбранными символами
                            while(spisok.getUsingWords().contains(String.valueOf(alphabet[numForIncorrect]).hashCode())){
                                numForIncorrect = random.nextInt(alpha.length());
                            }
                            String symb = String.valueOf(alphabet[numForIncorrect]);
                            switch (i+1){ // установка символа на кнопку
                                case 1:
                                    btn1.setText(symb);
                                    break;
                                case 2:
                                    btn2.setText(symb);
                                    break;
                                case 3:
                                    btn3.setText(symb);
                                    break;
                                case 4:
                                    btn4.setText(symb);
                                    break;
                            }
                            spisok.AddWord(String.valueOf(alphabet[numForIncorrect]).hashCode()); // запись использованной информации
                            spisok.AddButton(i+1);
                        }
                    }

                    break;

                case 3: // Тип задания с вариантами да или нет на правильность составленной пары
                    question.setText(R.string.question3);
                    btn3.setVisibility(View.GONE); // Делаем 2 кнопки невидимыми. (Варианты ответов только да или нет.)
                    btn4.setVisibility(View.GONE);
                    btn1.setText(R.string.AnswerYES);
                    btn2.setText(R.string.AnswerNO);

                    btn = random.nextInt(2) + 1;
                    switch (btn){
                        case 1:
                            //верно
                            wordQuestion.setText(words.get(currentNum).getName() +" - "+ words.get(currentNum).getTranslation());

                            break;

                        case 2:
                            //ложь
                            String incorrectTranslation = words.get(random.nextInt(words.size())).getTranslation();
                            while (incorrectTranslation.equals(words.get(currentNum).getTranslation())){
                                incorrectTranslation = words.get(random.nextInt(words.size())).getTranslation();
                            }
                            wordQuestion.setText(words.get(currentNum).getName() +" - "+ incorrectTranslation);
                            break;
                    }
                    spisok.setAnswerButton(btn);
                    break;
            }

            return true;  // тест продолжается
        }
        return false; // если ложь => в активности запрос результатов теста

    }

    public int getPoints() { // Возвращение количества очков, набранных при прохождении теста
        return Points;
    }

    public List<Word> getMistakes() {  // Возвращение списка слов, в которых пользователь допустил ошибки
        return mistakes;
    }

    public int getWordsSize() {  // Возвращение количества слов в используемом словаре
        return words.size();
    }

    public void BigMix(){
        Collections.shuffle(words);
    }
}
