package com.example.angelina_wu.bookstoreuser;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.File;

import static android.provider.BaseColumns._ID;

public class UserActivity extends AppCompatActivity {

    public static final String SELLER_NAME = "seller_name";
    public static final String SELLER_ID = "seller_id";
    public static final String BOOK_NAME = "book_name";
    public static final String PRICE = "price";
    public static final String SOLD = "sold";
    public static final String BUYER_NAME = "buyer_name";
    public static final String BUYER_ID = "buyer_id";
    SharedPreferences mPref;
    String mUserName;
    int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        File file = new File("/data/data/com.example.angelina_wu.bookstoreuser/shared_prefs", "MainActivity.xml");
        if (file.exists()) {
            mPref = getSharedPreferences("MainActivity", 0);
            mUserName = mPref.getString("NAME", null);
            mUserId = mPref.getInt("ID", 0);
        }
        display();
    }
    public void delete(View view) {
        String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/information";
        Uri uri = Uri.parse(URL);
        int d = getContentResolver().delete(uri,null,null);
        Toast.makeText(getBaseContext(), "Delete all data : "+d , Toast.LENGTH_SHORT).show();
    }
    public void allListOnClick(View view) {
        setContentView(R.layout.activity_user);
        display();
    }
    public void myListSellOnClick(View view) {
        setContentView(R.layout.activity_user);
        displaySell();
    }
    public void myListBuyOnClick(View view) {
        setContentView(R.layout.activity_user);
        displayBuy();
    }
    public void displayBuy() {
        String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/information";
        Uri uri = Uri.parse(URL);
        String[] columns = new String[]{
                _ID,
                SELLER_NAME,
                SELLER_ID,
                BOOK_NAME,
                PRICE
        };
        String selection = BUYER_NAME + " = ?  AND " + BUYER_ID + " = ? ";
        String[] selectionArgs = {mUserName , Integer.toString(mUserId)};
        Cursor c = getContentResolver().query(uri, columns, selection, selectionArgs, null);
        if (c != null) {
            c.moveToFirst();
        }

        int[] to = new int[]{
                R.id.id,
                R.id.seller_name,
                R.id.seller_id,
                R.id.book_name,
                R.id.price,
        };

        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list, c, columns, to, 0);

        ListView listView = (ListView) findViewById(R.id.listView);
        if (null != listView) {
            listView.setAdapter(dataAdapter);
        }
    }
    public void displaySell() {
        // Retrieve student records
        String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/information";
        Uri uri = Uri.parse(URL);
        String[] columns = new String[]{
                _ID,
                BOOK_NAME,
                PRICE,
                SOLD
        };
        String selection = SELLER_NAME + " = ?  AND " + SELLER_ID + " = ? ";
        String[] selectionArgs = {mUserName , Integer.toString(mUserId)};
        Cursor c = getContentResolver().query(uri, columns, selection, selectionArgs, null);
        //getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        if (c != null) {
            c.moveToFirst();
        }

        int[] to = new int[]{
                R.id.id,
                R.id.book_name,
                R.id.price,
                R.id.sold
        };


        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_sell, c, columns, to, 0);

        final ListView listView = (ListView) findViewById(R.id.listView);
        if (null != listView) {
            listView.setAdapter(dataAdapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                    final int position = (int)listView.getItemIdAtPosition(index);
                    new AlertDialog.Builder(UserActivity.this)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   // Toast.makeText(getBaseContext(), ""+position , Toast.LENGTH_SHORT).show();
                                    String selection = _ID + " = ? " ;
                                    String select = Integer.toString(position);
                                    String[] selectionArgs = { select };
                                    String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/information";
                                    Uri uri = Uri.parse(URL);
                                    int d = getContentResolver().delete(uri,selection,selectionArgs);
                                    Toast.makeText(getBaseContext(), ""+d , Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    return false;
                }
            });
        }
    }
    public void display() {

        String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/information";
        Uri uri = Uri.parse(URL);
        String[] columns = new String[]{
                _ID,
                SELLER_NAME,
                SELLER_ID,
                BOOK_NAME,
                PRICE
        };
        String selection = SOLD + " = ? ";
        String[] selectionArgs = {"0"};
        Cursor c = getContentResolver().query(uri, columns, selection, selectionArgs, null);
        //getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        if (c != null) {
            c.moveToFirst();
        }

        int[] to = new int[]{
                R.id.id,
                R.id.seller_name,
                R.id.seller_id,
                R.id.book_name,
                R.id.price,
        };

        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list, c, columns, to, 0);
        ListView listView = (ListView) findViewById(R.id.listView);

        if (null != listView) {
            listView.setAdapter(dataAdapter);
        }
    }

    public void add(View view) {

        setContentView(R.layout.add);

        TextView name = (TextView) findViewById(R.id.user_name);
        TextView id = (TextView) findViewById(R.id.user_id);

        if ((name != null) && (id != null)) {
            name.append(mUserName);
            id.append(Integer.toString(mUserId));
        }
    }

    public void insert(View view) {

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        String b = "";
        int p = 0;
        EditText bookName = (EditText) findViewById(R.id.addBookName);
        EditText price = (EditText) findViewById(R.id.addPrice);

        if ((bookName != null) && (price != null)) {
            try {
                p = Integer.parseInt(price.getText().toString());
                b = bookName.getText().toString();
                ContentValues values = new ContentValues();
                values.put(SELLER_NAME, mUserName);
                values.put(SELLER_ID, mUserId);
                values.put(BOOK_NAME, b);
                values.put(PRICE, p);
                values.put(SOLD, false);
                values.put(BUYER_NAME, "");
                values.put(BUYER_ID, "");

                String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/information";
                Uri uri = Uri.parse(URL);
                getContentResolver().insert(uri, values);
                bookName.setText("");
                price.setText("");
                Toast.makeText(getBaseContext(), " Success !! ", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(getBaseContext(), "Something Error !  There's not input data !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void search(View view) {

        setContentView(R.layout.search);

        TextView name = (TextView) findViewById(R.id.user_name);
        TextView id = (TextView) findViewById(R.id.user_id);

        if ((name != null) && (id != null)) {
            name.append(mUserName);
            id.append(Integer.toString(mUserId));
        }
    }
    public void searchBook (View view) {

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        TextView book = (TextView)findViewById(R.id.searchBookName) ;
        String searchBook = "";
        if(null != book){
            searchBook = book.getText().toString();
        }


        setContentView(R.layout.result);
        String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/information";
        Uri uri = Uri.parse(URL);

        String[] columns = new String[]{
                _ID,
                SELLER_NAME,
                SELLER_ID,
                PRICE,
                SOLD
        };
        String selection = BOOK_NAME + " = ?   " ;
        String[] selectionArgs = { searchBook };
        String sortOrder = SOLD +" , "+ PRICE;
        Cursor c = getContentResolver().query(uri, columns, selection, selectionArgs, sortOrder);
        //getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        if (c != null) {
            c.moveToFirst();
        }

        int[] to = new int[]{
                R.id.id,
                R.id.seller_name,
                R.id.seller_id,
                R.id.price,
                R.id.sold
        };


        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_search_book, c, columns, to, 0);

        final ListView listView = (ListView) findViewById(R.id.listViewResult);
        if (null != listView) {
            listView.setAdapter(dataAdapter);
        }

        TextView text = (TextView) findViewById(R.id.searchText);
        if(null != text){
            text.setText(" BOOK : " + searchBook);
        }

    }

    public void searchSeller (View view) {

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        TextView sName = (TextView)findViewById(R.id.searchSellerName) ;
        TextView sId = (TextView)findViewById(R.id.searchSellerId) ;
        String searchSName = "";
        String searchSId = "";
        if( (null != sName) && (null != sId) ){
            searchSName = sName.getText().toString();
            searchSId = sId.getText().toString();
        }


        setContentView(R.layout.result);
        String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/information";
        Uri uri = Uri.parse(URL);

        String[] columns = new String[]{
                _ID,
                BOOK_NAME,
                PRICE,
                BUYER_NAME,
                BUYER_ID
        };
        String selection = SELLER_NAME + " = ?  AND " + SELLER_ID + " = ? ";
        String[] selectionArgs = { searchSName, searchSId };
        Cursor c = getContentResolver().query(uri, columns, selection, selectionArgs, SOLD);
        //getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        if (c != null) {
            c.moveToFirst();
        }

        int[] to = new int[]{
                R.id.id,
                R.id.book_name,
                R.id.price,
                R.id.buyer_name,
                R.id.buyer_id
        };


        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_search_seller, c, columns, to, 0);

        final ListView listView = (ListView) findViewById(R.id.listViewResult);
        if (null != listView) {
            listView.setAdapter(dataAdapter);
        }

        TextView text = (TextView) findViewById(R.id.searchText);
        if(null != text){
            text.setText(" Seller : " + searchSName + " - " + searchSId);
        }

    }
}
