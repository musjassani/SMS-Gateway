package com.example.myapplication;

import android.Manifest;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView numTexto;
    private TextView txtTexto;
    private TextView edPort;
    private MyHTTPD server;
    private TextView textIpaddr;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textIpaddr = (TextView) findViewById(R.id.ipaddr);
        numTexto = (TextView) findViewById(R.id.numSMS);
        txtTexto = (TextView) findViewById(R.id.txtSMS);
        edPort = (EditText) findViewById(R.id.port);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
    }

    @Override
    protected void onResume() { // lance apres onCreate
        super.onResume();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) { // test si WiFi actif
            Toast.makeText(this, "WiFi désactivé", Toast.LENGTH_LONG).show();
        }
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        if (ipAddress == 0) {  // test si @IP affectee
            Toast.makeText(this, "Pas d'adresse IP", Toast.LENGTH_LONG).show();
        }
        // formate l'@ en norme IPV4 en faisant confiance a l'utilisateur sur les données saisies
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        int port = Integer.parseInt(edPort.getText().toString()); // on recupere le port choisi par l'utilisateur
        // affiche le socket d'ecoute
        textIpaddr.setText("Accès : http://" + formatedIpAddress + ":" + port);
        try {
            if (server == null) {
                server = new MyHTTPD(port, numTexto, txtTexto); // cree le serveur NanoHTTPD si inexistant
            }
            server.start();             // lance ou relance le serveur
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
