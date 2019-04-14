package home.david.finances;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.NumberFormat;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by david on 2/16/18.
 */

public class Database {
    private static SQLiteDatabase database;

    public Database(Context context) {
        if (database == null) {
            File database_file = new File(context.getExternalFilesDir(null), "finances.db");
            database = SQLiteDatabase.openOrCreateDatabase(database_file, null);
            createTable();
        }
    }

    public static String getTotal(String datetime) {
        String result = null;
        if (database != null) {
            Cursor c = database.rawQuery("select sum(cost) as total from expenses where datetime=?", new String[]{datetime});
            if (c.moveToFirst()) {
                result = "total: " + NumberFormat.getCurrencyInstance().format(c.getDouble(0));
            }
            c.close();
        } else {
            result = "no database";
        }
        return result;
    }

    public static ArrayList<TableRow> select(String sql) {
        ArrayList<TableRow> rows = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                rows.add(new TableRow(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rows;
    }

    public void close() {
        database.close();
    }

    private void createTable() {
        database.execSQL("create table if not exists expenses (item text, category text, cost real, upc text, more_info text, datetime text, transferred integer)");
        database.execSQL("create table if not exists queries (aquery text)");
        database.execSQL("create table if not exists bills (bill text, duedate text, amount text)");
        database.execSQL("create table if not exists defaults (bill text, duedate text, amount text)");
        database.execSQL("create table if not exists income (item text, duedate text, amount text)");
    }

    public void insert(ContentValues values) {
        //Log.v("MESSAGE","content values submitting to database:"+values.toString());
        insertTable("expenses", values);
    }

    public void insert(TableRow values) {
        insert(values.getContentValues());
    }

    public void insertTable(String table, ContentValues values) {
        database.insert(table, null, values);
    }
    public void update(TableRow values) {
        updateTable("expenses", values);
    }

    public void updateTable(String table, TableRow values) {
        if (values.hasRowItem("rowid")) {
            ContentValues editedValues = values.getContentValues();
            database.update(table, editedValues, "rowid=?", new String[]{values.getRowItemAsString("rowid")});
        }
    }

    public void updateBills(String table, TableRow values) {
        database.update(table, values.getContentValues(), "bill=?", new String[]{values.getRowItemAsString("bill")});
        //database.execSQL("update "+table+" set amount='"
        //        + values.getRowItemAsString("amount")+"', duedate='"
        //        +values.getRowItemAsString("duedate")+"' where bill='"+values.getRowItemAsString("bill")+"'");
    }

    public void deleteFromTable(String table, TableRow values) {
        if (values.hasRowItem("rowid")) {
            Integer rowid = (Integer) values.getRowItem("rowid");
            deleteFromTable(table, rowid.intValue());
        }
    }

    public void deleteFromTable(String table, int id) {
        if (id > 0) {
            database.delete(table, "rowid=?", new String[]{id+""});
        }
    }


    public ArrayList<TableRow> select(String[] columns) {
        ArrayList<TableRow> rows = new ArrayList<>();
        Cursor cursor = database.query("expense", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                rows.add(new TableRow(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rows;
    }

    public ArrayList<TableRow> selectRaw(String sql) {
        ArrayList<TableRow> rows=new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    rows.add(new TableRow(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException ex) {
            TableRow row=new TableRow();
            row.addRowItem("none", ex.getCause());
            rows.add(row);
        }
        return rows;
    }



    public String getLastDatetime() {
        String datetime = null;
        String sql = "select datetime from expenses order by rowid desc limit 1";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            datetime = cursor.getString(0);
        }
        cursor.close();
        return datetime;
    }

    public void addQuery(String query) {
        ContentValues values=new ContentValues();
        values.put("aquery", query);
        database.insert("queries", null, values);
    }

    public ArrayList<String> getQueries() {
        ArrayList<String> queries=new ArrayList<>();
        Cursor cursor=database.query("queries", new String[]{"aquery"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                queries.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return queries;
    }

    public void editQuery(String oldQuery, String newQuery) {
        ContentValues values=new ContentValues();
        values.put("query", newQuery);
        database.update("queries", values, "query='?'", new String[]{oldQuery});
    }

    public ArrayList<String> getTables() {
        ArrayList<String> tables=new ArrayList<>();
        Cursor cursor=database.rawQuery("select tbl_name from SQLITE_MASTER", null);
        if (cursor.moveToFirst()) {
            do {
                tables.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        return tables;
    }

}
