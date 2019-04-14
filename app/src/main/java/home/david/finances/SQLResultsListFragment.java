package home.david.finances;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SQLResultsListFragment extends Fragment {

    private ArrayAdapter<TableRow> adapter;
    private Database database;
    private EditText sqlQuery;
    private EditingFieldsFragment editingFieldsFragment;

    public SQLResultsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sqlresults_list, container, false);
        ListView list = view.findViewById(R.id.edit_list);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener((parent, v, position, id) -> {
            //Log.v("message", "parent:" + parent + " view:" + v + " position:" + position + " id:" + id);

            TableRow item = (TableRow) parent.getItemAtPosition(position);
            if (item != null) {
                Log.v("MESSAGE", item.dataAsString());
                if (item.size() == 1 && item.hasRowItem("datetime")) {
                    getSql("select rowid,* from expenses where datetime='" + item.getRowItemAsString("datetime") + "'", item.getRowItemAsString("datetime"), true);
                    if (editingFieldsFragment != null)
                        editingFieldsFragment.setDateTime(item.getRowItemAsString("datetime"));

                } else {

                    if (item.hasRowItem("datetime")) {
                        if (editingFieldsFragment != null)
                            editingFieldsFragment.setDateTime(item.getRowItemAsString("datetime"));
                    }
                    if (item.hasRowItem("rowid")) {
                        if (editingFieldsFragment != null)
                            editingFieldsFragment.setEditing_item(item);
                        //editing_item = item;
                        //submit_btn.setText("Edit");
                    }
                }
            }
            return true;
        });
        sqlQuery = view.findViewById(R.id.sql_text);
        sqlQuery.setOnLongClickListener(v -> {
            String datetime=database.getLastDatetime();
            String sql = "select * from expenses where datetime='"+database.getLastDatetime()+"'";
            sqlQuery.setText(sql);
            getSql(sql, datetime, true);
            return true;
        });
        view.findViewById(R.id.sql_btn).setOnClickListener((v) -> sqlAction());
        return view;
    }

    public void setAdapter(ArrayAdapter<TableRow> adapter) {
        this.adapter = adapter;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    private void getSql(String sql, String datetime, boolean includeTotal) {
        ArrayList<TableRow> rows = Database.select(sql);
        adapter.clear();
        if (includeTotal) {
            rows.addAll(Database.select("select sum(cost) as total from expenses where datetime='" + datetime + "'"));
        }
        adapter.addAll(rows);
    }


    private void sqlAction() {
        getSql(sqlQuery.getText().toString(), null, false);
    }

    public void setEditingFragment(EditingFieldsFragment editingFieldsFragment) {
        this.editingFieldsFragment=editingFieldsFragment;
    }
}
