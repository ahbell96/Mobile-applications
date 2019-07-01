package com.example.ahbel.app_15565106;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Storage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
    }

    public void saveUserData(View view) {

        // linking the edit texts to the ones created on the XML.
        EditText name = (EditText)findViewById(R.id.nameText);
        EditText email = (EditText)findViewById(R.id.emailText);

        try {
            SharedPreferences userInfo = getSharedPreferences("userData", Context.MODE_PRIVATE);
            // creating a new shared preferences to user data.
            SharedPreferences.Editor editor = userInfo.edit();
            // when the shared preferences is created, it will then be edited (if it has not already been created).

            // whatever is in the editor, convert into a string, which is as follows in the braces.
            editor.putString("user", name.getText().toString());
            editor.putString("email", email.getText().toString());
            editor.commit();
            // ^ saving to the editor

            // when the data has been saved, a toast message will be shown.
            Context context = getApplicationContext();
            CharSequence text = "User data saved!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        catch(Exception ex) {
            // insert your own error message here
        }
    }

    public void showUserData(View view) {

        // create references to the two textView widgets that will display the data
        TextView name = (TextView)findViewById(R.id.showNameText);
        TextView email = (TextView)findViewById(R.id.showEmailText);

        // get the saved values of username3 and email from the Shared Preferenced file 'userData' file
        SharedPreferences userInfo = getSharedPreferences("userData", Context.MODE_PRIVATE);

        // set the name and email textView widget to the values from Shared Preferenced file 'userData'
        name.setText(userInfo.getString("user", ""));
        email.setText(userInfo.getString("email", ""));
    }
}
