package home.david.finances;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParentImageFragment extends Fragment {

    private FewFragmentsAdapter fragmentsAdapter;
    private ReceiptImageHolder receiptImageHolder;
    private ReceiptImageFilesList receiptImageFilesList;
    private ParentEditFragment parentEditFragment;
    private Bitmap bitmap;

    public ParentImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentsAdapter = new FewFragmentsAdapter(getActivity().getSupportFragmentManager());
        receiptImageHolder = new ReceiptImageHolder();
        receiptImageFilesList = new ReceiptImageFilesList();
        fragmentsAdapter.add(receiptImageHolder);
        fragmentsAdapter.add(receiptImageFilesList);
        receiptImageFilesList.setReceiptImageHolder(receiptImageHolder);
        receiptImageFilesList.setParentEditFragment(parentEditFragment);
        receiptImageHolder.setReceiptImageFilesList(receiptImageFilesList);
        if (bitmap != null) {
            receiptImageHolder.setBitmap(bitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_image, container, false);
        ViewPager pager = view.findViewById(R.id.image_view_pager);
        pager.setAdapter(fragmentsAdapter);
        receiptImageFilesList.setViewPager(pager);
        receiptImageHolder.setPager(pager);
        return view;
    }

    public void setParentEditFragment(ParentEditFragment parentEditFragment) {
        this.parentEditFragment = parentEditFragment;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
