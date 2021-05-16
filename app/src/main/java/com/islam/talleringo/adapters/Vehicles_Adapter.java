package com.islam.talleringo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.islam.talleringo.R;
import com.islam.talleringo.database.Vehicles.Vehicle;

import java.util.List;

public class Vehicles_Adapter  extends RecyclerView.Adapter<Vehicles_Adapter.ViewHolder> {
    private final int render_layout;
    private final List<Vehicle> vehicles;
    private final OnItemClickListener itemClickListener;
    private final OnMenuItemClickListener menuItemClickListener;

    public  Vehicles_Adapter(List<Vehicle> vehicles, int render_layout, OnItemClickListener ItemClickListener, OnMenuItemClickListener menuItemClickListener){
        this.render_layout = render_layout;
        this.vehicles = vehicles;
        this.itemClickListener = ItemClickListener;
        this.menuItemClickListener = menuItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.render_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(vehicles.get(position).ID,
                vehicles.get(position).Model,
                vehicles.get(position).Brand,
                vehicles.get(position).Year,
                itemClickListener,
                menuItemClickListener
        );
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final TextView txtYear;
        private final TextView txtBrand;
        private final ImageButton btnMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtName = itemView.findViewById(R.id.txt_vehicle_name);
            this.txtBrand = itemView.findViewById(R.id.txt_about_name);
            this.txtYear = itemView.findViewById(R.id.txt_vehicle_year);
          this.btnMenu = itemView.findViewById(R.id.btn_maintenance_menu);
        }

        public void bind(int id, String name, String brand, String year, OnItemClickListener itemClickListener, OnMenuItemClickListener menuItemClickListener){
            this.txtName.setText(name);
            this.txtBrand.setText(brand);
            this.txtYear.setText(year);

            itemView.setOnClickListener(v -> itemClickListener.OnItemClick(id, getAdapterPosition()));

            this.btnMenu.setOnClickListener(v -> menuItemClickListener.OnMenuItemClick(id, getAdapterPosition(), btnMenu));
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int id, int position);
    }

    public interface OnMenuItemClickListener{
        void OnMenuItemClick(int id, int position, ImageButton button);
    }


}
