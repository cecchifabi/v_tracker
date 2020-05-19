package com.v_tracker.ui.covid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.v_tracker.R;

public class CovidFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Message", "I'm in COVID");
        View root = inflater.inflate(R.layout.fragment_covid, container, false);
        final TextView textView = root.findViewById(R.id.text_covid);
        textView.setText("Covid");
        return root;
    }
}
