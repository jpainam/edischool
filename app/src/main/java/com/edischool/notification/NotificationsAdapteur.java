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
import com.edischool.pojo.Notification;
import com.edischool.utils.Constante;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapteur extends RecyclerView.Adapter<NotificationsAdapteur.ViewHolder> {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "NotificationAdapter";
    private Context mContext;
    private List<Notification> mDataSeach= new ArrayList<>();
    private List<Notification> notificationsList;// table qui permet la sauvegarde des donnée l
    private static final int DELEITEM=104;
    public NotificationsAdapteur(Context mContext, List<Notification> notifications) {
        this.mContext = mContext;
        this.notificationsList = notifications;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        if(viewType == 1) { //for empty layout// text envoie
            view=mInflater.inflate(R.layout.neux_notifications,parent,false);
            return new ViewHolder(view);
        } else{
            view=mInflater.inflate(R.layout.neux_notifications,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
           final  Notification n = notificationsList.get(position);
           holder.icone.setText(n.getNotificationType().substring(0, 1));

            holder.titre.setText(n.getNotificationTitle());
            holder.message.setText(n.getNotificationMessage());
            holder.date.setText(n.getCreateAt().toString());
            if(n.isRead()) {
                holder.notiflu.setImageResource(R.drawable.ic_check_red_24dp);
            }
             holder.itemnotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailNotifi= new Intent(mContext, DetailNotification.class);
                     detailNotifi.putExtra("notifications", n);
                    ((Activity) mContext).startActivityForResult(detailNotifi,DELEITEM);
                   // mContext.startActivity(detailNotifi);
                    //((Activity)mContext).overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//transition simple
                  // ((Activity)mContext).finish();
                }
            });




    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Notification c=notificationsList.get(position);
        String type=c.getNotificationType();

      /*  if(type.equals("absence")){
            return 1;
        }else{
            return 1;
        }*/
        return 1;
    }

    public void removeNotification(int position) {
        final Notification notif = notificationsList.get(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection(Constante.NOTIFICATIONS_COLLECTION).document(user.getPhoneNumber())
                .collection(Constante.USER_NOTIFICATIONS_COLLECTION).document(notif.getNotificationId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Notification delete " + notif.getNotificationTitle());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error deleting document " + e);
                    }
                });
    }
    public boolean existe(Notification notification ,List<Notification> liste){
        for(Notification item:liste){
            if((item.getNotificationId() == notification.getNotificationId()) ){
               return true;
            }
        }
        return false ;
    }


    public void serchNotifi(String texte) {
        mDataSeach.removeAll( mDataSeach);
        for(Notification item: notificationsList){
            if((item.getCreateAt().toString().toLowerCase().toLowerCase().contains(texte.toLowerCase()))
                    ||(item.getNotificationTitle().toLowerCase().toLowerCase().contains(texte.toLowerCase()))
                    ||(item.getNotificationMessage().toLowerCase().toLowerCase().contains(texte.toLowerCase()))
                    ||(item.getNotificationType().toLowerCase().toLowerCase().contains(texte.toLowerCase()))){

                if(!existe(item, mDataSeach))
                   mDataSeach.add(0, item);
            }
        }
       /// mData.retainAll( mData);
       // mData=mDataSeach;
        // if(mDataSeach.size()>0){
            notificationsList = mDataSeach;
        Log.e("seach","seachs");
      //    }
        if(texte.isEmpty()){
            // mData = notificationsList;
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView icone;
        TextView titre;
        TextView message;
        TextView date;
        RelativeLayout itemnotification;
        ImageView notiflu;
        public ViewHolder(View itemView) {
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
    public void onViewRecycled(@NonNull ViewHolder holder) {
      /*  int position = holder.getAdapterPosition();
        if (mData.get(position) != null) {
            mData.get(position).getPlayer().release();
        }*/
        super.onViewRecycled(holder);
    }




}
