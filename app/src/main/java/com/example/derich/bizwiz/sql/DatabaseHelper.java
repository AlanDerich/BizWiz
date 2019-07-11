package com.example.derich.bizwiz.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.derich.bizwiz.model.User;

import java.util.ArrayList;

/**
 * Created by group 7 on 3/27/19.
 */
public class DatabaseHelper  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Bizwiz.db";

    private static final String TABLE_USER = "user";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    public static final String TABLE_CLIENT = "clients";

    public static final String COLUMN_CLIENT_ID = "client_id";
    public static final String COLUMN_CLIENT_FULLNAME = "client_fullName";
    public static final String COLUMN_CLIENT_DEBT = "client_debt";
    public static final String COLUMN_NUMBER = "Number";
    public static final String COLUMN_CLIENT_EMAIL= "client_Email";
    public static final String COLUMN_STATUS = "status";

    public static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_QUANTITY = "product_quantity";
    public static final String COLUMN_PRODUCT_PRICE = "product_price";
    public static final String COLUMN_STATUSS = "status";

    public static final String TABLE_TRANSACTIONS = "transactions";

    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_TYPE = "transaction_type";
    public static final String TRANSACTION_DATE = "transaction_date";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
             + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + "); ";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private String CREATE_CLIENT_TABLE = "CREATE TABLE " + TABLE_CLIENT + "("
            + COLUMN_CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CLIENT_FULLNAME + " TEXT,"
             + COLUMN_CLIENT_DEBT + " INTEGER," +COLUMN_NUMBER + " INTEGER," + COLUMN_CLIENT_EMAIL + " TEXT," + COLUMN_STATUS +
            " TINYINT); ";

    private String DROP_CLIENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_CLIENT;


    private String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PRODUCT_NAME + " TEXT,"
            +COLUMN_QUANTITY + " INTEGER," + COLUMN_PRODUCT_PRICE + " TEXT," + COLUMN_STATUS +
            " TINYINT); ";

    private String DROP_PRODUCTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_PRODUCTS;

    private String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
            + TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TRANSACTION_TYPE + " TEXT,"
            +TRANSACTION_DATE + " TEXT); ";

    private String DROP_TRANSACTION_TABLE = "DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS;


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CLIENT_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);

    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CLIENT_TABLE);
        db.execSQL(DROP_PRODUCTS_TABLE);
        db.execSQL(DROP_TRANSACTION_TABLE);
        onCreate(db);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void updatePassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, password);
        db.update(TABLE_USER, values, COLUMN_USER_EMAIL+" = ?",new String[] { email });
        db.close();
    }
    public boolean checkUser(String email){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_CLIENT, null);
        return res;
    }
    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_TRANSACTIONS, null);
        return cur;
    }


    public Cursor getAllQuantity() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PRODUCTS, null);
        return cursor;
    }


    public int previousBal(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select product_quantity from products where product_name=? order by null desc limit 1", new String[]{productName});
        int balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getInt(cursor.getColumnIndex("product_quantity"));
        } else {
            balance = 0;
        }
        return balance;

    }public int productPrice(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select product_price from products where product_name=? order by null desc limit 1", new String[]{productName});
        int price;
        if (cursor.moveToFirst()) {
            price = cursor.getInt(cursor.getColumnIndex("product_price"));
        } else {
            price = 0;
        }
        return price;

    }
    public int previousDebt(String client_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select client_debt from clients where client_fullName=? order by null desc limit 1", new String[]{client_name});
        int debt;
        if (cursor.moveToFirst()) {
            debt = cursor.getInt(cursor.getColumnIndex("client_debt"));
        } else {
            debt = 0;
        }
        return debt;

    }



    public boolean updateData (String client_name, String new_client_debt, String product_name, String quantity , String date, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        ContentValues contentValues2 = new ContentValues();
        contentValues.put((COLUMN_CLIENT_FULLNAME ),client_name );
        contentValues.put(COLUMN_CLIENT_DEBT,new_client_debt );
        contentValues1.put((COLUMN_PRODUCT_NAME ),product_name );
        contentValues1.put(COLUMN_QUANTITY,quantity );
        contentValues2.put(TRANSACTION_DATE,date);
        contentValues2.put(TRANSACTION_TYPE,type);
        db.update(TABLE_CLIENT,contentValues,"client_fullName = ? " , new String[]{client_name});
        db.update(TABLE_PRODUCTS,contentValues1,"product_name = ? " , new String[]{product_name});
        db.insert(TABLE_TRANSACTIONS, null, contentValues2);
        return true;
    }
    public boolean updateDebt (String client_name, String new_client_debt, String currentDateandTime, String type ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put((COLUMN_CLIENT_FULLNAME ),client_name );
        contentValues.put(COLUMN_CLIENT_DEBT,new_client_debt );
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.update(TABLE_CLIENT,contentValues,"client_fullName = ? " , new String[]{client_name});
        return true;
    }
    public boolean updateQuantity (String product_name, String new_value, String currentDateandTime, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put((COLUMN_PRODUCT_NAME ),product_name );
        contentValues.put(COLUMN_QUANTITY,new_value );
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.update(TABLE_PRODUCTS,contentValues,"product_name = ?",new String[] {product_name});
        return true;
    }


    public boolean addClient(String client_fullName, String client_debt,String Number,String client_Email, int status, String currentDateandTime, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();

        contentValues.put(COLUMN_CLIENT_FULLNAME, client_fullName);
        contentValues.put(COLUMN_CLIENT_DEBT, client_debt);
        contentValues.put(COLUMN_NUMBER, Number);
        contentValues.put(COLUMN_CLIENT_EMAIL, client_Email);
        contentValues.put(COLUMN_STATUS, status);
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.insert(TABLE_CLIENT, null, contentValues);
        db.close();
        return true;
    }

    public boolean syncAttempt(String currentDateandTime, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.close();
        return true;
    }

    /*
     * This method taking two arguments
     * first one is the id of the name for which
     * we have to update the sync status
     * and the second one is the status that will be changed
     * */
    public boolean updateClientStatus(int client_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_CLIENT, contentValues, COLUMN_CLIENT_ID + "=" + client_id, null);
        db.close();
        return true;
    }

    /*
     * this method will give us all the name stored in sqlite
     * */
    public Cursor getClients() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CLIENT + " ORDER BY " + COLUMN_CLIENT_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
     * this method is for getting all the unsynced name
     * so that we can sync it with database
     * */
    public Cursor getUnsyncedClients() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CLIENT + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public boolean addProduct(String product_name,String Quantity,String product_price, int status, String currentDateandTime, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_NAME, product_name);
        contentValues.put(COLUMN_QUANTITY, Quantity);
        contentValues.put(COLUMN_PRODUCT_PRICE, product_price);
        contentValues.put(COLUMN_STATUSS, status);
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.insert(TABLE_PRODUCTS, null, contentValues);
        db.close();
        return true;
    }

    public boolean addTransaction(String transaction_type,String transaction_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TRANSACTION_TYPE, transaction_type);
        contentValues.put(TRANSACTION_DATE, transaction_date);


        db.insert(TABLE_TRANSACTIONS, null, contentValues);
        db.close();
        return true;
    }

    /*
     * This method taking two arguments
     * first one is the id of the name for which
     * we have to update the sync status
     * and the second one is the status that will be changed
     * */
    public boolean updateProductStatus(int product_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_PRODUCTS, contentValues, COLUMN_PRODUCT_ID + "=" + product_id, null);
        db.close();
        return true;
    }

    /*
     * this method will give us all the name stored in sqlite
     * */
    public Cursor getProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + COLUMN_PRODUCT_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
     * this method is for getting all the unsynced name
     * so that we can sync it with database
     * */
    public Cursor getUnsyncedProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUSS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*  public boolean checkDebt(String debt){
        String[] columns = {
                COLUMN_CLIENT_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_CLIENT_DEBT + " = ?";
        String[] selectionArgs = { debt };

        Cursor cursor = db.query(TABLE_CLIENT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    } */
    public ArrayList<String> getAllClients() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_CLIENT;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String clientName = cursor.getString(cursor.getColumnIndex("client_fullName"));
                    list.add(clientName);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return list;
    }
    public ArrayList<String> getAllProducts() {
        ArrayList<String> list1 = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
            Cursor cur = db.rawQuery(selectQuery, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String productName = cur.getString(cur.getColumnIndex("product_name"));
                    list1.add(productName);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return list1;
    }


}