package com.brac.bracebatra.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Event;
import com.brac.bracebatra.model.Institute;
import com.brac.bracebatra.model.News;
import com.brac.bracebatra.model.Sync;
import com.brac.bracebatra.ui.adapter.EventsAdapter;
import com.brac.bracebatra.ui.adapter.NewsAdapter;
import com.brac.bracebatra.util.CalendarContentResolver;
import com.brac.bracebatra.util.SheardPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventActivity extends AppCompatActivity {

    List<Event> eventList = new ArrayList<Event>();
    RecyclerView recyclerView;
    EventsAdapter adapter;
    Map<String,String> calendars = new HashMap<String,String>();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                eventDeleteAndFillRequest();
                eventList.clear();
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        adapter = new EventsAdapter(eventList, EventActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_events);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setAdapter(adapter);
        checkPermissions(120, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);


        addData();
        //Toast.makeText(this, "All event sync to your calender ", Toast.LENGTH_SHORT).show();
    }

    private void addData() {

        List<Event> events = new Select().from(Event.class).execute();

        /*for(Event event:events)
        {


            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            try {
                Date d = f.parse(event.dateTime);
                long milliseconds = d.getTime();
              *//*  if(calendars.contains(event.title))
                    continue;*//*
               // pushAppointmentsToCalender(EventActivity.this,event.title,event.details+"\nKeynote Speaker : "+event.getKeynoteSpeaker(),event.getLocation(),1,milliseconds,true,false);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SheardPref.putKey(this,"iseventadded",true);
*/
        eventList.addAll(events);
        adapter.notifyDataSetChanged();

    }
    boolean isEventExist(String title,long time)
    {
        long begin = time;// starting time in milliseconds
        long end = begin + 1000 * 60 * 60*60*360;// ending time in milliseconds
                String[] proj =
            new String[]{
                    CalendarContract.Instances._ID,
                    CalendarContract.Instances.BEGIN,
                    CalendarContract.Instances.END,
                    CalendarContract.Instances.EVENT_ID};
        Cursor cursor =
                CalendarContract.Instances.query(getContentResolver(), proj, begin, end, title);
        if (cursor.getCount() > 0) {
            // deal with conflict
            return true;
        }
        return false;
    }
    private void addToDeviceCalendar(String startDate, String title,String description, String location) {

        try{

            String stDate = startDate;
            String enDate = startDate;

            GregorianCalendar calDate = new GregorianCalendar();
            GregorianCalendar calEndDate = new GregorianCalendar();

            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy,MM,dd,HH,mm");
            Date date,edate;
            try {
                date = originalFormat.parse(startDate);
                stDate=targetFormat.format(date);

            } catch (ParseException ex) {}

            long startMillis = 0;
            long endMillis = 0;
            Log.d("start",startDate);

            String dates=startDate;

            String SD_YeaR = dates.charAt(6)+""+dates.charAt(7)+dates.charAt(8)+""+dates.charAt(9);
            String SD_MontH = dates.charAt(3)+""+dates.charAt(4);
            String SD_DaY = dates.charAt(0)+""+dates.charAt(1);
            //String SD_DaY = "30";
            String SD_HouR = dates.charAt(11)+""+dates.charAt(12);
            String SD_MinutE = dates.charAt(14)+""+dates.charAt(15);


            Log.e("YeaR ", SD_YeaR);
            Log.e("MontH ",SD_MontH );
            Log.e("DaY ", SD_DaY);
            Log.e(" HouR", SD_HouR);
            Log.e("MinutE ", SD_MinutE);

            calDate.set(Integer.parseInt(SD_YeaR), Integer.parseInt(SD_MontH), Integer.parseInt(SD_DaY), Integer.parseInt(SD_HouR), Integer.parseInt(SD_MinutE));
            startMillis = calDate.getTimeInMillis();

            try {
                edate = originalFormat.parse(startDate);
                enDate=targetFormat.format(edate);

            } catch (ParseException ex) {}




            String ED_YeaR = SD_YeaR;
            String ED_MontH = SD_MontH;
            String ED_DaY = SD_DaY;

            String ED_HouR = SD_HouR;
            String ED_MinutE = SD_MinutE;


            calEndDate.set(Integer.parseInt(ED_YeaR), Integer.parseInt(ED_MontH)-1, Integer.parseInt(ED_DaY), Integer.parseInt(ED_HouR), Integer.parseInt(ED_MinutE));
            endMillis = calEndDate.getTimeInMillis();

            try {
                ContentResolver cr = getApplicationContext().getContentResolver();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.DTSTART, startMillis);
                values.put(CalendarContract.Events.DTEND, calDate.getTimeInMillis() + 60 * 60 * 1000);
                values.put(CalendarContract.Events.TITLE, title);
                values.put(CalendarContract.Events.DESCRIPTION, description);
                values.put(CalendarContract.Events.EVENT_LOCATION,location);
                values.put(CalendarContract.Events.HAS_ALARM,1);
                values.put(CalendarContract.Events.CALENDAR_ID, 1);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance()
                        .getTimeZone().getID());
                System.out.println(Calendar.getInstance().getTimeZone().getID());
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                long eventId = Long.parseLong(uri.getLastPathSegment());
                Log.d("Event_Id", String.valueOf(eventId));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }
    public void eventDeleteAndFillRequest() {

        new Delete().from(Event.class).execute();
        String url = "http://bepmis.brac.net/rest/apps/events/list?uname=kholilur&passw=123456";

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jObject = new JSONObject(response);

                            Log.d("response INSTITUTE", response);
                            JSONArray jsonArray = jObject.getJSONArray("model");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Event event = new Event();

                                event.setEventId(jsonObject.getString("id"));
                                event.setTitle(jsonObject.getString("title"));
                                event.setDetails(jsonObject.getString("description"));
                                event.setLocation(jsonObject.getString("eventPlace"));
                                event.setDateTime(jsonObject.getString("eventDate"));
                                event.setOrgBy(jsonObject.getString("eventOrganizer"));
                                event.setKeynoteSpeaker(jsonObject.getString("keynoteSpeaker"));

                                List<Event> events = new Select().from(Event.class)
                                        .where("eventId = ?",jsonObject.getString("id")).execute();
                                if(events.size()==0){
                                    event.save();
                                }

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addData();
                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue rq = Volley.newRequestQueue(EventActivity.this);
        rq.add(sr);
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

}
