package com.example.demo0426;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

public class GroupBuildingsAdapter extends RecyclerView.Adapter<GroupBuildingsAdapter.ViewHolder> {

    private Context context;
    private List<GroupBuildings> groupBuildingsList;

    static  class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;
        public ViewHolder( View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            imageView=itemView.findViewById(R.id.groups_image);
            textView=itemView.findViewById(R.id.groupd_description);
        }
    }
    private onRecycleViewClickListener listener;
    public void setItemClickListener(onRecycleViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public GroupBuildingsAdapter(List<GroupBuildings> groupBuildingsList){
        this.groupBuildingsList=groupBuildingsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(context==null){
            context=viewGroup.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.groups_buildings_item,viewGroup,false);

        if(listener != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(v);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClickListener(v);
                    return true;
                }
            });
        }

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        GroupBuildings groupBuildings=groupBuildingsList.get(i);
        viewHolder.textView.setText(groupBuildings.getDescription());
        Glide.with(context).load(groupBuildings.getImageViewId()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return groupBuildingsList.size();
    }
}
