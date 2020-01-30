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

    private TextView mNameField, mPhoneField, mAgeField, mLocationField, mDescriptionField;

    private Button mBack;

    private ImageView mProfileImage;

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
        mAgeField = (TextView) findViewById(R.id.age);
        mDescriptionField = (TextView) findViewById(R.id.description);
        mLocationField = (TextView) findViewById(R.id.location);
        // Fake data
        mAgeField.setText(String.format("%s", Integer.toString((int) Math.floor(Math.random() * 100))));
        mLocationField.setText(String.format("%s", randomLocation[(int) Math.floor(Math.random() * randomLocation.length)]));
        mDescriptionField.setText(String.format("%s", randomDescription[(int) Math.floor(Math.random() * randomDescription.length)]));

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
                    }
                    if (map.get("profileImageUrl") != null) {
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl) {
                            case "default":
                                Glide.with(getApplication()).load("https://i.pinimg.com/originals/08/61/b7/0861b76ad6e3b156c2b9d61feb6af864.jpg").placeholder(R.mipmap.ic_launcher).into(mProfileImage);
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
