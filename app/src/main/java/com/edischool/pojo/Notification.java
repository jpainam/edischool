package com.edischool.pojo;

public class Notification {
    private String notificationMessage;
    private String senderPhoneNumber;  //senderUserEmail

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
}
