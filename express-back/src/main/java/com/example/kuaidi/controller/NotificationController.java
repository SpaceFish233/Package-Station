package com.example.kuaidi.controller;

import com.example.kuaidi.entity.Notification;
import com.example.kuaidi.service.NotificationService;
import com.example.kuaidi.vo.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 获取通知列表（用户+管理员均可访问）
     */
    @GetMapping("/notifications")
    public ResultVO<List<Notification>> getAll() {
        return ResultVO.success(notificationService.getAll());
    }

    /**
     * 发布通知（管理员）
     */
    @PostMapping("/admin/notifications")
    public ResultVO<Void> create(@RequestBody Notification notification, HttpServletRequest request) {
        Integer publisherId = (Integer) request.getAttribute("staffId");
        notification.setPublisherId(publisherId);
        notificationService.create(notification);
        return ResultVO.success(null);
    }

    /**
     * 编辑通知（管理员）
     */
    @PutMapping("/admin/notifications/{id}")
    public ResultVO<Void> update(@PathVariable Integer id, @RequestBody Notification notification) {
        notification.setId(id);
        notificationService.update(notification);
        return ResultVO.success(null);
    }

    /**
     * 删除通知（管理员）
     */
    @DeleteMapping("/admin/notifications/{id}")
    public ResultVO<Void> delete(@PathVariable Integer id) {
        notificationService.delete(id);
        return ResultVO.success(null);
    }
}
