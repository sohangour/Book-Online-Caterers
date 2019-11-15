package com.example.sohancaterers.RegisterFiles;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.sohancaterers.MainActivity;
import com.example.sohancaterers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.example.sohancaterers.App.LOGIN;
import static com.example.sohancaterers.RegisterFiles.SignUpIn.onForgotPasswordFragment;


public class SignInFragment extends Fragment {

    private MaterialEditText emailidA, passwordA;
    private TextView signup, forgot, logo, skip_login;
    private Button signin;
    private String email, pass;
    private LinearLayout backgroud;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private NotificationManagerCompat notificationManagerCompat;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        notificationManagerCompat=NotificationManagerCompat.from(getContext());
        progressDialog = new ProgressDialog(getContext());
        //find id from xml
        signin = view.findViewById(R.id.signin);
        signup = view.findViewById(R.id.signup);
        emailidA = view.findViewById(R.id.emailid);
        passwordA = view.findViewById(R.id.password);
        forgot = view.findViewById(R.id.forgotPassword);
        backgroud = view.findViewById(R.id.background_id);
        logo = view.findViewById(R.id.logo);

        skip_login = view.findViewById(R.id.skip_login);
        skip_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //on text change listener
        emailidA.addTextChangedListener(new TextWatcher() {
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
        passwordA.addTextChangedListener(new TextWatcher() {
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
//on click listener
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();

            }


        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left)
                        .replace(R.id.signupin_framelayout, new SignUpFragment()).addToBackStack(null)
                        .commit();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onForgotPasswordFragment = true;
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left)
                        .replace(R.id.signupin_framelayout, new ForgotPasswordFragment())
                        .commit();
            }
        });

        backgroud.setOnClickListener(new View.OnClickListener() {
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

    private void checkInputs() {
        if (!TextUtils.isEmpty(emailidA.getText())) {
            if (!TextUtils.isEmpty(passwordA.getText()) && passwordA.length() >= 6) {
                signin.setEnabled(true);
                signin.setTextColor(Color.rgb(255, 255, 255));
            } else {
                signin.setEnabled(false);
                signin.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            signin.setEnabled(false);
            signin.setTextColor(Color.argb(50, 255, 255, 255));
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

    private void checkEmailAndPassword() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailidA.getText().toString().trim().matches(emailPattern)) {
            if (passwordA.getText().toString().length() >= 6) {

                signin.setEnabled(false);
                signin.setTextColor(Color.argb(50, 255, 255, 255));
                usersign();

            } else {
                // error.setAlpha(1);
                passwordA.setError("Passsword length is more than 6 characters!");
            }
        } else {
            // error.setAlpha(1);
            emailidA.setError("Invalid Email");
        }
    }


    public void usersign() {
        email = emailidA.getText().toString().trim();
        pass = passwordA.getText().toString();
        progressDialog.setMessage("Login......");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                    Notification notification=new NotificationCompat.Builder(getContext(),LOGIN)
                            .setSmallIcon(R.drawable.ic_check_black_24dp)
                            .setContentTitle("Welcome!")
                            .setContentText(email)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .build();
                    notificationManagerCompat.notify(1,notification);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        signin.setEnabled(true);
                        signin.setTextColor(Color.rgb(255, 255, 255));


                    }
                });
    }


}
