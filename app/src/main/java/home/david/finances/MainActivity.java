package home.david.finances;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new Database(this);

        ParentImageFragment parentImageFragment=new ParentImageFragment();
        ParentEditFragment parentEditFragment=new ParentEditFragment();
        parentImageFragment.setParentEditFragment(parentEditFragment);
        parentEditFragment.setDatabase(database);

        Intent data = getIntent();
        Uri opened = data.getData();
        if (opened != null) {
            try {
                parentImageFragment.setBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(opened)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
//         else {
//            parentImageFragment.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.blank_receipt));
//        }

        Log.d("MESSAGE","black:"+ ContextCompat.getColor(this,R.color.black));
        Log.d("MESSAGE","t black:"+ ContextCompat.getColor(this,R.color.translucentBlack));

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.image_fragment_holder, parentImageFragment).commit();
        manager.beginTransaction().replace(R.id.edit_fragment_holder, parentEditFragment).commit();
    }

    @Override
    public void finish() {
        super.finish();
        database.close();
    }
}
