package com.example.shout_bluetoothp2pnetwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContextWrapper context;
    private BluetoothAdapter bluetoothAdapter;
    private ChatUtils chatUtils;

    private ListView ListMainChat;
    private EditText edCreateMessage;
    private Button btnSendMessage;
    private ArrayAdapter<String> adapterMainChat;


    private final int LOCATION_PERMISSION_REQUEST = 101;
    private final int SELECT_DEVICE = 102;

    public static final int MESSAGE_STATE_CHANGED = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_DEVICE_NAME = 3;
    public static final int MESSAGE_TOAST= 4;

    public static final String DEVICE_NAME = "deviceName";
    public static final String TOAST = "toast";
    private String connectecDevice;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case MESSAGE_STATE_CHANGED:
                    switch (message.arg1){
                        case ChatUtils.STATE_NONE:
                            setState("Not Connected");
                            break;
                        case ChatUtils.STATE_LISTEN:
                            setState("Not Connected");
                            break;
                        case ChatUtils.STATE_CONNECTING:
                            setState("Connecting...");
                            break;
                        case ChatUtils.STATE_CONNECTED:
                            setState("Connected: "+ connectecDevice);
                            break;

                    }
                    break;
                case MESSAGE_READ:
                    byte[] buffer = (byte[]) message.obj;
                    String inputBuffer = new String(buffer,0, message.arg1);
                    adapterMainChat.add(connectecDevice + ": " + inputBuffer );
                    break;
                case MESSAGE_WRITE:
                    byte[] buffer1 = (byte[]) message.obj;
                    String outputBuffer = new String(buffer1);
                    adapterMainChat.add("Me: "+outputBuffer);
                    break;
                case MESSAGE_DEVICE_NAME:
                    connectecDevice = message.getData().getString(DEVICE_NAME);
                    Toast.makeText(context,connectecDevice,Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(context,message.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
                    break;

            }
            return false;
        }
    });


    private  void setState(CharSequence subTitle) {
        getSupportActionBar().setSubtitle(subTitle);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        chatUtils = new ChatUtils(context,handler);

        initBluetooth();
        chatUtils = new ChatUtils(context,handler);
    }


    private void  init(){
         ListMainChat = findViewById(R.id.list_conversation);
         edCreateMessage = findViewById(R.id.ed_enter_message);
         btnSendMessage = findViewById(R.id.btn_send_msg);

         adapterMainChat = new ArrayAdapter<String >(context, R.layout.message_layout);
        ListMainChat.setAdapter(adapterMainChat);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edCreateMessage.getText().toString();
                if(!message.isEmpty()){
                    edCreateMessage.setText("");
                    chatUtils.write(message.getBytes());
                }
            }
        });
    }


    private  void initBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(context, "NO bluetooth found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search_devices:
//                Toast.makeText(context,"Clicked search devices",Toast.LENGTH_SHORT).show();
                checkPermissions();
                return true;
            case R.id.menu_enable_bluetooth:
//                Toast.makeText(context,"Clicked enable bluetooth",Toast.LENGTH_SHORT).show();
                enableBluetooth();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != getPackageManager().PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }else{
            Intent intent = new Intent(context, DeviceListActivity.class);
            startActivityForResult(intent,SELECT_DEVICE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == SELECT_DEVICE && resultCode == RESULT_OK){
            String address = data.getStringExtra("deviceAddress");

//            Toast.makeText(context,"Address" + "\n" + address, Toast.LENGTH_SHORT).show();
            chatUtils.connect(bluetoothAdapter.getRemoteDevice(address));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOCATION_PERMISSION_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED){
                Intent intent = new Intent(context,DeviceListActivity.class);
                startActivityForResult(intent,SELECT_DEVICE);
            }else{
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("Location permission is required.")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPermissions();
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.finish();
                            }
                        }).show();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void enableBluetooth(){
        if(!bluetoothAdapter.isEnabled()){
//            Toast.makeText(context,"Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
            bluetoothAdapter.enable();
        }

        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoveryIntent = new Intent (BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoveryIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(chatUtils != null){
            chatUtils.stop();
        }
    }
}