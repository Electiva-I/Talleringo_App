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
    private int render_layout;
    private List<Vehicle> vehicles;
    private OnItemClickListener itemClickListener;
    private OnMenuItemClickListener menuItemClickListener;

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
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
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
        private TextView txtName, txtYear, txtBrand;
        private ImageButton btnMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtName = itemView.findViewById(R.id.txt_vehicle_name);
            this.txtBrand = itemView.findViewById(R.id.txt_maintenance_model);
            this.txtYear = itemView.findViewById(R.id.txt_vehicle_year);
          this.btnMenu = itemView.findViewById(R.id.btn_maintenance_menu);
        }

        public void bind(int id, String name, String brand, String year, OnItemClickListener itemClickListener, OnMenuItemClickListener menuItemClickListener){
            this.txtName.setText(name);
            this.txtBrand.setText(brand);
            this.txtYear.setText(year);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.OnItemClick(id, getAdapterPosition());
                }
            });

            this.btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuItemClickListener.OnMenuItemClick(id, getAdapterPosition(), btnMenu);
                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int id, int position);
    }

    public interface OnMenuItemClickListener{
        void OnMenuItemClick(int id, int position, ImageButton button);
    }


}
