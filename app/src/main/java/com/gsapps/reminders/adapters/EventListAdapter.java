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
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.model.MeetingEventDTO;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;
import static com.bumptech.glide.Glide.with;
import static com.gsapps.reminders.R.id.event_date;
import static com.gsapps.reminders.R.id.event_icon;
import static com.gsapps.reminders.R.id.event_name;
import static com.gsapps.reminders.R.layout.item_event;
import static com.gsapps.reminders.util.CalendarUtils.getDateString;

public class EventListAdapter extends Adapter<Holder> {
    private final List<EventDTO> eventDTOList;
    private static LayoutInflater inflater = null;
    private final Context context;

    public EventListAdapter(Context context, List<EventDTO> eventDTOList) {
        this.eventDTOList = eventDTOList;
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
        EventDTO eventDTO = eventDTOList.get(position);
        with(context).load(eventDTO.getIcon()).into(holder.eventIcon);
        holder.eventName.setText(eventDTO.getTitle());
        String dateFormat = eventDTO instanceof MeetingEventDTO ? "dd/MM/YYYY hh:mm a" : "dd/MM/YYYY";
        holder.eventDate.setText(getDateString(eventDTO.getStartTs(), dateFormat));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return eventDTOList.size();
    }

    class Holder extends ViewHolder {
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
