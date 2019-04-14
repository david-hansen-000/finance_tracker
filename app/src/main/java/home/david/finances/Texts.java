package home.david.finances;

import android.content.ContentValues;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by david on 2/16/18.
 */

public class Texts {
    private EditText item;
    private EditText upc;
    private EditText cost;
    private EditText category;
    private EditText datetime;
    private EditText more;
    private TextView total;
    private SimpleFunction function;

    public Texts(View activity) {
        item = activity.findViewById(R.id.item_et);

        upc = activity.findViewById(R.id.upc_et);
        cost = activity.findViewById(R.id.cost_et);
        category = activity.findViewById(R.id.category_et);
        datetime = activity.findViewById(R.id.datetime_et);
        more = activity.findViewById(R.id.more_et);
        total = activity.findViewById(R.id.total);

        STextWatcher itemWatcher=new STextWatcher(item);
        itemWatcher.addAllToWatching("tax", "discount");
        item.addTextChangedListener(itemWatcher);
        item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (item.getText().toString().startsWith("tax")) {
                    category.setText("tax");
                    upc.setText("");
                }
                if (item.getText().toString().startsWith("discount")) {
                    category.setText("discount");
                    upc.setText("");
                }
            }
        });

        datetime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newValue = s.toString();
                if (newValue.matches("\\d\\d/\\d\\d/\\d\\d\\s\\d\\d:\\d\\d:\\d\\d")) {
                    item.requestFocus();
                    setTotal();
                }
                if (newValue.matches("\\d\\d/\\d\\d/\\d\\d\\s\\d\\d") || newValue.matches("\\d\\d/\\d\\d/\\d\\d\\s\\d\\d:\\d\\d")) {
                    datetime.setText(newValue + ":");
                }
                if (newValue.matches("\\d\\d/\\d\\d/\\d\\d")) {
                    datetime.setText(newValue + " ");
                }
                if (newValue.matches("\\d\\d") || newValue.matches("\\d\\d/\\d\\d")) {
                    datetime.setText(newValue + "/");
                }
                datetime.setSelection(datetime.getText().length());
            }
        });

        more.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("s")) {
                    s.append("tore:");
                }
                if (s.toString().equals("store:w")) {
                    s.append("almart 3366");
                }
            }
        });

        STextWatcher categoryWatcher=new STextWatcher(category);
        categoryWatcher.addAllToWatching("food", "miscellaneous", "home", "electronics", "bath", "clothing");

        category.addTextChangedListener(categoryWatcher);
        defaultValues();
    }

    public void setTotal(String text) {
        total.setText(text);
    }

    public void setFunction(SimpleFunction function) {
        this.function=function;
    }

    public void setTotal() {
        total.setText(Database.getTotal(getDatetime()));
    }

    public void defaultValues() {
        category.setText("food");
        more.setText("store:walmart 3366");
        datetime.requestFocus();
    }


    public String getItem() {
        return item.getText().toString();
    }

    public void setItem(String item) {
        this.item.setText(item);
    }

    public String getUPC() {
        return upc.getText().toString();
    }

    public Double getCost() {
        Double value = 0.0;
        String c = cost.getText().toString();
        if (c != null && !c.equals("")) {
            value = Double.parseDouble(c);
        }
        return value;
    }

    public void setCost(String cost) {
        this.cost.setText(cost);
    }

    public String getCategory() {
        return category.getText().toString();
    }

    public void setCategory(String category) {
        this.category.setText(category);
    }

    public String getDatetime() {
        return getdate();
    }

    public void setDatetime(String datetime) {
        this.datetime.setText(datetime);
    }

    public String getMore() {
        return more.getText().toString();
    }

    public void setMore(String more) {
        this.more.setText(more);
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if (getItem() != null && !getItem().equals(""))
            values.put("item", getItem());
        if (getUPC() != null && !getUPC().equals(""))
            values.put("upc", getUPC());
        if (getCost() != 0)
            values.put("cost", getCost());
        if (getCategory() != null && !getCategory().equals(""))
            values.put("category", getCategory());
        if (getDatetime() != null && !getDatetime().equals(""))
            values.put("datetime", getDatetime());
        if (getMore() != null && !getMore().equals(""))
            values.put("more_info", getMore());
        item.requestFocus();
        return values;
    }

    public TableRow getTableRow() {
        TableRow values = new TableRow();
        if (getItem() != null && !getItem().equals(""))
            values.addRowItem("item", getItem());
        if (getUPC() != null && !getUPC().equals(""))
            values.addRowItem("upc", getUPC());
        if (getCost() != 0)
            values.addRowItem("cost", getCost());
        if (getCategory() != null && !getCategory().equals(""))
            values.addRowItem("category", getCategory());
        if (getDatetime() != null && !getDatetime().equals(""))
            values.addRowItem("datetime", getDatetime());
        if (getMore() != null && !getMore().equals(""))
            values.addRowItem("more_info", getMore());
        return values;
    }

    @Override
    public String toString() {
        return "item:" + item.getText().toString() +
                ", upc:" + upc.getText().toString() +
                ", cost:" + getCost() +
                ", category:" + category.getText().toString() +
                ", datetime:" + getDatetime().toString() +
                ", more:" + more.getText().toString();
    }

    public static String convertSQLtime(String date) {
        String result = null;
        //Log.v("MESSAGE","date:"+date);
//        StringBuilder b=new StringBuilder();
//        char[] c=date.toCharArray();
//        for (int i=0; i<c.length; i++) {
//            b.append(i).append(":").append(c[i]).append(" ");
//        }
//        Log.v("MESSAGE", b.toString());
        String year, month, day, time;
        year = date.substring(2, 4);
        month = date.substring(5, 7);
        day = date.substring(8, 10);
        time = date.substring(11, 19);
        result = month + "/" + day + "/" + year + " " + time;
        return result;
    }

    private String getdate() {
        String date = null;
        String year, month, day, hour, minute, second;
        String dateText = datetime.getText().toString();
        boolean dateError = false;
        if (dateText.matches("\\d\\d/\\d\\d/\\d\\d\\s\\d\\d:\\d\\d:\\d\\d")) {
            month = dateText.substring(0, 2);
            day = dateText.substring(3, 5);
            year = "20" + dateText.substring(6, 8);
            hour = dateText.substring(9, 11);
            minute = dateText.substring(12, 14);
            second = dateText.substring(15, 17);

            if (Integer.parseInt(month) > 12) {
                Log.v("MESSAGE", month + " is not a valid month");
                dateError = true;
            }
            if (Integer.parseInt(day) > 31) {
                Log.v("MESSAGE", day + " is not a valid day");
                dateError = true;
            }
            Calendar c = Calendar.getInstance();
            if (Integer.parseInt(year) > c.get(Calendar.YEAR) || Integer.parseInt(year) < 2011) {
                Log.v("MESSAGE", year + " is above current year or less than 2011");
                dateError = true;
            }
            if (Integer.parseInt(hour) > 23) {
                Log.v("MESSAGE", hour + " is not a valid hour");
                dateError = true;
            }
            if (Integer.parseInt(minute) > 59) {
                dateError = true;
                Log.v("MESSAGE", minute + " is not a valid minute");
            }
            if (Integer.parseInt(second) > 59) {
                dateError = true;
                Log.v("MESSAGE", second + " is not a valid second");
            }
            if (!dateError) {
                date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + ".0";
            }

        }
        return date;
    }

    public void setUpc(String upc) {
        this.upc.setText(upc);
    }

}
