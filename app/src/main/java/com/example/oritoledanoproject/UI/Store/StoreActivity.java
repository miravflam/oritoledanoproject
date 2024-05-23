package com.example.oritoledanoproject.UI.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oritoledanoproject.Data.CurrentUser.CurrentUser;
import com.example.oritoledanoproject.Data.FirebaseHelper.FirebaseHelper;
import com.example.oritoledanoproject.R;
import com.example.oritoledanoproject.UI.Login.MainActivity;
import com.example.oritoledanoproject.UI.Login.ModuleLogin;

import org.w3c.dom.Text;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {


    TextView tvName;
    Button btnAddProduct, btnlogout;
    TableLayout tableLayout;
    LinkedList<TableRow> rowList = new LinkedList<>();
    static String[] Credentials;
    ModuleStore moduleStore;
    EditText etMax,etMin;
    Button btnReset,btnFilter;
    private Spinner fitSpinner, situationSpinner,categorySpinner;
    private static final String[] fits = {"מגדר ומידה", "גבר", "אישה", "ילד","ילדה","תינוק","תינוקת"};
    private static final String[] situation = {"מצב המוצר","חדש","כמו חדש","משומש"};
    private static final String[] category = {"סוג מוצר","נעליים","גרביים וגרביונים","ספורט","תחפושות","ביוטי","כובעים פאות ואביזרי שיער","אקססוריז","חולצה קצרה","גופייה","שמלה","ג'ינסים","מכנסיים קצרים","מכנסיים ארוכים","סווצרטים","אלגנט"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        tableLayout = findViewById(R.id.tableLayout);

        fitSpinner = (Spinner)findViewById(R.id.fitsSpinner);
        ArrayAdapter<String> fitsAdapter = new ArrayAdapter<String>(StoreActivity.this,
                android.R.layout.simple_spinner_item,fits);

        fitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fitSpinner.setAdapter(fitsAdapter);

        fitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!situationSpinner.getSelectedItem().equals("מצב המוצר") || !categorySpinner.getSelectedItem().equals("סוג מוצר"))
                {
                    fitSpinner.setSelection(0);
                    Toast.makeText(StoreActivity.this, "רק קריטריון אחד בכל פעם!", Toast.LENGTH_SHORT).show();
                    return;
                }

                tableLayout.removeAllViews();
                if(fitSpinner.getSelectedItem().equals("מגדר ומידה"))
                {
                    for (int i = 0; i < rowList.size(); i++) {
                        TableRow invisRow = new TableRow(getApplicationContext());
                        TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        invisRow.setLayoutParams(params2);
                        TextView invisImageView = new TextView(getBaseContext());
                        invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                100,
                                0.25f));
                        invisRow.addView(invisImageView);

                       tableLayout.addView(rowList.get(i));
                       tableLayout.addView(invisRow);
                    }
                    return;
                }
                for (int i = 0; i < rowList.size(); i++) {
                    for (int j = 0; j < rowList.get(i).getChildCount(); j++) {
                        View childView = rowList.get(i).getChildAt(j);
                        if (childView instanceof TextView) {
                            TextView textView = (TextView) childView;
                            if(textView.getTag() != null &&textView.getTag().equals("genderView")) {
                                if (textView.getText().toString().contains(fitSpinner.getSelectedItem().toString())) {
                                    TableRow invisRow = new TableRow(getApplicationContext());
                                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    invisRow.setLayoutParams(params2);
                                    TextView invisImageView = new TextView(getBaseContext());
                                    invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            100,
                                            0.25f));
                                    invisRow.addView(invisImageView);
                                    tableLayout.addView(rowList.get(i));
                                    tableLayout.addView(invisRow);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        situationSpinner = (Spinner)findViewById(R.id.situationSpinner);
        ArrayAdapter<String> situationAdapter = new ArrayAdapter<String>(StoreActivity.this,
                android.R.layout.simple_spinner_item,situation);

        situationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        situationSpinner.setAdapter(situationAdapter);

        btnlogout = findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moduleStore.DoesRemember()) {
                    moduleStore.DoNotRemember();
                }
                Intent intent = new Intent(StoreActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        situationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!fitSpinner.getSelectedItem().equals("מגדר ומידה") || !categorySpinner.getSelectedItem().equals("סוג מוצר"))
                {
                    situationSpinner.setSelection(0);
                    Toast.makeText(StoreActivity.this, "רק קריטריון אחד בכל פעם!", Toast.LENGTH_SHORT).show();
                    return;
                }

                tableLayout.removeAllViews();
                if(situationSpinner.getSelectedItem().equals("מצב המוצר"))
                {
                    for (int i = 0; i < rowList.size(); i++) {
                        TableRow invisRow = new TableRow(getApplicationContext());
                        TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        invisRow.setLayoutParams(params2);
                        TextView invisImageView = new TextView(getBaseContext());
                        invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                100,
                                0.25f));
                        invisRow.addView(invisImageView);

                        tableLayout.addView(rowList.get(i));
                        tableLayout.addView(invisRow);
                    }
                    return;
                }
                for (int i = 0; i < rowList.size(); i++) {
                    for (int j = 0; j < rowList.get(i).getChildCount(); j++) {
                        View childView = rowList.get(i).getChildAt(j);
                        if (childView instanceof TextView) {
                            TextView textView = (TextView) childView;
                            if(textView.getTag() != null &&textView.getTag().equals("situationView")) {
                                if (textView.getText().toString().contains(situationSpinner.getSelectedItem().toString())) {
                                    TableRow invisRow = new TableRow(getApplicationContext());
                                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    invisRow.setLayoutParams(params2);
                                    TextView invisImageView = new TextView(getBaseContext());
                                    invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            100,
                                            0.25f));
                                    invisRow.addView(invisImageView);
                                    tableLayout.addView(rowList.get(i));
                                    tableLayout.addView(invisRow);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        categorySpinner = (Spinner)findViewById(R.id.typeSpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(StoreActivity.this,
                android.R.layout.simple_spinner_item,category);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!situationSpinner.getSelectedItem().equals("מצב המוצר") || !fitSpinner.getSelectedItem().equals("מגדר ומידה"))
                {
                    categorySpinner.setSelection(0);
                    Toast.makeText(StoreActivity.this, "רק קריטריון אחד בכל פעם!", Toast.LENGTH_SHORT).show();
                    return;
                }

                tableLayout.removeAllViews();
                if(categorySpinner.getSelectedItem().equals("סוג מוצר"))
                {
                    for (int i = 0; i < rowList.size(); i++) {
                        TableRow invisRow = new TableRow(getApplicationContext());
                        TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        invisRow.setLayoutParams(params2);
                        TextView invisImageView = new TextView(getBaseContext());
                        invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                100,
                                0.25f));
                        invisRow.addView(invisImageView);

                        tableLayout.addView(rowList.get(i));
                        tableLayout.addView(invisRow);
                    }
                    return;
                }
                for (int i = 0; i < rowList.size(); i++) {
                    for (int j = 0; j < rowList.get(i).getChildCount(); j++) {
                        View childView = rowList.get(i).getChildAt(j);
                        if (childView instanceof TextView) {
                            TextView textView = (TextView) childView;
                            if(textView.getTag() != null &&textView.getTag().equals("typeView")) {
                                if (textView.getText().toString().contains(categorySpinner.getSelectedItem().toString())) {
                                    TableRow invisRow = new TableRow(getApplicationContext());
                                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    invisRow.setLayoutParams(params2);
                                    TextView invisImageView = new TextView(getBaseContext());
                                    invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            100,
                                            0.25f));
                                    invisRow.addView(invisImageView);
                                    tableLayout.addView(rowList.get(i));
                                    tableLayout.addView(invisRow);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        moduleStore = new ModuleStore(this);
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("getting all products");
        pd.setCancelable(false);
        pd.show();
        moduleStore.getProducts(new FirebaseHelper.productsFetched() {
            @Override
            public void onProductsFetched(LinkedList<Map<String, Object>> list) {
                pd.dismiss();
                tvName = findViewById(R.id.userNameTextView);
                btnAddProduct= findViewById(R.id.btnAddProduct);
                etMax = findViewById(R.id.etMax);
                etMin = findViewById(R.id.etMin);
                btnReset = findViewById(R.id.btnReset);
                btnFilter = findViewById(R.id.btnFilter);

                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tableLayout.removeAllViews();
                        for (int i = 0; i < rowList.size(); i++) {
                            TableRow invisRow = new TableRow(getApplicationContext());
                            TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            invisRow.setLayoutParams(params2);
                            TextView invisImageView = new TextView(getBaseContext());
                            invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    100,
                                    0.25f));
                            invisRow.addView(invisImageView);

                            tableLayout.addView(rowList.get(i));
                            tableLayout.addView(invisRow);
                        }
                        fitSpinner.setSelection(0);
                        categorySpinner.setSelection(0);
                        situationSpinner.setSelection(0);
                        etMax.setText("");
                        etMin.setText("");
                    }
                });


                btnFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etMax.getText().toString().isEmpty() && etMin.getText().toString().isEmpty())
                        {
                            tableLayout.removeAllViews();
                            for (int i = 0; i < rowList.size(); i++) {
                                TableRow invisRow = new TableRow(getApplicationContext());
                                TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                invisRow.setLayoutParams(params2);
                                TextView invisImageView = new TextView(getBaseContext());
                                invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        100,
                                        0.25f));
                                invisRow.addView(invisImageView);

                                tableLayout.addView(rowList.get(i));
                                tableLayout.addView(invisRow);
                            }
                            return;
                        }
                        if(etMax.getText().toString().equals(".") || etMin.getText().toString().equals("."))
                        {
                            Toast.makeText(StoreActivity.this, "invalid numbers", Toast.LENGTH_SHORT).show();
                            etMin.setText("");
                            etMax.setText("");
                            return;
                        }
                        if(Double.parseDouble(etMax.getText().toString()) < Double.parseDouble(etMin.getText().toString()))
                        {
                            return;
                        }
                        if(!etMin.getText().toString().isEmpty() && !etMin.getText().toString().isEmpty())
                        {
                                tableLayout.removeAllViews();
                                for (int i = 0; i < rowList.size(); i++) {
                                    for (int j = 0; j < rowList.get(i).getChildCount(); j++) {
                                        View childView = rowList.get(i).getChildAt(j);
                                        if (childView instanceof TextView) {
                                            TextView textView = (TextView) childView;
                                            if (textView.getTag() != null && textView.getTag().equals("priceView")) {
                                                double num = Double.parseDouble(textView.getText().toString().substring(textView.getText().toString().indexOf(" ")));
                                                if (num <= Double.parseDouble(etMax.getText().toString()) && num >= Double.parseDouble(etMin.getText().toString())) {
                                                    TableRow invisRow = new TableRow(getApplicationContext());
                                                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    invisRow.setLayoutParams(params2);
                                                    TextView invisImageView = new TextView(getBaseContext());
                                                    invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                                            100,
                                                            0.25f));
                                                    invisRow.addView(invisImageView);
                                                    tableLayout.addView(rowList.get(i));
                                                    tableLayout.addView(invisRow);
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                        else if(etMax.getText().toString().isEmpty() && !etMin.getText().toString().isEmpty())
                        {
                                tableLayout.removeAllViews();
                                for (int i = 0; i < rowList.size(); i++) {
                                    for (int j = 0; j < rowList.get(i).getChildCount(); j++) {
                                        View childView = rowList.get(i).getChildAt(j);
                                        if (childView instanceof TextView) {
                                            TextView textView = (TextView) childView;
                                            if (textView.getTag() != null && textView.getTag().equals("priceView")) {
                                                double num = Double.parseDouble(textView.getText().toString().substring(textView.getText().toString().indexOf(" ")));
                                                if (num >= Double.parseDouble(etMin.getText().toString())) {
                                                    TableRow invisRow = new TableRow(getApplicationContext());
                                                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    invisRow.setLayoutParams(params2);
                                                    TextView invisImageView = new TextView(getBaseContext());
                                                    invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                                            100,
                                                            0.25f));
                                                    invisRow.addView(invisImageView);
                                                    tableLayout.addView(rowList.get(i));
                                                    tableLayout.addView(invisRow);
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                        else if(!etMax.getText().toString().isEmpty() && etMin.getText().toString().isEmpty()) {
                                tableLayout.removeAllViews();
                                for (int i = 0; i < rowList.size(); i++) {
                                    for (int j = 0; j < rowList.get(i).getChildCount(); j++) {
                                        View childView = rowList.get(i).getChildAt(j);
                                        if (childView instanceof TextView) {
                                            TextView textView = (TextView) childView;
                                            if (textView.getTag() != null && textView.getTag().equals("priceView")) {
                                                double num = Double.parseDouble(textView.getText().toString().substring(textView.getText().toString().indexOf(" ")));
                                                if (num <= Double.parseDouble(etMax.getText().toString())) {
                                                    TableRow invisRow = new TableRow(getApplicationContext());
                                                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    invisRow.setLayoutParams(params2);
                                                    TextView invisImageView = new TextView(getBaseContext());
                                                    invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                                            100,
                                                            0.25f));
                                                    invisRow.addView(invisImageView);
                                                    tableLayout.addView(rowList.get(i));
                                                    tableLayout.addView(invisRow);
                                                }
                                            }
                                        }
                                    }
                                }
                        } 



                    }
                });

                if(moduleStore.DoesRemember() )
                    Credentials = moduleStore.getCredentials();
                else if (Credentials == null) {
                    Credentials = moduleStore.getCredentials();
                    moduleStore.DoNotRemember();
                }

                tvName.setText(CurrentUser.getName());

                btnAddProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentAdd = new Intent(StoreActivity.this, newProduct.class);
                        startActivity(intentAdd);
                    }
                });



                    for (int i = 0; i < list.size(); i++) {

                        TableRow invisRow = new TableRow(getApplicationContext());
                        TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        params2.setMargins(0,200,0,0);
                        invisRow.setLayoutParams(params2);
                        TextView invisImageView = new TextView(getBaseContext());
                        invisImageView.setLayoutParams(new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                100,
                                0.25f));


                        TableRow Row = new TableRow(getApplicationContext());
                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,200,0,0);
                        Row.setLayoutParams(params);
                        Row.setClickable(true);
                        Row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });


                        // Create ImageView
                        ImageView imageView = new ImageView(getBaseContext());
                        imageView.setImageDrawable(getDrawable(R.drawable.camera));
                        Glide.with(getBaseContext()).load((Uri) list.get(i).get("picture")).preload();
                        Glide.with(getBaseContext()).load((Uri) list.get(i).get("picture")).into(imageView);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setAdjustViewBounds(true);
                        imageView.setLayoutParams(new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                400,
                                0.25f));



                        // Create TextView for description
                        TextView descriptionTextView = new TextView(getBaseContext());
                        descriptionTextView.setText("תיאור מוצר: ");
                        descriptionTextView.append(list.get(i).get("description").toString() );
                        descriptionTextView.setTextSize(16);
                        descriptionTextView.setTextColor(getResources().getColor(android.R.color.black, null));
                        descriptionTextView.setLayoutParams(new TableRow.LayoutParams(
                                400,
                                400,
                                1f));
                        descriptionTextView.setTag("descriptionView");


                        // Create TextView for ID
                        TextView genderTextView = new TextView(getBaseContext());
                        genderTextView.setText("גזרה: ");
                        genderTextView.append(list.get(i).get("gender").toString());
                        genderTextView.setTextSize(16);
                        genderTextView.setTextColor(getResources().getColor(android.R.color.black, null));
                        genderTextView.setLayoutParams(new TableRow.LayoutParams(
                                400,
                                400,
                                1f));
                        genderTextView.setTag("genderView");


                        TextView typeTextView = new TextView(getBaseContext());
                        typeTextView.setText("סוג המוצר: ");
                        typeTextView.append(list.get(i).get("type").toString());
                        typeTextView.setTextSize(16);
                        typeTextView.setTextColor(getResources().getColor(android.R.color.black, null));
                        typeTextView.setLayoutParams(new TableRow.LayoutParams(
                                400,
                                400,
                                1f));
                        typeTextView.setTag("typeView");


                        TextView situationTextView = new TextView(getBaseContext());
                        situationTextView.setText("מצב המוצר: ");
                        situationTextView.append(list.get(i).get("situation").toString());
                        situationTextView.setTextSize(16);
                        situationTextView.setTextColor(getResources().getColor(android.R.color.black, null));
                        situationTextView.setLayoutParams(new TableRow.LayoutParams(
                                400,
                                400,
                                1f));
                        situationTextView.setTag("situationView");

                        TextView priceTextView = new TextView(getBaseContext());
                        priceTextView.setText("מחיר: ");
                        priceTextView.append(list.get(i).get("price").toString());
                        priceTextView.setTextSize(16);
                        priceTextView.setTextColor(getResources().getColor(android.R.color.black, null));
                        priceTextView.setLayoutParams(new TableRow.LayoutParams(
                                400,
                                400,
                                1f));
                        priceTextView.setTag("priceView");

                        final int currentIndex = i;
                        Row.setClickable(true);
                        Row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog dialog=new Dialog(StoreActivity.this);
                                dialog.setContentView(R.layout.extra_info_dialog);
                                TextView nameView = dialog.findViewById(R.id.tvname);
                                TextView phoneView = dialog.findViewById(R.id.tvphone);
                                TextView addressView = dialog.findViewById(R.id.tvadress);

                                nameView.setText("שם : " + list.get(currentIndex).get("name").toString());
                                phoneView.setText("טלפון : " + list.get(currentIndex).get("phone").toString());
                                addressView.setText("כתובת : " + list.get(currentIndex).get("address").toString());
                                Button btnClose = dialog.findViewById(R.id.btnclose);
                                btnClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialog.setCancelable(false);
                                dialog.show();

                            }
                        });


                        // add to TableRow
                        Row.addView(imageView);
                        Row.addView(descriptionTextView);
                        Row.addView(genderTextView);
                        Row.addView(typeTextView);
                        Row.addView(situationTextView);
                        Row.addView(priceTextView);
                        tableLayout.addView(Row);
                        invisRow.addView(invisImageView);
                        tableLayout.addView(invisRow);
                        rowList.add(Row);

                    }



            }
        });

    }
}