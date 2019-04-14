package home.david.finances;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AuditDialog extends Dialog {
    private Action action;
    private Context context;
    private Database database;
    private EditText editText;

    public AuditDialog(@NonNull Context context, Database database) {
        super(context);
        this.context = context;
        this.database = database;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audit_dialog_layout);
        ListView listView = findViewById(R.id.audit_dialog_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.audit_text, database.getQueries());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(((parent, view, position, id) -> {
            editText.setText(adapter.getItem(position));
        }));

        editText = findViewById(R.id.audit_dialog_et);

        Button button = findViewById(R.id.audit_dialog_button);
        button.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (!text.equals("")) {
                if (action != null) {
                    log("sql:" + text);
                    action.perform(text);
                }
            }
            hide();
        });
        Button save = findViewById(R.id.audit_dialog_save);
        save.setOnClickListener(v -> {
            String text = editText.getText().toString();
            database.addQuery(text);
            Toast.makeText(context, "query saved", Toast.LENGTH_SHORT).show();
        });
    }

    private void log(String message) {
        Log.i("AuditDialog", message);
    }

    public void setAction(Action action) {
        this.action = action;
    }


    interface Action {
        void perform(String text);
    }
}
