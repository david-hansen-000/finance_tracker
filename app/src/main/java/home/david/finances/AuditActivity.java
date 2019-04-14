package home.david.finances;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AuditActivity extends AppCompatActivity {


    private EditText editText; //TODO: change this to EditText
    private Button button;
    private TextView sql_results;
    private Database database;
    private AuditDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        if (database==null) {
            database=new Database(this);
        }

        editText=findViewById(R.id.sql_text);
        button=findViewById(R.id.sql_submit_btn);
        sql_results=findViewById(R.id.sql_results_txt);

        button.setOnClickListener(view -> submit());

        button.setOnLongClickListener(v -> {
            String text=editText.getText().toString();
            database.addQuery(text);
            Toast.makeText(this, "query saved", Toast.LENGTH_SHORT).show();
            return true;
        });

        findViewById(R.id.sql_tv).setOnLongClickListener(v -> {
            editText.setText("");
            return true;
        });

        dialog=new AuditDialog(this, database);
        dialog.setAction(text -> {
            editText.setText(text);
            submit();
        });


        editText.setOnLongClickListener(v -> {
            dialog.show();
            return true;
        });

    }

    private void submit() {
        sql_results.setText("");
        ArrayList<TableRow> results=Database.select(editText.getText().toString());
        if (results.size()>0) {
            for(TableRow row:results) {
                sql_results.append(row.conciseString()+"\n");
            }
        } else {
            sql_results.append("no results\n");
        }
    }
}
