package com.example.oritoledanoproject.UI.Store;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oritoledanoproject.R;
import com.example.oritoledanoproject.UI.Login.MainActivity;

import java.io.IOException;

public class newProduct extends AppCompatActivity implements View.OnClickListener {

    EditText EtInfo, etPrice;
    ImageView btnImg;
    Button btnSend,btnBack;
    ModuleProduct module;
    Bitmap photo;
    private Spinner fitSpinner, situationSpinner,categorySpinner;
    private static final String[] fits = {"מגדר ומידה", "גבר", "אישה", "ילד","ילדה","תינוק","תינוקת"};
    private static final String[] situation = {"מצב המוצר","חדש","כמו חדש","משומש"};
    private static final String[] category = {"סוג מוצר","נעליים","גרביים וגרביונים","ספורט","תחפושות","ביוטי","כובעים פאות ואביזרי שיער","אקססוריז","חולצה קצרה","גופייה","שמלה","ג'ינסים","מכנסיים קצרים","מכנסיים ארוכים","סווצרטים","אלגנט"};

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        photo = (Bitmap) result.getData().getExtras().get("data");
                        btnImg.setTag("Pic");
                        btnImg.setImageBitmap(photo);
                    }
                    else Toast.makeText(newProduct.this, "בוטל", Toast.LENGTH_SHORT).show();
                }
            });

    ActivityResultLauncher<Intent> GalleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        btnImg.setImageURI(result.getData().getData());
                        btnImg.setTag("Pic");
                        try {
                            photo = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), result.getData().getData()), 200, 200, false);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else Toast.makeText(newProduct.this, "בוטל", Toast.LENGTH_SHORT).show();
                }
            });

    ActivityResultLauncher<PickVisualMediaRequest> OlderGalleryResultActivity =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    // There are no request codes
                    btnImg.setImageURI(uri);
                    btnImg.setTag("Pic");
                    try {
                        photo = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 200, 200, false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        btnImg = findViewById(R.id.btnImg);
        btnImg.setOnClickListener(this);
        btnImg.setTag("NoPic");

        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        EtInfo=  findViewById(R.id.tvInfo);

        etPrice = findViewById(R.id.etPrice);

        //איך לגשת למה שבחרו
        //fitSpinner.getSelectedItem().toString()

        fitSpinner = (Spinner)findViewById(R.id.fitsSpinner);
        ArrayAdapter<String>fitsAdapter = new ArrayAdapter<String>(newProduct.this,
                android.R.layout.simple_spinner_item,fits);

        fitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fitSpinner.setAdapter(fitsAdapter);





        situationSpinner = (Spinner)findViewById(R.id.situationSpinner);
        ArrayAdapter<String> situationAdapter = new ArrayAdapter<String>(newProduct.this,
                android.R.layout.simple_spinner_item,situation);

        situationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        situationSpinner.setAdapter(situationAdapter);




        categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(newProduct.this,
                android.R.layout.simple_spinner_item,category);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        module = new ModuleProduct(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnImg)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("איך תעדיף לעלות את התמונה");
            builder.setPositiveButton("מצלמה", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    someActivityResultLauncher.launch(intent);
                }
            }).setNegativeButton("גלריה", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent galleryIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);

                    try {
                        GalleryActivityResultLauncher.launch(galleryIntent);
                    } catch (Exception e)
                    {
                        OlderGalleryResultActivity.launch(new PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                .build());
                    }
                }
            }).show();
        }
        if (view == btnSend){
           if(module.checkUps(fitSpinner.getSelectedItem().toString(), situationSpinner.getSelectedItem().toString(), categorySpinner.getSelectedItem().toString(), EtInfo.getText().toString(), etPrice.getText().toString() ,btnImg))
           {
               module.addProduct(fitSpinner.getSelectedItem().toString(), categorySpinner.getSelectedItem().toString(), situationSpinner.getSelectedItem().toString(), EtInfo.getText().toString(), etPrice.getText().toString() ,photo);
           }
        }
        if (view ==btnBack){
            Intent intentback = new Intent(newProduct.this,StoreActivity.class);
            startActivity(intentback);
        }

    }
}