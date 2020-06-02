package com.v_tracker.ui.userinfo;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.v_tracker.MainActivity;
import com.v_tracker.R;
import com.v_tracker.ui.Database.Database;
import com.v_tracker.ui.models.ListAdapter;
import com.v_tracker.ui.models.Position;
import com.v_tracker.ui.models.User;
import com.v_tracker.ui.models.UserList;


import java.util.ArrayList;
import java.util.List;

import eu.bitm.NominatimReverseGeocoding.Address;
import eu.bitm.NominatimReverseGeocoding.NominatimReverseGeocodingJAPI;

public class UserinfoFragment extends Fragment {
    List<Position> listOfPositions;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RecyclerView mRecyclerView;
    ListAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(MainActivity.V_TRACKER_INFO, "I'm in USER TRACKER INFO");
        View root = inflater.inflate(R.layout.fragment_userinfo, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerViewList);
        mAdapter = new ListAdapter(getContext(), getMyList());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    private ArrayList<UserList> getMyList() {
        final ArrayList<UserList> list = new ArrayList<>();

        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                User user = documentSnapshot.toObject(User.class);
                listOfPositions = user.getListOfPositions();

                if (listOfPositions != null) {
                    for (int i = listOfPositions.size() - 1; i >= 0; i--) {
                        UserList m = new UserList();
                        m.setLat(listOfPositions.get(i).getLatitude());
                        m.setLon(listOfPositions.get(i).getLongitude());
                        m.setTimestamp(listOfPositions.get(i).getTimestamp());
                        if(i > listOfPositions.size() - 10 )
                            m.setStreet(getStreetName(listOfPositions.get(i).getLatitude(), listOfPositions.get(i).getLongitude()));

                        else m.setStreet("");
                        list.add(m);
                    }
                }


                mAdapter.setModels(list);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        return list;
    }

    public String getStreetName(double lat, double lon){
        NominatimReverseGeocodingJAPI nominatim1 = new NominatimReverseGeocodingJAPI(); //create instance with default zoom level (18)
        Address a = nominatim1.getAdress(lat, lon);
        return a.getRoad();

    }
}
