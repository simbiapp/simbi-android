package org.simbi.simbiapp.models;


import com.google.gson.annotations.SerializedName;

/**
 * POJO implementation for Question Object
 */
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
