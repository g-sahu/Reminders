package com.gsapps.reminders.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gsapps.reminders.model.Event;
import com.gsapps.reminders.model.MeetingEvent;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.bumptech.glide.Glide.with;
import static com.gsapps.reminders.R.id.event_date;
import static com.gsapps.reminders.R.id.event_icon;
import static com.gsapps.reminders.R.id.event_name;
import static com.gsapps.reminders.R.layout.item_event;
import static com.gsapps.reminders.util.ReminderUtils.getDateString;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.Holder> {
    private final List<Event> eventList;
    private static LayoutInflater inflater = null;
    private final Context context;

    public EventListAdapter(Context context, List<Event> eventList) {
        this.eventList = eventList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override @NonNull
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = inflater.inflate(item_event, parent, false);
        return new Holder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Event event = eventList.get(position);
        with(context).load(event.getIcon()).into(holder.eventIcon);
        holder.eventName.setText(event.getName());

        if(event instanceof MeetingEvent) {
            holder.eventDate.setText(getDateString(event.getStartDate(), "dd/MM/YYYY hh:mm a"));
        } else {
            holder.eventDate.setText(getDateString(event.getStartDate(), "dd/MM/YYYY"));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
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
