package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private ArrayList<PantryItem> pantryItems;

    // Interface for Edit and Delete clicks
    public interface OnPantryItemClickListener {

        void onEdit(PantryItem item);

        void onDelete(PantryItem item);
    }

    private OnPantryItemClickListener listener;


    public PantryAdapter(
            ArrayList<PantryItem> pantryItems,
            OnPantryItemClickListener listener) {

        this.pantryItems = pantryItems;
        this.listener = listener;
    }


    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {

        PantryItem item = pantryItems.get(position);


        holder.tvIngredientName.setText(
                item.getName()
        );


        holder.tvQuantity.setText(
                "Quantity: " +
                        item.getQuantity() +
                        " " +
                        item.getUnit()
        );


        if (item.getExpiryDate() == null ||
                item.getExpiryDate().isEmpty()) {

            holder.tvExpiry.setText(
                    "Expiry: Not provided"
            );

        } else {

            holder.tvExpiry.setText(
                    "Expiry: " +
                            item.getExpiryDate()
            );
        }


        // EDIT BUTTON

        holder.btnEdit.setOnClickListener(v -> {

            if (listener != null) {

                listener.onEdit(item);
            }
        });


        // DELETE BUTTON

        holder.btnDelete.setOnClickListener(v -> {

            if (listener != null) {

                listener.onDelete(item);
            }
        });
    }


    @Override
    public int getItemCount() {

        return pantryItems.size();
    }


    public static class PantryViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvQuantity;
        TextView tvExpiry;

        Button btnEdit;
        Button btnDelete;


        public PantryViewHolder(
                @NonNull View itemView) {

            super(itemView);


            tvIngredientName =
                    itemView.findViewById(
                            R.id.tvIngredientName
                    );


            tvQuantity =
                    itemView.findViewById(
                            R.id.tvQuantity
                    );


            tvExpiry =
                    itemView.findViewById(
                            R.id.tvExpiry
                    );


            btnEdit =
                    itemView.findViewById(
                            R.id.btnEdit
                    );


            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete
                    );
        }
    }
}