package com.edischool.publicite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Notification;
import com.edischool.pojo.Publicite;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PubAdapteur extends RecyclerView.Adapter<PubAdapteur.ViewHolder> {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "NotificationAdapter";
    private Context mContext;
    private List<Publicite> publiciteslist;
    private List<Publicite> mDataSave;// table qui permet la sauvegarde des donnée l


    private static final int DELEITEM=104;
    public PubAdapteur(Context mContext, List<Publicite> publicites) {
        this.mContext = mContext;
        this.publiciteslist = publicites;
        this. mDataSave= publicites;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        if(viewType == 1) { //for empty layout// text envoie
            view=mInflater.inflate(R.layout.pub_item,parent,false);
            return new ViewHolder(view);
        } else{
            view=mInflater.inflate(R.layout.pub_item,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
           final  Publicite pub = publiciteslist.get(position);
           holder.textpub.setText(pub.getText());
          // holder.imagepub.setImageResource(pub.getImage());
           holder.imagepub.setImageResource(R.drawable.female_avatar);

           holder.single_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
          /*  Intent detailNotifi= new Intent(mContext, DetailNotification.class);
                    // detailNotifi.putExtra("notifications", n);
                    detailNotifi.putExtra("titre", n.getNotificationTitle());
                    detailNotifi.putExtra("message", n.getNotificationMessage());
                    detailNotifi.putExtra("type", n.getNotificationType());
                    detailNotifi.putExtra("numero", n.getSenderPhoneNumber());
                    detailNotifi.putExtra("id", n.getNotificationId());
                    detailNotifi.putExtra("date", n.getCreateAt());
                    ((Activity) mContext).startActivityForResult(detailNotifi,DELEITEM);
*/

                }
            });




    }

    @Override
    public int getItemCount() {
        return publiciteslist.size();
    }

    @Override
    public int getItemViewType(int position) {
        Publicite c=publiciteslist.get(position);
        String type=c.getType();

      /*  if(type.equals("absence")){
            return 1;
        }else{
            return 1;
        }*/
        return 1;
    }


    public boolean existe(Notification notification ,List<Notification> liste){
        for(Notification item:liste){
            if((item.getNotificationId() == notification.getNotificationId()) ){
               return true;
            }
        }
        return false ;
    }




    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView textpub;
        LinearLayout single_layout;
        CircleImageView imagepub;
        public ViewHolder(View itemView) {
            super(itemView);
            textpub=(TextView)itemView.findViewById(R.id.textpub);
            imagepub=(CircleImageView)itemView.findViewById(R.id.imagepub);
            single_layout=(LinearLayout)itemView.findViewById(R.id.single_layout);
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
