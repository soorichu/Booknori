package co.soori.booknori;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
    private String htmlPageUrl;
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
        String imageString;
        byte[] imageByte;
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
            if(item.imageByte!=null) {
                Bitmap decodedByte = BitmapFactory.decodeByteArray(item.imageByte, 0, item.imageByte.length);
                if(decodedByte.getRowBytes()>1)
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
                    itemClick(item);
                }
            });
            return retView;

        }


    }


    private void itemClick(bookItem item){
        Toast.makeText(getApplicationContext(), item.name, Toast.LENGTH_SHORT).show();
        final ClipboardManager clipboardManager =  (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", item.imageString));
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
            htmlPageUrl = "https://www.google.co.kr/search?tbm=bks&q=";

            if(bookName!=null && bookName.length()>0 && bookName != " ") {
                bookName = bookName.replace(" ", "+");
                htmlPageUrl += bookName;
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();
                Elements links = doc.select("div.rc");

                int num = 0;

                for (Element link : links) {
                    String title, author, image, isbn;
                    bookItem item = new bookItem();

                    try {
                        title = link.select("h3.r").text();
                        title = title.replace("null", "");
                        item.name = title;
                    }catch(Exception e){
                        title = "";
                    }
                    try {
                        author = link.select("a.fl").first().text();
                        author = author.replace("null", "");
                        item.author = author;
                    }catch(Exception e){
                        author = "";
                    }
                    try {
                        isbn = link.select("cite._Rm").first().text();
                        isbn = isbn.substring(isbn.indexOf("=") + 1);
                        isbn = isbn.replace("null", "");
                        item.isbn = isbn;
                    }catch(Exception e){
                        isbn = "";
                    }
                    try{
                        image = link.select("img#bksthumb"+(num+1)).first().attr("src");
                        image = image.substring(image.indexOf(",") + 1);
               //         image = "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";
                        item.imageString = image;
                        try {
                            item.imageByte = Base64.decode(image, Base64.DEFAULT);
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }catch(Exception e){
                        image = "";
                    }

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
