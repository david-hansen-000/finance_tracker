package home.david.finances;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditingFieldsFragment extends Fragment {

    private Texts texts;
    private Database database;
    private TableRow editing_item;
    private Button submit_btn;
    private ArrayAdapter<TableRow> adapter;

    public EditingFieldsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editing_fields, container, false);
        submit_btn = view.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(this::submitAction);
        texts = new Texts(view);
        view.findViewById(R.id.defaults_btn).setOnClickListener((v) -> defaultsAction());
        return view;
    }

    private void submitAction(View view) {
        if (submit_btn.getText().equals("submit")) {
            Log.d("MESSAGE", "we're submitting:" + texts.getTableRow().dataAsString());

            database.insert(texts.getContentValues());
            texts.setTotal();
            getSql("select rowid,* from expenses where datetime='" + texts.getDatetime() + "'", true);
        }
        if (submit_btn.getText().equals("Edit")) {
            if (editing_item != null && editing_item.hasRowItem("rowid")) {
                Log.d("MESSAGE", "we're editing");
                TableRow changedValues = new TableRow();
                TableRow currentValues = texts.getTableRow();
                Log.d("MESSAGE", "current:" + currentValues.dataAsString());
                Log.d("MESSAGE", "edited:" + editing_item.dataAsString());

                changedValues.addRowItem("rowid", editing_item.getRowItem("rowid"));
                //database.update(texts.getTableRow((Integer) editing_item.getRowItem("rowid")));
                String[] columns = new String[]{"item", "upc", "cost", "category", "more_info"};
                boolean noValues=true;
                for (String column : columns) {
                    if (currentValues.hasRowItem(column)) {
                        String currentValue = currentValues.getRowItemAsString(column);
                        String oldValue = editing_item.getRowItemAsString(column);
                        if (!currentValue.equals(oldValue)) {
                            noValues=false;
                            changedValues.addRowItem(column, currentValue);
                        }
                    }
                }
                if (!noValues) {
                    Log.d("MESSAGE", "changed:" + changedValues.dataAsString());
                    database.update(changedValues);
                } else {
                    Log.d("MESSAGE","nothing changed");
                }
                submit_btn.setText("submit");
                getSql("select rowid,* from expenses where datetime='" + texts.getDatetime() + "'", true);
            }

        }
    }


    private void defaultsAction() {
        texts.defaultValues();
    }


    private void getSql(String sql, boolean includeTotal) {
        ArrayList<TableRow> rows = Database.select(sql);
        adapter.clear();
        if (includeTotal) {
            rows.addAll(Database.select("select sum(cost) as total from expenses where datetime='" + texts.getDatetime() + "'"));
        }
        adapter.addAll(rows);
    }

    public void setDatabase(Database data) {
        database = data;
    }

    public void setEditing_item(TableRow editing) {
        editing_item = editing;
        submit_btn.setText("Edit");
        setupTexts(editing);
    }

    public void setAdapter(ArrayAdapter<TableRow> adapter) {
        this.adapter = adapter;
    }

    private void setupTexts(TableRow item) {
        String[] columns = new String[]{"item", "upc", "cost", "category", "more_info"};
        for (String column : columns) {

            if (item.hasRowItem(column)) {
                String value = item.getRowItemAsString(column);
                switch (column) {
                    case "item": {
                        texts.setItem(value);
                        break;
                    }
                    case "upc": {
                        texts.setUpc(value);
                        break;
                    }
                    case "cost": {
                        texts.setCost(value);
                        break;
                    }
                    case "category": {
                        texts.setCategory(value);
                        break;
                    }
                    case "more_info": {
                        texts.setMore(value);
                        break;
                    }
                }
            }
        }
    }

    public void setDateTime(String datetime) {
        texts.setDatetime(Texts.convertSQLtime(datetime));
    }

    private class GetTotal extends AsyncTask<Void, Void, String> {
        private Database database;
        private String datetime;
        private TextView textView;

        public GetTotal(Database database, String datetime, TextView textView) {
            this.database = database;
            this.datetime = datetime;
            this.textView = textView;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String total = database.getTotal(datetime);
            return total;
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText("Total:" + s);
        }
    }

    public void newReceipt() {
        defaultsAction();
    }
}
