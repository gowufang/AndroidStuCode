package me.wufang.httprequestpra;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import me.wufang.httprequestpra.entity.Games;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView responseText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequestButton = (Button) findViewById(R.id.btn_send_response);
        Button sendRequestButton2 = (Button) findViewById(R.id.btn_send_response2);
        Button parseWithJson = (Button) findViewById(R.id.btn_parse_with_json);
        Button parseWithGson = (Button) findViewById(R.id.btn_parse_with_gson);


        sendRequestButton.setOnClickListener(this);
        sendRequestButton2.setOnClickListener(this);
        parseWithJson.setOnClickListener(this);
        parseWithGson.setOnClickListener(this);

        responseText = (TextView) findViewById(R.id.tv_response);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_response:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                            URL url = new URL("https://www.baidu.com");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            InputStream in = connection.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder stringBuilder = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                stringBuilder.append(line);
                            }
                            showResponse(stringBuilder.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            case R.id.btn_send_response2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url("https://www.baidu.com")
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            showResponse(responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            case R.id.btn_parse_with_json:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Request request = new Request.Builder().url("http://10.0.2.2/get_data.json").build();

                            Response response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            parseWithJSON(responseData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            case R.id.btn_parse_with_gson:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Request request = new Request.Builder().url("http://10.0.2.2/get_data.json")
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            Log.d("parseWithGSON", responseData);
                            parseWithGSON(responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
        }
    }

    private void parseWithGSON(String responseData) {

        Gson gson = new Gson();
        // List<Person> people = gson.fromJson(jsonData, new TypeToken<List<Person>>() {}.getType()

//                Games games=gson.fromJson(responseData,Games.class);

        List<Games> games = gson.fromJson(responseData, new TypeToken<List<Games>>() {
        }.getType());
        for (Games game : games) {
            final String id = game.getId();
            final String name = game.getName();
            final String version = game.getVersion();
            Log.d("parseWithGSON", "id" + id);
            Log.d("parseWithGSON", "name" + name);
            Log.d("parseWithGSON", "version" + version);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, id + name + version, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void parseWithJSON(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String id = jsonObject.getString("id");
                final String name = jsonObject.getString("name");
                final String version = jsonObject.getString("version");
                Log.d("parseWithJSON", "id" + id);
                Log.d("parseWithJSON", "name" + name);
                Log.d("parseWithJSON", "version" + version);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, id + " " + name + " " + version, Toast.LENGTH_LONG).show();
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showResponse(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(s);
            }
        });
    }
}
