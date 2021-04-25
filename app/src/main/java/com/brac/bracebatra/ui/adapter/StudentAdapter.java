package com.brac.bracebatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Institute;
import com.brac.bracebatra.model.Student;
import com.brac.bracebatra.ui.activity.AttendanceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit on 12/16/2017.
 */


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<Student> schoolList;
    private Context context;
    private int lastPosition = -1;
    List<Institute> institutes;

    public StudentAdapter(List<Student> schoolList, Context context) {
        this.schoolList = schoolList;
        this.context = context;
        institutes=new ArrayList<Institute>();

    }

    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_school, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.tvTitle.setText(schoolList.get(position).getStudentFirstName());
        institutes=new Select().from(Institute.class).where("insId= ?",schoolList.get(position).getQueryInstituteId()).execute();
        holder.tvAddress.setText(institutes.get(0).getName());
        holder.totalStudent.setText(schoolList.get(position).getStudentId());




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

