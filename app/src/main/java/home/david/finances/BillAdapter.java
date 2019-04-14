package home.david.finances;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class BillAdapter extends ArrayAdapter {
    private ArrayList<Bill> bills;
    private Context context;
    private Database database;
    public BillAdapter(@NonNull Context context, Database database) {
        super(context, R.layout.bill_layout);
        this.context=context;
        this.database=database;
        bills=new ArrayList<>();
    }

    public void addItem(Bill bill) {
        bills.add(bill);
    }

    public void setBills(ArrayList<Bill> bills) {
        this.bills=bills;
        sortBills();
    }


    public void sortBills() {
        Collections.sort(bills);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.bill_layout, parent, false);
        }

        TextView bill_textview= view.findViewById(R.id.bill_layout_text);
        EditText bill_amount=view.findViewById(R.id.bill_layout_amount);
        EditText bill_date = view.findViewById(R.id.bill_layout_duedate);
        Button bill_btn=view.findViewById(R.id.bill_layout_button);

        Bill bill=bills.get(position);
        bill_textview.setText( getLastPaid(bill.getBill_name()));
        bill_amount.setText(bill.getBill_amount());
        bill_date.setText(bill.getBill_duedate());
        bill_btn.setText(bill.getBill_name());
        bill_btn.setOnClickListener(bill.getButton_action());
        bill.setAmount_tv(bill_amount);
        bill.setDuedate_tv(bill_date);
        return view;
    }

    private String getLastPaid(String category) {
        String result=null;
        ArrayList<TableRow> data=database.selectRaw("select datetime from expenses where category='"+category+"' order by datetime desc limit 1");
        if (data.size()>0) {
            result="Last recorded payment of "+category+" was: " + data.get(0).getRowItemAsString("datetime");
        } else {
            result = "No last recorded payment for "+category+" was found.";
        }
        return result;
    }

    @Override
    public int getPosition(@Nullable Object item) {
        int result=-1;
        for (int i=0; i<bills.size(); i++) {
            if (bills.get(i).equals(item)) {
                result=i;
            }
        }
        return result;
    }

    @Override
    public int getCount() {
        return bills.size();
    }
}
