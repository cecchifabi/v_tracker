package com.v_tracker.ui.report;

import android.content.Intent;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(MainActivity.V_TRACKER_INFO, "I'm in REPORT");
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        txtStatus = root.findViewById(R.id.current_status);
        imgVirus = root.findViewById(R.id.imageViewVirus);
        changeStatus = root.findViewById(R.id.buttonChangeStatus);

        db = new Database();
        boolean state = db.getState();



        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScannedBarcodeActivity.class));
            }
        });
        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
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
                    txtStatus.setText("Your current status: INFECTED");
                    imgVirus.setBackgroundColor(0xFFD14D4D);
                }
                if(!user.getIsInfected()){
                    txtStatus.setText("Your current status: HEALTHY");
                    imgVirus.setBackgroundColor(0xC54DD14D);
                }
                isInfected = user.getIsInfected();
            }
        });
        return isInfected;
    }
}
