package com.example.oritoledanoproject.UI.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oritoledanoproject.Data.CurrentUser.CurrentUser;
import com.example.oritoledanoproject.Data.FirebaseHelper.FirebaseHelper;
import com.example.oritoledanoproject.R;

import java.net.URI;
import java.util.LinkedList;
import java.util.Map;

public class updateuseranddeleteproduct extends AppCompatActivity implements View.OnClickListener {

    TableLayout tvlay;
    Button btnupdate,btnback;
    ProgressDialog dialof;
    moduleupdateuser moduleupdateuser;
 EditText etname, etemail,etpass,etadress,etphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuseranddeleteproduct);
        moduleupdateuser = new moduleupdateuser(this);

        etname =findViewById(R.id.etname);
        etemail =findViewById(R.id.etemail);
        etpass =findViewById(R.id.etpass);
        etadress =findViewById(R.id.etaddres);
        etphone =findViewById(R.id.etphone);


        btnback =findViewById(R.id.btnreturn);
        btnupdate =findViewById(R.id.btnupdate);

        dialof = new ProgressDialog(this);
        dialof.setTitle("טוען את המידע");
        dialof.setCancelable(false);
        dialof.show();

        tvlay = findViewById(R.id.tvDeleteProduct);

        btnupdate.setOnClickListener(this);
        btnback.setOnClickListener(this);


        moduleupdateuser.getUser(CurrentUser.getFireID(), new FirebaseHelper.gotuser() {
            @Override
            public void ongotuser(String name, String pass, String email, String phone, String addres) {
                etname.setText(name);
                etpass.setText(pass);
                etemail.setText(email);
                etphone.setText(phone);
                etadress.setText(addres);

                moduleupdateuser.getUsersProduct(CurrentUser.getFireID(), new FirebaseHelper.userprodute() {
                    @Override
                    public void userProduct(LinkedList<Map<String, Object>> products) {


                        for (int i = 0; i < products.size(); i++) {

                            // Create TableRow to display marker details
                            TableRow Row = new TableRow(getBaseContext());
                            Row.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    0.25f));

                            // Create ImageView to display marker image
                            ImageView imageView = new ImageView(getBaseContext());
                            imageView.setImageURI(Uri.parse(products.get(i).get("photo").toString()));
                            Glide.with(getBaseContext()).load(products.get(i).get("photo").toString()).preload();
                            Glide.with(getBaseContext()).load(products.get(i).get("photo").toString()).into(imageView);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imageView.setAdjustViewBounds(true);
                            imageView.setLayoutParams(new TableRow.LayoutParams(
                                    500,
                                    500,
                                    0.25f));

                            // Create TextView for marker description
                            TextView descriptionTextView = new TextView(getBaseContext());
                            descriptionTextView.setText(" "+products.get(i).get("description").toString() +" ");
                            descriptionTextView.setTextSize(16);
                            descriptionTextView.setTextColor(getResources().getColor(android.R.color.black, null));
                            descriptionTextView.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    1f));

                            // Create TextView for marker ID
                            TextView idGender = new TextView(getBaseContext());
                            idGender.setText(" "+products.get(i).get("gender").toString()+" ");
                            idGender.setTextSize(16);
                            idGender.setTextColor(getResources().getColor(android.R.color.black, null));
                            idGender.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    1f));

                            TextView idPrice = new TextView(getBaseContext());
                            idPrice.setText(" "+products.get(i).get("price").toString()+" ");
                            idPrice.setTextSize(16);
                            idPrice.setTextColor(getResources().getColor(android.R.color.black, null));
                            idPrice.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    1f));


                            TextView situation = new TextView(getBaseContext());
                            situation.setText(" "+products.get(i).get("situation").toString()+" ");
                            situation.setTextSize(16);
                            situation.setTextColor(getResources().getColor(android.R.color.black, null));
                            situation.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    1f));


                            TextView type = new TextView(getBaseContext());
                            type.setText(" "+products.get(i).get("type").toString()+" ");
                            type.setTextSize(16);
                            type.setTextColor(getResources().getColor(android.R.color.black, null));
                            type.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    1f));



                            // Create Button to delete marker
                            Button actionButton = new Button(getBaseContext());
                            actionButton.setText("Delete");
                            final int x = i;
                            actionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    moduleupdateuser.deleteProduct(CurrentUser.getFireID(),products.get(x).get("id").toString());
                                    Row.removeAllViews();

                                }
                            });

                            actionButton.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    1f));

                            // Add views to TableRow
                            Row.addView(imageView);
                            Row.addView(descriptionTextView);
                            Row.addView(idGender);
                            Row.addView(idPrice);
                            Row.addView(situation);
                            Row.addView(type);
                            Row.addView(actionButton);

                            // Add TableRow to TableLayout
                            tvlay.addView(Row);
                        }
                        dialof.dismiss();

                    }
                });

            }
        });






    }

    @Override
    public void onClick(View v) {
        if (v==btnback){
            startActivity(new Intent(updateuseranddeleteproduct.this,StoreActivity.class));

        }
        if (v==btnupdate){
            if (moduleupdateuser.CheckUps(etname,etemail,etpass,etphone,etadress)){
                moduleupdateuser.updateUser(etname.getText().toString(), etpass.getText().toString(), etemail.getText().toString(), etadress.getText().toString(), etphone.getText().toString(), new FirebaseHelper.userupdated() {
                    @Override
                    public void onupdatecomleted() {
                        CurrentUser.initializeUser(etname.getText().toString(),etemail.getText().toString(),CurrentUser.getFireID());
                        startActivity(new Intent(updateuseranddeleteproduct.this,StoreActivity.class));
                        Toast.makeText(updateuseranddeleteproduct.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}