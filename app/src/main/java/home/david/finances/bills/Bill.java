package home.david.finances.bills;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import home.david.finances.Database;
import home.david.finances.PopupEditor;

public class Bill implements Comparable<Bill>,Cloneable {
    private String type;
    private Date duedate;
    private float amount;
    private TextView amount_tv;
    private TextView duedate_tv;
    private Database database;
    private Context context;
    private boolean negative;
    private PopupEditor popupEditor;
    public static SimpleDateFormat simpleDate=new SimpleDateFormat("yyyy-MM-dd");

    public Bill(Context context) {
        type=null;
        duedate=null;
        amount=0f;
        amount_tv=null;
        duedate_tv=null;
        database=null;
        this.context=context;
        popupEditor=new PopupEditor(context);
        negative=true;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public void setDuedate(String date) {
        Date d=null;
        try {
            d=simpleDate.parse(date);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        if (d!=null) {
            duedate=d;
        }
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setAmount(String amount) {
        float value = 0f;
        try {
            value = Float.parseFloat(amount);
        } catch (NumberFormatException nfe) {

        } catch (NullPointerException npe) {

        }
        setAmount(value);
    }

    public TextView getAmount_tv() {
        return amount_tv;
    }

    public void setAmount_tv(TextView amount_tv) {
        this.amount_tv = amount_tv;
        amount_tv.setText(amount+"");
        amount_tv.setOnClickListener(view -> {
            TextView tv=(TextView)view;
            popupEditor.setText(tv.getText().toString());
            popupEditor.setOkAction(v-> {
                float value=Float.parseFloat(popupEditor.getText());
                if (value>0 && value!= amount) {
                    this.amount = value;
                }
            });
        });
    }

    public TextView getDuedate_tv() {
        return duedate_tv;
    }

    public void setDuedate_tv(TextView duedate_tv) {
        this.duedate_tv = duedate_tv;
        duedate_tv.setText(Bill.simpleDate.format(duedate));
        duedate_tv.setOnClickListener(view -> {
            TextView tv=(TextView)view;
            popupEditor.setText(tv.getText().toString());
            popupEditor.setOkAction(v-> {
                Date value = null;
                try {
                    value = Bill.simpleDate.parse(popupEditor.getText());
                } catch (ParseException e) {
                    //e.printStackTrace();
                }
                if (value!=null && !value.equals(duedate)) {
                    this.duedate = value;
                }
            });
        });
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public boolean isNegative() {
        return negative;
    }

    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    public String toString() {
        return type;
    }

    public Bill copy() {
        Bill result = null;
        try {
            result = (Bill) super.clone();
            result.context=this.context;
            result.database=this.database;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int compareTo(@NonNull Bill bill) {
        return duedate.compareTo(bill.duedate);
    }
}
