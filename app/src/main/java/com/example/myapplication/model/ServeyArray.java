
package com.example.myapplication.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServeyArray {

    @SerializedName("survey_id")
    @Expose
    private Integer surveyId;
    @SerializedName("survey_name")
    @Expose
    private String surveyName;
    @SerializedName("serveyQuestionList")
    @Expose
    private List<ServeyQuestionList> serveyQuestionList = null;

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public List<ServeyQuestionList> getServeyQuestionList() {
        return serveyQuestionList;
    }

    public void setServeyQuestionList(List<ServeyQuestionList> serveyQuestionList) {
        this.serveyQuestionList = serveyQuestionList;
    }

}
