package com.dk.mp.jcxx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.jcxx.R;
import com.dk.mp.jcxx.entity.Jcxx;

import java.util.List;

/**
 * 作者：janabo on 2017/11/1 10:55
 */
public class JcxxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<Jcxx> mData;
    LayoutInflater inflater;

    public JcxxAdapter(Context mContext,List<Jcxx> mData) {
        this.mContext = mContext;
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.jcxx_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Jcxx j = mData.get(position);
        ((MyViewHolder) holder).name.setText(j.getName());
        ((MyViewHolder) holder).subtitle.setText(j.getSubTitle());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,subtitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
