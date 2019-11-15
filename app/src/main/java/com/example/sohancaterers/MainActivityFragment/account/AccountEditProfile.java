package com.example.sohancaterers.MainActivityFragment.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sohancaterers.R;
import com.example.sohancaterers.RegisterFiles.SignUpIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountEditProfile extends AppCompatActivity implements View.OnClickListener {

    private Button update;
    private TextView change_image;
    private MaterialEditText update_full_name, update_email, update_contact, password;

    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private ProgressDialog progressDialog;
    private String fullname, contact, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);


        change_image = findViewById(R.id.account_image_change);
        update_full_name = findViewById(R.id.update_full_name);
        update_email = findViewById(R.id.update_user_email);
        update_contact = findViewById(R.id.update_user_contact);
        password = findViewById(R.id.password);
        update = findViewById(R.id.update_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);


        documentReference = firebaseFirestore.collection("UserDetail").document(user.getEmail());


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    fullname = documentSnapshot.getString("Name");
                    contact = documentSnapshot.getString("Contact");
                    email = documentSnapshot.getString("Email");
                    update_full_name.setText(fullname.toUpperCase());
                    update_contact.setText(contact);
                    update_email.setText(email);

                }
            }
        });

        change_image.setOnClickListener(this);

        update.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View v) {
        if (change_image == v) {
            CropImage.activity()
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(AccountEditProfile.this);
        }
        if (update == v) {
           updateProfile();
        }
    }

    private void updateProfile() {
        final String fname = update_full_name.getText().toString();
        final String phone = update_contact.getText().toString();
        final String getemail = update_email.getText().toString();
        String pass = password.getText().toString();
        if (fname.isEmpty()) {
            update_full_name.setError("Enter Name ");
            update_full_name.requestFocus();
            return;
        }
        if (getemail.isEmpty()) {
            update_email.setError("Enter Email ");
            update_email.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            update_contact.setError("Enter Contact Number ");
            update_contact.requestFocus();
            return;
        }
        if (phone.length() != 10) {
            update_contact.setError("Mobile number must be 10 digit ");
            update_contact.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            password.setError("Enter password");
            password.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            password.setError("Minimum 6 character ");
            password.requestFocus();
            return;
        }
        if (fname.equals(fullname.toUpperCase()) && getemail.equals(email) && phone.equals(contact)) {
            Toast.makeText(this, "No changes Found", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Updating.......");
        progressDialog.show();

        AuthCredential authCredential = EmailAuthProvider
                .getCredential(user.getEmail(), password.getText().toString());
        user.reauthenticate(authCredential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){

                              if(getemail.equals(email)){

                                  documentReference.update("Name", fname);
                                  documentReference.update("Contact",phone);
                                  progressDialog.dismiss();
                                  Toast.makeText(AccountEditProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                                  finish();
                              }
                              else{
                                  user.updateEmail(getemail)
                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  if(task.isSuccessful()){
                                                      FirebaseUser newuser=FirebaseAuth.getInstance().getCurrentUser();
                                                      String newuseremail=newuser.getEmail();
                                                      documentReference.delete();
                                                      DocumentReference updateref=firebaseFirestore.collection("UserDetail")
                                                              .document(newuseremail);
                                                      Map<String, Object> note = new HashMap<>();
                                                      note.put("Email", newuseremail);
                                                      note.put("Name", fullname);
                                                      note.put("Contact", contact);
                                                      updateref.set(note);
                                                      progressDialog.dismiss();
                                                      Toast.makeText(AccountEditProfile.this, newuseremail, Toast.LENGTH_SHORT).show();
                                                      // Logout From Firebase
                                                      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                                      firebaseAuth.signOut();
                                                      finish();
                                                      Intent intent = new Intent(AccountEditProfile.this, SignUpIn.class);
                                                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                      startActivity(intent);


                                                  }
                                              }
                                          })
                                          .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  progressDialog.dismiss();
                                                  Toast.makeText(AccountEditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                              }
                                          });
                              }//else inner




                          } // isSuccessful outer
                    } //on complete task outer
                }) //on successlistern outer
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AccountEditProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }); //failure listener outer
    } //update profile close
}
