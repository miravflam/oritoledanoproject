package com.example.oritoledanoproject.UI.Store;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oritoledanoproject.R;

public class newProduct extends AppCompatActivity implements View.OnClickListener {

    EditText EtInfo;
    ImageButton btnImg;
    Button btnSend;
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
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        btnImg.setImageBitmap(photo);
                    }
                    else Toast.makeText(newProduct.this, "cancelled", Toast.LENGTH_SHORT).show();
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
                    }
                    else Toast.makeText(newProduct.this, "cancelled", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        btnImg = findViewById(R.id.btnImg);
        btnImg.setOnClickListener(this);

        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        EtInfo=  findViewById(R.id.tvInfo);

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


    }

    @Override
    public void onClick(View view) {
        if(view == btnImg)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("which");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    someActivityResultLauncher.launch(intent);
                }
            }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                    GalleryActivityResultLauncher.launch(intent);
                }
            }).show();
        }
        if (view == btnSend){
            if(fitSpinner.getSelectedItem().toString().equals("מגדר ומידה")){
                Toast.makeText(this, "בחר מגדר ומידה", Toast.LENGTH_SHORT).show();
                return;
            }

            if(situationSpinner.getSelectedItem().toString().equals("מצב המוצר")){
                Toast.makeText(this, "בחר את מצב המוצר", Toast.LENGTH_SHORT).show();
                return;
            }

            if(categorySpinner.getSelectedItem().toString().equals("סוג מוצר")){
                Toast.makeText(this, "בחר את סוג מוצר", Toast.LENGTH_SHORT).show();
                return;
            }
            if (EtInfo.getText().toString().equals("")){
                Toast.makeText(this, "רשום תיאור", Toast.LENGTH_SHORT).show();
                return;
            }

        }

    }
}