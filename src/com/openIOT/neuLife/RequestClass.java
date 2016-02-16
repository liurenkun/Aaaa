package com.openIOT.neuLife;

/**
 * Created by hyin on 2/11/16.
 */
import java.util.Map;
import java.util.HashMap;

public class RequestClass {
    public static final String ACTION_SIGN_UP = "signup";
    public static final String ACTION_LOG_IN = "login";

    String action;
    Map<String, String> data = new HashMap<String, String>();

    public String getAction(){
        return action;
    }

    public void setAction(String action){
        this.action = action;
    }

    public Map<String, String> getData(){ return data; }

    public void setData(HashMap<String, String> data){ this.data = data; }

    public String getDataEntry(String key){
        // TODO
        return data.get(key);
    }

    public void addDataEntry(String key, String value){
        if (data==null){
            data = new HashMap<String, String>();
        }
        data.put(key, value);
    }

    public RequestClass(String action){
        this.action = action;
    }

    public RequestClass(){

    }




}

