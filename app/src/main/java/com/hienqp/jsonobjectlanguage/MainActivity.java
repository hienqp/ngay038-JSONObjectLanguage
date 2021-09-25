package com.hienqp.jsonobjectlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButtonVN, imageButtonEN;
    TextView textViewInformation;

    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureViewObjectFromLayout();

        // khi thực thi AsyncTask với đường dẫn INTERNET phải khai báo uses-permission INTERNET trong AndroidManifest.xml
        new ReadJSONAsyncTask().execute("https://khoapham.vn/KhoaPhamTraining/json/tien/demo3.json");

        imageButtonVN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseLanguage("vn");
            }
        });

        imageButtonEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseLanguage("en");
            }
        });
    }

    private class ReadJSONAsyncTask extends AsyncTask<String, Void, String> {
        StringBuilder stringBuilder = new StringBuilder();
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            content = s;

            chooseLanguage("vn");
        }
    }

    private void chooseLanguage(String languageCode) {
        try {
            JSONObject object = new JSONObject(content);
            JSONObject languageObject = object.getJSONObject("language");
            JSONObject vnLanguageObject = languageObject.getJSONObject(languageCode);
            String name = vnLanguageObject.getString("name");
            String address = vnLanguageObject.getString("address");
            String course1 = vnLanguageObject.getString("course1");
            String course2 = vnLanguageObject.getString("course2");
            String course3 = vnLanguageObject.getString("course3");

            displayToTextView(name, address, course1, course2, course3);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayToTextView(String name, String address, String course1, String course2, String course3) {
        textViewInformation.setText(name + "\n" + address + "\n" + course1 + "\n" + course2 + "\n" + course3);
    }

    private void captureViewObjectFromLayout() {
        imageButtonEN = (ImageButton) findViewById(R.id.imageButton_en);
        imageButtonVN = (ImageButton) findViewById(R.id.imageButton_vn);
        textViewInformation = (TextView) findViewById(R.id.textView_information );
    }
}