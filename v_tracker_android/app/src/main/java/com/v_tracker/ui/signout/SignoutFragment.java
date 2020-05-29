package com.v_tracker.ui.signout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.v_tracker.LoginActivity;
import com.v_tracker.MainActivity;
import com.v_tracker.R;

public class SignoutFragment extends Fragment {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(MainActivity.V_TRACKER_INFO, "I'm in SIGN OUT");
        sharedPref = getActivity().getSharedPreferences("PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        LoginActivity.isLoggedIn = false;
        editor.putBoolean("IS_LOGGED", LoginActivity.isLoggedIn);
        editor.commit();
        View root = inflater.inflate(R.layout.fragment_signout, container, false);
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        return root;
    }
}
