package com.v_tracker.ui.userinfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v_tracker.R;

public class UserinfoFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Message", "I'm in USER TRACKER INFO");
        View root = inflater.inflate(R.layout.fragment_userinfo, container, false);
        final TextView textView = root.findViewById(R.id.text_userinfo);
        textView.setText("User Tracker Info");
        return root;
    }
}
