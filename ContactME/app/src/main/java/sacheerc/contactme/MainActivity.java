package sacheerc.contactme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button button1;
    SurfaceView cameraView;
    TextView textView;
    TextView viewName;
    TextView viewEmail;
    CameraSource cameraSource;
    public static String number="";
    public static String name="";
    public static String email="";

    final int RequestCameraPermissionID = 1001;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RequestCameraPermissionID:
                {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
//method for identify 10 digits standered contactnumber
    public static String informationCreater(String string){
        int lenString =string.length();
        String ansString="";
        for(int i=0;i<lenString;i++){
            if(string.charAt(i)=='0'){
                for(int j=i;j<lenString;j++){
                    if(ansString.length()==10)
                        return ansString;
                    if (Character.isDigit(string.charAt(j))==true)
                        ansString=ansString+Character.toString(string.charAt(j));
                    else if(string.charAt(j)=='-'||string.charAt(j)==' ')
                        ansString=ansString;
                    else if(string.charAt(j)=='\n')
                        ansString="";
                }
                i=i+10;
            }
        }
        return " Cannot identify a Number";
    }

    public static String nameFinder(String string){
        int lenString =string.length();
        String ansString="";
        int i=1;
        while(i<lenString){
            if(Character.isLetter(string.charAt(i))==true && (string.charAt(i-1)==' ' || string.charAt(i-1)=='-' || string.charAt(i-1)==':'|| string.charAt(i-1)=='\n')){
                int j=i;
                int condition=0;
                while(string.charAt(j)!='\n' && condition==0){
                    if(Character.isLetter(string.charAt(j))==true||string.charAt(j)=='.' ||string.charAt(j)==' '){
                        ansString+=Character.toString(string.charAt(j));
                        j+=1;
                    }
                    else{
                        ansString="";
                        condition=1;
                    }
                }
                i=j;
                if(ansString!="")
                    return ansString;

            }
            else
                i+=1;
        }
        return " Cannot identify Contact Name..!";

    }

    public static String emailFinder(String string){
        int lenString =string.length();
        String ansString="";
        int i=0;
        while(i<lenString){
            if(string.charAt(i)=='@'){
                int j=i;
                int k=i+1;
                int condition = 0;
                while(condition==0){
                    if(string.charAt(j)==' '||string.charAt(j)==':'||string.charAt(j)=='-'||string.charAt(j)=='\n'||j==0){
                        if(j==0)
                            ansString=Character.toString(string.charAt(j))+ansString;
                        condition=1;
                    }
                    else{
                        ansString=Character.toString(string.charAt(j))+ansString;
                        if(j!=0)
                            j-=1;
                    }
                }
                condition=0;
                while(condition==0){
                    if(string.charAt(k)==' '||string.charAt(k)==':'||string.charAt(k)=='-'||string.charAt(k)=='\n'){
                        condition=1;
                    }
                    else{
                        ansString=ansString+Character.toString(string.charAt(k));
                        if(k!=lenString-1)
                            k+=1;

                    }
                }
                i=k;
                return ansString;
            }
            i+=1;
        }
        return " Cannot detect an Email";

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        textView = (TextView) findViewById(R.id.title);
        viewName = (TextView) findViewById(R.id.view_name);
        viewEmail = (TextView) findViewById(R.id.view_email);
        button = (Button)findViewById(R.id.button);
        button1= (Button) findViewById(R.id.refreshName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSaveContacts();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependancies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        RequestCameraPermissionID);

                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() !=0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder =new StringBuilder();
                                for(int i=0;i< items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }

                                if(name==""||name==" Cannot identify Contact Name..!")
                                    name=nameFinder(stringBuilder.toString());
                                if(email==""||email==" Cannot detect an Email")
                                    email=emailFinder(stringBuilder.toString());
                                if(number==""||number==" Cannot identify a Number")
                                    number=informationCreater(stringBuilder.toString());

                                viewEmail.setText(email);
                                textView.setText(number);
                                viewName.setText(name);
                            }
                        });
                    }
                }
            });
        }
    }

    public void openSaveContacts(){
        Intent intent = new Intent(this,SaveContacts.class);
        startActivity(intent);
    }

    public void refresh(){
        name="";
        email="";
        number="";

    }



}
