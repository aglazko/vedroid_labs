package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> StoreContacts ;
    public  static final int RequestPermissionCode  = 1 ;
    private boolean accessGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            accessGranted = savedInstanceState.getBoolean("android:hasCurrentPermissionsRequest", false);
        }
        StoreContacts = new ArrayList<>();

        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.contacts);
        final ArrayAdapter arrayAdapter = new ArrayAdapter<>(
                this, R.layout.contact, R.id.textView, StoreContacts);

        listView.setAdapter(arrayAdapter);



        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableRuntimePermission();
                StoreContacts.clear();
                GetContactsIntoArrayList();
                arrayAdapter.notifyDataSetChanged();

            }
        });

    }

    public void GetContactsIntoArrayList(){
        if (!accessGranted){
            Toast.makeText(this, "Can't display contacts, no permissions",
                    Toast.LENGTH_LONG).show();
            return;
        }
        String field = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {field},null, null, null);

        while (cursor.moveToNext()) {
            StoreContacts.add(cursor.getString(cursor.getColumnIndex(field)));
        }
        cursor.close();

    }
    private void request(String[] permissions){
        ActivityCompat.requestPermissions(this, permissions, RequestPermissionCode);
    }

    public void EnableRuntimePermission() {
        final String read = Manifest.permission.READ_CONTACTS;

        if (ActivityCompat.checkSelfPermission(this, read) != PackageManager.PERMISSION_GRANTED) {
            request(new String[]{read});
        }
        else{
            accessGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestPermissionCode){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accessGranted = true;
                }
        }
    }
}