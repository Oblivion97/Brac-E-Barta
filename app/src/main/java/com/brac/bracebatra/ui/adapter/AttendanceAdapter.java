package com.brac.bracebatra.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Attendance;
import com.brac.bracebatra.model.Event;
import com.brac.bracebatra.model.Student;
import com.brac.bracebatra.util.AttendanceSync;

import java.util.List;

/**
 * Created by hhson on 8/12/2016.
 */
public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private List<Student> attendanceList;
    private Context context;
    private int lastPosition = -1;
    private AttendanceSync attendanceSync;


    public AttendanceAdapter(List<Student> newsList, Context context , AttendanceSync attendanceSync) {
        this.attendanceList = newsList;
        this.context = context;
        this.attendanceSync = attendanceSync;

    }

    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Student attendance = attendanceList.get(position);
        holder.name.setText(attendance.studentFirstName);

        holder.attendence.setChecked(attendance.isAttend);
        holder.uniform.setChecked(attendance.hasUniform);


        holder.attendence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    attendanceSync.onAttCheck(attendance.sid,holder.attendence.isChecked(), holder.uniform.isChecked(),attendance);

            }
        });

        holder.uniform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                attendanceSync.onAttCheck(attendance.getSid(),holder.attendence.isChecked(), holder.uniform.isChecked(),attendance);

            }
        });


    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        CheckBox attendence;
        CheckBox uniform;


        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.att_student_name);
            attendence = (CheckBox) itemView.findViewById(R.id.cb_attend);
            uniform = (CheckBox) itemView.findViewById(R.id.cb_uniform);
            this.setIsRecyclable(false);

        }
    }
}

