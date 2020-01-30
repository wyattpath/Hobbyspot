package com.wyattpath.hobbyspot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView mNameField, mPhoneField, mAgeField, mSexField, mHobbyField, mLocationField, mDescriptionField;

    private Button mBack;

    private ImageView mProfileImage;
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

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId, name, phone, profileImageUrl, userSex;
    private String[] randomLocation = {"Hamburg-Mitte", "Altona", "Eimsbüttel", "Hamburg-Nord", "Wandsbek", "Bergedorf", "Harburg"};

    private String[] randomDescription = {
            "Eigentlich wollte ich die Welt erobern ... Aber es regnet!",
            "Bei jeder Streitfrage gibt es zwei Standpunkte: meinen und den falschen.",
            "Ich lese keine Anleitungen. Ich drücke Knöpfe, bis es klappt.",
            "Wenn ich Dir jetzt Recht gebe, liegen wir beide falsch.",
            "Ich bin freiwillig hier.",
            "Wenn ich Du wäre, wäre ich gerne ich.",
            "Meine Nachbarn hören gute Musik – ob sie wollen oder nicht.",
            "Was passiert, wenn man Cola und Bier gleichzeitig trinkt? Man colabiert.",
            "Ich brauche Freunde :(",
            "Ich bin athletisch. Ich surfe das Web stundenlang.",
            "Wenn ich jemanden treffe, mache ich Schuhkontakt bevor Augenkontakt.",
            "Manchmal brauche ich den ganzen Tag um nichts zu machen.",
            "Ich bin nicht faul. Ich spare nur Energie.",
            "Ich bin von Natur aus witzig, weil mein Leben ein Witz ist :'D",
            "Bleib ruhig und ignoriere mich.",
            "Ich glaube Scheitern sollte auch eine Option sein.",
            "Ich habe so viel gelernt von meinen Fehlern. Ich glaub ich mach noch mehr."
    };

    private Uri resultUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mNameField = (TextView) findViewById(R.id.name);
        mPhoneField = (TextView) findViewById(R.id.phone);
        mSexField = (TextView) findViewById(R.id.sex);
        mAgeField = (TextView) findViewById(R.id.age);
        mHobbyField = (TextView) findViewById(R.id.hobby);
        mDescriptionField = (TextView) findViewById(R.id.description);
        mLocationField = (TextView) findViewById(R.id.location);
        // Fake data
        mAgeField.setText(String.format("%s", Integer.toString((int) Math.floor(Math.random() * 100))));
        mLocationField.setText(String.format("%s", randomLocation[(int) Math.floor(Math.random() * randomLocation.length)]));
        mDescriptionField.setText(String.format("%s", randomDescription[(int) Math.floor(Math.random() * randomDescription.length)]));
        mHobbyField.setText(String.format("%s", randomHobbies[(int) Math.floor(Math.random() * randomHobbies.length)]));

        mProfileImage = (ImageView) findViewById(R.id.profileImage);

        mBack = (Button) findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance();
        userId = getIntent().getExtras().getString("userId");

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();
        mProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

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
                        mSexField.setText(userSex.equals("Male") ? "Männlich" : "Weiblich");
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
