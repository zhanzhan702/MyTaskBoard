package com.example.mtbbackend.service.impl;

import com.example.mtbbackend.entity.Task;
import com.example.mtbbackend.mapper.TaskMapper;
import com.example.mtbbackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Task> getAllTasks() {
        // selectList(null) 表示查询所有
        return taskMapper.selectList(null);
    }

    @Override
    public Task createTask(Task task) {
        task.setId(UUID.randomUUID().toString());
        taskMapper.insert(task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if(task.getCompleted()){
            task.setFinishedTime();
        }
        taskMapper.updateById(task);
        return taskMapper.selectById(task.getId());
    }

    @Override
    public void deleteTask(String id) {
        taskMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getStats() {
        List<Task> allTasks=taskMapper.selectList(null);
        long completed=allTasks.stream().filter(Task::getCompleted).count();
        long uncompleted=allTasks.size() - completed;

        List<Map<String,Object>> dailyCounts=taskMapper.countByDate();
        Map<String,Object> stats=new LinkedHashMap<>();
        stats.put("completed",completed);
        stats.put("uncompleted",uncompleted);
        stats.put("daily",dailyCounts);
        return stats;
    }
}
