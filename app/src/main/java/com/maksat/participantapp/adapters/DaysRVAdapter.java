package com.maksat.participantapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maksat.participantapp.MainActivity;
import com.maksat.participantapp.R;
import com.maksat.participantapp.models.EventProgramByDays;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DaysRVAdapter extends RecyclerView.Adapter<DaysRVAdapter.ViewHolder> {
    public DaysRVAdapter(MainActivity mainActivity, List<EventProgramByDays> programByDays) {
        this.mainActivity=mainActivity;
        this.programByDays=programByDays;
    }

    private final MainActivity mainActivity;
    private int selectedPosition=0;
    private List<EventProgramByDays> programByDays;

    private static OnItemClickListener mListener;

    public OnItemClickListener getOnItemClickListener() {
        return mListener;
    }


    public interface OnItemClickListener {

        void onItemClick(Integer position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void performClick(Integer clickPosition){
        if (mListener!=null) {
            mListener.onItemClick(clickPosition);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.cv_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (position==selectedPosition){
                holder.divider.setVisibility(View.VISIBLE);
            }else {
                holder.divider.setVisibility(View.INVISIBLE);
            }
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd", new Locale("ru"));
            Date date=format.parse(programByDays.get(position).getDate());
            String dateStr="- -";
            if (date != null) {
                format.applyPattern("dd MMMM");
                dateStr=format.format(date);
                /*calendar.setTime(date);
                dateStr=calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.MONTH);*/

            }
            holder.day_tv.setText(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null) {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = position;
                    notifyItemChanged(selectedPosition);
                    mListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return programByDays.size();
    }

    public void setProgramByDays(List<EventProgramByDays> newProgramByDays){
        programByDays=newProgramByDays;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView day_tv;
        private final View divider;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day_tv=itemView.findViewById(R.id.day_tv);
            divider=itemView.findViewById(R.id.divider17);
        }
    }
}
