package com.example.thepunchsystemandroid.Adapter;



import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.thepunchsystemandroid.Entity.indexStudents;
import com.example.thepunchsystemandroid.R;

import java.util.List;

public  class studentAdapter extends RecyclerView.Adapter<studentAdapter.ViewHolder> {
    private List<indexStudents> mStudent;
    private Context context;
     class ViewHolder extends RecyclerView.ViewHolder{
        ImageView studentImage;
        TextView studenetName;
        TextView studenetId;
        TextView TodayTime;
        TextView weekTime;
        TextView weekLeftTime;
        TextView punch;
        TextView grade;
        public ViewHolder(View view){
            super(view);
            ///
            studenetId=(TextView) view.findViewById(R.id.rank_id);
            studentImage=(ImageView) view.findViewById(R.id.student_rank_id);
            studenetName=(TextView) view.findViewById(R.id.rank_id_name);
            grade=view.findViewById(R.id.rank_grade);
            TodayTime=view.findViewById(R.id.Today_time);
            weekTime=view.findViewById(R.id.weekTime);
            weekLeftTime=view.findViewById(R.id.weekLeftTime);
            punch=view.findViewById(R.id.startOrStop);
        }
    }
    public studentAdapter(Context context,List<indexStudents> studentList){
        mStudent=studentList;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /////

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item,parent,false);
        ViewHolder holders = new ViewHolder(view);
        ////
        return holders;
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, int position) {
         int num=position+1;
        indexStudents stu=mStudent.get(position);
        holder.studenetId.setText(""+num);
        holder.studenetName.setText(stu.getName());
        holder.TodayTime.setText(stu.getTodayTime()+"h");
        holder.weekTime.setText(stu.getWeekTime()+"h");
        holder.weekLeftTime.setText(stu.getWeekLeftTime()+"h");
        holder.grade.setText(stu.getGrade()+"级");
        if(stu.getPunch()){
            holder.punch.setText("打卡中");
            holder.punch.setTextColor(Color.parseColor("#FFE91E1E"));
        }else {
            holder.punch.setText("未打卡");
            holder.punch.setTextColor(Color.parseColor("#1FCC25"));
        }
        RequestOptions requestOptions=new RequestOptions().circleCrop();
        requestOptions.placeholder(R.drawable.loading);


        Glide.with(context)
                .load(stu.getAvatar())
                .apply(requestOptions)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.studentImage.setImageDrawable(resource);
                    }
                });



    }

    @Override
    public int getItemCount() {
        return mStudent.size();
    }
}
