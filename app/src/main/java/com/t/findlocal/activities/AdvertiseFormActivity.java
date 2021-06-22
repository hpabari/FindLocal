package com.t.findlocal.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.t.findlocal.R;

import androidx.appcompat.app.AppCompatActivity;

public class AdvertiseFormActivity extends AppCompatActivity {

    private Button submitButton;
    private EditText nameEditTextView;
    private EditText businessNameEditTextView;
    private EditText informationEditTextView;
    private EditText questionsEditTextView;
    private EditText emailEditTextView;
    private EditText numberEditTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise_form);

        submitButton = findViewById(R.id.submit_button);
        nameEditTextView = findViewById(R.id.name_edit_text);
        businessNameEditTextView = findViewById(R.id.business_name_edit_text);
        informationEditTextView = findViewById(R.id.information_edit_text);
        questionsEditTextView = findViewById(R.id.questions_edit_text);
        emailEditTextView = findViewById(R.id.email_edit_text);
        numberEditTextView = findViewById(R.id.number_edit_text);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = "Name: " + nameEditTextView.getText().toString() + "\n"
                        + "Business Name: " + businessNameEditTextView.getText().toString() + "\n"
                        + "Business Information: " + informationEditTextView.getText().toString() + "\n"
                        + "Questions: " + questionsEditTextView.getText().toString() + "\n"
                        + "Email: " + emailEditTextView.getText().toString() + "\n"
                        + "Number: " + numberEditTextView.getText().toString() + "\n";

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hp282@student.le.ac.uk"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Find Local Business Advertise Query");
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
                startActivity(emailIntent);
            }
        });
    }
}
