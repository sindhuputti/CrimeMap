package com.sindhu.cirmemap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.customobjects.model.QBPermissions;
import com.quickblox.customobjects.model.QBPermissionsLevel;


public class SendCrime extends AppCompatActivity {
    Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_form);
        spinner = (Spinner) findViewById(R.id.spinner);
    }

    public void submit(View view) {
        String desc = getDescription().getText().toString();
        String strLat = getLatitude().getText().toString();
        String strLong = getLongitude().getText().toString();
        if (desc.isEmpty())
            Toast.makeText(getApplicationContext(), "Please enter description", Toast.LENGTH_SHORT).show();
        else if (strLat.isEmpty())
            Toast.makeText(getApplicationContext(), "Please enter Latitude", Toast.LENGTH_SHORT).show();
        else if (strLong.isEmpty())
            Toast.makeText(getApplicationContext(), "Please enter Longitude", Toast.LENGTH_SHORT).show();
        else {
            sendToQB(strLat, strLong, desc, spinner.getSelectedItem().toString());
        }


    }

    private void sendToQB(String lat, String lng, String desc, String type) {
        QBPermissions permissions = new QBPermissions();
        permissions.setReadPermission(QBPermissionsLevel.OPEN);
        permissions.setUpdatePermission(QBPermissionsLevel.OPEN);
        permissions.setDeletePermission(QBPermissionsLevel.OPEN);
        QBCustomObject object = new QBCustomObject();
        object.setPermission(permissions);
        object.setUserId(MapsActivity.myUser.getId());
        object.putString("name", "sindhu");
        object.putString("Latitude", lat);
        object.putString("Longitude", lng);
        object.putString("Description", desc);
        object.putString("Type", type);

        object.setClassName("Crimes");

        QBCustomObjects.createObject(object, new QBEntityCallback<QBCustomObject>() {
            @Override
            public void onSuccess(QBCustomObject createdObject, Bundle params) {
                Toast.makeText(getApplicationContext(), "Thanks for posting a Crime!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException errors) {
                Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private EditText getLatitude() {
        return (EditText) findViewById(R.id.latitude);
    }

    private EditText getLongitude() {
        return (EditText) findViewById(R.id.longitude);
    }

    private EditText getDescription() {
        return (EditText) findViewById(R.id.description);
    }
}
