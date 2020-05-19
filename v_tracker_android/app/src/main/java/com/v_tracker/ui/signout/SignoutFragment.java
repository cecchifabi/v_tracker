package com.v_tracker.ui.signout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v_tracker.R;

public class SignoutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Message", "I'm in SIGN OUT");
        View root = inflater.inflate(R.layout.fragment_signout, container, false);
        final TextView textView = root.findViewById(R.id.text_signout);
        textView.setText("Sign Out");
        return root;
    }
}
