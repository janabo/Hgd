package com.dk.mp.tsg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.tsg.entity.BookRecord;
import com.dk.mp.tsg.R;

import java.util.List;

/**
 * 作者：janabo on 2017/10/25 10:56
 */
public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<BookRecord> mData;
    LayoutInflater inflater;

    public RecordAdapter(Context mContext,List<BookRecord> mData) {
        this.mContext = mContext;
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_tsg_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookRecord j = mData.get(position);
        ((MyViewHolder) holder).name.setText(j.getName());
        ((MyViewHolder) holder).jssj.setText(j.getJssj());
        ((MyViewHolder) holder).ghsj.setText(j.getYjhssj());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name, jssj,ghsj;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            jssj = (TextView) itemView.findViewById(R.id.jssj);
            ghsj = (TextView) itemView.findViewById(R.id.ghsj);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
