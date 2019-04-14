package home.david.finances;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

public class PopupEditor extends PopupWindow {
    private EditText editText;
    private Button button;
    private Button delete;
    private PopupEditor editor;

    public PopupEditor(Context context) {
        super(context);
        editor=this;
        View view=LayoutInflater.from(context).inflate(R.layout.popup_edit, null);
        setContentView(view);
        editText=view.findViewById(R.id.popup_editText);
        button=view.findViewById(R.id.popup_button);
        delete=view.findViewById(R.id.popup_delete_btn);
    }


    public void setText(String text) {
        editText.setText(text);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setOkAction(View.OnClickListener action) {
        button.setOnClickListener(action);
    }

    public void setDeleteAction(View.OnClickListener action) {
        delete.setOnClickListener(action);
    }
}
