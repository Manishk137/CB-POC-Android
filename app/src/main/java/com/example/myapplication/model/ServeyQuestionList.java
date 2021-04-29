package com.example.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServeyQuestionList {

    @SerializedName("questionId")
    @Expose
    private Integer questionId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("type")
    @Expose
    private String type;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}