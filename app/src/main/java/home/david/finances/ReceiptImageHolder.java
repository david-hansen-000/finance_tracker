package home.david.finances;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptImageHolder extends Fragment {
    private DisplayMetrics metrics;
    private Bitmap bitmap;
    private ImageView imageView;
    private PictureFile pictureFile;
    private ReceiptImageFilesList receiptImageFilesList;
    private Button changeBtn;
    private BackgroundProcess backgroundProcess;
    private ViewPager pager;

    public ReceiptImageHolder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receipt_image_holder, container, false);
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageView = view.findViewById(R.id.receipt_image_view);
        if (bitmap != null) {
            imageView.setImageBitmap(scaledBitmap(bitmap));
        } else {
            Bitmap blank = BitmapFactory.decodeResource(getResources(), R.drawable.blank_receipt);
            imageView.setImageBitmap(scaledBitmap(blank));
            //backgroundProcess=new BackgroundProcess(scaledBitmap(blank), ContextCompat.getColor(getContext(),R.color.translucentGreen), imageView);
        }
        changeBtn=view.findViewById(R.id.change_btn);
        changeBtn.setOnClickListener(this::changeBtn);
        view.findViewById(R.id.refresh_btn).setOnClickListener((v) -> receiptImageFilesList.refreshList());
        return view;
    }


    private void changeBtn(View view) {
        if (pictureFile!=null) {
            File changed_file=new File(pictureFile.getFile().getParent(),"edited_"+pictureFile.toString());
            if (pictureFile.getFile().renameTo(changed_file)) {
                Log.d("MESSAGE", "file renamed");
                receiptImageFilesList.refreshList();
                view.setBackgroundColor(Color.GREEN);
                pager.setCurrentItem(1);
            }
        }
    }

    private Bitmap scaledBitmap(Bitmap bitmap) {
        int bm_width=bitmap.getWidth();
        int bm_height=bitmap.getHeight();
        int scaled_height= metrics.widthPixels*bm_height/bm_width;
        return Bitmap.createScaledBitmap(bitmap, metrics.widthPixels, scaled_height, false);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setBitmapAndRefresh(Bitmap bitmap) {
        if (imageView!=null) {
            //backgroundProcess.changeBitmap(scaledBitmap(bitmap));
            imageView.setImageBitmap(scaledBitmap(bitmap));
        }
    }

    public void setPictureFile(PictureFile pictureFile) {
        this.pictureFile = pictureFile;
        try {
            if (pictureFile.getFile().length()>0) {
                Log.i("RECEIPTIMAGEHOLDER", "pictureFile:" + pictureFile.getFile().getAbsolutePath());
                Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(Uri.fromFile(pictureFile.getFile())));
                setBitmapAndRefresh(bitmap);
                changeBtn.setBackgroundColor(Color.GRAY);
            } else {
                Log.i("RECEIPTIMAGEHOLDER", pictureFile.getFile().getAbsolutePath()+" is empty");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setReceiptImageFilesList(ReceiptImageFilesList receiptImageFilesList) {
        this.receiptImageFilesList = receiptImageFilesList;
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }
}
