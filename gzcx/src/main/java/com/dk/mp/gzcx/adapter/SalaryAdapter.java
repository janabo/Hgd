package com.dk.mp.gzcx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.gzcx.R;
import com.dk.mp.gzcx.SalaryDetailActivity;
import com.dk.mp.gzcx.entity.Salary;

import java.util.List;

/**
 * 作者：janabo on 2017/10/24 12:01
 */
public class SalaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<Salary> mData;
    LayoutInflater inflater;

    public SalaryAdapter(Context mContext,List mData) {
        this.mContext = mContext;
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_salary_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Salary j = mData.get(position);
        ((MyViewHolder) holder).name.setText(j.getTitle());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,SalaryDetailActivity.class);//跳转工资详情
                    Salary salary = mData.get(getLayoutPosition());
                    intent.putExtra("month",salary.getRq());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
