package home.david.finances;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class SaveReceiptIntent extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savereceiptintent);
        TextView text=findViewById(R.id.file_saved_text);
        text.setOnClickListener((view)->finish());
        Intent intent=getIntent();
        Uri image_uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (image_uri != null) {
            try {
                Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(image_uri));
                Date date=new Date();
                String formatted="r"+new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(date)+".png";
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), formatted);
                FileOutputStream fos = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                text.append("file:"+file.getAbsolutePath()+" saved");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
