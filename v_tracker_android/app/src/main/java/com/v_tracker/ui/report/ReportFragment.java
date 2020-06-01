package com.v_tracker.ui.report;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.v_tracker.MainActivity;
import com.v_tracker.R;
import com.v_tracker.ScannedBarcodeActivity;
import com.v_tracker.ui.Database.Database;
import com.v_tracker.ui.models.Position;
import com.v_tracker.ui.models.User;

import java.util.List;

public class ReportFragment extends Fragment {
    Database db ;
    TextView txtStatus;
    Button changeStatus;
    ImageView imgVirus;
    boolean isInfected;
    public static String code_message;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(MainActivity.V_TRACKER_INFO, "I'm in REPORT");
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        txtStatus = root.findViewById(R.id.current_status);
        imgVirus = root.findViewById(R.id.imageViewVirus);
        changeStatus = root.findViewById(R.id.buttonChangeStatus);

        db = new Database();
        boolean state = db.getState();

        sharedPref = getActivity().getSharedPreferences("PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("IS_SCANNING_QR", true);
                editor.commit();
                startActivity(new Intent(getActivity(), ScannedBarcodeActivity.class));
            }
        });
        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        editor.putBoolean("IS_SCANNING_QR", false);
        editor.commit();
        getState();
    }
    public boolean getState() {
        List<Position> list;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if(user.getIsInfected()) {
                    txtStatus.setText(getResources().getString(R.string.infected));
                    imgVirus.setBackgroundColor(0xFFD14D4D);
                }
                if(!user.getIsInfected()){
                    txtStatus.setText(getResources().getString(R.string.healthy));
                    imgVirus.setBackgroundColor(0xC54DD14D);
                }
                isInfected = user.getIsInfected();
            }
        });
        return isInfected;
    }
}
