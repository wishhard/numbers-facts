package com.wishhard.nf.numbersfacts.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.wishhard.nf.numbersfacts.pojos.NpItem;
import com.wishhard.nf.numbersfacts.utils.NumbersApiUrlHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NumbersApiService extends IntentService {

    public static final String MY_SERVICE_MESSAGE = "com.wishhard.cm.textapp_numberApiService_mgs";
    public static final String NUMBERS_API_URI_ACTION = "com.wishhard.cm.textapp_naua";
    public static final String ACTION_NUMBER_API_PAYLOAD = "com.wishhard.cm.textapp.action.nas_load";

    public static final String ACTION_ERROR_FROM_NNMB_API = "com.wishhard.cm.textapp.error_mg.from.numb";

    public static final String[] ERROR_FROM_TAB = {"tab1","tab2","tab3"};

    private NpItem npItem;
    private String error;


    public NumbersApiService() {
        super("NumbersApiService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        NumbersApiUrlHelper nh = intent.getParcelableExtra(NUMBERS_API_URI_ACTION);
        error = intent.getStringExtra(ACTION_ERROR_FROM_NNMB_API);

        try {
            requestor(nh);
        } catch (IOException e) {

        }


    }

    private void requestor(NumbersApiUrlHelper nh) throws IOException {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false);

        final OkHttpClient client = clientBuilder.build();

        Request.Builder rqBuilder = new Request.Builder().url(nh.requestAddress());

        Request request = rqBuilder.build();



        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String s = response.body().string();
            npItem = getJsonToNpItem(s);

            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(ACTION_NUMBER_API_PAYLOAD, npItem);
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
        } else {

            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(ACTION_NUMBER_API_PAYLOAD, npItem);
            messageIntent.putExtra(ACTION_ERROR_FROM_NNMB_API,error);
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            throw new IOException("Error is " + response.code());
        }



    }

    private NpItem getJsonToNpItem(String jsonStr) {
        NpItem npItem =  null;

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonP = jsonParser.parse(jsonStr);

            JsonObject jsonObject = jsonP.getAsJsonObject();

            npItem = new NpItem();
            npItem.setFact(jsonObject.get("text").getAsString());
            npItem.setFound(jsonObject.get("found").getAsBoolean());
            npItem.setType(jsonObject.get("type").getAsString());

            return npItem;
        } catch (JsonSyntaxException e) {

        }

        return npItem;

    }


}
