package com.example.comsense; // Replace with your package name

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<String> phrasesList = new ArrayList<>();

    // ...

    // ...
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private Button startListeningButton;
    private ImageView menuButton;
    private TextView recognizedTextView;
    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private static final int NORMAL_BUTTON_COLOR = Color.parseColor("#6200EA"); // Change to the normal color
    private static final int LISTENING_BUTTON_COLOR = Color.parseColor("#006400");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate() called");

        startListeningButton = findViewById(R.id.startListeningButton);
        recognizedTextView = findViewById(R.id.textView);
        menuButton = findViewById(R.id.menuButtonLayout);

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        // ...
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuButton.setOnClickListener(v -> {
            // Toggle the menu when the menu button is clicked
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Check and request permission to record audio
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }

        // Initialize SpeechRecognizer
        initializeSpeechRecognizer();

        // Button listener
        startListeningButton.setOnClickListener(v -> {
            if (!isListening) {
                startListening();
            } else {
                stopListening();
            }
        });

        //log this
    }



    public void onCancelClicked(View view) {
        // Close the dialog
        ((Dialog) view.getTag()).dismiss();
        //toast logout successful
        Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
    }

    private void initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {}

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> voiceResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (voiceResults != null && !voiceResults.isEmpty()) {
                    String recognizedText = voiceResults.get(0);
                    recognizedTextView.setText(recognizedText);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
    }

    private void startListening() {
        isListening = true;
        startListeningButton.setBackgroundColor(LISTENING_BUTTON_COLOR);
        startListeningButton.setText("Stop Listening"); // Change button text

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.startListening(intent);
    }

    private void stopListening() {
        isListening = false;
        startListeningButton.setBackgroundColor(NORMAL_BUTTON_COLOR);
        startListeningButton.setText("Start Listening"); // Change button text

        speechRecognizer.stopListening();
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can initialize SpeechRecognizer here
                initializeSpeechRecognizer();
            } else {
                // Permission denied, handle accordingly
                // You can show a message to the user or disable the speech-related functionality.
                Toast.makeText(this, "Permission denied for recording audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle navigation drawer item clicks@Override
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item1) {
            // Handle the "Profile" item click
            // Implement code to open the user's profile page
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);
            return true;
        } else if (id == R.id.menu_item2) {
            // Handle the "Settings" item click
            // Implement code to open the settings page
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.menu_item3) {
            // Handle the "Saved Phrases" item click
            // Start the SavedPhrasesActivity
            Intent savedPhrasesIntent = new Intent(this, SavedPhrasesActivity.class);
            startActivity(savedPhrasesIntent);
            return true;
        } else if (id == R.id.menu_item4) {
            // Handle the "Feedback" item click
            // Implement code to open the feedback page
            Intent feedbackIntent = new Intent(this, FeedbackActivity.class);
            startActivity(feedbackIntent);
            return true;
        } else if (id == R.id.menu_item5) {
            // Handle the "Logout" item click
            // Implement code to log the user out
            // For example, return to the login screen
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Close the current activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item1) {
            // Handle the "Profile" item click
            // Implement code to open the user's profile page
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.menu_item3) {
            // Handle the "Saved Phrases" item click
            // Start the SavedPhrasesActivity
            Intent savedPhrasesIntent = new Intent(this, SavedPhrasesActivity.class);
            startActivity(savedPhrasesIntent);
        } else if (id == R.id.menu_item4) {
            // Handle the "Feedback" item click
            // Implement code to open the feedback page
            Intent feedbackIntent = new Intent(this, FeedbackActivity.class);
            startActivity(feedbackIntent);
        } else if (id == R.id.menu_item5) {
            // Handle the "Logout" item click
            // Implement code to log the user out
            // For example, return to the login screen
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Close the current activity
        }

        // Close the navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
