package com.islam.talleringo.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.islam.talleringo.R;
import com.islam.talleringo.models.Develops;
import com.islam.talleringo.utils.App;

import java.util.List;

public class About_Adapter  extends RecyclerView.Adapter<About_Adapter.ViewHolder> {
    private final int render_layout;
    private final List<Develops> develops;
    private final OnItemClickListener itemClickListener;
    public  About_Adapter(List<Develops> develops, int render_layout, OnItemClickListener ItemClickListener){
        this.render_layout = render_layout;
        this.develops = develops;
        this.itemClickListener = ItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.render_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("DeBUG", position+"");
        holder.bind(develops.get(position).Name,
                develops.get(position).Username,
                develops.get(position).Image,
                itemClickListener
        );
    }

    @Override
    public int getItemCount() {
        return develops.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final TextView txtUsername;
        private final ImageView imageViewProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtName = itemView.findViewById(R.id.txt_about_name);
            this.txtUsername = itemView.findViewById(R.id.txt_about_user);
            this.imageViewProfile = itemView.findViewById(R.id.imageViewProfile);

        }

        public void bind(String name, String user, String Image, OnItemClickListener itemClickListener){
            this.txtName.setText(name);
            this.txtUsername.setText(user);
            Glide.with(App.getContext()).load(Image)
                    .circleCrop()
                    .into(imageViewProfile);

            itemView.setOnClickListener(v -> itemClickListener.OnItemClick(user, getAdapterPosition()));
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(String user, int position);
    }




}
