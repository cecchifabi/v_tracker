package com.v_tracker.ui.report;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.v_tracker.MainActivity;
import com.v_tracker.R;
import com.v_tracker.ScannedBarcodeActivity;
import com.v_tracker.ui.Database.Database;

public class ReportFragment extends Fragment {
    Database db = new Database();

    Button changeStatus;
    public static String code_message;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(MainActivity.V_TRACKER_INFO, "I'm in REPORT");
        View root = inflater.inflate(R.layout.fragment_report, container, false);

       // db.updateState(true);
        //final TextView textView = root.findViewById(R.id.title);
        //textView.setText("Report");
        changeStatus = root.findViewById(R.id.buttonChangeStatus);
        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScannedBarcodeActivity.class));
            }
        });
        return root;
    }
}
