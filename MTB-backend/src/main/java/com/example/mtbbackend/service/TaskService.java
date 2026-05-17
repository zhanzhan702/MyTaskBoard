package com.example.mtbbackend.service;

import com.example.mtbbackend.entity.Task;

import java.util.List;
import java.util.Map;

public interface TaskService {
    List<Task> getAllTasks();
    Task createTask(Task task);
    Task updateTask(Task task);
    void deleteTask(String id);
    Map<String, Object> getStats();//返回统计信息
}
