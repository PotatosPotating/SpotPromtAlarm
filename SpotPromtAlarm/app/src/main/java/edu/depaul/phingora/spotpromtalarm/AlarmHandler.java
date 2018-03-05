package edu.depaul.phingora.spotpromtalarm;


import android.content.Intent;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Random;

public class AlarmHandler extends AppCompatActivity implements View.OnClickListener {
    private Location location;
    private String title;
    private int randomEventID;
    private Date startTime;
    private Date endTime;
    private String note;
    private Double latitude;
    private Double longitude;
    private TextView eventID;
    private TextView latTV;
    private TextView longTV;
    private EditText eventName;
    private EditText eventDescription;
    private Button save;
    private Button addToCalander;
    private TimePicker timePicker;
    private DatePicker datePicker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmadd);
        Random r = new Random();
        randomEventID = (r.nextInt(10000) + 1);
        Bundle b = getIntent().getExtras();
        latitude = b.getDouble("latitude");
        longitude = b.getDouble("longitude");
        latTV = (TextView) findViewById(R.id.lat_to_display);
        longTV = (TextView) findViewById(R.id.long_to_display);
        eventID = (TextView) findViewById(R.id.event_id);
        eventName = (EditText) findViewById(R.id.event_name);
        eventDescription = (EditText) findViewById(R.id.event_notes);
        save = (Button) findViewById(R.id.save);
        addToCalander =(Button) findViewById(R.id.calander_button);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        latTV.setText("" + latitude);
        longTV.setText("" + longitude);
        eventID.setText("" + randomEventID);
        save.setOnClickListener(this);
        addToCalander.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save: {
                if (eventName.getText().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Can't leave Event Name empty", Toast.LENGTH_SHORT).show();
                } else if (eventDescription.getText().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Can't leave Event Description empty", Toast.LENGTH_SHORT).show();
                } else {
                    LatLng l = new LatLng(latitude, longitude);
                    title = eventName.getText().toString();
                    note = eventDescription.getText().toString();
                    SingleAlarm alarm = new SingleAlarm(l, randomEventID, title, note);
                    Alarms.addToList(alarm);
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            }
            case R.id.calander_button: {
                if (eventName.getText().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Can't leave Event Name empty", Toast.LENGTH_SHORT).show();
                } else if (eventDescription.getText().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Can't leave Event Description empty", Toast.LENGTH_SHORT).show();
                } else {
                    LatLng l = new LatLng(latitude, longitude);
                    title = eventName.getText().toString();
                    note = eventDescription.getText().toString();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                    cal.set(Calendar.MINUTE,timePicker.getMinute());
                    cal.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                    cal.set(Calendar.MONTH,datePicker.getMonth());
                    cal.set(Calendar.YEAR,datePicker.getYear());
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", cal.getTimeInMillis());
                    intent.putExtra("allDay", false);
                    intent.putExtra("rrule", "FREQ=DAILY");
                    intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                    intent.putExtra("title", title);
                    intent.putExtra("description",note);
                    intent.putExtra("location",latitude+","+longitude);
                    startActivity(intent);
                }
                break;
            }
        }
    }
}