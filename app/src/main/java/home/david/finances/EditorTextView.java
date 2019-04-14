package home.david.finances;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EditorTextView extends android.support.v7.widget.AppCompatTextView {
    private final boolean wider;
    private TableRow row;
    private PopupEditor popup;
    private String key;
    private Database database;
    private String table;

    public EditorTextView(Context context, boolean wider) {
        super(context);
        this.wider = wider;
    }

    public void setThisText(String column_text) {
        String text = null;

        if (!wider && column_text.length() > 13) {
            text = column_text.substring(0, 13);
        } else {
            text = column_text;
        }
        super.setText(text);
    }

    private void onClick(View view) {
        String text = row.getRowItemAsString(key);
        popup.setText(text);
        popup.setFocusable(true);
        popup.setTouchable(true);
        log("popup has:" + text);
        popup.setOkAction(v -> {
            Button button = (Button) v;

            if (popup.getText() == null || popup.getText() == "" || popup.getText().equals(text)) {
                log("popup hasn't changed or has no value:" + popup.getText());
            } else {
                log("value changed to " + popup.getText());
                setThisText(popup.getText());
                TableRow update = new TableRow();
                update.addRowItem("rowid", row.getRowItemAsString("rowid"));
                update.addRowItem(key, popup.getText());
                log(table + " TableRow update:" + update);
                database.updateTable(table, update);
            }


            popup.dismiss();
        });
        popup.setDeleteAction(v -> {
            int id = (int) row.getRowItem("rowid");
            if (id > 0) {
                log("deleting " + id + " from " + table);
                database.deleteFromTable(table, id);
            }
            popup.dismiss();
        });
        popup.showAsDropDown(view);

    }

    public Properties getProperties() {
        return new Properties();
    }

    public void setProperties(Properties properties) {
        this.row = properties.row;
        this.popup = properties.popup;
        this.database = properties.database;
        table = properties.table;
        setLayoutParams(properties.layoutParams);
        key = row.getColumnNames().get(properties.i);
        String column_text = row.getRowItemAsString(key);

        setThisText(column_text);
        setOnClickListener(this::onClick);
    }

    private void log(String message) {
        Log.i("EditTextView", message);
    }

    public class Properties {
        private Context context;
        private PopupEditor popup;
        private TableRow row;
        private String table;
        private ViewGroup.LayoutParams layoutParams;
        private int i;
        private Database database;

        public Properties() {

        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public PopupEditor getPopup() {
            return popup;
        }

        public void setPopup(PopupEditor popup) {
            this.popup = popup;
        }

        public TableRow getRow() {
            return row;
        }

        public void setRow(TableRow row) {
            this.row = row;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public ViewGroup.LayoutParams getLayoutParams() {
            return layoutParams;
        }

        public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
            this.layoutParams = layoutParams;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public Database getDatabase() {
            return database;
        }

        public void setDatabase(Database database) {
            this.database = database;
        }
    }


}
