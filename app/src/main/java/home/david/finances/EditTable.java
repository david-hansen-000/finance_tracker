package home.david.finances;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class EditTable extends AppCompatActivity {
    private LinearLayout layout;
    private Database database;
    private Context context;
    private PopupEditor popup;
    private String table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table);
        context=this;
        database=new Database(this);
        layout=findViewById(R.id.edit_view_list);
        Spinner tables=findViewById(R.id.table_spinner);
        Button button=findViewById(R.id.edit_table_refresh_btn);
        button.setOnClickListener(v->setList(table));
        final ArrayAdapter<String> tableAdapter=new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        tableAdapter.addAll(database.getTables());
        tables.setAdapter(tableAdapter);
        tables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                table =tableAdapter.getItem(position);
                log("should have selected:"+table);
                setList(table);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setUpPopup();
    }

    private void setUpPopup() {
        popup= new PopupEditor(this);
    }

    public void setList(String table) {
        layout.removeAllViews();
        boolean wider=false;
        ArrayList<TableRow> rows=database.selectRaw("select rowid,* from "+table+" order by rowid desc limit 20");
        if (rows.size()<3) {
            wider=true;
        }
        ArrayList<LinearLayout> rowsLayout=new ArrayList<>();
        ArrayList<String> column = null;
        ArrayList<LinearLayout> columnsLayout=new ArrayList<>();
        ViewGroup.LayoutParams tv_params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (wider) {
            tv_params.width=400;
        } else {
            tv_params.width = 250;
        }
        LinearLayout.LayoutParams row_params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams column_params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        if (wider) {
            column_params.width=400;
        } else {
            column_params.width = 250;
        }
        for (TableRow row:rows) {
            LinearLayout rowLayout=new LinearLayout(context);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(row_params);

            column=row.getColumnNames();
            for (int i=0; i<column.size(); i++) {
                LinearLayout columnLayout=new LinearLayout(context);
                columnLayout.setOrientation(LinearLayout.VERTICAL);
                columnLayout.setLayoutParams(column_params);
                TextView upper=new TextView(context);
                upper.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                upper.setLayoutParams(tv_params);
                upper.setText(column.get(i));
                EditorTextView lower=new EditorTextView(context, wider);
                EditorTextView.Properties properties = lower.getProperties();
                properties.setDatabase(database);
                properties.setPopup(popup);
                properties.setI(i);
                properties.setLayoutParams(tv_params);
                properties.setRow(row);
                properties.setTable(table);
                lower.setProperties(properties);
                columnLayout.addView(upper);
                columnLayout.addView(lower);
                columnsLayout.add(columnLayout);
                rowLayout.addView(columnLayout);
            }
            rowsLayout.add(rowLayout);
            layout.addView(rowLayout);
        }

    }

    private void log(String message) {
        Log.i("EditTable", message);
    }
}
