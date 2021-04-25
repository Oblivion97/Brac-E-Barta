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

import com.brac.bracebatra.R;
import com.brac.bracebatra.model.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by hhson on 8/12/2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> newsList;
    private Context context;
    private int lastPosition = -1;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;

    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final News  news = newsList.get(position);

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

        holder.tvTitle.setText(news.nTitle);
        holder.tvShortDetails.setText(news.nBody);
        holder.tvDetails.setText(news.nBody);
        holder.tvPostBy.setText("Post By : "+ news.nPostBy);
        holder.tvDate.setText("Date : " +news.nDate);


    }
    long getStringTimeToLong(String s)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
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
        TextView tvPostBy;
        TextView tvDate;



        public ViewHolder(View itemView) {
            super(itemView);
            explore = (TextView) itemView.findViewById(R.id.layout_news_explore);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_news_title);
            tvShortDetails = (TextView) itemView.findViewById(R.id.news_short_body);
            tvDetails = (TextView) itemView.findViewById(R.id.news_body);
            tvPostBy = (TextView) itemView.findViewById(R.id.tv_news_post_by);
            tvDate = (TextView) itemView.findViewById(R.id.tv_news_date);


        }
    }
}

