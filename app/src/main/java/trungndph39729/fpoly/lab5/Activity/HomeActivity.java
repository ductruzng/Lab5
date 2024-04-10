package trungndph39729.fpoly.lab5.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import trungndph39729.fpoly.lab5.R;
import trungndph39729.fpoly.lab5.Service.HttpRequest;
import trungndph39729.fpoly.lab5.adapter.PopularAdapter;
import trungndph39729.fpoly.lab5.databinding.ActivityHomeBinding;
import trungndph39729.fpoly.lab5.databinding.ActivityLoginBinding;
import trungndph39729.fpoly.lab5.model.Fruit;
import trungndph39729.fpoly.lab5.model.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        httpRequest = new HttpRequest();
        sharedPreferences = getSharedPreferences("INFO", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        httpRequest.callAPI().getListFruit("Bearer "+token).enqueue(getListFruitResponse);

        binding.fabAdd.setOnClickListener(view -> startActivity(new Intent(this,AddFruitActivity.class)));
    }

    retrofit2.Callback<Response<ArrayList<Fruit>>> getListFruitResponse = new Callback<Response<ArrayList<Fruit>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    ArrayList<Fruit> ds = response.body().getData();

                    getData(ds);

                    Toast.makeText(HomeActivity.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {
            Log.d(">>>> getListFruit", "onFailure: " + t.getMessage());
        }
    };

    private void getData(ArrayList<Fruit> ds) {
        binding.recycleViewFruit.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recycleViewFruit.setAdapter(new PopularAdapter(ds));
    }
}