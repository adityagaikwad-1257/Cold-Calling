package com.adi.coldcall;

import static com.adi.coldcall.Utils.getDurationsFromLong;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.coldcall.databinding.CallViewHolderBinding;
import com.adi.coldcall.models.Call;

import java.util.Date;

public class CallAdapter extends ListAdapter<Call, CallAdapter.CallViewHolder> {

    private final static DiffUtil.ItemCallback<Call> DIFF_UTIL_CALL_BACK = new DiffUtil.ItemCallback<Call>() {
        @Override
        public boolean areItemsTheSame(@NonNull Call oldItem, @NonNull Call newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Call oldItem, @NonNull Call newItem) {
            return oldItem.equals(newItem);
        }
    };

    Activity activity;
    private CallViewHolderClickListener listener;

    public CallAdapter(Activity activity) {
        super(DIFF_UTIL_CALL_BACK);
        this.activity = activity;
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CallViewHolderBinding binding = CallViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new CallViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        holder.binding.setCall(getItem(position));

        if (getItem(position).getDuration() == 0) holder.binding.duration.setText("0");
        else {
            /*
            durations[0] : seconds
            durations[1] : minutes
            durations[2] : hours
         */
            int[] durations = getDurationsFromLong(getItem(position).getDuration());

            StringBuilder durationText = new StringBuilder();
            if (durations[2] > 0) durationText.append(durations[2]).append(" Hours");
            if (durations[1] > 0) durationText.append(durations[1]).append(" Min");
            if (durations[0] > 0) durationText.append(durations[0]).append(" Sec");

            holder.binding.duration.setText(durationText);
        }

        /*
            long to Date
         */
        Date date = new Date(getItem(position).getDate());
        holder.binding.dateAndTime.setText(date.toString());

        /*
            setting click listener to phone icon on view holder
         */
        holder.binding.callBtn.setOnClickListener(v -> {
            if (listener != null) listener.onCallClick(getItem(position).getPhoneNumber().substring(3));
        });
    }

    public void setCallClickListener(CallViewHolderClickListener listener) {
        this.listener = listener;
    }

    /*
        returns Call item at the specified position
     */
    public Call getCallAtPosition(int position){
        return getItem(position);
    }

    /*
        this interface provides a click listener to phone icon on view holder
     */
    @FunctionalInterface
    public interface CallViewHolderClickListener{
        void onCallClick(String phone);
    }

    public static class CallViewHolder extends RecyclerView.ViewHolder {
        CallViewHolderBinding binding;

        public CallViewHolder(@NonNull CallViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
