package com.project.ics.day30networktest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int SHOW_RESPONSE=0;
    private Button sendRequest;
    private TextView responseText;

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response= (String) msg.obj;
                    responseText.setText(response);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendRequest= (Button) findViewById(R.id.send_request);
        responseText= (TextView) findViewById(R.id.response);
        sendRequest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.send_request){
            sendRequestWithHttpURLConnection();
        }


    }
    private void sendRequestWithHttpURLConnection(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL("http://127.0.0.1/get_data.json");
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    InputStream in=connection.getInputStream();
                    InputStreamReader inputStreamReader=new InputStreamReader(in);
                    BufferedReader reader=new BufferedReader(inputStreamReader);

                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }

                    String s=response.toString();
                    parseJSONWithJSONObject(s);


                    Message message=new Message();
                    message.what=SHOW_RESPONSE;

                    message.obj=response.toString();
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String jsonData){
        try {
            JSONArray jsonArray=new JSONArray(jsonData);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id=jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String version=jsonObject.getString("version");
                Log.d("mainaty","id is "+id);
                Log.d("mainaty","name is "+name);
                Log.d("mainaty","version is "+version);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
