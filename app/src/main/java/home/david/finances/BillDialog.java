package home.david.finances;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.Toast;

public class BillDialog extends Dialog {
    private Button default_btn;
    private Button record_btn;
    private Button cancel_btn;
    private SimpleBill bill;
    private Database database;
    private Context context;
    public BillDialog(@NonNull Context context, SimpleBill bill, Database database) {
        super(context);
        this.context=context;
        this.bill=bill;
        this.database=database;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_dialog_layout);
        default_btn=findViewById(R.id.bill_dialog_default_btn);
        record_btn=findViewById(R.id.bill_dialog_record_btn);
        cancel_btn=findViewById(R.id.bill_dialog_cancel_btn);

        default_btn.setOnClickListener(view->{
            TableRow row=bill.getTableRow().clone();
            String day=row.getRowItemAsString("duedate").substring(8, 10);
            row.addRowItem("duedate", day);
            database.updateBills("defaults", bill.getTableRow());
            Toast.makeText(context, "defaults updated", Toast.LENGTH_SHORT).show();
            closeWindow();
        });


        record_btn.setOnClickListener(view->{
            TableRow record=new TableRow();
            record.addRowItem("item", bill.getName());
            record.addRowItem("category", bill.getName());
            record.addRowItem("cost", Float.parseFloat(bill.getAmount()));
            record.addRowItem("datetime", bill.getDuedate()+" 00:00:00.0");
            database.insert(record);
            Toast.makeText(context, "bill was recorded", Toast.LENGTH_SHORT).show();
            closeWindow();
        });

        cancel_btn.setOnClickListener(view-> {
            closeWindow();
        });
    }

    private void closeWindow() {
        hide();
    }
}
