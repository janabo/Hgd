package com.dk.mp.hy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.core.entity.HyDepartMent;
import com.dk.mp.core.entity.HyJbxx;
import com.dk.mp.hy.R;
import com.dk.mp.hy.ui.PhonesDialog;
import com.dk.mp.hy.ui.TxlPersonsActivity;

import java.util.List;



/**
 * 作者：janabo on 2016/12/22 17:54
 */
public class TxlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Activity activity;
    Context mContext;
    List mData;
    int type;//1,星标 2，部门
    LayoutInflater inflater;

    public TxlAdapter(Context mContext,Activity activity,List mData, int type) {
        this.mContext = mContext;
        this.activity = activity;
        this.mData = mData;
        this.type = type;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_hy_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( holder instanceof MyViewHolder ) {
            if(type == 1) {
                HyJbxx j = (HyJbxx) mData.get(position);
                ((MyViewHolder) holder).name.setText(j.getName());
            }else{
                HyDepartMent j = (HyDepartMent) mData.get(position);
                ((MyViewHolder) holder).name.setText(j.getNameDepart());
            }
        }
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
                    if(type == 2){
                        Intent intent = new Intent(mContext, TxlPersonsActivity.class);
                        HyDepartMent j = (HyDepartMent) mData.get(getLayoutPosition());
                        intent.putExtra("id",j.getIdDepart());
                        intent.putExtra("title",j.getNameDepart());
                        mContext.startActivity(intent);
                    }else{
                        final PhonesDialog dlg = new PhonesDialog(mContext,activity);
                        HyJbxx j = (HyJbxx) mData.get(getLayoutPosition());
                        dlg.show(j);
                    }
                }
            });
        }
    }
}
