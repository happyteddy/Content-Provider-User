package com.example.angelina_wu.bookstoreuser;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final String URISTRING = "content://com.example.angelina_wu.bookstorecontentproviderowner/information";
    public static final Uri URI = Uri.parse(URISTRING);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);

        File file = new File("/data/data/com.example.angelina_wu.bookstoreuser/shared_prefs", "MainActivity.xml");
        if (file.exists()) {
            mPref = getSharedPreferences("MainActivity", 0);
            mUserName = mPref.getString("NAME", null);
            mUserId = mPref.getInt("ID", 0);
        }
        display();
    }

    public void delete(View view) {
        int d = getContentResolver().delete(URI, null, null);
        Toast.makeText(getBaseContext(), R.string.toast_delete + d, Toast.LENGTH_SHORT).show();
        display();
    }

    public void allListOnClick(View view) {
        setContentView(R.layout.show);
        display();
    }

    public void myListSellOnClick(View view) {
        setContentView(R.layout.show);
        displaySell();
    }

    public void myListBuyOnClick(View view) {
        setContentView(R.layout.show);
        displayBuy();
    }

    public void displayBuy() {
        TextView text = (TextView) findViewById(R.id.infoText);
        if (null != text) {
            String showBuyer = getString(R.string.text_Buy) + mUserName + getString(R.string.empty) + Integer.toString(mUserId) ;
            text.setText(showBuyer);
        }
        String[] columns = new String[]{
                _ID,
                SELLER_NAME,
                SELLER_ID,
                BOOK_NAME,
                PRICE
        };
        String selection = BUYER_NAME + " = ?  AND " + BUYER_ID + " = ? ";
        String[] selectionArgs = {mUserName, Integer.toString(mUserId)};
        Cursor c = getContentResolver().query(URI, columns, selection, selectionArgs, null);
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
        TextView text = (TextView) findViewById(R.id.infoText);
        if (null != text) {
            String showSeller = getString(R.string.text_Sell) + mUserName + getString(R.string.empty) + Integer.toString(mUserId);
            text.setText(showSeller);
        }
        // Retrieve student records

        String[] columns = new String[]{
                _ID,
                BOOK_NAME,
                PRICE,
                SOLD
        };
        String selection = SELLER_NAME + " = ?  AND " + SELLER_ID + " = ? ";
        String[] selectionArgs = {mUserName, Integer.toString(mUserId)};
        Cursor c = getContentResolver().query(URI, columns, selection, selectionArgs, null);
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
                    final int position = (int) listView.getItemIdAtPosition(index);
                    final String selection = _ID + " = ? ";
                    final String select = Integer.toString(position);
                    final String[] selectionArgs = {select};

                    String[] columns = {_ID, SOLD, BOOK_NAME, PRICE};
                    Cursor soldCursor = getContentResolver().query(URI, columns, selection, selectionArgs, null);

                    soldCursor.moveToFirst();
                    final String isSold = soldCursor.getString(1);
                    final String book = soldCursor.getString(2);
                    final int price = soldCursor.getInt(3);
                    soldCursor.close();
                    final String mes = getString(R.string.book_name) + book + getString(R.string.price) + Integer.toString(price) ;
                    new AlertDialog.Builder(UserActivity.this)
                            .setMessage(mes)
                            .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int d = getContentResolver().delete(URI, selection, selectionArgs);
                                    displaySell();
                                }
                            })
                            .setNegativeButton(R.string.update, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LayoutInflater inflater = LayoutInflater.from(UserActivity.this);
                                    final View v = inflater.inflate(R.layout.dialog_update, null);
                                    if (Integer.parseInt(isSold) == 0) {
                                        new AlertDialog.Builder(UserActivity.this)
                                                .setTitle(R.string.title_newPrice)
                                                .setView(v)
                                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        EditText editText = (EditText) (v.findViewById(R.id.updatePrice));
                                                        String newPrice = editText.getText().toString();
                                                        ContentValues args = new ContentValues();
                                                        args.put(PRICE, newPrice);
                                                        getContentResolver().update(URI, args, selection, selectionArgs);
                                                        displaySell();
                                                    }
                                                }).show();
                                    } else {
                                        new AlertDialog.Builder(UserActivity.this)
                                                .setMessage(R.string.mes_cantUpdate)
                                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // do nothing
                                                    }
                                                }).show();
                                    }
                                }
                            }).show();
                    return false;
                }
            });
        }
    }

    public void display() {
        TextView text = (TextView) findViewById(R.id.infoText);
        if (null != text) {
            text.setText(R.string.text_Sold_False);
        }

        String[] columns = new String[]{
                _ID,
                SELLER_NAME,
                SELLER_ID,
                BOOK_NAME,
                PRICE
        };
        String selection = SOLD + " = ? ";
        String[] selectionArgs = {"0"};
        Cursor c = getContentResolver().query(URI, columns, selection, selectionArgs, null);
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
        final ListView listView = (ListView) findViewById(R.id.listView);

        if (null != listView) {
            listView.setAdapter(dataAdapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                    final int position = (int) listView.getItemIdAtPosition(index);
                    final String selection = _ID + " = ? ";
                    final String select = Integer.toString(position);
                    final String[] selectionArgs = {select};

                    String[] columns = {_ID, SELLER_NAME, SELLER_ID};
                    Cursor soldCursor = getContentResolver().query(URI, columns, selection, selectionArgs, null);

                    soldCursor.moveToFirst();
                    final String sellerName = soldCursor.getString(1);
                    final int sellerId = soldCursor.getInt(2);
                    soldCursor.close();

                    if ((sellerName.equals(mUserName)) && (sellerId==mUserId)) {
                        new AlertDialog.Builder(UserActivity.this)
                                .setMessage(R.string.mes_cantBuy)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    } else {
                        new AlertDialog.Builder(UserActivity.this)
                                .setMessage(R.string.mes_wantToBuy)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ContentValues args = new ContentValues();
                                        args.put(SOLD, true);
                                        args.put(BUYER_NAME,mUserName);
                                        args.put(BUYER_ID, mUserId);
                                        getContentResolver().update(URI, args, selection, selectionArgs);
                                        displaySell();
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //  do nothing
                                    }
                                }).show();
                    }
                    return false;
                }
            });
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

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

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

                getContentResolver().insert(URI, values);
                bookName.setText("");
                price.setText("");
                Toast.makeText(getBaseContext(), R.string.success, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), R.string.toast_noData, Toast.LENGTH_SHORT).show();
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

    public void searchBook(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        TextView book = (TextView) findViewById(R.id.searchBookName);
        String searchBook = "";
        if (null != book) {
            searchBook = book.getText().toString();
        }


        setContentView(R.layout.show);

        String[] columns = new String[]{
                _ID,
                SELLER_NAME,
                SELLER_ID,
                PRICE,
                SOLD
        };
        String selection = BOOK_NAME + " = ?   ";
        String[] selectionArgs = {searchBook};
        String sortOrder = SOLD + " , " + PRICE;
        Cursor c = getContentResolver().query(URI, columns, selection, selectionArgs, sortOrder);
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

        final ListView listView = (ListView) findViewById(R.id.listView);
        if (null != listView) {
            listView.setAdapter(dataAdapter);
        }

        TextView text = (TextView) findViewById(R.id.infoText);
        if (null != text) {
            String showBookName = getString(R.string.book_name) + searchBook;
            text.setText(showBookName);
        }

    }

    public void searchSeller(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        TextView sName = (TextView) findViewById(R.id.searchSellerName);
        TextView sId = (TextView) findViewById(R.id.searchSellerId);
        String searchSName = "";
        String searchSId = "";
        if ((null != sName) && (null != sId)) {
            searchSName = sName.getText().toString();
            searchSId = sId.getText().toString();
        }

        setContentView(R.layout.show);

        String[] columns = new String[]{
                _ID,
                BOOK_NAME,
                PRICE,
                BUYER_NAME,
                BUYER_ID
        };
        String selection = SELLER_NAME + " = ?  AND " + SELLER_ID + " = ? ";
        String[] selectionArgs = {searchSName, searchSId};
        Cursor c = getContentResolver().query(URI, columns, selection, selectionArgs, SOLD);
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

        final ListView listView = (ListView) findViewById(R.id.listView);
        if (null != listView) {
            listView.setAdapter(dataAdapter);
        }

        TextView text = (TextView) findViewById(R.id.infoText);
        if (null != text) {
            String showSeller = getString(R.string.seller) + searchSName + getString(R.string.empty) + searchSId;
            text.setText(showSeller);
        }
    }
}
