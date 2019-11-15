package com.example.sohancaterers.RegisterFiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sohancaterers.MainActivity;
import com.example.sohancaterers.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpIn extends AppCompatActivity {
//private FrameLayout frameLayout;

private  FirebaseAuth firebaseAuth;
public static boolean onForgotPasswordFragment =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupin);
       firebaseAuth=FirebaseAuth.getInstance();

        setFragment(new SignInFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.signupin_framelayout,fragment);
        fragmentTransaction.commit();
    }

  @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK)
        {
            if (onForgotPasswordFragment)
            {
                onForgotPasswordFragment=false;
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right)
                        .replace(R.id.signupin_framelayout, new SignInFragment())
                        .commit();

                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
