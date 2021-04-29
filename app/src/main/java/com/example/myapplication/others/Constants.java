package com.example.myapplication.others;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.myapplication.model.ServeyQuestionList;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    private static String port = "http://43.224.136.47:8081/";
    public static String apiLogin = port + "chatboat/webapi/chat/validate";
    public static String apiChat = port + "chatboat/webapi/chat/track";
    public static String apiSurvey = port + "chatboat/webapi/survey/getSurveyQuestionList";
    public static String apiSearchQuestions = port + "chatboat/webapi/chat/searchQuestions";
    public static String apiSubmitSurvey = port + "chatboat/webapi/survey/submitSurvey";

    public static ArrayList arrayList = new ArrayList();
    public static List<ServeyQuestionList> arrayListFeedBack = null;
    public static String FeedBackData = "";
    public static String userEmail = "";

    @NotNull
    public static Boolean isInternetConnected(@NotNull Context activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static String loadJSONFromAsset(Context context, String file_name) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(file_name + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
