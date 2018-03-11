package co.soori.booknori;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.List;

import co.soori.booknori.db.Book;
import co.soori.booknori.db.DbBookHelper;

public class SearchingActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private EditText searchEdit;
    private String htmlPageUrl = "https://www.google.co.kr/search?tbm=bks&q=";
    private String bookName;

    private ListView googleBookList;
    private ArrayList<bookItem> listAllItems = new ArrayList<bookItem>();
    private BookListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);


        searchEdit = (EditText) findViewById(R.id.google_search_edit);
        //       textviewHtmlDocument = (TextView) findViewById(R.id.html_text);


        fab = (FloatingActionButton) findViewById(R.id.google_search_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        setupList();
                        setupAdapter();
                    }
                }.start();

            }


        });
    }

    private void setupList(){
        googleBookList = (ListView) findViewById(R.id.google_search_list);
    }
    private void setupAdapter(){
        JsoupAsyncTask adapterAsyncTask = new JsoupAsyncTask(SearchingActivity.this);
        adapterAsyncTask.execute();
    }

    public class bookItem{
        String name;
        String author;
        String image;
        String isbn;
    }

    public class BookListAdapter extends ArrayAdapter<bookItem>{
        public BookListAdapter(Context context){
            super(context, R.layout.google_book_item);
            setSource(listAllItems);
        }

        @Override
        public void bindView(View view, bookItem item){
            TextView name = (TextView) view.findViewById(R.id.google_book_name);
            name.setText(item.name);
            TextView author = (TextView) view.findViewById(R.id.google_book_author);
            author.setText(item.author);
            TextView isbn = (TextView) view.findViewById(R.id.google_book_isbn);
            isbn.setText(item.isbn);
            ImageView image = (ImageView) view.findViewById(R.id.google_book_image);
            if(item.image!=null && item.image!="") {
                byte[] decodedString = Base64.decode(item.image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if(decodedByte.getRowBytes() > 1)
                    image.setImageBitmap(decodedByte);
            }
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent){
            View retView = super.getView(position, converView, parent);
            final int pos = position;
            final View parView = retView;

            TextView name = (TextView) retView.findViewById(R.id.google_book_name);
            name.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    bookItem item = listAllItems.get(pos);
                    itemClick(item.name);
                }
            });
            return retView;

        }


    }


    private void itemClick(String name){
        Toast.makeText(getApplicationContext(), name + " is selected", Toast.LENGTH_SHORT).show();
    }


    private class JsoupAsyncTask extends AsyncTask<String, Void, String> {
        Context mContext;


        public JsoupAsyncTask(Context context){
            mContext = context;
        }

        @Override
        protected void onPreExecute() {

            bookName = searchEdit.getText().toString();
            listAllItems.clear();

            if(bookName!=null && bookName.length()>0 && bookName != " ") {
                bookName = bookName.replace(" ", "+");
                htmlPageUrl += bookName;
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();
                Elements links = doc.select("div.g");

                int num = 0;

                for (Element link : links) {
                    String title, author, image, isbn;
                    try {
                        title = link.select("h3.r").text();
                        title = title.replace("null", "");
                    }catch(Exception e){
                        title = "";
                    }
                    try {
                        author = link.select("a.fl").first().text();
                        author = author.replace("null", "");
                    }catch(Exception e){
                        author = "";
                    }
                    try {
                        isbn = link.select("cite._Rm").first().text();
                        isbn = isbn.substring(isbn.indexOf("=") + 1);
                        isbn = isbn.replace("null", "");
                    }catch(Exception e){
                        isbn = "";
                    }
                    try{
                        image = link.select("img#bksthumb"+(num+1)).first().attr("src");
                        image = image.substring(image.indexOf(",") + 1);
                        image = image.replace("null", "");
                    }catch(Exception e){
                        image = "";
                    }
                    bookItem item = new bookItem();
                    item.name = title;
                    item.author = author;
                    item.isbn = isbn;
                    item.image = image;
                    listAllItems.add(item);
                    num++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listAdapter = new BookListAdapter(mContext);
            googleBookList.setAdapter(listAdapter);

        }
    }



}
