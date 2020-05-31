package com.v_tracker.ui.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.v_tracker.MainActivity;
import com.v_tracker.R;

public class ReportFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(MainActivity.V_TRACKER_INFO, "I'm in REPORT");
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        final TextView textView = root.findViewById(R.id.text_report);
        textView.setText("Report");
        return root;
    }
}
