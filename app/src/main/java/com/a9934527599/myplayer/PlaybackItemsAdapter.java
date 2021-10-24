package com.a9934527599.myplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaybackItemsAdapter extends RecyclerView.Adapter<PlaybackItemsAdapter.ViewHolder> {

    ArrayList<iconModel> iconModelList;
    private Context context;

    public PlaybackItemsAdapter(ArrayList<iconModel> iconModelList, Context context) {
        this.iconModelList = iconModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.icons_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img_View.setImageResource(iconModelList.get(position).getImg());
        holder.title_View.setText(iconModelList.get(position).getTitel());
    }

    @Override
    public int getItemCount() {
        return iconModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_View;
        TextView title_View;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_View= itemView.findViewById(R.id.recyclerView_icons);
            title_View= itemView.findViewById(R.id.icon_title);
        }
    }
}
