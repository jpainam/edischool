package com.edischool.pojo;

import java.io.Serializable;

public class Notification implements Serializable {
    private String notificationMessage;
    private String notificationTitle;
    private String notificationType;
    private String createAt;
    private String notificationId;
    private boolean isRead;

    private String senderPhoneNumber;  //senderUserEmail

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public Notification(){

    }
    public Notification(String notificationMessage, String senderPhoneNumber) {
        this.notificationMessage = notificationMessage;
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getCreateAt() {
        return createAt;
    }



    public void setCreateAt(String serverTimestamp) {
        createAt = serverTimestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}
