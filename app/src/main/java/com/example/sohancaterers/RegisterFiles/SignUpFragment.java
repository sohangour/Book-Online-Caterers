package com.example.sohancaterers.RegisterFiles;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sohancaterers.MainActivity;
import com.example.sohancaterers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment {
    private Button signup;
    private MaterialEditText emailName, fullName, phoneNumber, password, cpassword;
    private TextView signin, logo;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private LinearLayout backgroudnested;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        //findid from xml
        signup = view.findViewById(R.id.signup);
        signin = view.findViewById(R.id.signin);
        emailName = view.findViewById(R.id.emailName);
        fullName = view.findViewById(R.id.fullName);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        password = view.findViewById(R.id.password);
        cpassword = view.findViewById(R.id.cpassword);
        logo = view.findViewById(R.id.logo);
        backgroudnested = view.findViewById(R.id.backgroudnested);
        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ontext change listener

        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        emailName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//onclick listener

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();

            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right)
                        .replace(R.id.signupin_framelayout, new SignInFragment())
                        .commit();
            }
        });
        backgroudnested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedView();

            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedView();

            }
        });

    }


    private void registeruser() {

        final String email = emailName.getText().toString().trim();
        final String fullname = fullName.getText().toString().trim();
        final String phonenumber = phoneNumber.getText().toString().trim();
        String pass = password.getText().toString().trim();
        progressDialog.setMessage("Registering user......");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> map = new HashMap<>();
                            map.put("Email", email);
                            map.put("Name", fullname);
                            map.put("Contact", phonenumber);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String emailName = user.getEmail();
                            firebaseFirestore = FirebaseFirestore.getInstance();
                            DocumentReference documentReference = firebaseFirestore.collection("UserDetail").document(emailName);
                            documentReference.set(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getActivity(), MainActivity.class));
                                                getActivity().finish();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    } //oncomplete listener
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                signup.setEnabled(true);
                signup.setTextColor(Color.rgb(255, 255, 255));
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }); //add on complete listener

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(fullName.getText().toString().trim())) {
            if (!TextUtils.isEmpty(phoneNumber.getText()) && phoneNumber.length() == 10) {
                if (!TextUtils.isEmpty(emailName.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(password.getText()) && password.length() >= 6) {
                        if (!TextUtils.isEmpty(cpassword.getText()) && cpassword.length() >= 6) {
                            signup.setEnabled(true);
                            signup.setTextColor(Color.rgb(255, 255, 255));
                        } else {
                            signup.setEnabled(false);
                            signup.setTextColor(Color.argb(50, 255, 255, 255));
                        }
                    } else {
                        signup.setEnabled(false);
                        signup.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    signup.setEnabled(false);
                    signup.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                signup.setEnabled(false);
                signup.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            signup.setEnabled(false);
            signup.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void checkEmailAndPassword() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailName.getText().toString().matches(emailPattern)) {
            if (password.getText().toString().equals(cpassword.getText().toString())) {
                signup.setEnabled(false);
                signup.setTextColor(Color.argb(50, 255, 255, 255));
                registeruser();
            } else {

                cpassword.setError("Passsword does not match!");
            }
        } else {
            emailName.setError("Invalid Email");
        }
    }

    private void focusedView() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getActivity().getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }
}//main class
