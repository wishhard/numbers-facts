package com.wishhard.nf.numbersfacts.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.wishhard.nf.numbersfacts.pojos.ImgLinksPojo;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PixaBayService extends IntentService {

    public static final String MY_SERVICE_MESSAGE = "com.wishhard.cm.textapp_PixaBayService_mgs";
    public static final String ACTION_PB_SQ = "com.wishhard.cm.textapp.action.pb_sq";
    public static final String ACTION_PIXABAY_PAYLOAD = "com.wishhard.cm.textapp.action.pb_load";
    public static final String ACTION_PIXABAY_EROR = "com.wishhard.cm.textapp.action.pb_ERROR";

    private ImgLinksPojo[] arr;

    private static final String PIXABAY_API_KEY = "10847657-90cfab837c43e8b77127f2777";
    public PixaBayService() {
        super("PixaBayService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String sq = intent.getStringExtra(ACTION_PB_SQ);
        try {
            requestor(sq);
        } catch (IOException e) {

        }
    }

    private void requestor(String sq) throws IOException {

        HttpUrl urlB = new HttpUrl.Builder().scheme("http")
                .host("www.pixabay.com")
                .addPathSegment("api")
                .addQueryParameter("key",PIXABAY_API_KEY)
                .addQueryParameter("q",sq)
                .addQueryParameter("image_type","photo").build();
        String url  = urlB.toString();

        OkHttpClient client = new OkHttpClient();
        Request.Builder rqBuilder = new Request.Builder().url(url);
        Request request = rqBuilder.build();

        Response  response = client.newCall(request).execute();
        try{
            if(response.isSuccessful()) {
                String s = response.body().string();
                arr = imgLnksArr(s);

                Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
                messageIntent.putExtra(ACTION_PIXABAY_PAYLOAD, arr);
                LocalBroadcastManager manager =
                        LocalBroadcastManager.getInstance(getApplicationContext());
                manager.sendBroadcast(messageIntent);
            }
        } catch (IOException e) {
            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(ACTION_PIXABAY_PAYLOAD, arr);
            messageIntent.putExtra(ACTION_PIXABAY_EROR,"error");
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            throw new IOException("Error is " + response.code());
        }
    }


    private ImgLinksPojo[] imgLnksArr(String jsonStr) {
        ImgLinksPojo[] items = null;

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonP = jsonParser.parse(jsonStr);

            JsonObject jsonObject = jsonP.getAsJsonObject();
            JsonArray hits = jsonObject.getAsJsonArray("hits");
            items = new ImgLinksPojo[hits.size()];
            for (int i = 0; i < hits.size(); i++) {
                jsonObject = hits.get(i).getAsJsonObject();

                ImgLinksPojo item = new ImgLinksPojo();
                item.setLargeImgUrl(jsonObject.get("webformatURL").getAsString());
                item.setThumbnailImgUrl(jsonObject.get("previewURL").getAsString());
                items[i] = item;
            }
            return items;
        } catch (JsonSyntaxException e) {

        }

        return items;

    }

}
