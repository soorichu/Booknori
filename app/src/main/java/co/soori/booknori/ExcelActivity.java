package co.soori.booknori;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import co.soori.booknori.R;

public class ExcelActivity extends AppCompatActivity {

    private Button textRead, textWrite, excelRead, excelWrite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel);

        textRead = findViewById(R.id.text_read);
        textWrite =findViewById(R.id.text_write);
        excelRead = findViewById(R.id.excel_read);
        excelWrite = findViewById(R.id.excel_write);

        textRead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                readText(getApplicationContext(),"readText.txt");
            }
        });
        textWrite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(writeText(getApplicationContext(), "writeText.txt"))
                    Log.d("Write Text", "Success");
            }
        });
        excelRead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                readExcel(getApplicationContext(), "readExcel.xls");
            }
        });
        excelWrite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(writeExcel(getApplicationContext(), "WriteExcel.xls"))
                    Log.d("Write Excel", "Success");
            }
        });

    }

    public static boolean writeText(Context context, String fileName){
        if(!isExternalStorageAvailable() || isExternalStorageReadOnly()){
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        File file = new File(context.getExternalFilesDir(null), fileName);
        PrintStream p = null;
        boolean success = false;

        try{
            OutputStream os = new FileOutputStream(file);
            p = new PrintStream(os);
            p.println("This is a TEST");
            Log.w("FileUtils", "Writing file " + file);
            success = true;

        }catch(IOException e){
            Log.w("FileUtils", "Error writing " + file, e);
        }catch(Exception e){
            Log.w("FileUtils", "Failed to save file", e);
        }finally{
            try{
                if(null != p)
                    p.close();
            }catch(Exception e){
                Log.w("FileUtils", "Error" + file, e);
            }
        }
        return success;
    }

    private static void readText(Context context, String filename){
        if(!isExternalStorageAvailable() || isExternalStorageReadOnly()){
            Log.w("FileUtils", "Storage not available or read only");
            return;
        }

        FileInputStream fis = null;

        try{
            File file = new File(context.getExternalFilesDir(null), filename);
            fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while((strLine = br.readLine()) != null){
                Log.w("FileUtils", "File data: " + strLine);
                Toast.makeText(context, "File Data: " + strLine, Toast.LENGTH_SHORT).show();
            }
            in.close();

        }catch(Exception e){
            Log.e("FileUtils", "failed to load file", e);
        }finally{
            try{
                if(null!=fis)
                    fis.close();
            }catch(IOException e){
                Log.e("FileUtils", "fail ", e);
            }
        }
        return;
    }

    private static void readExcel(Context context, String filename){
        if(!isExternalStorageAvailable() || isExternalStorageReadOnly()){
            Log.w("FileUtils", "Storage not available or read only");
            return;
        }

        FileInputStream fis = null;

        try{
            File file = new File(context.getExternalFilesDir(null), filename);
            fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while((strLine = br.readLine())!= null){
                Log.w("FileUtils", "File data: " + strLine);
                Toast.makeText(context, "File Data: " + strLine, Toast.LENGTH_SHORT).show();
            }
            in.close();
        }catch(Exception e){
            Log.e("FileUtils", "failed to load file", e);
        }finally {
            try{
                if(null != fis)
                    fis.close();
            }catch(IOException e){

            }

        }
    }

    private static boolean writeExcel(Context context, String fileName){
        if(!isExternalStorageAvailable() || isExternalStorageReadOnly()){
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        boolean success = false;
        Workbook wb = new HSSFWorkbook();
        Cell c;
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        Sheet sheet1 = wb.createSheet("Sheet1");

        Row row = sheet1.createRow(0);
        c = row.createCell(0);
        c.setCellValue("Student ID");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Student Name");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Student Age");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Student Phone");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));

        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try{
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file " + file);
            success = true;
        }catch(IOException e){
            Log.w("FileUtils", "Error writing " + file, e);
        }catch(Exception e){
            Log.w("FileUtils", "Failed to save file ", e);

        }finally{
            try{
                if(null != os)
                    os.close();
            }catch(Exception e){
            }
        }
        return success;

    }

    public static boolean isExternalStorageReadOnly(){
        String extStorageState = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState))
            return false;

        return false;
    }

    public static boolean isExternalStorageAvailable(){
        String extStorageState = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(extStorageState))
            return true;

        return false;
    }
}
