package com.ja.robotaltamira;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
/* PERMISOS */
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import androidx.core.app.ActivityCompat;

/* Widget llamadas */
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.TextView;

/*SMS*/
import android.telephony.SmsManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity<TAG> extends AppCompatActivity {

    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;
    private static String TAG = "LOG_PRUEBAS_BASICAS";
    ArrayList<Long> list = new ArrayList<>();
    String url = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3";
    String fileName = "file_example_MP3_5MG.mp3";


    private static final int CALL_REQUEST_CODE = 101;
    private static final int SMS_REQUEST_CODE = 102;
    private static final int WRI_REQUEST_CODE = 103;
    private static final int INT_REQUEST_CODE = 104;
    private static final int WRIINT_REQUEST_CODE = 105;
    private Button button;
    private Button ButtonPrueba;
    String message;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final String TAG = "LOG_PRUEBAS_BASICAS";
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        Download_Uri = Uri.parse("http://ipv4.download.thinkbroadband.com/10MB.zip");
        setupPermissions();
        Log.i(TAG, "INICIO PRUEBAS BASICAS");

        /* LLAMADA ON NET */
        button = (Button) findViewById(R.id.Boton_Llamada_numero_B_ON_Net);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                EditText NumeroBOnNet = findViewById(R.id.NumeroB_On_Net);
                if (!NumeroBOnNet.getText().toString().equals("")) {
                    String num_telefono = String.valueOf(NumeroBOnNet.getText().toString());
                    String uri = "tel:" + num_telefono.trim() ;
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(uri));
                    startActivity(callIntent);
                }
            }
        });
        /* LLAMADA OFF NET */
        button = (Button) findViewById(R.id.Boton_Llamada_numero_B_Off_Net);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                EditText NumeroBOffNet = findViewById(R.id.NumeroB_Off_Net);
                if (!NumeroBOffNet.getText().toString().equals("")) {
                    String num_telefono = String.valueOf(NumeroBOffNet.getText().toString());
                    String uri = "tel:" + num_telefono.trim() ;
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(uri));
                    startActivity(callIntent);
                }
            }
        });
        /* SMS */
        button = (Button) findViewById(R.id.Boton_SMS_ON_OFF);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                EditText NumeroBOnNet = findViewById(R.id.NumeroB_On_Net);
                EditText NumeroBOffNet = findViewById(R.id.NumeroB_Off_Net);
                if (!NumeroBOnNet.getText().toString().equals("")) {
                    String num_telefono_on = String.valueOf(NumeroBOnNet.getText().toString());
                    String num_telefono_off = String.valueOf(NumeroBOffNet.getText().toString());
                    message = "Mensaje de Prueba Altamira";
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(num_telefono_on, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS ON NET ENVIADO..", Toast.LENGTH_LONG).show();
                    smsManager.sendTextMessage(num_telefono_off, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS OFF NET ENVIADO.", Toast.LENGTH_LONG).show();
                }
            }
        });
        /* INTERNET */


        button = (Button) findViewById(R.id.Boton_INTERNET);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.i(TAG, "INICIO PRUEBA INTERNET MANUAL");
                Uri downloadURI = Uri.parse(url);
                DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                try {
                    if (manager != null){
                        Log.e("ERROR:MAIN", "M: url a bajar "+ downloadURI);
                        DownloadManager.Request request = new DownloadManager.Request(downloadURI);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setTitle(fileName)
                                .setDescription("Downloading"+fileName)
                                .setAllowedOverMetered(true)
                                .setAllowedOverRoaming(true)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
                                .setMimeType(getMimeType(downloadURI));
                        Log.e("ERROR:MAIN", "M: " + Environment.DIRECTORY_DOWNLOADS + " | " +fileName);
                        manager.enqueue(request);

                        Toast.makeText(MainActivity.this,"Download Started!",Toast.LENGTH_SHORT).show();

                    }else{
                        Intent intent = new Intent(Intent.ACTION_VIEW, downloadURI);
                        startActivity(intent);
                    }
                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR:MAIN", "E: "+e.getMessage());
                }

            }
        });

        /* PRUEBAS COMPLETAS */

        ButtonPrueba = (Button) findViewById(R.id.Boton_prueba);
        ButtonPrueba.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextView FechaInicio = (TextView)findViewById(R.id.ShowFecInicio);
                setDate(FechaInicio);
                TextView FechaFin = (TextView)findViewById(R.id.ShowFecFin);

                //LLAMADAS
                EditText NumeroBOnNet = findViewById(R.id.NumeroB_On_Net);
                if (!NumeroBOnNet.getText().toString().equals("")) {
                    Log.i(TAG, "Llamada ONNET INICIO");
                    String num_telefono = String.valueOf(NumeroBOnNet.getText().toString());
                    String uri = "tel:" + num_telefono.trim() ;
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(uri));
                    startActivity(callIntent);
                    Log.i(TAG, "Llamada ONNET FIN");
                }

                try {
                    Log.i(TAG, "Sleep iniciado");
                    Thread.sleep(40000);
                    Log.i(TAG, "Sleep finalizado");
                } catch (Exception e) {
                }
                Log.i(TAG, "Espera de 40 segundos");

                EditText NumeroBOffNet = findViewById(R.id.NumeroB_Off_Net);
                if (!NumeroBOffNet.getText().toString().equals("")) {
                    Log.i(TAG, "Llamada OFFNET INICIO");
                    String num_telefono = String.valueOf(NumeroBOffNet.getText().toString());
                    String uri = "tel:" + num_telefono.trim() ;
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(uri));
                    startActivity(callIntent);
                    Log.i(TAG, "Llamada OFFNET FIN");
                }
                setDate(FechaFin);
                //SMS
                EditText NumeroBOnNetSms = findViewById(R.id.NumeroB_On_Net);
                EditText NumeroBOffNetSms = findViewById(R.id.NumeroB_Off_Net);
                if (!NumeroBOnNet.getText().toString().equals("")) {
                    Log.i(TAG, "SMSs  INICIO");
                    String num_telefono_on = String.valueOf(NumeroBOnNetSms.getText().toString());
                    String num_telefono_off = String.valueOf(NumeroBOffNetSms.getText().toString());
                    message = "Mensaje de Prueba Altamira";
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(num_telefono_on, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS ON NET ENVIADO..", Toast.LENGTH_LONG).show();
                    smsManager.sendTextMessage(num_telefono_off, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS OFF NET ENVIADO.", Toast.LENGTH_LONG).show();

                }
                setDate(FechaFin);
                //INTERNET


                Uri downloadURI = Uri.parse(url);
                DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                try {
                    if (manager != null){
                        Log.e("ERROR:MAIN", "M: url a bajar "+ downloadURI);
                        DownloadManager.Request request = new DownloadManager.Request(downloadURI);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setTitle(fileName)
                                .setDescription("Downloading"+fileName)
                                .setAllowedOverMetered(true)
                                .setAllowedOverRoaming(true)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
                                .setMimeType(getMimeType(downloadURI));
                        Log.e("ERROR:MAIN", "M: " + Environment.DIRECTORY_DOWNLOADS + " | " +fileName);
                        manager.enqueue(request);

                        Toast.makeText(MainActivity.this,"Download Started!",Toast.LENGTH_SHORT).show();

                    }else{
                        Intent intent = new Intent(Intent.ACTION_VIEW, downloadURI);
                        startActivity(intent);
                    }
                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR:MAIN", "E: "+e.getMessage());
                }



            }
        });


    }

    private void setupPermissions() {
        int permissioncall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissioncall != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permiso para llamar denegado, se solicita permiso");
            makeRequest();
        }
        int permissionsms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissionsms != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permiso para sms denegado, se solicita permiso");
            makeRequest1();
        }
        int permissionwri = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionwri != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permiso para escritura denegado, se solicita permiso");
            makeRequest2();
        }
        int permissionint = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permissionint != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permiso para internet denegado, se solicita permiso");
            makeRequest3();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
    }

    protected void makeRequest1() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS}, SMS_REQUEST_CODE);
    }

    protected void makeRequest2() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRI_REQUEST_CODE);
    }

    protected void makeRequest3() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, INT_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CALL_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] !=  PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permiso Llamada Denegado Por El usuario");
                } else {
                    Log.i(TAG, "Permiso Llamada Concedido Por El usuario");
                }
            }
            case SMS_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] !=  PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permiso SMS Denegado Por El usuario");
                } else {
                    Log.i(TAG, "Permiso SMS Concedido Por El usuario");
                }
            }
            case WRI_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] !=  PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permiso WRITE Denegado Por El usuario");
                } else {
                    Log.i(TAG, "Permiso WRITE Concedido Por El usuario");
                }
            }
            case INT_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] !=  PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permiso INTERNET Denegado Por El usuario");
                } else {
                    Log.i(TAG, "Permiso INTERNET Concedido Por El usuario");
                }
            }

        }
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.e("IN", "" + referenceId);
            list.remove(referenceId);
            if (list.isEmpty())
            {
                Log.e("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MainActivity.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("GadgetSaint")
                                .setContentText("All Download completed");
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());
                TextView FechaFinNavega = (TextView)findViewById(R.id.ShowFecFin);
                setDate(FechaFinNavega);

            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }

    private String getMimeType (Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }


    public void setDate (TextView view){

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");//formating according to my need
        String date = formatter.format(today);
        view.setText(date);
        Log.i(TAG, "poner fecha : " + date);
    }

}