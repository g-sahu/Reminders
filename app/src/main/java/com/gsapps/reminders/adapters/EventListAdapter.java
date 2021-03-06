package com.gsapps.reminders.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.gsapps.reminders.adapters.EventListAdapter.Holder;
import com.gsapps.reminders.models.EventDTO;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;
import static com.bumptech.glide.Glide.with;
import static com.gsapps.reminders.R.id.event_date;
import static com.gsapps.reminders.R.id.event_icon;
import static com.gsapps.reminders.R.id.event_name;
import static com.gsapps.reminders.R.layout.item_event;
import static com.gsapps.reminders.util.CalendarUtils.getDateTimeString;

public class EventListAdapter extends Adapter<Holder> {
    private final List<EventDTO> eventDTOList;
    private static LayoutInflater inflater;
    private final Context context;

    public EventListAdapter(Context context, List<EventDTO> eventDTOList) {
        this.eventDTOList = eventDTOList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override @NonNull
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        EventDTO eventDTO = eventDTOList.get(position);
        with(context).load(eventDTO.getIcon()).into(holder.eventIcon);
        holder.eventName.setText(eventDTO.getTitle());
        holder.eventDate.setText(getDateTimeString(eventDTO.getStartTs(), eventDTO.getDateFormat(), true));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return eventDTOList.size();
    }

    public static class Holder extends ViewHolder {
        final ImageView eventIcon;
        final TextView eventName, eventDate;

        Holder(View itemView) {
            super(itemView);
            eventIcon = itemView.findViewById(event_icon);
            eventName = itemView.findViewById(event_name);
            eventDate = itemView.findViewById(event_date);
        }
    }
}
