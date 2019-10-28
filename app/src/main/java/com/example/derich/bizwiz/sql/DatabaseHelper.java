package com.example.derich.bizwiz.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.derich.bizwiz.model.User;

import java.util.ArrayList;

/**
 * Created by group 7 on 3/27/19.
 */
public class DatabaseHelper  extends SQLiteOpenHelper implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "Bizwiz.db";

    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_USER_QUESTION = "user_question";
    public static final String COLUMN_USER_ANSWER = "user_answer";
    public static final String COLUMN_USER_STATUS = "user_status";

    public static final String TABLE_CLIENT = "clients";
    public static final String COLUMN_CLIENT_ID = "client_id";
    public static final String COLUMN_CLIENT_FULLNAME = "client_fullName";
    public static final String COLUMN_CLIENT_DEBT = "client_debt";
    public static final String COLUMN_CLIENT_ADDED_DEBT = "client_added_debt";
    public static final String COLUMN_NUMBER = "Number";
    public static final String COLUMN_CLIENT_EMAIL= "client_Email";
    public static final String COLUMN_STATUS = "status";

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_QUANTITY = "product_quantity";
    public static final String COLUMN_PRODUCT_BUYING_PRICE = "product_buying_price";
    public static final String COLUMN_PRODUCT_RETAIL_PRICE = "product_retail_price";
    public static final String COLUMN_PRODUCT_WHOLESALE_PRICE = "product_wholesale_price";
    public static final String COLUMN_SOLD_PRODUCT = "product_sold_product";
    public static final String COLUMN_STATUSS = "status";

    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_TYPE = "transaction_type";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_USER = "transaction_user";
    public static final String COLUMN_TRANSACTION_STATUS = "transaction_status";

    public static final String TABLE_SALES = "sales";
    public static final String COLUMN_SALES_ID= "sales_id";
    public static final String COLUMN_OPENING_CASH_SALES = "opening_cash_sales";
    public static final String COLUMN_WHOLESALE_SALES = "wholesale_sales";
    public static final String COLUMN_RETAIL_SALES = "retail_sales";
    public static final String COLUMN_SALES_DATE = "sales_date";
    public static final String COLUMN_SALES_TIME = "sales_time";
    public static final String COLUMN_SALES_USER = "sales_user";
    public static final String COLUMN_DAILY_EXPENSE = "daily_expense";
    public static final String COLUMN_DEBTS_PAID = "debts_paid";
    public static final String COLUMN_DAILY_DEBT = "daily_debt";
    public static final String COLUMN_SALES_STATUS = "sales_status";

    public static final String TABLE_MPESA = "mpesa";
    public static final String MPESA_ID = "id";
    public static final String DATE_MILLIS = "date_in_millis";
    public static final String TIME_OF_TRANSACTION = "time_of_transaction";
    public static final String COLUMN_OPENING_FLOAT = "opening_float";
    public static final String COLUMN_OPENING_CASH = "opening_cash";
    public static final String COLUMN_ADDED_FLOAT = "added_float";
    public static final String COLUMN_ADDED_CASH = "added_cash";
    public static final String COLUMN_REDUCTED_FLOAT = "reducted_float";
    public static final String COLUMN_REDUCTED_CASH = "reducted_cash";
    public static final String COLUMN_CLOSING_CASH = "closing_cash";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_MPESA_STATUS = "status";

    public static final String TABLE_REPORT = "report";
    public static final String REPORT_ID = "report_id";
    public static final String REPORT_DATE = "report_date";
    public static final String REPORT_TIME = "report_time";
    public static final String REPORT_USER = "report_user";
    public static final String REPORT_PRODUCT = "report_product";
    public static final String REPORT_WHOLESALE_SALES = "report_wholesale_sales";
    public static final String REPORT_RETAIL_SALES = "report_retail_sales";
    public static final String REPORT_DEBT_SALES = "report_debt_sales";
    public static final String REPORT_ADDED_ITEMS = "report_added_items";
    public static final String REPORT_SOLD_ITEMS = "report_sold_items";
    public static final String REPORT_STATUS = "report_status";

    private String CREATE_REPORT_TABLE = "CREATE TABLE " + TABLE_REPORT + "(" +
            REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            REPORT_DATE + " TEXT," +
            REPORT_TIME + " TEXT," +
            REPORT_USER + " TEXT," +
            REPORT_PRODUCT + " TEXT," +
            REPORT_WHOLESALE_SALES + " TEXT," +
            REPORT_RETAIL_SALES + " TEXT," +
            REPORT_DEBT_SALES + " TEXT," +
            REPORT_SOLD_ITEMS + " TEXT," +
            REPORT_ADDED_ITEMS + " TEXT," +
            REPORT_STATUS + " TINYINT); ";
    private String DROP_REPORT_TABLE = "DROP TABLE IF EXISTS " + TABLE_REPORT;

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
             + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT, "+ COLUMN_USER_QUESTION + " TEXT, "+ COLUMN_USER_ANSWER + " TEXT, "+COLUMN_USER_STATUS +
            " TINYINT); ";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private String CREATE_SALES_TABLE = "CREATE TABLE " + TABLE_SALES + "(" +
            COLUMN_SALES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_OPENING_CASH_SALES + " TEXT," +
            COLUMN_RETAIL_SALES + " TEXT," +
            COLUMN_WHOLESALE_SALES + " TEXT," +
            COLUMN_SALES_DATE + " TEXT," +
            COLUMN_SALES_TIME + " TEXT," +
            COLUMN_SALES_USER + " TEXT," +
            COLUMN_DAILY_DEBT + " TEXT," +
            COLUMN_DEBTS_PAID + " TEXT," +
            COLUMN_DAILY_EXPENSE + " TEXT," +
            COLUMN_SALES_STATUS + " TINYINT);";

    private String DROP_SALES_TABLE = "DROP TABLE IF EXISTS " + TABLE_SALES;

    private String CREATE_CLIENT_TABLE = "CREATE TABLE " + TABLE_CLIENT + "("
            + COLUMN_CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CLIENT_FULLNAME + " TEXT,"
             + COLUMN_CLIENT_DEBT + " TEXT," +  COLUMN_CLIENT_ADDED_DEBT + " TEXT," +COLUMN_NUMBER + " INTEGER," + COLUMN_CLIENT_EMAIL + " TEXT," + COLUMN_STATUS +
            " TINYINT); ";

    private String DROP_CLIENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_CLIENT;


    private String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PRODUCT_NAME + " TEXT,"
            +COLUMN_QUANTITY + " INTEGER," + COLUMN_PRODUCT_BUYING_PRICE + " TEXT,"+ COLUMN_SOLD_PRODUCT + " TEXT," + COLUMN_PRODUCT_RETAIL_PRICE + " TEXT," + COLUMN_PRODUCT_WHOLESALE_PRICE + " TEXT," + COLUMN_STATUS +
            " TINYINT); ";

    private String DROP_PRODUCTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_PRODUCTS;

    private String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
            + TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TRANSACTION_TYPE + " TEXT," + TRANSACTION_USER + " TEXT,"
            +TRANSACTION_DATE + " TEXT, "+ COLUMN_TRANSACTION_STATUS +
            " TINYINT); ";

    private String DROP_TRANSACTION_TABLE = "DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS;

    private String CREATE_MPESA_TABLE = "CREATE TABLE " + TABLE_MPESA + " ( "+ MPESA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ DATE_MILLIS + " TEXT, " + COLUMN_OPENING_FLOAT + " TEXT , " + COLUMN_OPENING_CASH +
            " TEXT, " + COLUMN_ADDED_CASH + " TEXT, " + COLUMN_ADDED_FLOAT + " TEXT, " + COLUMN_REDUCTED_CASH + " TEXT, " + COLUMN_CLOSING_CASH + " TEXT, " + COLUMN_REDUCTED_FLOAT + " TEXT , " + COLUMN_COMMENT +" TEXT, " + TIME_OF_TRANSACTION +" TEXT, " + COLUMN_MPESA_STATUS + " TINYINT );" ;
    private String DROP_MPESA_TABLE = "DROP TABLE IF EXISTS " + TABLE_MPESA;


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CLIENT_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
        db.execSQL(CREATE_MPESA_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
        db.execSQL(CREATE_REPORT_TABLE);

    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CLIENT_TABLE);
        db.execSQL(DROP_PRODUCTS_TABLE);
        db.execSQL(DROP_TRANSACTION_TABLE);
        db.execSQL(DROP_MPESA_TABLE);
        db.execSQL(DROP_SALES_TABLE);
        db.execSQL(DROP_REPORT_TABLE);
        onCreate(db);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_QUESTION, user.getQuestion());
        values.put(COLUMN_USER_ANSWER, user.getAnswer());
        values.put(COLUMN_USER_STATUS, 0);

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void updatePassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_STATUS, 0);
        db.update(TABLE_USER, values, COLUMN_USER_NAME+" = ?",new String[] { username });
        db.close();
    }
    public boolean checkUser(String username){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = { username };

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

        return cursorCount > 0;
    }

    public boolean checkUser(String username, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { username, password };

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

        return cursorCount > 0;
    }
    public boolean checkUserPassword(String username, String question, String answer){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_QUESTION + " =?" + " AND " + COLUMN_USER_ANSWER + " =?";
        String[] selectionArgs = { username,question,answer };

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

        return cursorCount > 0;
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_TRANSACTIONS+ " ORDER BY " + TRANSACTION_DATE + " DESC;", null);
        return cur;
    }

    public Cursor getAllTransactionsUser(String username) {
        String[] params = new String[] {username};
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + TRANSACTION_USER + " = ? "  + " ORDER BY " + TRANSACTION_DATE + " DESC;";
        Cursor cur = db.rawQuery(sql,params);
        return cur;

    }


   /* public Cursor getAllQuantity() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PRODUCTS, null);
        return cursor;
    }
*/

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

    }
    public int previousSoldBal(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select product_sold_product from products where product_name=? order by null desc limit 1", new String[]{productName});
        int balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getInt(cursor.getColumnIndex("product_sold_product"));
        } else {
            balance = 0;
        }
        return balance;

    }
    public float getProductsPrice(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select product_buying_price from products where product_name=? order by null desc limit 1", new String[]{productName});
        float balance;
        if (cursor.moveToFirst()) {
            balance = cursor.getFloat(cursor.getColumnIndex("product_buying_price"));
        } else {
            balance = 0;
        }
        return balance;

    }

    public int productRetailPrice(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select product_retail_price from products where product_name=? order by null desc limit 1", new String[]{productName});
        int price;
        if (cursor.moveToFirst()) {
            price = cursor.getInt(cursor.getColumnIndex("product_retail_price"));
        } else {
            price = 0;
        }
        return price;

    }
    public int productWholesalePrice(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select product_wholesale_price from products where product_name=? order by null desc limit 1", new String[]{productName});
        int price;
        if (cursor.moveToFirst()) {
            price = cursor.getInt(cursor.getColumnIndex("product_wholesale_price"));
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
    public int previousUnsyncedDebt(String client_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select client_added_debt from clients where client_fullName=? order by null desc limit 1", new String[]{client_name});
        int debt;
        if (cursor.moveToFirst()) {
            debt = cursor.getInt(cursor.getColumnIndex("client_added_debt"));
        } else {
            debt = 0;
        }
        return debt;

    }
  /*  public int previousSalesDebt(String username,String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select daily_debt from sales where sales_user=? and sales_date=? order by null desc limit 1", new String[]{username,date});
        int debt;
        if (cursor.moveToFirst()) {
            debt = cursor.getInt(cursor.getColumnIndex("daily_debt"));
        } else {
            debt = 0;
        }
        return debt;

    }

    public int previousWholesaleAmount(String username,String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select wholesale_sales from sales where sales_user=? and sales_date=? order by null desc limit 1", new String[]{username,date});
        int amount;
        if (cursor.moveToFirst()) {
            amount = cursor.getInt(cursor.getColumnIndex("wholesale_sales"));
        } else {
            amount = 0;
        }
        return amount;

    }
    public int previousRetailAmount(String username,String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select retail_sales from sales where sales_user=? and sales_date=? order by null desc limit 1", new String[]{username,date});
        int debt;
        if (cursor.moveToFirst()) {
            debt = cursor.getInt(cursor.getColumnIndex("retail_sales"));
        } else {
            debt = 0;
        }
        return debt;

    }
*/
    public void deleteClient(String client_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM clients where client_fullName=? ", new String[]{client_name});

    }

    public void deleteProduct(String product_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM products where product_name=? ", new String[]{product_name});

    }
    public int clientDeleted(String client_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select client_fullName from clients where client_fullName=? order by null desc limit 1", new String[]{client_name});
        int status;
        if (cursor.moveToFirst()) {
            status = cursor.getInt(cursor.getColumnIndex("client_fullName"));
        } else {
            status = 0;
        }
        return status;

    }

    public int syncStatus(String client_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select status from clients where client_fullName=? order by null desc limit 1", new String[]{client_name});
        int status;
        if (cursor.moveToFirst()) {
            status = cursor.getInt(cursor.getColumnIndex("status"));
        } else {
            status = 0;
        }
        return status;

    }

    public boolean updateData (String client_name, String added_debt, String new_client_debt, String product_name, String quantity ,String sold_products, String date, String type, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        ContentValues contentValues2 = new ContentValues();
        contentValues.put((COLUMN_CLIENT_FULLNAME ),client_name );
        contentValues.put(COLUMN_CLIENT_ADDED_DEBT,added_debt );
        contentValues.put(COLUMN_CLIENT_DEBT,new_client_debt );
        contentValues.put(COLUMN_STATUS, 0);
        contentValues1.put((COLUMN_PRODUCT_NAME ),product_name );
        contentValues1.put(COLUMN_QUANTITY,quantity );
        contentValues1.put(COLUMN_SOLD_PRODUCT,sold_products);
        contentValues1.put(COLUMN_STATUSS,0);
        contentValues2.put(TRANSACTION_DATE,date);
        contentValues2.put(TRANSACTION_TYPE,type);
        contentValues2.put(TRANSACTION_USER,user);
        contentValues2.put(COLUMN_TRANSACTION_STATUS, 0);
        db.update(TABLE_CLIENT,contentValues,"client_fullName = ? " , new String[]{client_name});
        db.update(TABLE_PRODUCTS,contentValues1,"product_name = ? " , new String[]{product_name});
        db.insert(TABLE_TRANSACTIONS, null, contentValues2);
        return true;
    }
    public boolean updateDebt (String client_name,String unsynced_debt, String new_client_debt, String currentDateandTime, String type, String user ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put((COLUMN_CLIENT_FULLNAME ),client_name );
        contentValues.put(COLUMN_CLIENT_ADDED_DEBT,unsynced_debt );
        contentValues.put(COLUMN_CLIENT_DEBT,new_client_debt );
        contentValues.put(COLUMN_STATUS,0);
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        contentValues1.put(TRANSACTION_USER,user);
        contentValues1.put(COLUMN_TRANSACTION_STATUS, 0);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.update(TABLE_CLIENT,contentValues,"client_fullName = ? " , new String[]{client_name});
        return true;
    }
    public boolean updateQuantity (String product_name, String new_value,String sold_products, String currentDateandTime, String type, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put((COLUMN_PRODUCT_NAME ),product_name );
        contentValues.put(COLUMN_QUANTITY,new_value );
        contentValues.put(COLUMN_STATUSS, 0);
        contentValues.put(COLUMN_SOLD_PRODUCT,sold_products);
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        contentValues1.put(TRANSACTION_USER,username);
        contentValues1.put(COLUMN_TRANSACTION_STATUS, 0);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.update(TABLE_PRODUCTS,contentValues,"product_name = ?",new String[] {product_name});
        return true;
    }
    public boolean increaseQuantity (String product_name, String new_value, String stock,String currentDateandTime, String type, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put((COLUMN_PRODUCT_NAME ),product_name );
        contentValues.put(COLUMN_QUANTITY,new_value );
        contentValues.put(COLUMN_SOLD_PRODUCT,stock);
        contentValues.put(COLUMN_STATUSS, 0);
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        contentValues1.put(TRANSACTION_USER,username);
        contentValues1.put(COLUMN_TRANSACTION_STATUS, 0);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.update(TABLE_PRODUCTS,contentValues,"product_name = ?",new String[] {product_name});
        return true;
    }
    public Cursor getSales(String amount,String username,String date,String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[] {username,date};
        String sql = "SELECT * FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_USER + " = ? " + " AND " + COLUMN_SALES_DATE + " = ? " + " ORDER BY " + COLUMN_SALES_DATE + " ASC;";
        Cursor c = db.rawQuery(sql,params);
        return c;
    }

  /*  public boolean updateDebt(String amount,String username,String date,String time){
        if (getSales( amount, username, date, time).moveToFirst()){
            updateDebtSales( amount, username, date, time);
            return true;
        }
        else {
            insertDebtSales( amount, username, date, time);
            return true;
        }
    }
    public void updateDebtSales (String amount, String username, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put((COLUMN_DAILY_DEBT ),amount );
        contentValues.put(COLUMN_SALES_USER,username );
        contentValues.put(COLUMN_SALES_DATE,date);
        contentValues.put(COLUMN_SALES_TIME,time);
        contentValues.put(COLUMN_SALES_STATUS,0);
        db.update(TABLE_SALES,contentValues,"sales_date = ? AND sales_user = ?",new String[] {date, username});
    }
    */
    public boolean insertDebtSales (String amount, String username, String date, String time, String product_name, String quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put(COLUMN_DAILY_DEBT,amount );
        contentValues.put(COLUMN_OPENING_CASH_SALES,0 );
        contentValues.put(COLUMN_RETAIL_SALES,0 );
        contentValues.put(COLUMN_WHOLESALE_SALES,0 );
        contentValues.put(COLUMN_DEBTS_PAID,0 );
        contentValues.put(COLUMN_DAILY_EXPENSE,0 );
        contentValues.put(COLUMN_SALES_USER,username );
        contentValues.put(COLUMN_SALES_DATE,date);
        contentValues.put(COLUMN_SALES_TIME,time);
        contentValues.put(COLUMN_SALES_STATUS,0);
        contentValues1.put(REPORT_DATE,date);
        contentValues1.put(REPORT_USER,username);
        contentValues1.put(REPORT_TIME,time);
        contentValues1.put(REPORT_DEBT_SALES,amount);
        contentValues1.put(REPORT_WHOLESALE_SALES,0);
        contentValues1.put(REPORT_RETAIL_SALES,0);
        contentValues1.put(REPORT_ADDED_ITEMS,0);
        contentValues1.put(REPORT_PRODUCT,product_name);
        contentValues1.put(REPORT_SOLD_ITEMS,quantity);
        contentValues1.put(REPORT_STATUS,0);
        db.insert(TABLE_SALES,null,contentValues);
        db.insert(TABLE_REPORT,null,contentValues1);
    return true;

    }


    public void insertOpeningCashSales (String amount, String username, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put((COLUMN_OPENING_CASH_SALES ),amount );
        contentValues.put(COLUMN_DAILY_DEBT,0 );
        contentValues.put(COLUMN_RETAIL_SALES,0 );
        contentValues.put(COLUMN_WHOLESALE_SALES,0 );
        contentValues.put(COLUMN_DEBTS_PAID,0 );
        contentValues.put(COLUMN_DAILY_EXPENSE,0 );
        contentValues.put(COLUMN_SALES_USER,username );
        contentValues.put(COLUMN_SALES_DATE,date);
        contentValues.put(COLUMN_SALES_TIME,time);
        contentValues.put(COLUMN_SALES_STATUS,0);
        db.insert(TABLE_SALES,null,contentValues);
    }

    public boolean insertDebtsPaid (String amount, String username, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put((COLUMN_DEBTS_PAID ),amount );
        contentValues.put(COLUMN_OPENING_CASH_SALES,0 );
        contentValues.put(COLUMN_DAILY_DEBT,0 );
        contentValues.put(COLUMN_RETAIL_SALES,0 );
        contentValues.put(COLUMN_WHOLESALE_SALES,0 );
        contentValues.put(COLUMN_DAILY_EXPENSE,0 );
        contentValues.put(COLUMN_SALES_USER,username );
        contentValues.put(COLUMN_SALES_DATE,date);
        contentValues.put(COLUMN_SALES_TIME,time);
        contentValues.put(COLUMN_SALES_STATUS,0);
        db.insert(TABLE_SALES,null,contentValues);
    return true;
    }

    public boolean insertCashRetailSale (String amount, String username, String date, String time, String product_name, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put((COLUMN_RETAIL_SALES ),amount );
        contentValues.put((COLUMN_OPENING_CASH_SALES ),0 );
        contentValues.put(COLUMN_DAILY_DEBT,0 );
        contentValues.put(COLUMN_WHOLESALE_SALES,0 );
        contentValues.put(COLUMN_DEBTS_PAID,0 );
        contentValues.put(COLUMN_DAILY_EXPENSE,0 );
        contentValues.put(COLUMN_SALES_USER,username );
        contentValues.put(COLUMN_SALES_DATE,date);
        contentValues.put(COLUMN_SALES_TIME,time);
        contentValues.put(COLUMN_SALES_STATUS,0);
        contentValues1.put(REPORT_STATUS,0);
        contentValues1.put(REPORT_DATE,date);
        contentValues1.put(REPORT_TIME,time);
        contentValues1.put(REPORT_USER,username);
        contentValues1.put(REPORT_WHOLESALE_SALES,0);
        contentValues1.put(REPORT_DEBT_SALES,0);
        contentValues1.put(REPORT_RETAIL_SALES,amount);
        contentValues1.put(REPORT_ADDED_ITEMS,0);
        contentValues1.put(REPORT_SOLD_ITEMS,value);
        contentValues1.put(REPORT_PRODUCT,product_name);

        db.insert(TABLE_SALES,null,contentValues);
        db.insert(TABLE_REPORT,null,contentValues1);
   return true;
    }

    public boolean insertCashWholesaleSale (String amount,String username,String date,String time, String product_name, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put(COLUMN_WHOLESALE_SALES,amount );
        contentValues.put((COLUMN_OPENING_CASH_SALES ),0 );
        contentValues.put(COLUMN_DAILY_DEBT,0 );
        contentValues.put(COLUMN_RETAIL_SALES,0 );
        contentValues.put(COLUMN_DEBTS_PAID,0 );
        contentValues.put(COLUMN_DAILY_EXPENSE,0 );
        contentValues.put(COLUMN_SALES_USER,username );
        contentValues.put(COLUMN_SALES_DATE,date);
        contentValues.put(COLUMN_SALES_TIME,time);
        contentValues.put(COLUMN_SALES_STATUS,0);
        contentValues1.put(REPORT_STATUS,0);
        contentValues1.put(REPORT_DATE,date);
        contentValues1.put(REPORT_TIME,time);
        contentValues1.put(REPORT_USER,username);
        contentValues1.put(REPORT_WHOLESALE_SALES,amount);
        contentValues1.put(REPORT_DEBT_SALES,0);
        contentValues1.put(REPORT_SOLD_ITEMS,value);
        contentValues1.put(REPORT_RETAIL_SALES,0);
        contentValues1.put(REPORT_ADDED_ITEMS,0);
        contentValues1.put(REPORT_PRODUCT,product_name);

        db.insert(TABLE_SALES,null,contentValues);
        db.insert(TABLE_REPORT,null,contentValues1);
    return true;
    }


    public void insertSalesExpenses (String amount,String username,String date,String time, String product_name,String stock){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put(COLUMN_DAILY_EXPENSE,amount );
        contentValues.put((COLUMN_OPENING_CASH_SALES ),0 );
        contentValues.put(COLUMN_DAILY_DEBT,0 );
        contentValues.put(COLUMN_RETAIL_SALES,0 );
        contentValues.put(COLUMN_DEBTS_PAID,0 );
        contentValues.put(COLUMN_WHOLESALE_SALES,0 );
        contentValues.put(COLUMN_SALES_USER,username );
        contentValues.put(COLUMN_SALES_DATE,date);
        contentValues.put(COLUMN_SALES_TIME,time);
        contentValues.put(COLUMN_SALES_STATUS,0);
        contentValues1.put(REPORT_DATE,date);
        contentValues1.put(REPORT_TIME,time);
        contentValues1.put(REPORT_USER,username);
        contentValues1.put(REPORT_PRODUCT,product_name);
        contentValues1.put(REPORT_WHOLESALE_SALES,0);
        contentValues1.put(REPORT_RETAIL_SALES,0);
        contentValues1.put(REPORT_DEBT_SALES,0);
        contentValues1.put(REPORT_SOLD_ITEMS,0);
        contentValues1.put(REPORT_ADDED_ITEMS,stock);
        contentValues1.put(REPORT_STATUS,0);
        db.insert(TABLE_SALES,null,contentValues);
        db.insert(TABLE_REPORT,null, contentValues1);
    }


    public boolean addClient(String client_fullName,int added_debt, String client_debt,String Number,String client_Email, int status, String currentDateandTime, String type, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();

        contentValues.put(COLUMN_CLIENT_FULLNAME, client_fullName);
        contentValues.put(COLUMN_CLIENT_ADDED_DEBT, added_debt);
        contentValues.put(COLUMN_CLIENT_DEBT, client_debt);
        contentValues.put(COLUMN_NUMBER, Number);
        contentValues.put(COLUMN_CLIENT_EMAIL, client_Email);
        contentValues.put(COLUMN_STATUS, status);
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        contentValues1.put(TRANSACTION_USER,user);
        contentValues1.put(COLUMN_TRANSACTION_STATUS, 0);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.insert(TABLE_CLIENT, null, contentValues);
        db.close();
        return true;
    }

    public boolean syncAttempt(String currentDateandTime, String type, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        contentValues1.put(TRANSACTION_USER,user);
        contentValues1.put(COLUMN_TRANSACTION_STATUS, 0);
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
    public void updateClientStatus(int client_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_CLIENT_ADDED_DEBT,0);
        db.update(TABLE_CLIENT, contentValues, COLUMN_CLIENT_ID + "=" + client_id, null);
        db.close();
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
    public int unsyncedClients() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CLIENT + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        return count;
    }



    public boolean addProduct(String product_name,String Quantity,String product_buying_price,String product_retail_price,String product_wholesale_price, int status, String currentDateandTime, String type, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_NAME, product_name);
        contentValues.put(COLUMN_QUANTITY, Quantity);
        contentValues.put(COLUMN_PRODUCT_BUYING_PRICE, product_buying_price);
        contentValues.put(COLUMN_PRODUCT_RETAIL_PRICE, product_retail_price);
        contentValues.put(COLUMN_PRODUCT_WHOLESALE_PRICE, product_wholesale_price);
        contentValues.put(COLUMN_SOLD_PRODUCT, 0);
        contentValues.put(COLUMN_STATUSS, status);
        contentValues1.put(TRANSACTION_DATE,currentDateandTime);
        contentValues1.put(TRANSACTION_TYPE,type);
        contentValues1.put(TRANSACTION_USER,user);
        contentValues1.put(COLUMN_TRANSACTION_STATUS, 0);
        db.insert(TABLE_TRANSACTIONS, null, contentValues1);
        db.insert(TABLE_PRODUCTS, null, contentValues);
        db.close();
        return true;
    }


    /*
     * This method taking two arguments
     * first one is the id of the name for which
     * we have to update the sync status
     * and the second one is the status that will be changed
     * */
    public void updateProductStatus(int product_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_SOLD_PRODUCT,0);
        db.update(TABLE_PRODUCTS, contentValues, COLUMN_PRODUCT_ID + "=" + product_id, null);
        db.close();
    }

    public void updateUSerStatus(int user_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_STATUS, status);
        db.update(TABLE_USER, contentValues, COLUMN_USER_ID + "=" + user_id, null);
        db.close();
    }
    public void updateReportStatus(int report_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REPORT_STATUS, status);
        db.update(TABLE_REPORT, contentValues, REPORT_ID + "=" + report_id, null);
        db.close();
    }
    public void updateMpesaStatus(int mpesa_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MPESA_STATUS, status);
        db.update(TABLE_MPESA, contentValues, MPESA_ID + "=" + mpesa_id, null);
        db.close();
    }
    public void updateSalesStatus(int sales_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SALES_STATUS, status);
        db.update(TABLE_SALES, contentValues, COLUMN_SALES_ID + "=" + sales_id, null);
        db.close();
    }
    public void updateTransactionStatus(int transaction_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TRANSACTION_STATUS, status);
        db.update(TABLE_TRANSACTIONS, contentValues, TRANSACTION_ID + "=" + transaction_id, null);
        db.close();
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
    public int unsyncedProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUSS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        return count;
    }

    public Cursor getUnsyncedTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[]{"0"};
        String sql = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TRANSACTION_STATUS + " = ?;";
        Cursor cursor = db.rawQuery(sql, param);
        return cursor;
    }
    public int unsyncedTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[]{"0"};
        String sql = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TRANSACTION_STATUS + " = ?;";
        Cursor cursor = db.rawQuery(sql, param);
        int count = cursor.getCount();
        return count;
    }

    public Cursor getUnsyncedUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[]{"0"};
        String sql = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_STATUS + " = ?;";
        return db.rawQuery(sql, param);
    }
    public int unsyncedUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        return count;

    }
    public Cursor getUnsyncedReports() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[]{"0"};
        String sql = "SELECT * FROM " + TABLE_REPORT + " WHERE " + REPORT_STATUS + " = ?;";
        return db.rawQuery(sql, param);
    }
    public int unsyncedReports() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_REPORT + " WHERE " + REPORT_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        return count;
    }

    public Cursor getUnsyncedMpesa() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[]{"0"};
        String sql = "SELECT * FROM " + TABLE_MPESA + " WHERE " + COLUMN_MPESA_STATUS + " = ?;";
        return db.rawQuery(sql, param);
    }
    public int unsyncedMpesa() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_MPESA + " WHERE " + COLUMN_MPESA_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        return count;
    }
    public Cursor getUnsyncedSales() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[]{"0"};
        String sql = "SELECT * FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_STATUS + " = ?;";
        return db.rawQuery(sql, param);
    }
    public int unsyncedSales() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        return count;
    }

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


    public ArrayList<String> getAllSalesDates() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "SELECT DISTINCT " + COLUMN_SALES_DATE + " FROM " + TABLE_SALES + " ORDER BY " + COLUMN_SALES_DATE + " ASC;";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String dateOfTransaction = cursor.getString(cursor.getColumnIndex("sales_date"));
                    list.add(dateOfTransaction);
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
    public ArrayList<String> getAllReportsDates() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "SELECT DISTINCT " + REPORT_DATE + " FROM " + TABLE_REPORT + " ORDER BY " + REPORT_DATE + " ASC;";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String dateOfTransaction = cursor.getString(cursor.getColumnIndex("report_date"));
                    list.add(dateOfTransaction);
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
    public ArrayList<String> getAllSalesUsers() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "SELECT DISTINCT " + COLUMN_SALES_USER + " FROM " + TABLE_SALES;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String dateOfTransaction = cursor.getString(cursor.getColumnIndex("sales_user"));
                    list.add(dateOfTransaction);
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
    public ArrayList<String> getAllReportsUsers() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "SELECT DISTINCT " + REPORT_USER + " FROM " + TABLE_REPORT;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String dateOfTransaction = cursor.getString(cursor.getColumnIndex("report_user"));
                    list.add(dateOfTransaction);
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
    public ArrayList<String> getAllReportProducts() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "SELECT DISTINCT " + REPORT_PRODUCT + " FROM " + TABLE_REPORT + " ORDER BY " + REPORT_PRODUCT + " ASC;";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String dateOfTransaction = cursor.getString(cursor.getColumnIndex("report_product"));
                    list.add(dateOfTransaction);
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


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}