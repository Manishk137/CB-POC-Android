package com.example.myapplication.model;

public class Message {
    private String text;
    private Long time;
    //private MemberData memberData;
    private boolean belongsToCurrentUser;

    public Message(String text, boolean belongsToCurrentUser, Long time) {
        this.text = text;
        this.time = time;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public Long getTime() {
        return time;
    }

//    public MemberData getMemberData() {
//        return memberData;
//    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}
