package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityRegisterScreenBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;

import java.util.HashMap;
import java.util.Objects;

public class RegisterScreen extends BaseActivity {
    private ActivityRegisterScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.registeredClickToLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterScreen.this, LoginActivity.class));
            finish();
        });
        binding.showHideButton.setOnClickListener(v -> {
            showHideChanger();
        });
        binding.registerButton.setOnClickListener(v -> {
            if (isEveryThingValid()){
                showProgressDialog("We are registering you.....");
                getAuth().createUserWithEmailAndPassword(
                        binding.registerEmailEditText.getText().toString(),
                        binding.registerPasswordEditText.getText().toString()
                ).addOnSuccessListener(authResult -> {
                    hideProgressDialog();
                    HashMap<String, String> userData = new HashMap<>();
                    userData.put(Constants.userName,binding.registerNameEditText.getText().toString());
                    userData.put(Constants.userEmail,binding.registerEmailEditText.getText().toString());
                    userData.put(Constants.userAddress,binding.registerAddressEditText.getText().toString());
                    getUsersReference().document(Objects.requireNonNull(getAuth().getUid())).set(userData).addOnSuccessListener(unused -> {
                        toast("Registration successful");
                        savePreferenceString(Constants.userEmail, binding.registerEmailEditText.getText().toString());
                        startActivity(new Intent(RegisterScreen.this, MainActivity.class));
                        finishAffinity();
                    }).addOnFailureListener(e -> toast("Failed to put data in firestore"));
                }).addOnFailureListener(e -> {
                    hideProgressDialog();
                   if (e instanceof FirebaseAuthUserCollisionException){
                       binding.registerEmailEditText.requestFocus();
                       binding.registerEmailEditText.setError("Email already exists");
                   }
                });
            }
        });
    }

    private void showHideChanger() {
        if (binding.registerPasswordEditText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            binding.registerPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.showHideButton.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        } else {
            binding.registerPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.showHideButton.setImageResource(R.drawable.ic_baseline_visibility_24);
        }
        binding.registerPasswordEditText.setSelection(binding.registerPasswordEditText.length());
    }

    private Boolean isEveryThingValid() {
        if (binding.registerNameEditText.getText().toString().isEmpty()) {
            binding.registerNameEditText.requestFocus();
            binding.registerNameEditText.setError("Please enter your name|");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.registerEmailEditText.getText().toString())
                .matches()
        ) {
            binding.registerEmailEditText.requestFocus();
            binding.registerEmailEditText.setError("Please enter a valid email address");
            return false;
        } else if (binding.registerPasswordEditText.getText().toString().length() < 6) {
            binding.registerPasswordEditText.requestFocus();
            binding.registerPasswordEditText.setError("Password must be at least 6 characters long");
            return false;
        } else if (binding.registerAddressEditText.getText().toString().length() < 20) {
            binding.registerAddressEditText.requestFocus();
            binding.registerAddressEditText.setError("Address is too short");
            return false;
        } else {
            return true;
        }
    }
}