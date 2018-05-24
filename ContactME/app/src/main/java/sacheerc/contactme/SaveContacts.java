package sacheerc.contactme;

import java.util.ArrayList;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.EditText;
import android.view.View;
import android.provider.ContactsContract.Data;
import android.widget.ImageButton;
import android.widget.TextView;


public class SaveContacts extends AppCompatActivity {
    EditText contactNumber ;
    EditText contactEmail;
    EditText contactName;
    private ImageButton saveButton;
    private ImageButton newContact;

 //Parsing values from main method
    private static String name=MainActivity.name;
    private static  String number=MainActivity.number;
    private static String email = MainActivity.email;
//Parsing values end...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_contacts);

//Assigning TEXT_VIEWS to variables
        contactNumber =(EditText) findViewById(R.id.mPhoneNumber);
        contactName = (EditText) findViewById(R.id.contactName);
        contactEmail = (EditText) findViewById(R.id.mEmailAddress);
        saveButton = (ImageButton) findViewById(R.id.save);
        newContact =(ImageButton) findViewById((R.id.newContactButton));

//Display details to TEXT_VIEWS
        contactNumber.setText(number);
        contactName.setText(name);
        contactEmail.setText(email);

//Set home button to go to Main Interface
        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

//Set save button to write Contacts to the devise
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeContact(contactName.getText().toString(),contactNumber.getText().toString(),contactEmail.getText().toString());

            }
        });
    }

//Method to write capture details to mobile phone Contacts
    private void writeContact(String displayName, String number, String email) {
        ArrayList contentProviderOperations = new ArrayList();
        //insert raw contact using RawContacts.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null).withValue(RawContacts.ACCOUNT_NAME, null).build());
        //insert contact display name using Data.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID, 0).withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, displayName).build());
        //insert mobile number using Data.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, number).withValue(Phone.TYPE, Phone.TYPE_MOBILE).build());
        //insert email using Data.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(Data.MIMETYPE,Email.CONTENT_ITEM_TYPE)
                .withValue(Email.DATA, email).withValue(Email.TYPE, Email.TYPE_WORK).build());

        try {
            getApplicationContext().getContentResolver().
                    applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
//Display the message after saving process
        TextView view = (TextView)findViewById(R.id.saved);
        view.setText("Contact saved successfully");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
//Intent activity Starter
    public void openHome(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
