package com.example.kuaidi.service.impl;

import com.example.kuaidi.entity.Notification;
import com.example.kuaidi.exception.BusinessException;
import com.example.kuaidi.mapper.NotificationMapper;
import com.example.kuaidi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public List<Notification> getAll() {
        return notificationMapper.findAll();
    }

    @Override
    public void create(Notification notification) {
        if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
            throw new BusinessException("通知标题不能为空");
        }
        notificationMapper.insert(notification);
    }

    @Override
    public void update(Notification notification) {
        if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
            throw new BusinessException("通知标题不能为空");
        }
        Notification existing = notificationMapper.findById(notification.getId());
        if (existing == null) {
            throw new BusinessException("通知不存在");
        }
        notificationMapper.update(notification);
    }

    @Override
    public void delete(Integer id) {
        Notification existing = notificationMapper.findById(id);
        if (existing == null) {
            throw new BusinessException("通知不存在");
        }
        notificationMapper.deleteById(id);
    }
}
