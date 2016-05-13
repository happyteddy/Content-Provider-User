package com.example.angelina_wu.bookstoreuser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;

public class MainActivity extends AppCompatActivity {
    public static final String USER_INFO_PREFS = "user_info_prefs";
    public static final String USER_INFO_PREFS_NAME = "NAME";
    public static final String USER_INFO_PREFS_ID = "ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enter(View view) {
        EditText name = (EditText) findViewById(R.id.userName);
        EditText id = (EditText) findViewById(R.id.userId);

        SharedPreferences prefs = getSharedPreferences(USER_INFO_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        if ((name != null) && (id != null)) {
            try {
                editor.putString(USER_INFO_PREFS_NAME, name.getText().toString());
                editor.putInt(USER_INFO_PREFS_ID, Integer.parseInt(id.getText().toString()));
                editor.commit(); //commit() 直接將異動結果寫入檔案
                name.setText("");
                id.setText("");
                Intent intent = new Intent(this, UserActivity.class);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(getBaseContext(), " NumberFormatException !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
