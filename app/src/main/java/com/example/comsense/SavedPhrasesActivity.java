package com.example.comsense;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class SavedPhrasesActivity extends AppCompatActivity {

    private ListView phrasesListView;
    private Button addButton2;
    private TextToSpeech textToSpeech;
    private ArrayList<String> phrasesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_phrases);

        phrasesListView = findViewById(R.id.phrasesListView);
        addButton2 = findViewById(R.id.addButton2);

        // Initialize the TextToSpeech engine
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Set the language (you can set it to the user's preference)
                textToSpeech.setLanguage(Locale.getDefault());
            }
        });

        // Initialize the list of phrases (you can load from a database or file)
        phrasesList = new ArrayList<>();
        phrasesList.add("There's a fire! Please evacuate the building immediately.");
        phrasesList.add("I need medical assistance. Please call 911.");
        phrasesList.add("We need help. Please contact the police right away.");
        phrasesList.add("I'm feeling unwell and need medical attention.");
        phrasesList.add("There's been an accident. Call for an ambulance immediately.");
        phrasesList.add("I'm in danger. Please get help as soon as possible.");
        phrasesList.add("We need to find a safe place to take shelter.");
        phrasesList.add("I'm lost and need assistance to find my way.");
        phrasesList.add("There's a gas leak. Please contact emergency services.");
        phrasesList.add("I see someone suspicious. Please notify the authorities.");


        // Add more phrases to the list as needed

        // Set up the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phrasesList);
        phrasesListView.setAdapter(adapter);

        // Handle the "Add Sentence" button click
        addButton2.setOnClickListener(v -> {
            // Implement code to add a new sentence to the
            openAddSentenceDialog();

            // You can open a dialog or a new activity for this purpose
        });

        // Set an item click listener for the ListView
        phrasesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPhrase = phrasesList.get(position);
            // Use TextToSpeech to read the selected phrase aloud
            textToSpeech.speak(selectedPhrase, TextToSpeech.QUEUE_FLUSH, null, null);
        });
    }

    public void openAddSentenceDialog() {
        Log.d(TAG, "openAddSentenceDialog() called");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_sentence, null);
        builder.setView(dialogView);

        EditText editTextNewSentence = dialogView.findViewById(R.id.editTextNewSentence);
        Button buttonAdd = dialogView.findViewById(R.id.addSentenceButton);

        AlertDialog dialog = builder.create();

        buttonAdd.setOnClickListener(v -> {
            String newSentence = editTextNewSentence.getText().toString();
            if (!newSentence.isEmpty()) {
                // Add the new sentence to your list or data source
                phrasesList.add(newSentence);

                // Update your UI or adapter to reflect the changes

                // Dismiss the dialog
                dialog.dismiss();
            } else {
                // Handle the case when the input is empty or invalid
                Toast.makeText(this, "Please enter a valid sentence", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public void onCancelClicked(View view) {
        // Close the dialog
        ((Dialog) view.getTag()).dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when the activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
