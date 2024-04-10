package trungndph39729.fpoly.lab5.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import trungndph39729.fpoly.lab5.R;
import trungndph39729.fpoly.lab5.Service.HttpRequest;
import trungndph39729.fpoly.lab5.databinding.ActivityLoginBinding;
import trungndph39729.fpoly.lab5.databinding.ActivityMainBinding;
import trungndph39729.fpoly.lab5.model.Response;
import trungndph39729.fpoly.lab5.model.User;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        httpRequest = new HttpRequest();
        changeStatusBarColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        binding.tvForgot.setOnClickListener(view -> {


        });

        binding.cirLoginButton.setOnClickListener(view -> {
            User user = new User();
            String _username = binding.editTextUsername.getText().toString();
            String _password = binding.editTextPassword.getText().toString();
            if (_username.isEmpty() || _password.isEmpty()){
                Toast.makeText(this, "No be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            user.setUsername(_username);
            user.setPassword(_password);
            httpRequest.callAPI().login(user).enqueue(responseUser);

        });


    }

    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(LoginActivity.this, "Login Successfully !", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("INFO", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("refreshToken", response.body().getRefreshToken());
                    editor.putString("id", response.body().getData().get_id());
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));


                } else {
                    Toast.makeText(LoginActivity.this, "Wrong username or password  !", Toast.LENGTH_SHORT).show();

                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Toast.makeText(LoginActivity.this, "Wrong username or password  !", Toast.LENGTH_SHORT).show();

            Log.d(">>>> Login","on Failure: "+ t.getMessage());
        }
    };

    public void onLoginClick(View View) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }
    }


}