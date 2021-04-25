package com.brac.bracebatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Attendance;
import com.brac.bracebatra.model.Institute;
import com.brac.bracebatra.ui.activity.AttendanceActivity;
import com.brac.bracebatra.ui.activity.SchoolActivity;

import java.util.List;

/**
 * Created by hhson on 8/12/2016.
 */
public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.ViewHolder> {
    private List<Institute> schoolList;
    private Context context;
    private int lastPosition = -1;

    public SchoolAdapter(List<Institute> schoolList, Context context) {
        this.schoolList = schoolList;
        this.context = context;

    }

    @Override
    public SchoolAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_school, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.tvTitle.setText(schoolList.get(position).getName());
        holder.tvAddress.setText(schoolList.get(position).getEducationType());
        holder.totalStudent.setText(schoolList.get(position).getTotalStudent());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,AttendanceActivity.class);
                intent.putExtra("SchoolName",schoolList.get(position).getName());
                intent.putExtra("TotalStudents",schoolList.get(position).getTotalStudent());
                intent.putExtra("queryInstituteId",schoolList.get(position).getInsId());
                intent.putExtra("option","School");
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return schoolList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvAddress;
        TextView totalStudent;
        View container;



        public ViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.item_school);
            tvTitle= (TextView) itemView.findViewById(R.id.school_name);
            totalStudent= (TextView) itemView.findViewById(R.id.total_student);
            tvAddress= (TextView) itemView.findViewById(R.id.subject);

        }
    }
}

