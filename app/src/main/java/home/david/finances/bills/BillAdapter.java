package home.david.finances.bills;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import home.david.finances.PopupEditor;
import home.david.finances.R;

public class BillAdapter extends ArrayAdapter<Bill> {
    private Context context;
    private ArrayList<Bill> bills;
    private float startingTotal;
    private float runningTotal;
    private NumberFormat numberFormat;
    public BillAdapter(@NonNull Context context, float startingTotal) {
        super(context, 0);
        this.context=context;
        bills=new ArrayList<>();
        this.startingTotal=startingTotal;
        runningTotal=0f;
        numberFormat=NumberFormat.getCurrencyInstance();
    }

    @Override
    public void addAll(Bill... items) {
        bills.addAll(Arrays.asList(items));
        log("added:"+items.length+" items");
        sortBills();
    }

    @Override
    public void addAll(@NonNull Collection<? extends Bill> collection) {
        bills.addAll(collection);
        log("added:"+collection.size()+" items");
        sortBills();
    }

    private void log(String message) {
        Log.i("BillAdapter", message);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if (view==null) {
            view=LayoutInflater.from(context).inflate(R.layout.bill_layout_complete, parent, false);
        }
        if (position==0) {
            runningTotal=startingTotal;
        }
        Bill bill=bills.get(position);
        TextView type=view.findViewById(R.id.complete_bill_type);
        TextView amount=view.findViewById(R.id.complete_bill_amount);
        TextView duedate=view.findViewById(R.id.complete_bill_duedate);
        TextView total=view.findViewById(R.id.complete_bill_total);
        if (bill.isNegative()) {
            runningTotal=runningTotal-bill.getAmount();
        } else {
            runningTotal=runningTotal+bill.getAmount();
        }
        total.setText(numberFormat.format(runningTotal));
        type.setText(bill.getType());
        bill.setAmount_tv(amount);
        bill.setDuedate_tv(duedate);
        return view;
    }

    public void sortBills() {
        Collections.sort(bills);
    }

    @Override
    public int getCount() {
        return bills.size();
    }

    @Nullable
    @Override
    public Bill getItem(int position) {
        return bills.get(position);
    }

    @Override
    public int getPosition(@Nullable Bill item) {
        int result=-1;
        for (int i=0; i<bills.size(); i++) {
            if (bills.get(i).equals(item)) {
                result=i;
            }
        }
        return result;
    }
}
