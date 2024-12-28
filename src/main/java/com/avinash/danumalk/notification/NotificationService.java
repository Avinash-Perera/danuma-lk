package com.avinash.danumalk.notification;

import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;

import java.util.UUID;


public interface NotificationService {

    void createNotification(User user, BasePostEntity post, String message);

    void markNotificationAsRead(UUID notificationId);
}
