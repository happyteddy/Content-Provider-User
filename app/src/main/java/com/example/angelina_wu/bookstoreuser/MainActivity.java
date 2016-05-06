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
    protected SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = getPreferences(MODE_APPEND);
    }
    public void enter (View view) {
        EditText name = (EditText) findViewById(R.id.userName);
        EditText id = (EditText) findViewById(R.id.userId);

        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        try {
            if ((name != null) && (id != null)) {
                editor.putString("NAME", name.getText().toString());
                editor.putInt("ID", Integer.parseInt(id.getText().toString()));
                editor.commit(); //commit() 直接將異動結果寫入檔案
            }
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        }catch (Exception e) {
            Toast.makeText(getBaseContext(), "Something Error !  There's not input data !", Toast.LENGTH_SHORT).show();
        }
        name.setText("");
        id.setText("");
    }
}
