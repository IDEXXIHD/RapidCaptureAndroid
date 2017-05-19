package com.idexx.labstation.rapidcaptureapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.idexx.labstation.rapidcaptureapp.model.ClinicDto;
import com.idexx.labstation.rapidcaptureapp.model.CreateClinicDto;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkActions;

public class CreateStudyActivity extends AppCompatActivity
{
    private Button submitButton;
    private EditText studyNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study);
        bindFields();
    }

    private void bindFields()
    {
        submitButton = (Button) findViewById(R.id.createStudySubmitButton);
        studyNameInput = (EditText) findViewById(R.id.createStudyStudyNameEditText);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleSubmit();
            }
        });
    }

    private void handleSubmit()
    {
//        final CreateClinicDto createClinicDto = new CreateClinicDto();
//        createClinicDto.setClinicName(studyNameInput.getText().toString());
//        new AsyncTask<Object, Object, ClinicDto>()
//        {
//            @Override
//            protected ClinicDto doInBackground(Object... params)
//            {
//                return NetworkActions.createClinic(createClinicDto);
//            }
//
//            @Override
//            protected void onPostExecute(ClinicDto clinicDto)
//            {
//                if(clinicDto != null)
//                {
//                    Toast.makeText(CreateStudyActivity.this, "Created study " + clinicDto.getClinicName(), Toast.LENGTH_SHORT).show();
//                    goHome();
//                }
//                else
//                {
//                    new AlertDialog.Builder(CreateStudyActivity.this)
//                            .setMessage("There was an error creating your study.")
//                            .setPositiveButton("OK", null)
//                            .show();
//                }
//            }
//        }.execute();
    }

    private void goHome()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
