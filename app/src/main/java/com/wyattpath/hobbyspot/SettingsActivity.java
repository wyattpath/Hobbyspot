package com.wyattpath.hobbyspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private EditText mNameField, mPhoneField, mAgeField, mLocationField, mDescriptionField, mHobbyField;

    private Button mBack, mConfirm;

    private ImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId, name, phone, profileImageUrl, userSex;
    private String[] randomLocation = {"Hamburg-Mitte", "Altona", "Eimsbüttel", "Hamburg-Nord", "Wandsbek", "Bergedorf", "Harburg"};
    private String[] randomPics = {
            "https://i.pinimg.com/originals/08/61/b7/0861b76ad6e3b156c2b9d61feb6af864.jpg",
            "https://i.imgur.com/dOx2wRl.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSpV2udHUjjVHp610e6nrtsLU-NK9XGd89lk76Ml3EeV5Bz0lAJQA&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbz5gmggoERzWj4QCNAUhUhm71eyQhgquZaGerfWgaOdE4Bh9fdw&s",
            "https://media.wired.com/photos/5cdefc28b2569892c06b2ae4/master/w_2560%2Cc_limit/Culture-Grumpy-Cat-487386121-2.jpg",
            "https://i.kym-cdn.com/entries/icons/original/000/014/285/sideeyechloe.jpg",
            "https://img.bleacherreport.net/img/images/photos/002/625/710/o-SEA-OTTER-BASKETBALL-facebook_crop_exact.jpg?w=1200&h=1200&q=75",
            "https://www.sportvideos.tv/wp-content/uploads/2017/11/uDQ-xAzcWo.jpg",
            "https://i.pinimg.com/236x/0a/a4/a7/0aa4a7d89f0c081b344f959e65958562--golf-humor-sport-humor.jpg",
            "https://i.ytimg.com/vi/9pTiPlcSp_Q/hqdefault.jpg",
            "https://www.punchline-gloucester.com/images/user/11041_bulldog.jpg",
            "https://i.pinimg.com/236x/bb/1c/c2/bb1cc2c07bff46d042fc5639da357246--sports-basketball-teddy-bears.jpg",
            "https://acegif.com/wp-content/uploads/funny-faces-42-gap.jpg"
    };
    private String[] randomHobbies = {
            "Samurai Schwerter sammeln", "LARPing", "Hacking", "Hexenkunst", "Zeitreisen",
            "Arbeiten", "Online mit Leuten streiten", "Celebrities stalken", "Awesome sein",
            "Schlafen", "Ich habe 14 Katzen", "Cosplay", "Blut spenden", "Netflix und chill",
            "Scrabble", "Tod spielen", "Frösche sezieren", "Essen", "Weltraum", "Batman sein", "GAMIIIIIIIIIING",
            "Knoten", "Das willst du nicht wissen...", "Extrem bügeln", "Ich mag nichts tun"};

    private Uri resultUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mNameField = (EditText) findViewById(R.id.name);
        mPhoneField = (EditText) findViewById(R.id.phone);
        mAgeField = (EditText) findViewById(R.id.age);
        mDescriptionField = (EditText) findViewById(R.id.description);
        mLocationField = (EditText) findViewById(R.id.location);
        mHobbyField = (EditText) findViewById(R.id.hobby);
        // Fake data
        mAgeField.setText(String.format("%s", Integer.toString((int) Math.floor(Math.random() * 100))));
        mLocationField.setText(String.format("%s", randomLocation[(int) Math.floor(Math.random() * randomLocation.length)]));
        mDescriptionField.setText(String.format("%s", "Ich brauche Freunde :("));
        mHobbyField.setText(String.format("%s", randomHobbies[(int) Math.floor(Math.random() * randomHobbies.length)]));

        mProfileImage = (ImageView) findViewById(R.id.profileImage);

        mBack = (Button) findViewById(R.id.back);
        mConfirm = (Button) findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();
        mProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        mConfirm.setOnClickListener(v -> saveUserInformation());
        mBack.setOnClickListener(v -> finish());
    }

    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        name = map.get("name").toString();
                        mNameField.setText(name);
                    }
                    if (map.get("phone") != null) {
                        phone = map.get("phone").toString();
                        mPhoneField.setText(phone);
                    }
                    if (map.get("sex") != null) {
                        userSex = map.get("sex").toString();
                    }
                    if (map.get("profileImageUrl") != null) {
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl) {
                            case "default":
                                Glide.with(getApplication()).load(randomPics[(int) Math.floor(Math.random() * randomPics.length)]).placeholder(R.mipmap.ic_launcher).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInformation() {
        name = mNameField.getText().toString();
        phone = mPhoneField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        mUserDatabase.updateChildren(userInfo);
        if (resultUri != null) {
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);
            uploadTask.addOnFailureListener(e -> finish());
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map userInfo = new HashMap();
                            userInfo.put("profileImageUrl", uri.toString());
                            mUserDatabase.updateChildren(userInfo);

                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finish();
                            return;
                        }
                    });
                }
            });
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);

        }
    }
}
