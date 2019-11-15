package com.example.sohancaterers.MainActivityFragment.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sohancaterers.MainActivity;
import com.example.sohancaterers.R;
import com.example.sohancaterers.RegisterFiles.SignInFragment;
import com.example.sohancaterers.RegisterFiles.SignUpFragment;
import com.example.sohancaterers.RegisterFiles.SignUpIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AccountFragment extends Fragment {

    private NavigationView account_navigation;
    private ImageView account_userimage;
    private TextView account_useremail, account_username, account_usercontact;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        account_navigation = view.findViewById(R.id.account_navigation);
        account_userimage = view.findViewById(R.id.account_userimage);
        account_useremail = view.findViewById(R.id.account_useremail);
        account_username = view.findViewById(R.id.account_username);
        account_usercontact = view.findViewById(R.id.account_usercontact);

        Menu menuNav = account_navigation.getMenu();
        MenuItem signin = menuNav.findItem(R.id.account_signin);
        MenuItem signout = menuNav.findItem(R.id.account_signout);
        MenuItem editprofile=menuNav.findItem(R.id.account_edit_profile);

        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        if (user1 == null) {
            editprofile.setVisible(false);
            signout.setVisible(false);
            signin.setVisible(true);

        } else {
            editprofile.setVisible(true);
            signin.setVisible(false);
            signout.setVisible(true);

        }

        account_navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.account_signout:
                        firebaseAuth.signOut();
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), SignUpIn.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.account_signin:
                        Intent intent1 = new Intent(getActivity(), SignUpIn.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        getActivity().finish();
                        return true;
                    case R.id.account_contact_us:
                        startActivity(new Intent(getContext(),ContactUsActivity.class));
                        return true;
                    case R.id.account_edit_profile:
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        if(user==null){
                            Toast.makeText(getContext(), "Please LogIn First", Toast.LENGTH_SHORT).show();
                        }
                        else
                        startActivity(new Intent(getContext(), AccountEditProfile.class));

                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        try {
            if (user != null) {
                documentReference = firebaseFirestore.collection("UserDetail").document(user.getEmail());
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "Error in Loading", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("Name");
                            String email = documentSnapshot.getString("Email");
                            String contact = documentSnapshot.getString("Contact");
                            account_username.setText(username.toUpperCase());
                            account_useremail.setText(email);
                            account_usercontact.setText(contact);

                        }
                    }
                });

            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }//on start


}