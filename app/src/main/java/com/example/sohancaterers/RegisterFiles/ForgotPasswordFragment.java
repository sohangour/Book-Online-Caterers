package com.example.sohancaterers.RegisterFiles;


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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sohancaterers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {

    private Button forgot;
    private TextView emailid, goback;
    private FirebaseAuth firebaseAuth;
    private FrameLayout backgroundforgot;
    private LinearLayout emailback;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgotpassword, container, false);
        forgot = view.findViewById(R.id.forgot);
        emailid = view.findViewById(R.id.emailid);
        firebaseAuth = FirebaseAuth.getInstance();
        backgroundforgot = view.findViewById(R.id.backgroundforgot);
        goback = view.findViewById(R.id.goback);
        emailback = view.findViewById(R.id.emailgone);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailid.addTextChangedListener(new TextWatcher() {
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
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right)
                        .replace(R.id.signupin_framelayout, new SignInFragment())
                        .commit();
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();

            }
        });
        backgroundforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedView();

            }
        });
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(emailid.getText())) {

            forgot.setEnabled(true);
            forgot.setTextColor(Color.rgb(255, 255, 255));

        } else {
            forgot.setEnabled(false);
            forgot.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void checkEmailAndPassword() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailid.getText().toString().matches(emailPattern)) {

            forgot.setEnabled(false);
            forgot.setTextColor(Color.argb(50, 255, 255, 255));
            sendLink();


        } else {
            // error.setAlpha(1);
            emailid.setError("Invalid Email");
        }
    }

    public void sendLink() {
        String email = emailid.getText().toString().trim();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            emailback.setVisibility(View.VISIBLE);
                           /* getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left)
                                    .replace(R.id.signupin_framelayout, new SignInFragment()).commit();*/
                           emailid.setText("");
                           emailid.setFocusable(false);
                            Toast.makeText(getContext(), "Link send at registerd email id successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            forgot.setEnabled(true);
                            forgot.setTextColor(Color.rgb(255, 255, 255));
                            Toast.makeText(getContext(), "Email Doesn't Exits Failed ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void focusedView() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getActivity().getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }


}
