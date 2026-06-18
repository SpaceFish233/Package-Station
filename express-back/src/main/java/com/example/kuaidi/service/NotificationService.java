package com.example.kuaidi.service;

import com.example.kuaidi.entity.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getAll();
    void create(Notification notification);
    void update(Notification notification);
    void delete(Integer id);
}
