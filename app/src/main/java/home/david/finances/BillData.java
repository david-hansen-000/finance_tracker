package home.david.finances;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BillData {
    private String[] allowedKeys = new String[]{"rent", "phone", "directv", "gas", "power", "insurance"};
    private HashMap<String, BillInner> inner_data;
    private Date today;

    public BillData() {
        today = new Date();
        inner_data = new HashMap<>();
        for (String key : allowedKeys) {
            inner_data.put(key, new BillInner());
        }
    }

    public BillData(ArrayList<TableRow> defaults, ArrayList<TableRow> currents) {
        this();

        String bill=null;
        String amount=null;
        String duedate=null;

        for (TableRow row: defaults) {
            bill=row.getRowItemAsString("bill");
            amount=row.getRowItemAsString("amount");
            duedate=row.getRowItemAsString("duedate");
            inner_data.get(bill).setDefault_amount(amount);
            inner_data.get(bill).setDefault_duedate(duedate);
        }

        for (TableRow row : currents) {
            bill=row.getRowItemAsString("bill");
            amount=row.getRowItemAsString("amount");
            duedate=row.getRowItemAsString("duedate");
            inner_data.get(bill).setCurrent_amount(amount);
            inner_data.get(bill).setCurrent_duedate(duedate);
        }

        //log(toString());
        decideCurrentOrDefault();

    }

    private void decideCurrentOrDefault() {
        Date billdate = null;
        for (String key : allowedKeys) {
            billdate = Bill.convertDate(getCurrentDuedate(key));
            if (billdate.compareTo(today) < 0) {
                inner_data.get(key).setCurrent_duedate(getConvertedDefaultdate(key, today));
                inner_data.get(key).setCurrent_amount(getDefaultAmount(key));
            }
        }
    }

    public ArrayList<String> getKeysAsArray() {
        return new ArrayList<>(Arrays.asList(allowedKeys));
    }

    public String[] getKeys() {
        return allowedKeys;
    }

    public String getCurrentAmount(String key) {
        return inner_data.get(key).current_amount;
    }

    public String getCurrentDuedate(String key) {
        return inner_data.get(key).current_duedate;
    }

    public String getDefaultAmount(String key) {
        return inner_data.get(key).default_amount;
    }

    public String getDefaultDuedate(String key) {
        return inner_data.get(key).default_duedate;
    }

    public String getConvertedDefaultdate(String key, Date date) {
        String defaultDay = getDefaultDuedate(key);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(defaultDay));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date.compareTo(c.getTime()) < 0) {
            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        }
        return sdf.format(c.getTime());
    }

    public ArrayList<Bill> getBillArrayList() {
        ArrayList<Bill> bills = new ArrayList<>();
        Bill bill = null;
        log("inner_data has:" + inner_data.size());
        if (inner_data.size() > 0) {
            for (String key : allowedKeys) {
                bill = new Bill();
                bill.setBill_name(key);
                bill.setBill_amount(inner_data.get(key).getCurrent_amount());
                bill.setBill_duedate(inner_data.get(key).getCurrent_duedate());
                bills.add(bill);
            }
        } else {
            log("inner_data has no data");
        }
        log("bills should have:" + bills.size());
        return bills;
    }

    public float getTotal() {
        float result = 0f;
        for (String key : allowedKeys) {
            result += Float.parseFloat(inner_data.get(key).current_amount);
        }
        return result;
    }


    private int getKeyIndex(String key) {
        int result = -1;
        for (int i = 0; i < allowedKeys.length; i++) {
            if (allowedKeys[i].equals(key)) {
                result = i;
            }
        }
        return result;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String key : allowedKeys) {
            result.append(key).append(":").append("\n")
                    .append("current_amount:")
                    .append(inner_data.get(key).getCurrent_amount())
                    .append("\n")
                    .append("current_duedate:")
                    .append(inner_data.get(key).getCurrent_duedate())
                    .append("\n")
                    .append("default_amount:")
                    .append(inner_data.get(key).getDefault_amount())
                    .append("\n")
                    .append("default_duedate:")
                    .append(inner_data.get(key).getDefault_duedate())
                    .append("\n");
        }
        return result.toString();
    }

    private class BillInner {
        private String current_amount;
        private String default_amount;
        private String current_duedate;
        private String default_duedate;

        public BillInner() {
            current_duedate = null;
            current_amount = null;
            default_duedate = null;
            default_amount = null;
        }

        public String getCurrent_amount() {
            return current_amount;
        }

        public void setCurrent_amount(String current_amount) {
            this.current_amount = current_amount;
        }

        public String getDefault_amount() {
            return default_amount;
        }

        public void setDefault_amount(String default_amount) {
            this.default_amount = default_amount;
        }

        public String getCurrent_duedate() {
            return current_duedate;
        }

        public void setCurrent_duedate(String current_duedate) {
            this.current_duedate = current_duedate;
        }

        public String getDefault_duedate() {
            return default_duedate;
        }

        public void setDefault_duedate(String default_duedate) {
            this.default_duedate = default_duedate;
        }

        @Override
        public String toString() {
            return "BillInner{" +
                    "current_amount='" + current_amount + '\'' +
                    ", default_amount='" + default_amount + '\'' +
                    ", current_duedate='" + current_duedate + '\'' +
                    ", default_duedate='" + default_duedate + '\'' +
                    '}';
        }
    }

    private void log(String message) {
        Log.i("BillData", message);
    }
}
