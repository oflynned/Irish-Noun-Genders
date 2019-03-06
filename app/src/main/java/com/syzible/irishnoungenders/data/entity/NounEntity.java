package com.syzible.irishnoungenders.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NounEntity {
    public enum Gender {
        MASCULINE, FEMININE
    }

    @SerializedName("declension")
    private int declension;

    @SerializedName("en")
    private List<String> englishForms;

    @SerializedName("ga")
    private String irishForm;

    @SerializedName("gender")
    private Gender gender;

    public int getDeclension() {
        return declension;
    }

    public void setDeclension(int declension) {
        this.declension = declension;
    }

    public List<String> getEnglishForms() {
        return englishForms;
    }

    public void setEnglishForms(List<String> englishForms) {
        this.englishForms = englishForms;
    }

    public String getIrishForm() {
        return irishForm;
    }

    public void setIrishForm(String irishForm) {
        this.irishForm = irishForm;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender.toUpperCase().equals(Gender.FEMININE.name()) ? Gender.FEMININE : Gender.MASCULINE;
    }
}
