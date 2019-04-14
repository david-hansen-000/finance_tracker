package home.david.finances;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

class MoneyAdapter extends ArrayAdapter<MoneyItem> {
    private ArrayList<MoneyItem> items;
    private Context context;
    public MoneyAdapter(@NonNull Context context) {
        super(context, 0);
        this.context=context;
        items=new ArrayList<>();
    }

    public void add(MoneyItem item) {
        items.add(item);
        Collections.sort(items);
    }

    public void addAll(Collection<? extends MoneyItem> c) {
        items.addAll(c);
        Collections.sort(items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if (view==null) {
            view= LayoutInflater.from(context).inflate(R.layout.money_item, parent, false);
        }
        MoneyItem item=items.get(position);
        TextView name=view.findViewById(R.id.money_list_name);
        TextView amount=view.findViewById(R.id.money_list_amount);
        TextView duedate=view.findViewById(R.id.money_list_duedate);
        name.setText(item.getName());
        amount.setText(NumberFormat.getCurrencyInstance().format(Double.parseDouble(item.getAmount())));
        duedate.setText(item.getDuedate());
        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public int getPosition(@Nullable MoneyItem item) {
        return items.indexOf(item);
    }
}
