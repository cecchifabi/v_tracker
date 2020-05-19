package com.v_tracker.ui.appinfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v_tracker.R;

public class AppinfoFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Message", "I'm in APP INFO");
        View root = inflater.inflate(R.layout.fragment_appinfo, container, false);
        final TextView textView = root.findViewById(R.id.text_appinfo);
        textView.setText("Info on this App");
        return root;
    }
}
