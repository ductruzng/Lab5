package trungndph39729.fpoly.lab5.Activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import trungndph39729.fpoly.lab5.R;
import trungndph39729.fpoly.lab5.Service.HttpRequest;
import trungndph39729.fpoly.lab5.databinding.ActivityAddFruitBinding;
import trungndph39729.fpoly.lab5.model.Distributor;
import trungndph39729.fpoly.lab5.model.Response;

public class AddFruitActivity extends AppCompatActivity {
    private HttpRequest httpRequest;
    private ActivityAddFruitBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityAddFruitBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());
       changeStatusBarColor();

       httpRequest = new HttpRequest();
       httpRequest.callAPI().getListDistributor().enqueue(getDistributorAPI);

    }
    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    ArrayList<Distributor> ds = response.body().getData();
                    ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                    for (int i = 0; i < ds.size(); i++) {

                        // creating an Object of HashMap class
                        HashMap<String, Object> map = new HashMap<>();

                        // Data entry in HashMap
                        map.put("distributorId", ds.get(i).getId());
                        map.put("distributorName", ds.get(i).getName());

                        // adding the HashMap to the ArrayList
                        list.add(map);
                    }
                    String[] from = {"distributorId","distributorId"};

                    int [] to = {R.id.distributorId,R.id.distributorName};
                    binding.spinnerDistributor.setAdapter(new SimpleAdapter(getApplicationContext(), list,R.layout.spinner_distributor,from,to));

                    Toast.makeText(AddFruitActivity.this, ""+response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            Log.d("ZZZZZZZZZZ", "onFailure: "+t.getMessage());
        }
    };
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
}