package com.example.datastorageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String FILENAME="namafile.txt";
    public static final String PREFNAME="com.example.datastorageapp.PREF";
    private TextView textBaca;
    private EditText editText;
    //Untuk external storage
    private static final String[] PERMISSIONS={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textBaca=findViewById(R.id.text_baca);
        editText=findViewById(R.id.edit_text);
    }
    //Mengecek permission sudah diijinkan atau belum pada External Storage
    //String... = string array
    private static boolean hasPermission(Context context, String... permissions){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && permissions!= null){
            for (String permission:permissions){
                if(ActivityCompat.checkSelfPermission(context,permission)
                        != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    public void simpan(View view) {
        //simpanFileSP();
        //simpanFileIS();
        simpanFileES();
    }

    public void hapus(View view) {
//        deleteFileSP();
//        deleteFileIS();
        deleteFileES();
    }

    public void baca(View view) {
//        bacaFileSP();
//        bacaFileIS();
        bacaFileES();
    }


    //InternalStorage
    public void simpanFileIS(){
        String isiFile = editText.getText().toString();
        //Buat File baru dengan isi kosong dan akan diisikan dengan
        //isiFile
        File path = getDir("NEWFOLDER", MODE_PRIVATE);
        File file = new File(path.toString(),FILENAME);
        FileOutputStream outputStream=null;
        try {
            file.createNewFile();
            //true = content file akan ditambah / diteruskan dari file yg tersimpan
            //false = content file akan diganti dengan file baru
            outputStream=new FileOutputStream(file,false);
            outputStream.write(isiFile.getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void bacaFileIS(){
        File path = getDir("NEWFOLDER", MODE_PRIVATE);
        File file=new File(path.toString(), FILENAME);
        if(file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line!=null){
                    text.append(line);
                    line=br.readLine();
                }
                br.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            textBaca.setText(text.toString());
        }else {
            textBaca.setText("");
        }
    }
    public void deleteFileIS(){
        File path = getDir("NEWFOLDER", MODE_PRIVATE);
        File file=new File(path.toString(), FILENAME);
        if(file.exists()){
            file.delete();
        }
    }

    //ExternalStorage
    public void simpanFileES(){
        //Pengecekan apakah user memberikan ijin ke external storage
        if (hasPermission(this, PERMISSIONS)){
            String isiFile = editText.getText().toString();
            //Buat File baru dengan isi kosong dan akan diisikan dengan
            //isiFile
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path.toString(),FILENAME);
            FileOutputStream outputStream=null;
            try {
                file.createNewFile();
                //true = content file akan ditambah / diteruskan dari file yg tersimpan
                //false = content file akan diganti dengan file baru
                outputStream=new FileOutputStream(file,false);
                outputStream.write(isiFile.getBytes());
                outputStream.flush();
                outputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            ActivityCompat.requestPermissions(this, PERMISSIONS,REQUEST_CODE);
        }
    }
    public void bacaFileES(){
        File path = Environment.getExternalStorageDirectory();
        File file=new File(path.toString(), FILENAME);
        if(file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line!=null){
                    text.append(line);
                    line=br.readLine();
                }
                br.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            textBaca.setText(text.toString());
        }else {
            textBaca.setText("");
        }
    }
    public void deleteFileES(){
        File path = Environment.getExternalStorageDirectory();
        File file=new File(path.toString(), FILENAME);
        if(file.exists()){
            file.delete();
        }
    }

    //SharedPreference
    public void simpanFileSP(){
        String isiFile=editText.getText().toString();
        SharedPreferences sharedPreferences=getSharedPreferences(
                PREFNAME, MODE_PRIVATE
        );
        SharedPreferences.Editor editor=sharedPreferences.edit();
        //Menyimpan file jenisnya tergantung yg ingin disimpan
        editor.putString(FILENAME,isiFile);
        editor.commit();
    }
    public void bacaFileSP(){
        SharedPreferences sharedPreferences=getSharedPreferences(
                PREFNAME,MODE_PRIVATE
        );
        if(sharedPreferences.contains(FILENAME)){
            String myText = sharedPreferences.getString(FILENAME, "");
            textBaca.setText(myText);
        }else {
            textBaca.setText("");
        }
    }
    public void deleteFileSP(){
        SharedPreferences sharedPreferences=getSharedPreferences(
                PREFNAME,MODE_PRIVATE
        );
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}