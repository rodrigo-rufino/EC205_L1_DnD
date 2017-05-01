package com.ec205.dnd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MagicListFragment extends Fragment {
    private LayoutInflater inflater;
    private DatabaseReference mDatabaseRef;

    MagicCustomAdapter magicCustomAdapter;
    ArrayList<Magic> magics = new ArrayList<>();
    ListView magicListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_magic, container, false);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Button addMagic = (Button) view.findViewById(R.id.button_add_magic);
        magicListView = (ListView) view.findViewById(R.id.list_view_magic);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                magicCustomAdapter = new MagicCustomAdapter(getContext(), magics);
                magicListView.setAdapter(magicCustomAdapter);
                magicCustomAdapter.notifyDataSetChanged();
            }
        });
        
        return view;
    }
}
