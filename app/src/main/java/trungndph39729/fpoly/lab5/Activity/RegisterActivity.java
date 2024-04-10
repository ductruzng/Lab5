package trungndph39729.fpoly.lab5.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import trungndph39729.fpoly.lab5.R;
import trungndph39729.fpoly.lab5.Service.HttpRequest;
import trungndph39729.fpoly.lab5.databinding.ActivityResignBinding;
import trungndph39729.fpoly.lab5.model.Response;
import trungndph39729.fpoly.lab5.model.User;

public class RegisterActivity extends AppCompatActivity {
    private ActivityResignBinding binding;

    File file;
    private HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        changeStatusBarColor();
        httpRequest = new HttpRequest();

        binding.cirRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"), binding.editTextEmail.getText().toString());
                RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"), binding.editTextPassword.getText().toString());
                RequestBody _username = RequestBody.create(MediaType.parse("multipart/form-data"), binding.editTextUsername.getText().toString());
                RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), binding.editTextName.getText().toString());
                MultipartBody.Part multipartBody;
                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                } else {
                    multipartBody = null;
                }

                httpRequest.callAPI().register(_username, _password, _email, _name, multipartBody).enqueue(responseUser);


            }
        });

        binding.avatar.setOnClickListener(view -> chooseImage());

    }

    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(RegisterActivity.this, "Register successfully !", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Log.d(">>>> GetList", "onFailure: " + t.getMessage());
        }
    };

    private void chooseImage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImage.launch(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Uri imagePath = data.getData();
                        file = createFileFromUri(imagePath, "avatar");

                        Glide.with(RegisterActivity.this)
                                .load(file)
                                .thumbnail(Glide.with(RegisterActivity.this).load(R.mipmap.ic_launcher))
                                .centerCrop()
                                .circleCrop()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(binding.avatar);
                    }
                }
            });

    private File createFileFromUri(Uri path, String name) {
        File _file = new File(RegisterActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = RegisterActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));

    }

}
