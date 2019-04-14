package home.david.finances;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class STextWatcher implements TextWatcher {
    private final EditText editText;
    private ArrayList<String> watchingList;

    public STextWatcher(EditText editText) {
        this.editText=editText;
        watchingList=new ArrayList<>();
    }

    public void addToWatching(String string) {
        watchingList.add(string);
    }

    public void addAllToWatching(String...strings) {
        watchingList.addAll(Arrays.asList(strings));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String two=null;
        for (String watch:watchingList) {
            two=watch.substring(0,2);
            if (two.equals(s.toString())) {
                editText.setText(watch);
                //editText.setSelection(2);
                editText.requestFocus();
                editText.setSelection(2,watch.length());
            }
        }

    }

    public void log(String message) {
        Log.i("STextWatcher", message);
    }
}
