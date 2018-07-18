package net.gulbers.lib.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import net.gulbers.glib.utils.Debug;
import net.gulbers.glib.utils.HttpHelper;
import net.gulbers.glib.view.GLoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GLoadingDialog(this, R.drawable.g_loading_logo).show();

        Debug.e(true, "TAG", "my error message");

        String url = "your/url/api"; // http url to post or get
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Sony");
            jsonObject.put("age", "30");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // put your json object here !
        new HttpHelper(new HttpHelper.HttpListener() {
            @Override
            public void onResponse(String response) {
                // here is the success response
                // you cannot update view here, use handler().post() instead.
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // here is where you can update the view
                    }
                });
            }

            @Override
            public void onFailed(String msg) {
                // this is what you get on failed
            }
        }).run(url, HttpHelper.POST, jsonObject);

        try {
            JSONArray files = new JSONArray();

            JSONObject file = new JSONObject();
            file.put("filename", "path/to/file.jpg");
            files.put(file);

            new HttpHelper(new HttpHelper.HttpListener() {
                @Override
                public void onResponse(String response) {
                    // here is the success response
                    // you cannot update view here, use handler().post() instead.
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // here is where you can update the view
                        }
                    });
                }

                @Override
                public void onFailed(String msg) {
                    // this is what you get on failed
                }
            }).run(url, jsonObject, files);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("name", "Sony Gultom");
        formBuilder.add("location", "Jakarta, Indonesia");
        new HttpHelper(null).run(url, formBuilder.build());
    }
}
