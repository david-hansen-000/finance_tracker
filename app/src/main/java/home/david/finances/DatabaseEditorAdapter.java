package home.david.finances;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

public class DatabaseEditorAdapter extends ArrayAdapter<TableRow> {

    private Context context;
    private ArrayList<TableRow> rows;
    private Database database;

    public DatabaseEditorAdapter(@NonNull Context context, Database database) {
        super(context, R.layout.edit_list_item);
        this.context=context;
        rows=new ArrayList<>();
        this.database=database;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= convertView;
        if (convertView==null) {
            view= LayoutInflater.from(context).inflate(R.layout.edit_list_item, parent);
        }
        LinearLayout layout=view.findViewById(R.id.edit_list_item_linear);
        TableRow item=rows.get(position);
        ArrayList<String> columns=item.getColumnNames();
        LinearLayout column= null;
        View inner= LayoutInflater.from(context).inflate(R.layout.column, layout);

        TextView upper=column.findViewById(R.id.column_editor_upper);
        TextView lower=column.findViewById(R.id.column_editor_lower);
        for (int i=0; i<columns.size(); i++) {
            column = (LinearLayout) inner;
            upper.setText(columns.get(i));
            lower.setText(item.getRowItemAsString(columns.get(i)));
            layout.addView(column);
        }
        log("should have a new item in list:"+position);
        return view;
    }

    @Override
    public int getPosition(@Nullable TableRow item) {
        return rows.indexOf(item);
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    public void addAll(Collection<? extends TableRow> rows) {
        this.rows.addAll(rows);
    }

    private class Column {
        private String name;
        private String content;

        private TextView upper;
        private TextView lower;
        public Column(TextView upper, TextView lower) {
            this.upper=upper;
            this.lower=lower;
        }


        protected void setName(String name) {
            this.name=name;
            upper.setText(name);
        }
        protected String getName() {
            return name;
        }
        protected void setContent(String content) {
            this.content=content;
            String text=null;
            if (content.length()>50) {
                text=content.substring(0,50);
            } else {
                text=content;
            }
            lower.setText(text);
        }
        protected String getContent() {
            return content;
        }


    }

    private void log(String message) {
        Log.i("DatabaseEditorAdapter", message);
    }
}
