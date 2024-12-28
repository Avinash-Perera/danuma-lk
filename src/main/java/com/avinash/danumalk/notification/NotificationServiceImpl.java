package com.avinash.danumalk.notification;

import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void createNotification(User user, BasePostEntity post, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .post(post)
                .message(message)
                .status(NotificationStatus.UNREAD)
                .build();

        notificationRepository.save(notification);
        var user_id = post.getOwner().getId().toString();

        // Send real-time notification to the user over WebSocket
        messagingTemplate.convertAndSendToUser(
                user_id,
                "/notifications",
                notification);
    }

    @Override
    @Transactional
    public void markNotificationAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        notification.setStatus(NotificationStatus.READ);
        notificationRepository.save(notification);
    }
}
