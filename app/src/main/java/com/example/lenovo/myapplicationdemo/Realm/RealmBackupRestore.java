package com.example.lenovo.myapplicationdemo.Realm;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.lenovo.myapplicationdemo.Class.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;


public class RealmBackupRestore {

    private String exportFileName = "DemoRealm.realm";
    private String importFileName = "demo.realm"; // database name
    private Context context;
    private Realm realm;

    public RealmBackupRestore(Context context) {
        this.realm = Realm.getDefaultInstance();
        this.context = context;
    }

    public void exportIntoStorage() {

        File exportRealmFile;
        new File(Environment.getExternalStorageDirectory().getPath()).mkdirs();
        exportRealmFile = new File(new File(Environment.getExternalStorageDirectory().getPath()), exportFileName);
        exportRealmFile.delete();

        realm.writeCopyTo(exportRealmFile);

        Toast.makeText(context.getApplicationContext(), "File exported to Path: " + new File(Environment.getExternalStorageDirectory().getPath()) + "/" + exportFileName, Toast.LENGTH_LONG).show();
        realm.close();
    }

    public void exportIntoStorageWithEncrypt() {

        File exportRealmFile;
        new File(Environment.getExternalStorageDirectory().getPath()).mkdirs();
        exportRealmFile = new File(new File(Environment.getExternalStorageDirectory().getPath()), exportFileName);
        exportRealmFile.delete();

        realm.writeEncryptedCopyTo(exportRealmFile, Utils.getEncryptKey());

        Toast.makeText(context.getApplicationContext(), "File exported to Path: " + new File(Environment.getExternalStorageDirectory().getPath()) + "/" + exportFileName, Toast.LENGTH_LONG).show();
        realm.close();
    }

    public String importFromStorage() {

        try {
            File file = new File(context.getApplicationContext().getFilesDir(), importFileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(new File(Environment.getExternalStorageDirectory().getPath()) + "/" + exportFileName));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}