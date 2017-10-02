package me.omico.currentactivity.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.omico.currentactivity.R;
import me.omico.currentactivity.model.CurrentActivityData;
import me.omico.util.ClipboardUtils;

/**
 * @author Omico 2017/10/2
 */

public class CurrentActivityDataAdapter extends RecyclerView.Adapter<CurrentActivityDataAdapter.CurrentActivityDataViewHolder> {

    @Nullable
    private List<CurrentActivityData> data;

    @Override
    public CurrentActivityDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new CurrentActivityDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrentActivityDataViewHolder holder, int position) {
        if (data != null) {
            holder.packageNameTextView.setText(data.get(position).getPackageName());
            setOnClickListener(holder.packageNameTextView);
            holder.activityNameTextView.setText(data.get(position).getActivityName());
            setOnClickListener(holder.activityNameTextView);
        }
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void setData(@Nullable List<CurrentActivityData> data) {
        this.data = data;
    }

    static class CurrentActivityDataViewHolder extends RecyclerView.ViewHolder {
        TextView packageNameTextView;
        TextView activityNameTextView;

        CurrentActivityDataViewHolder(View itemView) {
            super(itemView);
            packageNameTextView = itemView.findViewById(R.id.package_name);
            activityNameTextView = itemView.findViewById(R.id.activity_name);
        }
    }

    private void setOnClickListener(final TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardUtils.copyToClipboard(view.getContext(), textView.getText().toString());
            }
        });
    }
}
