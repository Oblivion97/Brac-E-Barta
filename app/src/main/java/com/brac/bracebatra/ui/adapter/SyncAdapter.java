package com.brac.bracebatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Update;
import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Event;
import com.brac.bracebatra.model.Sync;
import com.brac.bracebatra.ui.activity.AttendanceActivity;
import com.brac.bracebatra.ui.activity.SyncActivity;
import com.brac.bracebatra.util.IOnSyncListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by hhson on 8/12/2016.
 */
public class SyncAdapter extends RecyclerView.Adapter<SyncAdapter.ViewHolder> {
    private List<Sync> newsList;
    private Context context;
    private int lastPosition = -1;
    IOnSyncListener iOnSyncListener;

    public SyncAdapter(List<Sync> newsList, Context context,IOnSyncListener iOnSyncListener) {
        this.newsList = newsList;
        this.context = context;
        this.iOnSyncListener=iOnSyncListener;

    }

    @Override
    public SyncAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sync, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Sync  item = newsList.get(position);

        if (!item.isSynced){
            holder.layout.setBackgroundColor(Color.WHITE);
            holder.ibSync.setVisibility(View.GONE);
        }
        else
        {
            holder.layout.setBackgroundResource(R.color.sync);

        }

        holder.tvTitle.setText(item.Title);
        holder.tvTime.setText(item.date);



        holder.ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sync logic

                Intent intent = new Intent(context, AttendanceActivity.class);
                intent.putExtra("option","Edit");
                intent.putExtra("SchoolName",item.Title);
                intent.putExtra("date",item.date);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        View layout;

        TextView tvTitle;
        TextView tvTime;

        ImageButton ibSync;
        ImageButton ibEdit;






        public ViewHolder(View itemView) {
            super(itemView);
            layout =  itemView.findViewById(R.id.layout_sync);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_item_sync_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_item_sync_date);

            ibSync = (ImageButton) itemView.findViewById(R.id.ib_sync);
            ibEdit = (ImageButton) itemView.findViewById(R.id.ib_edit);


        }
    }
}

