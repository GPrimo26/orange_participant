package com.maksat.participantapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maksat.participantapp.MainActivity;
import com.maksat.participantapp.R;
import com.maksat.participantapp.models.EventProgram;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsRVAdapter extends RecyclerView.Adapter<EventsRVAdapter.ViewHolder> {
    public EventsRVAdapter(MainActivity activity, List<EventProgram> eventProgram) {
        this.mainActivity=activity;
        this.eventProgram=eventProgram;
    }

    private MainActivity mainActivity;
    private  List<EventProgram> eventProgram;

    @NonNull
    @Override
    public EventsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.cv_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsRVAdapter.ViewHolder holder, int position) {
        String text ="", start=eventProgram.get(position).getDateTimeStart(), end=eventProgram.get(position).getDateTimeFinish();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("ru"));
        try {
            Date date = format.parse(start);
            format.applyPattern("HH:mm");
            if (date != null) {
                text = format.format(date);

            }
            format.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
            date=format.parse(end);
            format.applyPattern("HH:mm");
            if (date != null) {
                text+=" - "+format.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.time_tv.setText(text);
        holder.place_tv.setText(eventProgram.get(position).getPlaceRus());
        holder.name_tv.setText(eventProgram.get(position).getNameRus());
        holder.sport_tv.setText(eventProgram.get(position).getSportNameRus());
    }

    @Override
    public int getItemCount() {
        return eventProgram.size();
    }

    public void setEventProgram(List<EventProgram> newProgram){
        eventProgram=newProgram;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView time_tv;
        private final TextView place_tv;
        private final TextView name_tv;
        private final TextView sport_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time_tv=itemView.findViewById(R.id.time_tv);
            place_tv=itemView.findViewById(R.id.place_tv);
            name_tv=itemView.findViewById(R.id.event_name_tv);
            sport_tv=itemView.findViewById(R.id.spot_tv);
        }
    }
}
