package trungndph39729.fpoly.lab5.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import trungndph39729.fpoly.lab5.handle.OnDistributorInteractionListener;
import trungndph39729.fpoly.lab5.R;
import trungndph39729.fpoly.lab5.Service.HttpRequest;
import trungndph39729.fpoly.lab5.adapter.DistributorAdapter;
import trungndph39729.fpoly.lab5.databinding.ActivityMainBinding;
import trungndph39729.fpoly.lab5.model.Distributor;
import trungndph39729.fpoly.lab5.model.Response;

public class MainActivity extends AppCompatActivity implements OnDistributorInteractionListener {
    private HttpRequest httpRequest;

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        httpRequest = new HttpRequest();
        httpRequest.callAPI()
                .getListDistributor()
                .enqueue(getDistributorAPI);
        binding.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    String key = binding.edSearch.getText().toString();

                    httpRequest.callAPI()
                            .searchDistributor(key)
                            .enqueue(getDistributorAPI);

                    return true;
                }
                return false;
            }
        });


        binding.addFab.setOnClickListener(view -> {
            addDis();
        });

    }

    @Override
    public void onDeleteDistributor(String distributorId) {
        // Gọi API để xóa distributor dựa trên id
        httpRequest.callAPI()
                .deleteDistributorById(distributorId)
                .enqueue(responseDistributorAPI);
    }

    @Override
    public  void  onUpdateDistributor(String id ,Distributor distributor){
        httpRequest.callAPI()
                .updateDistributorById(id,distributor)
                .enqueue(responseDistributorAPI);
    }
    private void addDis(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add);
        EditText editText = (EditText) dialog.findViewById(R.id.edName);
        Button button = (Button) dialog.findViewById(R.id.addBtn);
        button.setOnClickListener(view -> {
            String name = editText.getText().toString();
            Distributor distributor = new Distributor();
            distributor.setName(name);
            httpRequest.callAPI()
                    .addDistributor(distributor)
                    .enqueue(responseDistributorAPI);
            dialog.dismiss();
        });


        dialog.show();

    }

    private void getData(ArrayList<Distributor> list){
        binding.recycleDistributor.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.recycleDistributor.setAdapter(new DistributorAdapter(list,this,this));
    }

    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        ArrayList<Distributor> ds = response.body().getData();
                        getData(ds);
                        Toast.makeText(MainActivity.this, ""+response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                    }
                }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            Log.d("ZZZZZZZZZZ", "onFailure: "+t.getMessage());
        }
    };

    Callback<Response<Distributor>>  responseDistributorAPI =  new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        httpRequest.callAPI()
                                .getListDistributor()
                                .enqueue(getDistributorAPI);
                        Toast.makeText(MainActivity.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                    }
                }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {
            Log.d("ZZZZZZZZZZ", "onFailure: "+t.getMessage());

        }
    };
}