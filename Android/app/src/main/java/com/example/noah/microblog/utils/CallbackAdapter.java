package com.example.noah.microblog.utils;

import android.app.Activity;
import com.example.noah.microblog.form.ResponseForm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public abstract class CallbackAdapter implements Callback {
    private Activity activity;

    public CallbackAdapter(Activity activity) {
        this.activity = activity;
    }

    public abstract void onParseResponseForm(ResponseForm response);

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String responseJSON = response.body().string();
        try {
            JSONObject jsonObject = new JSONObject(responseJSON);
            Integer status = jsonObject.getInt("status");
            String message = jsonObject.getString("message");
            Object data = jsonObject.get("data");
            final ResponseForm responseForm = new ResponseForm(status, message, data);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onParseResponseForm(responseForm);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
