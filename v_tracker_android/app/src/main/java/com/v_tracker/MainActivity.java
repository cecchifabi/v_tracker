package com.v_tracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.v_tracker.ui.Database.Database;
import com.v_tracker.ui.models.Position;
import com.v_tracker.ui.models.User;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String V_TRACKER_INFO = "V_TRACKER_INFO";
    SharedPreferences sharedPref;

    FirebaseAuth mFirebaseAuth;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences("PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        else {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_map, R.id.nav_report, R.id.nav_covid, R.id.nav_userinfo, R.id.nav_signout, R.id.nav_appinfo)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }


        Intent intent = getIntent();
        //GET THIS WHEN CHANGING USER STATUS -- COULDNT HAVE A DIALOG MESSAGE TO CONFIRM IN THE QR CODE THREAD DONT KNOW WHY
        //SO WHEN RETURNING TO THIS ACTIVITY IT PUTS AN EXTRA INT
        // 0 IS THE DEFAULT VALUE NOTHING HAPPENS
        // 1 IS THE HEALTHY VALUE -- ASK USER TO CONFIRM CHANGE TO HEALTHY
        // 2 IS THE INFECTED VALUE
        int changeStatus = intent.getIntExtra("changeStatus", 0);
        if(changeStatus>0)
            changeUserStatus(changeStatus);
    }

    private void changeUserStatus(int changeStatus) {
        //HEALTHY
        if(changeStatus==1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("You're setting your current status to HEALTHY");
            builder.setTitle("Alert!");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    updateState(false);
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        //INFECTED
        if(changeStatus==2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("You're setting your current status to INFECTED");
            builder.setTitle("Alert!");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    updateState(true);
                    dialog.cancel();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(V_TRACKER_INFO, "MainActivity stopped: starting foreground service");
        startService();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(V_TRACKER_INFO, "MainActivity resumed: stopping foreground service");
        stopService();
    }

    public void startService() {
        if(sharedPref.getBoolean("IS_LOGGED", false)) {
            Intent serviceIntent = new Intent(this, LocationForegroundService.class);
            serviceIntent.putExtra("inputExtra", getResources().getString(R.string.notification_content));
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, LocationForegroundService.class);
        stopService(serviceIntent);
    }
    public void updateState(boolean isInfected) {
        List<Position> list;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                user.setInfected(isInfected);
                db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user);
            }
        });
    }
}
