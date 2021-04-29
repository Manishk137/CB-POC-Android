package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ChatData {

    @SerializedName("status")
    @Expose
    internal var status: String? = null

    @SerializedName("question")
    @Expose
    internal var question: String? = null

    @SerializedName("flag")
    @Expose
    internal var flag: String? = null

    @SerializedName("answer")
    @Expose
    internal var answer: String? = null

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getQuestion(): String? {
        return question
    }

    fun setQuestion(question: String) {
        this.question = question
    }

    fun getFlag(): String? {
        return flag
    }

    fun setFlag(flag: String) {
        this.flag = flag
    }

    fun getAnswer(): String? {
        return answer
    }

    fun setAnswer(answer: String) {
        this.answer = answer
    }

}
