package com.example.derich.bizwiz;

import android.provider.BaseColumns;

public final class DatabaseHelpless  {
    private DatabaseHelpless() {}
    public static final class clientsInfo implements BaseColumns{
        public static final String TABLE_NAME = "clients";
        public static final String COLUMN_ID = "client_id";
        public static final String COLUMN_NAME= "client_name";
        public static final String COLUMN_DEBT = "client_name";

                //CREATE TABLE TABLE_NAME(CLIENT_ID, COLUMN_NAME ......);
        public static  final String SQL_CREATE_TABLE =
                        "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + "INT UNIQUE NOT NULL ," + COLUMN_NAME + "TEXT NOT NULL," + COLUMN_DEBT +
                        "TEXT)";
    }
}
