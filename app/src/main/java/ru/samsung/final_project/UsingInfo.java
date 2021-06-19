package ru.samsung.final_project;

import android.content.Intent;

import java.util.ArrayList;

public class UsingInfo { // Класс, в экземплярах которого хранится информация об использованной информации, правильных ответах. Используется в классе Tester

    private ArrayList<Integer> usingWords = new ArrayList<>();
    private ArrayList<Integer> usingButtons = new ArrayList<>();
    private Integer AnswerButton;

    public UsingInfo() {
    }

    public void AddWord(int num){
        usingWords.add(num);
    }

    public void AddButton(int num){
        usingButtons.add(num);
    }

    public ArrayList<Integer> getUsingWords() {
        return usingWords;
    }

    public ArrayList<Integer> getUsingButtons() {
        return usingButtons;
    }

    public void setAnswerButton(Integer answerButton) {  // Установка кнопки с правильным ответом
        AnswerButton = answerButton;
    }

    public boolean CheckButton(int btn){ // Проверка правильности ответа
        if (btn == AnswerButton){
            return true;
        }
        return false;
    }

}
