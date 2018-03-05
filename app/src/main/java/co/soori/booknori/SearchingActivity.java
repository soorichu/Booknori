package co.soori.booknori;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchingActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private EditText searchEdit;
    private TextView textviewHtmlDocument;
    private String htmlPageUrl = "https://www.google.co.kr/search?tbm=bks&q=";
    private String htmlContent;
    private String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        searchEdit = (EditText) findViewById(R.id.google_search_edit);
        textviewHtmlDocument = (TextView) findViewById(R.id.html_text);

        fab = (FloatingActionButton) findViewById(R.id.google_search_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                        jsoupAsyncTask.execute();
                    }
                }.start();

            }


        });
    }



    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            bookName = searchEdit.getText().toString();
            if(bookName!=null && bookName.length()>0 && bookName != " ") {
                bookName = bookName.replace(" ", "+");
                htmlPageUrl += bookName;
                try {
                    Document doc = Jsoup.connect(htmlPageUrl).get();
                    Elements links = doc.select("div.g");

                    for (Element link : links) {
                        String title, author;
                        try {
                            title = link.select("h3.r").text();
                            title = title.replace("null", "");
                        }catch(Exception e){
                            title = "";
                        }
                        try {
                            author = link.select("a.fl").first().text();
                        }catch(Exception e){
                            author = "";
                        }

                        htmlContent += (title + " (" + author + ")\n\n");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            textviewHtmlDocument.setText(htmlContent);
        }
    }


}
