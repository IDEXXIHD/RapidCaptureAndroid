package com.idexx.labstation.rapidcaptureapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.idexx.labstation.rapidcaptureapp.model.ClinicDto;

import java.text.SimpleDateFormat;

public class ClinicDetailsActivity extends AppCompatActivity
{
    public static final String CLINIC_EXTRA = ClinicDetailsActivity.class.getCanonicalName() + "-CLINIC_EXTRA";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TextView clinicNameView;
    private TextView createdByView;
    private TextView creationDateView;

    private ClinicDto clinicDto;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_details);

        parseBundle();
        bindFields();
        populateFields();
    }

    private void parseBundle()
    {
        this.clinicDto = (ClinicDto) getIntent().getExtras().getSerializable(CLINIC_EXTRA);
    }

    private void bindFields()
    {
        clinicNameView = (TextView) findViewById(R.id.clinicDetailsClinicName);
        createdByView = (TextView) findViewById(R.id.clinicDetailsCreatedBy);
        creationDateView = (TextView) findViewById(R.id.clinicDetailsCreationDate);
    }

    private void populateFields()
    {
        clinicNameView.setText(clinicDto.getClinicName());
        createdByView.setText(clinicDto.getCreatedBy().getUsername());
        creationDateView.setText(sdf.format(clinicDto.getCreatedAt()));
    }
}
