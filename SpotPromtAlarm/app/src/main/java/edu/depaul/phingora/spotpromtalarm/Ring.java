package edu.depaul.phingora.spotpromtalarm;


import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Ring extends Activity implements View.OnClickListener {
    TextView title;
    TextView description;
    TextView id;
    int idInt;
    Button bt_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_layout);
        title =(TextView)findViewById(R.id.popup_title);
        description =(TextView)findViewById(R.id.popup_description);
        id=(TextView)findViewById(R.id.popup_id);
        bt_done=(Button)findViewById(R.id.popup_done_button);
        Bundle bundle = getIntent().getExtras();
        title.setText(bundle.getString("title"));
        description.setText(bundle.getString("description"));
        idInt = bundle.getInt("id");
        id.setText("Event ID: "+idInt);
        bt_done.setOnClickListener(this);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();

    }

    @Override
    public void onClick(View view) {
        Alarms.deleteByID(idInt);
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
