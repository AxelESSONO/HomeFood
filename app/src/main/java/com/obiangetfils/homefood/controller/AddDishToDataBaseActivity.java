package com.obiangetfils.homefood.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.obiangetfils.homefood.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class AddDishToDataBaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Context mContext;
    private ArrayList<String> categoryDishList = new ArrayList<>();
    private SearchableSpinner spinner;
    private String nameCategory;
    private EditText dishNameEdt, dishDescriptionEdt, dishPriceEdt;
    private ImageView dishImage;
    private Button addToDatabase;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish_to_data_base);

        nameCategory = "Ajout d'un nouveau plat";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(nameCategory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("menus");

        mContext = this;
        AddDataToArrayList();
        findViews();

        dishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        addToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField();
            }
        });

    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    private void checkField() {

        String dishName, dishDescription, dishPrice, dishCategory, dishUri;
        dishName = dishNameEdt.getText().toString();
        dishDescription = dishDescriptionEdt.getText().toString();
        dishPrice = dishPriceEdt.getText().toString();
        dishCategory = spinner.getSelectedItem().toString();
        dishUri = uri.getPath();

        //Check if field is not empty
        if (TextUtils.isEmpty(dishName) ||
                TextUtils.isEmpty(dishDescription) ||
                TextUtils.isEmpty(dishPrice) ||
                TextUtils.isEmpty(dishCategory) || (dishUri == null)) {
            Toasty.error(mContext, "Tous les champs sont obligatoirs",
                    Toast.LENGTH_SHORT, true).show();
        } else {

            SpotsDialog waitinDialog = (SpotsDialog) new SpotsDialog.Builder()
                    .setContext(mContext)
                    .setMessage("Téléchargement en cours d'exécution")
                    .setCancelable(false)
                    .build();
            waitinDialog.show();
            saveData(dishName, dishDescription, dishPrice, dishCategory, dishUri, waitinDialog);
        }

    }

    private void saveData(String dishName, String dishDescription, String dishPrice, String dishCategory, String dishUri, SpotsDialog waitinDialog) {
        uploadImage(dishName, dishDescription, dishPrice, dishCategory, dishUri, waitinDialog);
    }

    private void AddDataToArrayList() {
        categoryDishList.add("Entrée");
        categoryDishList.add("Desserts");
        categoryDishList.add("Résistance");
        categoryDishList.add("Sandwich");
        categoryDishList.add("Glace");
        categoryDishList.add("Jus et Fruit");

    }

    private void findViews() {
        dishNameEdt = (EditText) findViewById(R.id.dish_name_data);
        dishDescriptionEdt = (EditText) findViewById(R.id.dish_description_data);
        dishPriceEdt = (EditText) findViewById(R.id.dish_price_data);
        addToDatabase = (Button) findViewById(R.id.add_to_database);
        dishImage = (ImageView) findViewById(R.id.image_dish_data);

        spinner = (SearchableSpinner) findViewById(R.id.spinner);
        //For set Title to Spinner
        spinner.setTitle("Choisir une catégorie");
        setDataToAdapter(categoryDishList);
    }

    private void setDataToAdapter(ArrayList<String> arrayList) {
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayList);
        // Specify layout to be used when list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toasty.warning(mContext, "Aucune catégorie sélectionnée!!", Toast.LENGTH_SHORT, true).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                dishImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(final String dishName,
                             final String dishDescription,
                             final String dishPrice,
                             final String dishCategory,
                             final String dishUri,
                             final SpotsDialog waitinDialog) {
        if (uri != null) {
            final String dishKey = UUID.randomUUID().toString();
            // Defining the child of storageReference
            final StorageReference ref = storageReference.child(dishCategory + "/" + dishKey);

            // adding listeners on upload
            // or failure of image
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uriStorage) {
                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            reference.child("menus").child(dishCategory).child(dishKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshotDish) {

                                    if (!dataSnapshotDish.child("menus").child(dishCategory).child(dishKey).exists()) {

                                        HashMap<String, Object> dishHashMap = new HashMap<>();
                                        dishHashMap.put("dishKey", dishKey);
                                        dishHashMap.put("dishName", dishName);
                                        dishHashMap.put("dishDescription", dishDescription);
                                        dishHashMap.put("dishPrice", dishPrice);
                                        dishHashMap.put("dishCategory", dishCategory);
                                        dishHashMap.put("dishUri", uriStorage.toString());

                                        reference.child("menus").child(dishCategory).child(dishKey)
                                                .updateChildren(dishHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                waitinDialog.dismiss();
                                                Toasty.success(mContext, "Plat ajouté avec succès!!", Toast.LENGTH_SHORT, true).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Error, Image not uploaded
                                    waitinDialog.dismiss();
                                    Toasty.error(mContext, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT, true).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    waitinDialog.dismiss();
                    Toasty.error(mContext, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }
}