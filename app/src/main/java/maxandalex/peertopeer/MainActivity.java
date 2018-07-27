package maxandalex.peertopeer;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
=======
import android.os.ParcelUuid;
>>>>>>> 859f34737d60e2f14af7adcfa6f06512c17dc5fa
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import maxandalex.peertopeer.QrCode.GeneratedQRCode;
import maxandalex.peertopeer.QrCode.QRCodeReader;
import maxandalex.peertopeer.Server.FilesList;
import maxandalex.peertopeer.Server.Server;

public class MainActivity extends AppCompatActivity {

    private Button btn_CreateQRCode, btn_ReadQRCode, btn_ViewPairedList, btn_GetItemList, btn_TestBluetooth, btn_TestPosition;
    private static Context myContext;
    private final static int REQUEST_ENABLE_BT = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location position;


    private InputStream inStream;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.myContext = getApplicationContext();
        Contact.contactList.clear();
        CSVParser.ImportCSVToList();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                position = location;

                Log.i("POSITION", "nouvelle position --|-- lat :" + position.getLatitude() + " long :" + position.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("POSITION", "nouvelle position ----- lat :" + position.getLatitude() + " long :" + position.getLongitude());
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }

        }else{

        }



        btn_CreateQRCode = (Button) findViewById(R.id.btn_createQRCode);
        btn_ReadQRCode = (Button) findViewById(R.id.btn_receiveQRCode);
        btn_ViewPairedList = (Button) findViewById(R.id.btn_viewPairedList);
        btn_GetItemList = (Button) findViewById(R.id.button6);
        btn_TestBluetooth = (Button) findViewById(R.id.testBlueTooth);
        btn_TestPosition = (Button) findViewById(R.id.testPosition);

        btn_CreateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Génération du code QR", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, GeneratedQRCode.class);
                startActivity(intent);
            }
        });

        btn_ReadQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QRCodeReader.class);
                startActivity(intent);
            }
        });

        btn_ViewPairedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PairingView.class);
                startActivity(intent);
            }
        });

        btn_GetItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File[] fileList;
                fileList = FilesList.getFileList();
                Log.i("FILELIST", "Voici la file list : " + fileList[10].getName());
            }
        });
        btn_TestPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 30000, 5, locationListener);

            }
        });

        btn_TestBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter != null) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                        Log.i("BLUETOOTH", "Get paired device name: " + deviceName + " " + deviceHardwareAddress);

                        //TODO TESTER EN CHANGEANT LE TRUE PAR LE NOM DE L'AUTRE DEVICE
                        if(true){
                            ParcelUuid[] uuids = device.getUuids(); //(uuids[0].getUuid())
                            try {
                                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                                socket.connect();

                                outputStream = socket.getOutputStream();
                                inStream = socket.getInputStream();

                            } catch (IOException e) {
                                Log.e("FuckMeSideWays", "Error occurred when creating input stream", e);
                            }
                        }
                    }
                }



            }
        });

<<<<<<< HEAD


        //TODO SEEMS NOT TO WORK HUMMMM
        // but it does xD entc dans les logs ca dit que ca marche
        Server.startServer();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates("gps", 30000, 5, locationListener);

        }
=======
        //Server starter
        Server.StartServer();
>>>>>>> 859f34737d60e2f14af7adcfa6f06512c17dc5fa
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast.makeText(this, contents,Toast.LENGTH_LONG).show();
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                //Handle cancel
            }
        }
    }

    public static Context getAppContext() {
        return MainActivity.myContext;
    }
    public Location getPosition(){
        return position;
    }

}
