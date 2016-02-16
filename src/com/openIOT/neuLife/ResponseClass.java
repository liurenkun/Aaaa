package com.openIOT.neuLife;

/**
 * Created by hyin on 2/11/16.
 */
import java.util.HashMap;
import java.util.Map;

public class ResponseClass {
    public static final String CODE_INVALID_EMAIL = "invalid_email";
    public static final String CODE_INVALID_PASSWORD = "invalid_password";
    public static final String CODE_ACCOUNT_EXISTS = "account_exists";
    public static final String CODE_PASSWORD_HASH_FAILED = "password_hash_failed";
    public static final String CODE_UNKNOWN = "unknown_error";


    boolean ok;
    Map<String, String> data = new HashMap<String, String>();

    public boolean getOk(){ return ok; }

    public void setOk(boolean ok) { this.ok = ok; }

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


    public ResponseClass(boolean ok){
        this.ok = ok;
    }

    public ResponseClass(){

    }
}
