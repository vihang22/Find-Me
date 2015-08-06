package com.ckeeda.findme;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.renderscript.RenderScript;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class Findme_main extends Activity {

    TextView text_msg,sender;
    ImageView image;
    IntentFilter sms_received;
    AudioManager audioManager;
    int device_ring_mode;


    BroadcastReceiver msg_receiver = new BroadcastReceiver(){


        @Override
        public void onReceive(Context context, Intent intent) {
            init();
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            Object[] pdus=(Object[])intent.getExtras().get("pdus");
            SmsMessage Message=SmsMessage.createFromPdu((byte[]) pdus[0]);

            String sender = Message.getOriginatingAddress();
            String message_body = Message.getDisplayMessageBody();
            if(message_body.equalsIgnoreCase("normal")) {
                toNormalmode();
                setMessageData(message_body, sender);
            }
            if(message_body.equalsIgnoreCase("silent")) {
                toSilentmode();
                setMessageData(message_body, sender);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findme_main);
        Log.v("Main", "Starting");
        init();
     /*   text_msg = (TextView)findViewById(R.id.message);
        sender = (TextView)findViewById(R.id.sender);
        image = (ImageView)findViewById(R.id.ringer_image); */
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if((getIntent().getStringExtra(Message_receiver.MESSAGE_BODY) == null)) {

            device_ring_mode = audioManager.getRingerMode();
            if (device_ring_mode == AudioManager.RINGER_MODE_NORMAL)
                image.setImageResource(R.mipmap.normal);
            else
                image.setImageResource(R.mipmap.silent);
        }
        else
        {
            String msg = getIntent().getStringExtra(Message_receiver.MESSAGE_BODY);
            String msg_sender = getIntent().getStringExtra(Message_receiver.MESSAGE_SENDER);
            if(msg.equalsIgnoreCase("normal")) {
                Log.v("MAIN NORMAL",msg);
                toNormalmode();
                setMessageData(msg, msg_sender);
            }
            if(msg.equalsIgnoreCase("silent")) {
                Log.v("MAIN SLIENT",msg);
                toSilentmode();
                setMessageData(msg, msg_sender);
            }
        }
        sms_received = new IntentFilter();
        sms_received.addAction("android.provider.Telephony.SMS_RECEIVED");


    }

    void init(){
        text_msg = (TextView)findViewById(R.id.message);
        sender = (TextView)findViewById(R.id.sender);
        image = (ImageView)findViewById(R.id.ringer_image);
    }

    void setMessageData(String msg,String msg_sender) {
        sender.setText(msg_sender);
        text_msg.setText(msg);
    }

    void toSilentmode(){
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        audioManager.setRingerMode(AudioManager.FLAG_VIBRATE);
        image.setImageResource(R.mipmap.silent);
    }

    void toNormalmode(){
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setRingerMode(AudioManager.FLAG_VIBRATE);
        image.setImageResource(R.mipmap.normal);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(msg_receiver);
        Log.i("MAIN","UNREGISTER RECEIVER");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(msg_receiver,sms_received);
        Log.i("MAIN","REGISTER RECEIVER");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_findme_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
