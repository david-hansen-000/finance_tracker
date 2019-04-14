package home.david.finances;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptImageFilesList extends Fragment {
    private ArrayAdapter<PictureFile> picFiles;
    private ReceiptImageHolder receiptImageHolder;
    private ViewPager pager;
    private ParentEditFragment parentEditFragment;

    public ReceiptImageFilesList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picFiles=new ArrayAdapter<>(getContext(),R.layout.listtext);
        refreshList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.receipt_image_files_list, container, false);
        ListView list=view.findViewById(R.id.receipt_image_files_listview);
        list.setAdapter(picFiles);
        list.setOnItemClickListener(this::itemSelected);
        return view;
    }

    private void itemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        PictureFile file= (PictureFile) adapterView.getItemAtPosition(i);
        receiptImageHolder.setPictureFile(file);
        pager.setCurrentItem(0);
        parentEditFragment.getEditingFieldsFragment().newReceipt();
    }

    public void setReceiptImageHolder(ReceiptImageHolder receiptImageHolder) {
        this.receiptImageHolder = receiptImageHolder;
    }

    public void refreshList() {
        picFiles.clear();
        File pic_dir=getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (pic_dir.exists() && pic_dir.isDirectory()) {
            Log.v("MESSAGE", "size:" + pic_dir.listFiles().length);
            for (File pic : pic_dir.listFiles()) {
                if (pic.length()>0 && !pic.getName().startsWith("edited_")) {
                    picFiles.add(new PictureFile(pic));
                }
            }
        }
    }


    public void setViewPager(ViewPager pager) {
        this.pager=pager;
    }

    public void setParentEditFragment(ParentEditFragment parentEditFragment) {
        this.parentEditFragment = parentEditFragment;
    }
}
