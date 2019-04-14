package home.david.finances;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParentEditFragment extends Fragment implements FragmentDataExchange {

    private EditingFieldsFragment editingFieldsFragment;
    private FewFragmentsAdapter fragmentAdapter;
    private Database database;


    private static final int NUM_FRAGS=2;

    public ParentEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentAdapter= new FewFragmentsAdapter(getActivity().getSupportFragmentManager());
        editingFieldsFragment=new EditingFieldsFragment();
        SQLResultsListFragment resultsListFragment = new SQLResultsListFragment();
        fragmentAdapter.add(editingFieldsFragment);
        fragmentAdapter.add(resultsListFragment);
        ArrayAdapter<TableRow> adapter=new ArrayAdapter<>(getContext(), R.layout.listtext);
        editingFieldsFragment.setAdapter(adapter);
        editingFieldsFragment.setDatabase(database);
        resultsListFragment.setAdapter(adapter);
        resultsListFragment.setDatabase(database);
        resultsListFragment.setEditingFragment(editingFieldsFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_edit, container, false);
        ViewPager pager = view.findViewById(R.id.edit_view_pager);
        pager.setAdapter(fragmentAdapter);
        return view;
    }

    @Override
    public void editingItem(TableRow item) {
        if (editingFieldsFragment!=null) {
            editingFieldsFragment.setEditing_item(item);
        }
    }

    @Override
    public void datetime(String datetime) {
        if (editingFieldsFragment!=null) {
            editingFieldsFragment.setDateTime(datetime);
        }
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public EditingFieldsFragment getEditingFieldsFragment() {
        return editingFieldsFragment;
    }
}
