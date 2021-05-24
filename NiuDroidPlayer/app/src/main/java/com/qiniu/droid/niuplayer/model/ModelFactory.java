package com.qiniu.droid.niuplayer.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModelFactory {
    public interface OnResultListener {
        void onSuccess(int statusCode, Object data);
        void onFailure();
    }

    public static void createVideoItemListByURL(String url, final OnResultListener listener) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<VideoItem> arrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonItems = jsonObject.getJSONArray("Items");
                    for (int i = 0; i < jsonItems.length(); i++) {
                        JSONObject jsonItem = jsonItems.getJSONObject(i);
                        VideoItem item = new VideoItem();
                        item.setName(jsonItem.optString("key"));
                        item.setTime(jsonItem.optString("putTime"));
                        item.setSize(jsonItem.optString("fsize"));
                        arrayList.add(item);
                    }
                    listener.onSuccess(response.code(), arrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure();
                }
            }
        });
    }

    public static void createUpgradeInfoByURL(String url, final OnResultListener listener) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                UpgradeInfo upgradeInfo = new UpgradeInfo();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    upgradeInfo.setAppID(jsonObject.optString("AppId"));
                    upgradeInfo.setVersion(jsonObject.optString("Version"));
                    upgradeInfo.setDescription(jsonObject.optString("Description"));
                    upgradeInfo.setDownloadURL(jsonObject.optString("DownloadURL"));
                    upgradeInfo.setCreateTime(jsonObject.optString("CreateAt"));
                    listener.onSuccess(response.code(), upgradeInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure();
                }
            }
        });
    }
}
