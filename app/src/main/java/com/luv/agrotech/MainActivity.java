package com.luv.agrotech;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    TextView accountName, accountEmail;
    ImageView accountImageView;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startVoiceRecognitionActivity();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        View header = mNavigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cart, R.id.nav_history)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, navController);
        mAuth = FirebaseAuth.getInstance();
        accountName = header.findViewById(R.id.accountName);
        accountEmail = header.findViewById(R.id.accountEmail);
        accountImageView = header.findViewById(R.id.accountImageView);

        if(mAuth.getCurrentUser() == null){
            accountEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
//        PackageManager pm = getPackageManager();
//        List activities = pm.queryIntentActivities(new Intent(
//                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
//        if (activities.size() != 0) {
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startVoiceRecognitionActivity();
//                }
//            });
//        } else {
//            fab.setEnabled(false);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.contains("account")){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else if (matches.contains("rake")){
                startURL("https://www.amazon.in/Rakes/b?ie=UTF8&node=3639037031");
            } else if (matches.contains("shovel")){
                startURL("https://www.amazon.in/Shovels/b?ie=UTF8&node=3639039031");
            } else if (matches.contains("pickaxe")){
                startURL("https://www.amazon.in/Axes/b?ie=UTF8&node=3639008031");
            } else if (matches.contains("machete")){
                startURL("https://www.ubuy.co.in/search/?q=machetes");
            } else if (matches.contains("wheelbarrow")){
                startURL("https://www.amazon.in/Wheel-Barrow/s?k=Wheel+Barrow");
            } else if (matches.contains("mattock")){
                startURL("https://dir.indiamart.com/impcat/pick-mattock.html");
            } else if (matches.contains("pitchfork")){
                startURL("https://www.flipkart.com/home-improvement/lawn-and-gardening/gardening-tools/pitchforks/pr?sid=h1m%2Cum7%2Cyse%2Cutu");
            } else if (matches.contains("pruners")){
                startURL("https://www.flipkart.com/home-improvement/lawn-and-gardening/gardening-tools/pruners/pr?sid=h1m%2Cum7%2Cyse%2Civz");
            } else if (matches.contains("chainsaw")){
                startURL("https://www.amazon.in/Chain-Saws/b?ie=UTF8&node=3639139031");
            } else if (matches.contains("sprinkler")){
                startURL("https://www.amazon.in/Sprinklers/b?ie=UTF8&node=3639108031");
            } else if (matches.contains("sprayer")){
                startURL("https://www.flipkart.com/home-improvement/lawn-and-gardening/watering-equipments/garden-sprayers/pr?sid=h1m%2Cum7%2Ci06%2Cchn");
            } else if (matches.contains("tractor")){
                startURL("https://www.tractorjunction.com/");
            } else if (matches.contains("cultivator")){
                startURL("https://dir.indiamart.com/impcat/cultivator.html");
            } else if (matches.contains("harvester")){
                startURL("https://dir.indiamart.com/impcat/combine-harvester.html");
            } else if (matches.contains("harrow")){
                startURL("https://dir.indiamart.com/impcat/hydraulic-disc-harrow.html");
            } else if (matches.contains("fertiliser")){
                startURL("https://www.amazon.in/Fertilizer-Soil/b?ie=UTF8&node=3638818031");
            } else if (matches.contains("roller")){
                startURL("https://dir.indiamart.com/impcat/used-road-roller.html");
            } else if (matches.contains("baler")){
                startURL("https://www.tractorjunction.com/used-farm-implements-for-sell/baler/");
            } else if (matches.contains("plough")){
                startURL("https://dir.indiamart.com/impcat/agricultural-plough.html");
            }
        }
    }

    public void startURL(String url){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void startVoiceRecognitionActivity(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Which product would you like to buy?");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        accountName.setText(currentUser.getDisplayName());
        accountEmail.setText(currentUser.getEmail());
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
}
