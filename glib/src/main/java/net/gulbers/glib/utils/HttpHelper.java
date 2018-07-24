package net.gulbers.glib.utils;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by sgultom on 2/13/2018.
 */
public class HttpHelper {
    public static final int GET = 0;
    public static final int POST = 1;

    private static final String TAG = HttpHelper.class.getName();
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; " +
            "charset=utf-8");

    private HttpListener mListener;

    private int timeout;
    private boolean isDebug = false;

    /**
     * {@link HttpHelper} class
     *
     * @param listener {@link HttpListener} listener for http result
     */
    public HttpHelper(HttpListener listener) {
        this.mListener = listener;
        this.timeout = 10;
    }

    /**
     * Display debug option, default value is false
     *
     * @param debug true or false, default is false
     */
    public void setDebug(boolean debug) {
        this.isDebug = debug;
    }

    /**
     * Set http request timeout in second
     *
     * @param t Timeout in second
     */
    public void setTimeout(int t) {
        this.timeout = t;
    }

    /**
     * Run http request with JSON, method will be as POST, event method passed was GET
     *
     * @param url        Url > 5 char
     * @param method     GET: 0, POST: 1
     * @param jsonObject {@link JSONObject} data
     */
    public void run(String url, int method, JSONObject jsonObject) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS).readTimeout(timeout, TimeUnit.SECONDS)
                .build();
        Request.Builder reqBuilder;
        try {
            reqBuilder = new Request.Builder().url(url);
        } catch (Exception e) {
            if (mListener != null) {
                mListener.onFailed(e.getMessage());
            }
            return;
        }
        if (method == GET || jsonObject == null) {
            jsonObject = new JSONObject();
        }

        // add aditional info to prevent cache
        try {
            jsonObject.put("glib_timestamps", MUtils.getTimestamps());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.e(isDebug, TAG, "post params: " + jsonObject.toString());
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, jsonObject.toString());
        reqBuilder.post(body);

        Request request = reqBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (mListener != null) {
                    mListener.onFailed(e.getMessage());
                    Debug.e(isDebug, TAG, "onFailure: " + e.getMessage());
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws
                    IOException {
                if (!response.isSuccessful()) {
                    if (mListener != null) {
                        Debug.e(isDebug, TAG, "response unsuccessfully ");
                        mListener.onFailed("");
                    }
                } else {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String responseString = responseBody.string();
                        Debug.e(isDebug, TAG, "response: " + responseString);
                        if (mListener != null) {
                            mListener.onResponse(responseString);
                        }
                        responseBody.close();
                    } else {
                        if (mListener != null) {
                            mListener.onFailed("");
                        }
                    }
                }
            }
        });
    }

    /**
     * Post multipart form
     *
     * @param url       URL
     * @param postdata  {@link JSONObject} data
     * @param postFiles {@link JSONArray} files
     */
    public void run(String url, JSONObject postdata, JSONArray postFiles) {
        Request.Builder reqBuilder;
        try {
            reqBuilder = new Request.Builder().url(url);
        } catch (Exception e) {
            if (mListener != null) {
                mListener.onFailed(e.getMessage());
            }
            return;
        }

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        MediaType MEDIA_TYPE_IMG;

        Debug.e(isDebug, TAG, "url:" + url);
        Iterator keys = postdata.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                builder.addFormDataPart(key, postdata.getString(key));
                Debug.e(isDebug, TAG, "post " + key + ": " + postdata.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (postFiles != null && postFiles.length() > 0) {
            for (int i = 0; i < postFiles.length(); i++) {
                Debug.e(isDebug, TAG, "file" + i + "/" + postFiles.length());
                try {
                    JSONObject jObj = postFiles.getJSONObject(i);
                    keys = jObj.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        try {
                            File f = new File(jObj.getString(key));
                            Debug.e(isDebug, TAG, "file post: " + key + ": " + jObj.getString(key));
                            MEDIA_TYPE_IMG = jObj.getString(key).toLowerCase(Locale.getDefault()).endsWith("png") ?
                                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

                            builder.addFormDataPart("uploaded_file[]", key, RequestBody.create
                                    (MEDIA_TYPE_IMG, f));
                        } catch (JSONException e) {
                            Debug.e(isDebug, TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    Debug.e(isDebug, TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        RequestBody body = builder.build();
        Request request = reqBuilder.header("Accept", "application/json").header("Content-Type", "multipart/form-data").post(body).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Debug.e(isDebug, TAG, "onFailure: " + e.getMessage());
                if (mListener != null) {
                    mListener.onFailed(e.getMessage());
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (mListener != null) {
                    if (response.isSuccessful()) {
                        String resp = null;
                        if (response.body() != null) {
                            resp = response.body().string();
                        }
                        Debug.e(isDebug, TAG, "onResponse: " + resp);
                        mListener.onResponse(resp);
                    } else {
                        Debug.e(isDebug, TAG, "onResponse: not Successfull !!");
                        mListener.onFailed(response.toString());
                    }
                }
            }
        });
    }

    /**
     * Post http using POST FORM
     *
     * @param url      URL
     * @param formBody Post param in {@link RequestBody}
     */
    public void run(String url, RequestBody formBody) {
        Request.Builder reqBuilder;
        try {
            reqBuilder = new Request.Builder().url(url);
        } catch (Exception e) {
            if (mListener != null) {
                mListener.onFailed(e.getMessage());
            }
            return;
        }

        Debug.e(isDebug, TAG, "http url: " + url);
        Debug.e(isDebug, TAG, "post param: " + formBody.toString());
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS).readTimeout(timeout, TimeUnit.SECONDS)
                .build();
        reqBuilder.post(formBody);

        Request request = reqBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (mListener != null) {
                    mListener.onFailed(e.getMessage());
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws
                    IOException {
                if (!response.isSuccessful()) {
                    if (mListener != null) {
                        mListener.onFailed("");
                    }
                } else {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String responseString = responseBody.string();
                        Debug.e(isDebug, TAG, "response: " + responseString);
                        if (mListener != null) {
                            mListener.onResponse(responseString);
                        }
                        responseBody.close();
                    } else {
                        if (mListener != null) {
                            mListener.onFailed("");
                        }
                    }
                }
            }
        });
    }

    /**
     * Download file to specified path and file name
     *
     * @param url      URL file to be downloaded
     * @param filePath Path of downloaded file, pass null to download to ExternalStorageDirectory
     * @param fileName Filename of downloaded file. Pass null will bi save as the url name
     */
    public void download(String url, String filePath, String fileName) {
        Request.Builder reqBuilder;
        try {
            reqBuilder = new Request.Builder().url(url);
        } catch (Exception e) {
            if (mListener != null) {
                mListener.onFailed(e.getMessage());
            }
            return;
        }

        if (fileName == null) {
            int tmp = url.lastIndexOf("/");
            fileName = url.substring(tmp + 1);
        }
        final String fname = fileName;

        if (filePath == null) {
            filePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        final File fileDir = new File(filePath);

        Debug.e(isDebug, TAG, "http url: " + url);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS).readTimeout(timeout, TimeUnit.SECONDS)
                .build();
        Request request = reqBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (mListener != null) {
                    mListener.onFailed(e.getMessage());
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    if (mListener != null) {
                        mListener.onFailed(response.message());
                    }
                } else {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        boolean res = saveFile(fileDir, fname, responseBody.byteStream());
                        if (mListener != null) {
                            mListener.onResponse(res ? fname : null);
                        }
                        responseBody.close();
                    } else {
                        if (mListener != null) {
                            mListener.onFailed(response.message());
                        }
                    }
                }
            }
        });
    }

    private boolean saveFile(File dir, String fileName, InputStream inputStream) {
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            OutputStream outputStream = new FileOutputStream(new File(dir, fileName));

            byte data[] = new byte[1024];
            int count;
            while ((count = inputStream.read(data)) != -1) {
                // writing data to file
                outputStream.write(data, 0, count);
            }

            outputStream.flush();
            // closing streams
            outputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * HttpListener for result
     */
    public interface HttpListener {
        void onResponse(String response);

        void onFailed(String msg);
    }
}
