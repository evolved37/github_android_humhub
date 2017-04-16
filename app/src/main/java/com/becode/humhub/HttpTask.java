package com.becode.humhub;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;

public class HttpTask extends AsyncTask<Void, Void, JSONObject> {

    String result;
    String url;
    JSONObject json;
    JSONObject json_result;
    Boolean show_progress;
    List<NameValuePair> nameValuePairs;
    private OnTaskCompleted listener;
    private Context mContext;
    private ProgressDialog pd;

    public HttpTask(OnTaskCompleted listener, Context context, String url, List<NameValuePair> data, Boolean progress) {
        this.listener = listener;
        this.mContext = context;
        this.nameValuePairs = data;
        this.url = url;
        this.show_progress = progress;
    }

    protected void onPostExecute(JSONObject result) {

        if (show_progress == true) {
            pd.dismiss();
        }

        // Call the interface method
        if (listener != null && result != null)
            listener.onTaskCompleted(result);
    }

    @Override
    protected void onPreExecute() {
        Log.d("H2", "PREEXE");

        if (show_progress == true) {
            pd = new ProgressDialog(mContext);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Connessione in corso...");
            pd.setCancelable(false);
            pd.show();
        }
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        byte[] Bresult = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        try {

            if (nameValuePairs != null) {
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            }

            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                Bresult = EntityUtils.toByteArray(response.getEntity());
                result = new String(Bresult, "UTF-8");
            }

            Log.d("HTTP TASK", "Server response:" + result);


        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json_result;
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(JSONObject result);
    }
}