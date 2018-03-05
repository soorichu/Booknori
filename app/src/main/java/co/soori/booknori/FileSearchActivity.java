package co.soori.booknori;

import android.Manifest;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileSearchActivity extends AppCompatActivity {

    int totalSelected = 0;
    private EditText editView;
    private ListView listView;
    private TextView textView;
    MyListAdapter listadapter;
    ArrayList<listItem> listAllItems = new ArrayList<listItem>();
    ArrayList<listItem> listDispItems = new ArrayList<listItem>();
    private String root = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String CurPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private ArrayList<String> itemFiles = new ArrayList<String>();
    private ArrayList<String> pathFiles = new ArrayList<String>();
    private ArrayList<Integer> isDirs = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_search);

        textView = (TextView)findViewById(R.id.filePathText);
        setupList();
        setupAdapter();
        setupFilter();
        setupPermission();

    }

    private void setupPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        setupAdapter();
    }

    private void setupList(){
        listView = (ListView) findViewById(R.id.fileList);
    }

    public class listItem{
        int selectedNumber;
        boolean checked;
        String name;
        String path;
        int isdir;
    }

    public class MyListAdapter extends ArrayAdapter<listItem>{
        public MyListAdapter(Context context){
            super(context, R.layout.file_item);
            totalSelected = 0;
            setSource(listDispItems);
        }
        @Override
        public void bindView(View view, listItem item) {
            TextView name = (TextView) view.findViewById(R.id.name_saved);
            name.setText(item.name);
            CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox_saved);
            cb.setChecked(item.checked);
            ImageView imv = (ImageView) view.findViewById(R.id.file_icon);
            if(item.isdir == 1)
                imv.setImageDrawable(getResources().getDrawable(R.drawable.ic_folder_open_black_24dp));
            else if(item.isdir == 2)
                imv.setImageDrawable(getResources().getDrawable(R.drawable.ic_insert_drive_file_black_24dp));
            else if(item.isdir == 0)
                imv.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View retView = super.getView(position, convertView, parent);
            final int pos = position;
            final View parView = retView;
            CheckBox cb = (CheckBox) retView.findViewById(R.id.checkBox_saved);

            cb.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    listItem item = listDispItems.get(pos);
                    item.checked = !item.checked;
                    if(item.checked) {
                        totalSelected++;
                        item.selectedNumber = totalSelected;
                        Toast.makeText(FileSearchActivity.this, "Select: " + item.name
                                + ", Total: " + totalSelected, Toast.LENGTH_SHORT).show();
                        printDebug();
                    } else {
                        totalSelected--;
                        item.selectedNumber = totalSelected;
                        printDebug();
                    }

                }
            });
            TextView name = (TextView)retView.findViewById(R.id.name_saved);
            name.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    listItem item = listDispItems.get(pos);
                    itemClick(item.name, item.path);
                }
            });

            return retView;
        }

        public void fillter(String searchText) {
            listDispItems.clear();
            totalSelected = 0;
            for(int i = 0;i<listAllItems.size();i++){
                listItem item = listAllItems.get(i);
                item.checked = false;
                item.selectedNumber = 0;
            }
            if(searchText.length() == 0)
            {
                listDispItems.addAll(listAllItems);
            }
            else
            {
                for( listItem item : listAllItems)
                {
                    if(item.name.contains(searchText))
                    {
                        listDispItems.add(item);
                    }
                }
            }
            notifyDataSetChanged();
        }

        public void rootfillter(String s){
            final String search = s;
            if(search!=null && search.length()>0) {
                listDispItems.clear();
                totalSelected = 0;
                File f = new File(root);
                File[] list = f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(search) || name.contains(search);
                    }
                });

                listItem item = new listItem();
                for (int i = 0; i < list.length; i++) {
                    item.checked = false;
                    item.name = list[i].getName();
                    item.path = list[i].getPath();
                    item.isdir = 3;
                    listDispItems.add(item);
                }
                notifyDataSetChanged();
            }
        }
    }

    public class AdapterAsyncTask extends AsyncTask<String,Void,String> {
        private ProgressDialog mDlg;
        Context mContext;

        public AdapterAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDlg = new ProgressDialog(mContext);
            mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDlg.setMessage( "loading" );
            mDlg.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            // listAllItems MyListItem
            listAllItems.clear();
            listDispItems.clear();

            getDirInfo(CurPath);

            for(int i=0;i<itemFiles.size();i++){
                listItem item = new listItem();
                item.checked = false;
                item.name = itemFiles.get(i);
                item.path = pathFiles.get(i);
                item.isdir = isDirs.get(i);
                listAllItems.add(item);
                Log.w("item.name", item.name);

            }

            if (listAllItems != null) {
                Collections.sort(listAllItems, nameComparator);
            }
            listDispItems.addAll(listAllItems);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mDlg.dismiss();
            listadapter = new MyListAdapter(mContext);
            listView.setAdapter(listadapter);

            String searchText = editView.getText().toString();
            if( listadapter!=null ) listadapter.rootfillter(searchText);

            textView.setText("Location: " + CurPath);
        }
        private final Comparator<listItem> nameComparator
                = new Comparator<listItem>() {
            public final int
            compare(listItem a, listItem b) {
                return collator.compare(a.name, b.name);
            }
            private final Collator collator = Collator.getInstance();
        };
    }
    private void setupAdapter() {
        AdapterAsyncTask adaterAsyncTask = new AdapterAsyncTask(FileSearchActivity.this);
        adaterAsyncTask.execute();
    }
    private void setupFilter() {
        editView=(EditText)findViewById(R.id.savefilter);
        editView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String searchText = editView.getText().toString();
                if( listadapter!=null ) listadapter.fillter(searchText);
            }
        });
    }

    private int getSelectedItemCount() {
        int checkcnt = 0;
        for(int i=0;i<listDispItems.size();i++){
            listItem item = listDispItems.get(i);
            if( item.checked )
                checkcnt++;
        }
        return checkcnt;
    }

    private List<String> getSelectedItems() {
        List<String> ret = new ArrayList<String>();
        int count = 0;
        for(int i=0;i<listDispItems.size();i++){
            listItem item = listDispItems.get(i);
            if( item.checked ) {
                if( count < item.selectedNumber ){
                    count = item.selectedNumber;
                }
            }
        }
        for(int j=1;j<=count;j++) {
            for (int i = 0; i<listDispItems.size() ;i++ ){
                listItem item = listDispItems.get(i);
                if( item.checked && item.selectedNumber == j){
                    ret.add(item.name);
                }
            }
        }
        return ret;
    }
    private String getSelectedItem() {
        List<String> ret = new ArrayList<String>();
        for(int i=0;i<listDispItems.size();i++){
            listItem item = listDispItems.get(i);
            if( item.checked ) {
                return item.name;
            }
        }
        return "";
    }
    private void printDebug() {
        StringBuilder sb = new StringBuilder();
        sb.append("Count:"+getSelectedItemCount()+"\n");
        sb.append("getSelectedItem:"+getSelectedItem()+"\n");
        sb.append("getSelectedItems:");
        List<String> data = getSelectedItems();
        for(int i=0;i<data.size();i++){
            String item = data.get(i);
            sb.append(item+",");
        }
        //textView.setText(sb.toString());
    }

    private void getDirInfo(String dirPath)
    {
        if(!dirPath.endsWith("/"))
            dirPath = dirPath+"/";

        File f = new File(dirPath);
        File[] files = f.listFiles();

        if( files == null )
            return;

        itemFiles.clear();
        pathFiles.clear();
        isDirs.clear();

        if( !root.endsWith("/") )
            root = root+"/";

        if( !root.equals(dirPath) ) {
            itemFiles.add("../");
            pathFiles.add(f.getParent());
            isDirs.add(0);
        }

        for(int i=0; i < files.length; i++){
            File file = files[i];
            pathFiles.add(file.getPath());

            if(file.isDirectory()){
                itemFiles.add(file.getName() + "/");
                isDirs.add(1);}
            else {
                itemFiles.add(file.getName());
                isDirs.add(2);
            }
        }
    }

    private void itemClick(String name, String path) {
        File file = new File(path);
        if (file.isDirectory())
        {
            if(file.canRead()) {
                CurPath = path;
                setupAdapter();
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
            }
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("[" + file.getName() + "]")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
        }
    }
}
