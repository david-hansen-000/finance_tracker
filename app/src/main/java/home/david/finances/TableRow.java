package home.david.finances;

import android.content.ContentValues;
import android.database.Cursor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class TableRow {
    private HashMap<String, Object> rowMap;

    private ArrayList<String>rowOrder;

    /**
     *
     */
    public TableRow() {
        rowMap = new HashMap<>();
        rowOrder=new ArrayList<>();
    }

    public TableRow(Cursor cursor) {
        this();
        addRow(cursor);
    }

    public TableRow(HashMap<String, Object> map) {
        rowMap = map;
        rowOrder=new ArrayList<>();
    }

    public ArrayList<String> getRowOrder() {
        return rowOrder;
    }

    /**
     * Get a specified order for the row data
     *
     * @param columns in ArrayList&lt String&GT format which is the order the data will be returned
     * @return An ArrayList with objects in order of columns provided, if column name was in the TableRow
     */
    public ArrayList<Object> getDataByOrder(ArrayList<String> columns) {
        ArrayList<Object> results = new ArrayList<>();
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            if (rowMap.containsKey(columns.get(i))) {
                results.add(rowMap.get(columns.get(i)));
            }
        }
        return results;
    }

    /**
     * A method to return the TableRow
     *
     * @return Returns a HashMap&LT String,Object&GT object
     */
    public HashMap<String, Object> getRowMap() {
        return rowMap;
    }

    /**
     * A method to get the Column Names in the TableRow
     *
     * @return Returns an ArrayList&LT String&GT object of column names.
     */
    public ArrayList<String> getColumnNames() {
        ArrayList<String> columnNames = new ArrayList<>(rowMap.keySet());
        return columnNames;
    }

    /**
     * A method to get the column names as a single String object.
     *
     * @param separator The separator string to delimit each column name. If null will default to commas.
     * @return A String of column names in the TableRow separated by given String.
     */
    public String getColumnNamesAsString(String separator) {
        StringBuilder builder = new StringBuilder();

        if (separator == null || separator.equals("")) {
            separator = ",";
        }
        for (String column : rowMap.keySet()) {
            builder.append(column).append(separator);
        }
        for (int i = 0; i < separator.length(); i++) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    /**
     * A method to get the column names as a single String object.
     *
     * @return A String of column names in the TableRow separated by commas.
     */
    public String getColumnNamesAsString() {
        return getColumnNamesAsString(null);
    }

    /**
     * @param cursor
     */
    public void addRow(Cursor cursor) {

        int columnCount = cursor.getColumnCount();
        if (columnCount > 0) {
            for (int i = 0; i < columnCount; i++) {
                switch (cursor.getType(i)) {
                    case Cursor.FIELD_TYPE_FLOAT: {
                        rowMap.put(cursor.getColumnName(i), cursor.getDouble(i));
                        rowOrder.add(cursor.getColumnName(i));
                        break;
                    }
                    case Cursor.FIELD_TYPE_INTEGER: {
                        rowMap.put(cursor.getColumnName(i), cursor.getInt(i));
                        rowOrder.add(cursor.getColumnName(i));
                        break;
                    }
                    case Cursor.FIELD_TYPE_STRING: {
                        rowMap.put(cursor.getColumnName(i), cursor.getString(i));
                        rowOrder.add(cursor.getColumnName(i));
                        break;
                    }
                    default: {
                        rowMap.put(cursor.getColumnName(i), null);
                        rowOrder.add(cursor.getColumnName(i));
                    }
                }

            }
        }

    }

    public Object removeRowItem(String column) {
        return rowMap.remove(column);
    }

    public Object addRowItem(String columnName, Object item) {
        return rowMap.put(columnName, item);
    }

    public Object getRowItem(String columnName) {
        return rowMap.get(columnName);
    }

    public boolean hasRowItem(String columnName) {
        return rowMap.containsKey(columnName);
    }

    public String getRowItemAsString(String columnName) {
        Object data=rowMap.get(columnName);
        String result;
        if (data==null) {
            result="";
        } else {
            result=data.toString();
        }
        return result;
    }

    public String dataAsString() {
        StringBuilder builder = new StringBuilder();
        for (String column : rowMap.keySet()) {
            builder.append(column).append(":").append(rowMap.get(column)).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public String conciseString() {
        StringBuilder builder = new StringBuilder();
        if (rowOrder.size()<1) {
            for (String column : rowMap.keySet()) {
                builder.append(rowMap.get(column)).append(",");
            }
        } else {
           for (int i=0; i<rowOrder.size(); i++) {
               builder.append(rowMap.get(rowOrder.get(i))).append(",");
           }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    @Override
    public String toString() {
        return dataAsString();
    }

    @SuppressWarnings("unchecked")
    public TableRow clone() {
        TableRow row=null;
        if (rowMap.clone()==HashMap.class) {
            row = new TableRow((HashMap<String, Object>) rowMap.clone());
        }
        return row;
    }

    public ContentValues getContentValues() {
        ContentValues values=new ContentValues();
        for (String column:rowMap.keySet()) {
            if (!column.equals("rowid")) {
                if (rowMap.get(column).getClass() == String.class) {
                    values.put(column, (String) rowMap.get(column));
                }
                if (rowMap.get(column).getClass() == int.class) {
                    if (rowMap.get(column)==null) {
                        values.put(column,0);
                    } else {
                        values.put(column, (int) rowMap.get(column));
                    }
                }
                if (rowMap.get(column).getClass() == double.class) {
                    if (rowMap.get(column)==null) {
                        values.put(column,0.0);
                    } else {
                        values.put(column, (double) rowMap.get(column));
                    }
                }
            }
        }
        return values;
    }

    public int size() {
        return rowMap.size();
    }
}
