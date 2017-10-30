package com.dk.mp.ykt.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.ykt.CardMonthActivity;
import com.dk.mp.ykt.R;

import java.util.List;

/**
 * 作者：janabo on 2017/10/26 11:46
 */
public class MonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<String> mData;
    LayoutInflater inflater;
    String mTitle,cardno;

    public MonthAdapter(Context mContext,String title,String cardno,List<String> data) {
        this.mContext = mContext;
        this.mTitle = title;
        this.cardno = cardno;
        this.mData = data;
        inflater = LayoutInflater.from(mContext);
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_card_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).title.setText(mData.get(position)+"一卡通消费明细");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);// 取得实例
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CardMonthActivity.class);
                    intent.putExtra("month", mData.get(getLayoutPosition()));
                    intent.putExtra("title", mTitle);
                    intent.putExtra("cardNo",cardno);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
