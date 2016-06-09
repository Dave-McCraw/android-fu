package com.mpgs.asma.catalogue.model;

import android.app.ProgressDialog;
        import android.content.Context;
        import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
        import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.mpgs.asma.R;
import com.mpgs.asma.catalogue.model.Item;

public class ItemDownloader extends AsyncTask<URL, Void, List<Item>> {
    public interface ItemDownloadListener<T> {
        void onTaskComplete(List<T> s);
    }

    private HttpURLConnection urlConnection;
    private Context mContext;
    private ProgressDialog mDialog;
    private ItemDownloadListener mListener;

    public ItemDownloader(Context context, ItemDownloadListener listener) {
        this.mContext = context;
        mDialog = new ProgressDialog(mContext);
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog.setTitle(R.string.app_name);
        mDialog.setMessage("Retrieving data...");
        mDialog.show();
    }

    @Override
    protected List<Item> doInBackground(URL... params) {
        StringBuilder result = new StringBuilder();
        List<Item>
        lcs = new ArrayList<Item>();
        try {
            URL url = params[0];
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.1", 8080));
            urlConnection = (HttpURLConnection) url.openConnection(/*proxy*/);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(20 * 1000);
            urlConnection.setReadTimeout(20 * 1000);

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {


                Reader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                JsonReader reader = new JsonReader(in);
                JsonParser parser = new JsonParser();
                Gson gson = new Gson();
                JsonArray jArray = parser.parse(reader).getAsJsonArray();
                for(JsonElement obj : jArray )
                {
                    Item cse = gson.fromJson( obj , Item.class);

                    lcs.add(cse);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }


        return lcs;
    }

    @Override
    protected void onPostExecute(List<Item> s) {
        super.onPostExecute(s);
        mDialog.dismiss();
        mListener.onTaskComplete(s);
    }
}