package com.kodimstudio.myapplication.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kodimstudio.myapplication.R;

import java.util.List;

public class PerksListAdapter_frPerks extends RecyclerView.Adapter<PerksListAdapter_frPerks.ViewHolder> {

    public interface OnPerkClickListener{
        void onPerkClick(int position, ImageView perkIcon, ImageView perkIconBg);
    }

    private final OnPerkClickListener onPerkClickListener;
    private final LayoutInflater inflater;
    private final boolean[] selection;
    private final List<Drawable> perkIconArray;

    public PerksListAdapter_frPerks(Context context,
                                    List<Drawable> perkIconArray,
                                    OnPerkClickListener onPerkClickListener,
                                    boolean[] selection) {
        this.inflater = LayoutInflater.from(context);
        this.onPerkClickListener = onPerkClickListener;
        this.selection = selection;
        this.perkIconArray = perkIconArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.perk_list_item_fr_perks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.perkIcon.setBackground(perkIconArray.get(position));
        if (selection[position]) {
            holder.perkIconBg.setImageResource(R.drawable.perk_icon_bg_gray);
            holder.perkIcon.setAlpha(0.5f);
        }
        else {
            holder.perkIconBg.setImageResource(R.drawable.perk_icon_bg);
            holder.perkIcon.setAlpha(1.0f);
        }
        holder.itemView.setOnClickListener(v -> onPerkClickListener.onPerkClick(holder.getAdapterPosition(), holder.perkIcon, holder.perkIconBg));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return perkIconArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView perkIcon;
        final ImageView perkIconBg;

        ViewHolder(View view) {
            super(view);
            perkIcon = view.findViewById(R.id.perk_icon);
            perkIconBg = view.findViewById(R.id.perk_bg_icon);
        }
    }
}
