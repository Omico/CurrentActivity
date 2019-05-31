package me.omico.currentactivity.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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

    @NonNull
    @Override
    public CurrentActivityDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new CurrentActivityDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentActivityDataViewHolder holder, int position) {
        if (data != null) {
            CurrentActivityData itemData = data.get(position);
            holder.applicationTextView.setText(itemData.getApplicationName());
            holder.packageNameTextView.setText(itemData.getPackageName());
            holder.activityNameTextView.setText(itemData.getActivityName());
            setOnClickListener(holder.applicationTextView);
            setOnClickListener(holder.packageNameTextView);
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

    private void setOnClickListener(final TextView textView) {
        textView.setOnClickListener(view -> ClipboardUtils.copyToClipboard(view.getContext(), textView.getText().toString()));
    }

    static class CurrentActivityDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView applicationTextView;
        private final TextView packageNameTextView;
        private final TextView activityNameTextView;

        CurrentActivityDataViewHolder(View itemView) {
            super(itemView);
            applicationTextView = itemView.findViewById(R.id.application_name);
            packageNameTextView = itemView.findViewById(R.id.package_name);
            activityNameTextView = itemView.findViewById(R.id.activity_name);
        }
    }
}
