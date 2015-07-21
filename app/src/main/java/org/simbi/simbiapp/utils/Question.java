package org.simbi.simbiapp.utils;


import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("id")
    private int id;

    @SerializedName("asked_by")
    private String askedBy;

    @SerializedName("question")
    private String question;

    public int getId() {
        return id;
    }

    public String getAskedBy() {
        return askedBy;
    }

    public String getQuestion() {
        return question;
    }
}
