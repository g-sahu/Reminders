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

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.bumptech.glide.Glide.with;
import static com.gsapps.reminders.R.id.event_desc;
import static com.gsapps.reminders.R.id.event_icon;
import static com.gsapps.reminders.R.id.event_name;
import static com.gsapps.reminders.R.layout.item_event;

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
        holder.eventDesc.setText(event.getDesc());
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
        final TextView eventName, eventDesc;

        Holder(View itemView) {
            super(itemView);
            eventIcon = itemView.findViewById(event_icon);
            eventName = itemView.findViewById(event_name);
            eventDesc = itemView.findViewById(event_desc);
        }
    }
}
