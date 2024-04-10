package trungndph39729.fpoly.lab5.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import trungndph39729.fpoly.lab5.handle.OnDistributorInteractionListener;
import trungndph39729.fpoly.lab5.R;
import trungndph39729.fpoly.lab5.databinding.ViewholderDistributorBinding;
import trungndph39729.fpoly.lab5.model.Distributor;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.Viewholder> {
    ArrayList<Distributor> list;
    Context context;

    private OnDistributorInteractionListener listener;

    public DistributorAdapter(ArrayList<Distributor> list, Context context, OnDistributorInteractionListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DistributorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderDistributorBinding binding = ViewholderDistributorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorAdapter.Viewholder holder, int position) {

        holder.binding.idDistributorTxt.setText("ID: " + list.get(position).getId());
        holder.binding.nameDistributorTxt.setText("Name: " + (list.get(position).getName()));
        holder.binding.deleteBtn.setOnClickListener(view -> {
            deleteDis(list.get(position).getId());

        });

        holder.binding.editBtn.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_update);


            EditText editText = (EditText) dialog.findViewById(R.id.edName);
            editText.setText(list.get(position).getName());
            Button button = (Button) dialog.findViewById(R.id.addBtn);
            button.setOnClickListener(view1 -> {
                String name = editText.getText().toString();
                Distributor distributor = list.get(position);
                distributor.setName(name);
                if (listener != null) {
                    listener.onUpdateDistributor(distributor.getId(),distributor);
                }
                dialog.dismiss();

            });


            dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderDistributorBinding binding;

        public Viewholder(ViewholderDistributorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void deleteDis(String id) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Ban co chac la muon xoa khong?.");
        builder1.setCancelable(true);
        String idP = id;
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            listener.onDeleteDistributor(idP);
                        }
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
