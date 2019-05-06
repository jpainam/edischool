package com.edischool.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edischool.DetailNotification;
import com.edischool.R;
import com.edischool.pojo.Notifications;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationsAdapteur extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Notifications> mData= new ArrayList<>();
    private List<Notifications> mDataSeach= new ArrayList<>();
    private List<Notifications> mDataSave= new ArrayList<>();// table qui permet la sauvegarde des donnée l
    private static final int DELEITEM=104;
    public NotificationsAdapteur(Context mContext) {
        this.mContext = mContext;

    }

    public void add(Notifications notification){
        if(!existe(notification,mData)){
            mData.add(0,notification);
            mDataSave.add(0,notification);
        }

        notifyDataSetChanged();
    }
    public void updatecheck(Notifications notifications,final RecyclerView.ViewHolder holder ){
        NotificationDao dao = new NotificationDao(mContext);
        dao.updcheckNotification(notifications.getIdnotification());
        ((TextViewHolder) holder).notiflu.setImageResource(R.drawable.ic_check_red_24dp);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        if(viewType == 1) { //for empty layout// text envoie
            view=mInflater.inflate(R.layout.neux_notifications,parent,false);
            return new TextViewHolder(view);
        } else{
            view=mInflater.inflate(R.layout.neux_notifications,parent,false);
            return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof TextViewHolder ){//  message de type texte

           ((TextViewHolder) holder).icone.setText(mData.get(position).getType().substring(0, 1) );

            ((TextViewHolder) holder).titre.setText(mData.get(position).getTitre());
            ((TextViewHolder) holder).message.setText(mData.get(position).getMessage());
            ((TextViewHolder) holder).date.setText(mData.get(position).getDate());
            if(mData.get(position).getLu()==1)
               ((TextViewHolder) holder).notiflu.setImageResource(R.drawable.ic_check_red_24dp);

            ((TextViewHolder) holder).itemnotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatecheck(mData.get(position),((TextViewHolder)holder));
                    Intent detailNotifi= new Intent(mContext, DetailNotification.class);
                     detailNotifi.putExtra("notifications",mData.get(position));
                    ((Activity) mContext).startActivityForResult(detailNotifi,DELEITEM);
                   // mContext.startActivity(detailNotifi);
                    ((Activity)mContext).overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//transition simple
                  // ((Activity)mContext).finish();
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Notifications c=mData.get(position);
        String type=c.getType();

      /*  if(type.equals("absence")){
            return 1;
        }else{
            return 1;
        }*/
        return 1;
    }

    public void removeNotification(int position) {
        NotificationDao dao = new NotificationDao(mContext);
        dao.deleteNotifiction(mData.get(position).getIdnotification());
        mData.remove(position);
        notifyDataSetChanged();
    }
    public void removeNotifications(Notifications notifications) {
        NotificationDao dao = new NotificationDao(mContext);
        dao.deleteNotifiction(notifications.getIdnotification());
        ///////////////////////////////////////////////
        Iterator<Notifications> it = mData.iterator();
        while (it.hasNext()) {
            Notifications notification = it.next();
            if (notification.getIdnotification()==notifications.getIdnotification()) {
                it.remove();
            }
        }

        //////////////



        mData.remove(notifications);
        notifyDataSetChanged();
    }
    public boolean existe(Notifications notification ,List<Notifications> liste){
        for(Notifications item:liste){
            if((item.getIdnotification()==notification.getIdnotification()) ){
               return true;
            }
        }
        return false ;
    }


    public void serchNotifi(String texte) {
        mDataSeach.removeAll( mDataSeach);
        for(Notifications item:mDataSave){
            if((item.getDate().toLowerCase().toLowerCase().contains(texte.toLowerCase()))
                    ||(item.getTitre().toLowerCase().toLowerCase().contains(texte.toLowerCase()))
                    ||(item.getMessage().toLowerCase().toLowerCase().contains(texte.toLowerCase()))
                    ||(item.getType().toLowerCase().toLowerCase().contains(texte.toLowerCase()))){

                if(!existe(item,mDataSeach))
                   mDataSeach.add(0,item);
            }
        }
       /// mData.retainAll( mData);
       // mData=mDataSeach;
        // if(mDataSeach.size()>0){
            mData= mDataSeach;
        Log.e("seach","seachs");
      //    }
        if(texte.isEmpty()){
            mData= mDataSave;
        }
        notifyDataSetChanged();
    }

    public static class TextViewHolder extends  RecyclerView.ViewHolder{
        TextView icone;
        TextView titre;
        TextView message;
        TextView date;
        RelativeLayout itemnotification;
        ImageView notiflu;
        public TextViewHolder(View itemView) {
            super(itemView);
            icone=(TextView)itemView.findViewById(R.id.icone);
            titre=(TextView)itemView.findViewById(R.id.titre);
            message=(TextView)itemView.findViewById(R.id.message);
            date=(TextView)itemView.findViewById(R.id.date);
            notiflu=(ImageView)itemView.findViewById(R.id.notiflu);
            itemnotification=(RelativeLayout)itemView.findViewById(R.id.itemnotification);

        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
      /*  int position = holder.getAdapterPosition();
        if (mData.get(position) != null) {
            mData.get(position).getPlayer().release();
        }*/
        super.onViewRecycled(holder);
    }




}
