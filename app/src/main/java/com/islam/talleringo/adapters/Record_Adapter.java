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
import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.utils.App;

import java.util.List;

public class Record_Adapter  extends RecyclerView.Adapter<Record_Adapter.ViewHolder> {
    private final int render_layout;
    private final List<Record> records;
    private final OnItemClickListener itemClickListener;
    private final OnMenuItemClickListener menuItemClickListener;
    AppDatabase db = Room.databaseBuilder(App.getContext(),
            AppDatabase.class, "vehicle").allowMainThreadQueries().build();

    public  Record_Adapter(List<Record> records, int render_layout, OnItemClickListener ItemClickListener, OnMenuItemClickListener menuItemClickListener){
        this.render_layout = render_layout;
        this.records = records;
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


        holder.bind(records.get(position).ID,
                db.vehicleDAO().getVehicle(records.get(position).Vehicle_Id).get(0).toString(),
                records.get(position).Creation_Date,
                records.get(position).Detail,
                records.get(position).Cost,
                itemClickListener,
                menuItemClickListener
        );
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtModel;
        private final TextView txtDate;
        private final TextView txtDetail;
        private final TextView txtCost;
        private final ImageButton btnMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtModel = itemView.findViewById(R.id.txt_about_name);
            this.txtDate = itemView.findViewById(R.id.txt_maintenance_date);
            this.txtDetail = itemView.findViewById(R.id.txt_maintenance_detail);
            this.btnMenu = itemView.findViewById(R.id.btn_maintenance_menu);
            this.txtCost = itemView.findViewById(R.id.txt_record_cost);
        }

        public void bind(int id, String model, String date, String detail, Float cost, OnItemClickListener itemClickListener, OnMenuItemClickListener menuItemClickListener){
            this.txtModel.setText(model);
            this.txtDate.setText(date);
            this.txtDetail.setText(detail);
             App.getContext().getString(R.string.txt_dollar_char);
            this.txtCost.setText(App.getContext().getString(R.string.txt_dollar_char) + cost);

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
