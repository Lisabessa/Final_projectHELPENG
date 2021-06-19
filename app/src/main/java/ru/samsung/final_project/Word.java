package ru.samsung.final_project;

import java.io.Serializable;

public class Word implements Comparable<Word>, Serializable{ // Класс, описывающий слово для словарей с учебным материалом
    private String name; // Слово на английском
    private String transcription; // Транскрипция слова ДЛЯ ПРОИЗНОШЕНИЯ
    private String translation; // Перевод слова

    public Word(String name, String transcription, String translation) {
        this.name = name;
        this.transcription = transcription;
        this.translation = translation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranscription() {
        return transcription; }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public int compareTo(Word o) {
        return o.getName().compareTo(this.name);
    }
}
