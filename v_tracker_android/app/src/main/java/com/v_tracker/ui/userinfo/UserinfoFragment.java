package com.v_tracker.ui.userinfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v_tracker.MainActivity;
import com.v_tracker.R;

public class UserinfoFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(MainActivity.V_TRACKER_INFO, "I'm in USER TRACKER INFO");
        View root = inflater.inflate(R.layout.fragment_userinfo, container, false);
        final TextView textView = root.findViewById(R.id.text_userinfo);
        textView.setText("User Tracker Info");

        /*
        for(int i =display.listOfPositions.length-1; i>=0 ;i--){
            rdisplay.add(display.listOfPositions[i]);
        }*/

        return root;
    }
}
