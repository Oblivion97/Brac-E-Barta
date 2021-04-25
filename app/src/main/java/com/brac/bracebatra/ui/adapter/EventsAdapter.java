package com.brac.bracebatra.ui.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Event;
import com.brac.bracebatra.model.News;
import com.brac.bracebatra.ui.activity.EventActivity;
import com.brac.bracebatra.util.CalendarContentResolver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hhson on 8/12/2016.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<Event> newsList;
    private Activity context;
    private int lastPosition = -1;
    Map<String,String> calendars = new HashMap<String,String>();

    public EventsAdapter(List<Event> newsList, Activity context) {
        this.newsList = newsList;
        this.context = context;
        CalendarContentResolver calendarContentResolver=new CalendarContentResolver(context);

        calendars=calendarContentResolver.readCalendarEvent(context);

    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Event  event = newsList.get(position);

        holder.explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.tvShortDetails.getVisibility()== View.VISIBLE){
                    holder.tvShortDetails.setVisibility(View.GONE);
                    holder.tvDetails.setVisibility(View.VISIBLE);
                }
                else if(holder.tvDetails.getVisibility()== View.VISIBLE){
                    holder.tvDetails.setVisibility(View.GONE);
                    holder.tvShortDetails.setVisibility(View.VISIBLE);
                }


            }
        });
        /*if(calendars.containsKey(event.title)||calendars.get(event.title).toString().equals(event.title)||calendars.get(event.title).toString().contains(event.title))
        {
            holder.add_to_calender.setEnabled(false);
            holder.add_to_calender.setText("Event Already Added");
        }*/
        holder.keynote.setText("Key Note Speaker: "+event.keynoteSpeaker);



        holder.tvTitle.setText(event.title);
        holder.tvShortDetails.setText(event.details);
        holder.tvDetails.setText(event.details);
        holder.tvOrgBy.setText("Organized By : "+ event.orgBy);
        holder.tvDate.setText("Event Time : " +event.dateTime);
        holder.tvLocation.setText("Location : " +event.location);
        holder.add_to_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date d = f.parse(event.dateTime);
                    long milliseconds = d.getTime();
                    String s=calendars.get(event.title);


                    if(!(s.equals("added")||s.toString().contains("added")))
                    {
                        pushAppointmentsToCalender(context,event.title,event.details+"\nKeynote Speaker : "+event.getKeynoteSpeaker(),event.getLocation(),1,milliseconds,true,false);
                        Toast.makeText(context, "Event Added", Toast.LENGTH_SHORT).show();
                        calendars.put(event.title,"added");
                    }

                    else
                        Toast.makeText(context, "Already added ", Toast.LENGTH_SHORT).show();
                }catch (Exception ex)
                {
                    
                }
            }
        });


    }


    public static long pushAppointmentsToCalender(Activity curActivity, String title, String addInfo, String place, int status, long startDate, boolean needReminder, boolean needMailService) throws Exception{
        /***************** Event: note(without alert) *******************/

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1);
        eventValues.put("title", title);
        eventValues.put("description", addInfo);
        eventValues.put("eventLocation", place);

        long endDate = startDate + 1000 * 60 * 60; // For next 1hr

        eventValues.put("dtstart", startDate);
        eventValues.put("dtend", endDate);

        eventValues.put("allDay", 0);
        eventValues.put("eventStatus", status);
        eventValues.put("eventTimezone", "UTC/GMT +6:00");

    /*eventValues.put("visibility", 3); // visibility to default (0),
                                        // confidential (1), private
                                        // (2), or public (3):
    eventValues.put("transparency", 0); // You can control whether
                                        // an event consumes time
                                        // opaque (0) or transparent
                                        // (1).
      */
        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminderValues = new ContentValues();

            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5); // Default value of the
            // system. Minutes is a
            // integer
            reminderValues.put("method", 1); // Alert Methods: Default(0),
            // Alert(1), Email(2),
            // SMS(3)

            Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        }

        /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

        if (needMailService) {
            String attendeuesesUriString = "content://com.android.calendar/attendees";

            /********
             * To add multiple attendees need to insert ContentValues multiple
             * times
             ***********/
            ContentValues attendeesValues = new ContentValues();

            attendeesValues.put("event_id", eventID);
            attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
            attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
            // E
            // mail
            // id
            attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
            // Relationship_None(0),
            // Organizer(2),
            // Performer(3),
            // Speaker(4)
            attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
            // Required(2), Resource(3)
            attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
            // Decline(2),
            // Invited(3),
            // Tentative(4)

            Uri attendeuesesUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
        }

        return eventID;

    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView explore;

        TextView tvTitle;
        TextView tvShortDetails;
        TextView tvDetails;
        TextView tvOrgBy;
        TextView tvDate;
        TextView tvLocation;
        TextView keynote;
        Button add_to_calender;






        public ViewHolder(View itemView) {
            super(itemView);
            explore = (TextView) itemView.findViewById(R.id.layout_event_explore);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_event_title);
            tvShortDetails = (TextView) itemView.findViewById(R.id.event_short_body);
            tvDetails = (TextView) itemView.findViewById(R.id.event_body);
            tvOrgBy = (TextView) itemView.findViewById(R.id.tv_event_org_by);
            tvDate = (TextView) itemView.findViewById(R.id.tv_event_date);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_event_loc);
            keynote = (TextView) itemView.findViewById(R.id.keynote);
            add_to_calender = (Button) itemView.findViewById(R.id.add_to_calender);



        }
    }
}

