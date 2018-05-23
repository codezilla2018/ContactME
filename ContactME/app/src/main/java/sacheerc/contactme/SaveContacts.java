package sacheerc.contactme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.IOException;
import java.util.zip.Inflater;


public class SaveContacts extends AppCompatActivity {
    EditText contactNumber ;
    EditText contactEmail;
    EditText contactName;
    private static String name=MainActivity.name;
    private static  String number=MainActivity.number;
    private static String email = MainActivity.email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_contacts);

        contactNumber =(EditText) findViewById(R.id.mPhoneNumber);
        contactName = (EditText) findViewById(R.id.contactName);
        contactEmail = (EditText) findViewById(R.id.mEmailAddress);

        contactNumber.setText(number);
        contactName.setText(name);
        contactEmail.setText(email);


    }
}
