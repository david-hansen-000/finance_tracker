package home.david.finances;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Bills extends AppCompatActivity {
    private LinearLayout main;
    private Database database;

    private ArrayList<Bill> bill_list;
    private BillAdapter bill_adapter;
    private BillData billData;
    private TextView total;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bills_list);
        context = this;
        setTitle("Recurring Bills");
        total = findViewById(R.id.bill_list_total);
        database = new Database(this);
        billData = new BillData(database.selectRaw("select * from defaults"), database.selectRaw("select * from bills"));
        bill_adapter = new BillAdapter(this, database);
        bill_list = new ArrayList<>();
        fillData();
    }


    private void fillData() {
        bill_list = billData.getBillArrayList();
        for (Bill bill : bill_list) {
            bill.setButton_action(this::buttonAction);
        }
        log("bill_list should have:" + bill_list.size());
        bill_adapter.setBills(bill_list);
        ListView list = findViewById(R.id.bill_list);
        list.setAdapter(bill_adapter);
        log("list has:" + list.getAdapter().getCount());
        updateTotal();
    }

    private void log(String message) {
        Log.i("Bills", message);
    }

    private void buttonAction(View view) {
        Button b = (Button) view;
        String key = b.getText().toString();
        //TODO: this part here doesn't get current values that I just corrected in the edittext fields
        //--how to pass the correct values so it gets recorded properly?
        SimpleBill bill = new SimpleBill(key, billData.getCurrentDuedate(key), billData.getCurrentAmount(key));

        log("clicked " + bill + " with amount=" + bill.getAmount() + " and duedate=" + bill.getDuedate());
        TableRow row = new TableRow();
        row.addRowItem("amount", bill.getAmount());
        row.addRowItem("duedate", bill.getDuedate());
        row.addRowItem("bill", bill.getName());
        database.updateBills("bills", row);
        Toast.makeText(context, "saved to bills", Toast.LENGTH_SHORT).show();
        BillDialog dialog = new BillDialog(context, bill, database);
        dialog.show();
        updateTotal();
    }

    private void updateTotal() {
        total.setText("Total for bills:" + billData.getTotal());
    }


//TODO: another table in database with default values for bill types
//--ie: rent next date on first of month at 1400 amount
//--directv due on 6 of month at 12
//etc.

}
