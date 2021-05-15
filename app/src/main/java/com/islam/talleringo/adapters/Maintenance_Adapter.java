package com.islam.talleringo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.islam.talleringo.R;
import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.utils.App;

import java.util.List;

public class Maintenance_Adapter  extends RecyclerView.Adapter<Maintenance_Adapter.ViewHolder> {
    private int render_layout;
    private List<Maintenance> maintenances;
    private OnItemClickListener itemClickListener;
    private OnMenuItemClickListener menuItemClickListener;
    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();

    public  Maintenance_Adapter(List<Maintenance> maintenances, int render_layout, OnItemClickListener ItemClickListener, OnMenuItemClickListener menuItemClickListener){
        this.render_layout = render_layout;
        this.maintenances = maintenances;
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


        holder.bind(maintenances.get(position).ID,
                db.vehicleDAO().getVehicle(maintenances.get(position).Vehicle_Id).get(0).toString(),
                maintenances.get(position).Schedule_Date,
                maintenances.get(position).Detail,
                itemClickListener,
                menuItemClickListener
        );
    }

    @Override
    public int getItemCount() {
        return maintenances.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtModel, txtDate, txtDetail;
        private ImageButton btnMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtModel = itemView.findViewById(R.id.txt_maintenance_model);
            this.txtDate = itemView.findViewById(R.id.txt_maintenance_date);
            this.txtDetail = itemView.findViewById(R.id.txt_maintenance_detail);
            this.btnMenu = itemView.findViewById(R.id.btn_maintenance_menu);
        }

        public void bind(int id, String model, String date, String detail, OnItemClickListener itemClickListener, OnMenuItemClickListener menuItemClickListener){
            this.txtModel.setText(model);
            this.txtDate.setText(date);
            this.txtDetail.setText(detail);

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
