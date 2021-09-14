package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityLoginBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.showHideButton.setOnClickListener(v -> showHideChanger());
        binding.loginButton.setOnClickListener(v -> {
            if (isEveryThingValid()){
                showProgressDialog("Logging you In.....");
                getAuth().signInWithEmailAndPassword(
                        binding.loginEmailEditText.getText().toString(),
                        binding.loginPasswordEditText.getText().toString()
                ).addOnSuccessListener(authResult -> {
                    hideProgressDialog();
                    savePreferenceString(Constants.userEmail, binding.loginEmailEditText.getText().toString());
                    if (getPreferenceString(Constants.userEmail).equals(Constants.adminMail)){
                        startActivity(new Intent(LoginActivity.this, AddItem.class));
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    hideProgressDialog();
                    if (e instanceof FirebaseAuthInvalidUserException){
                        binding.loginEmailEditText.requestFocus();
                        binding.loginEmailEditText.setError("Email isn't registered");
                    }else if (e instanceof FirebaseAuthInvalidCredentialsException){
                        binding.loginPasswordEditText.requestFocus();
                        binding.loginPasswordEditText.setError("Provided password is wrong");
                    }
                });
            }
        });
        binding.loginClickToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterScreen.class));
            finish();
        });
    }

    private Boolean isEveryThingValid() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.loginEmailEditText.getText().toString())
                .matches()
        ) {
            binding.loginEmailEditText.requestFocus();
            binding.loginEmailEditText.setError("Please enter a valid email address");
            return false;
        } else if (binding.loginPasswordEditText.getText().toString().length() < 6) {
            binding.loginPasswordEditText.requestFocus();
            binding.loginPasswordEditText.setError("Password must be at least 6 characters long");
            return false;
        } else {
            return true;
        }
    }

    private void showHideChanger() {
        if (binding.loginPasswordEditText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            binding.loginPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.showHideButton.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        } else {
            binding.loginPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.showHideButton.setImageResource(R.drawable.ic_baseline_visibility_24);
        }
        binding.loginPasswordEditText.setSelection(binding.loginPasswordEditText.length());
    }

}