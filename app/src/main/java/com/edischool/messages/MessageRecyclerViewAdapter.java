package com.edischool.messages;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edischool.R;
import com.edischool.pojo.Message;

import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "MessageAdapter";
    public Context context;
    private List<Message> messageList;
    public PopupWindow popupWindow;
    private int[] androidColors;

    public MessageRecyclerViewAdapter(Context context, List<Message> absenceList) {
        this.context = context;
        this.messageList = absenceList;

    }

    @NonNull
    @Override
    public MessageRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessageRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = messageList.get(position);
        Message m = messageList.get(position);
        holder.tvBody.setText(m.getBody());
        holder.tvSubject.setText(m.getSubject());
        holder.tvSender.setText(m.getSender());
        holder.tvDate.setText(m.getDate());
        holder.tvUserText.setText(m.getSender().charAt(0) + "");
        this.androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int i2 = this.androidColors[position % this.androidColors.length];
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(i2);
        holder.tvUserText.setBackground(gradientDrawable);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public int getItemCount() {
        if (messageList != null) {
            return messageList.size();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvBody;
        public TextView tvDate;
        public TextView tvSender;
        public TextView tvSubject;
        public TextView tvUserText;
        public FrameLayout frameLayout;
        public Message mItem;

        public ViewHolder(View v) {
            super(v);
            tvBody = v.findViewById(R.id.msg_body);
            tvDate = v.findViewById(R.id.msg_date);
            tvSender = v.findViewById(R.id.msg_person);
            tvSubject = v.findViewById(R.id.msg_subject);
            frameLayout = v.findViewById(R.id.msg_recycle);
            tvUserText = v.findViewById(R.id.usertext);
            frameLayout.setOnClickListener(openPopup);
        }

        @Override
        public String toString() {
            return tvSubject.getText() + " '" + tvBody.getText();
        }

        private final View.OnClickListener openPopup = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.message_popup, null);
                inflate.setAnimation(AnimationUtils.loadAnimation(context, R.anim.animation));
                popupWindow = new PopupWindow(inflate, -1, -1, false);
                popupWindow.showAtLocation(inflate, 17, 0, 0);
                TextView tvSubject = inflate.findViewById(R.id.popup_subject);
                TextView tvName = inflate.findViewById(R.id.popup_name);
                TextView tvDetail = inflate.findViewById(R.id.popup_detail);
                TextView tvDate = inflate.findViewById(R.id.popup_date);
                RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.popup_close);
                tvDate.setText(mItem.getDate());
                tvDetail.setText(mItem.getBody());
                tvSubject.setText(mItem.getSubject());
                tvName.setText(mItem.getSender());
                popupWindow.setFocusable(false);
                popupWindow.showAsDropDown(inflate);
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        Log.d(TAG, "I click somewhere");
                        return true;
                    }
                });
            }
        };

    }
}
