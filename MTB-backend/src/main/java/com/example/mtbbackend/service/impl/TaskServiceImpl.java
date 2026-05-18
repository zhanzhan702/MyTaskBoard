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
import java.util.stream.Collectors;

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

        //修改时间格式，避免Jackson 序列化 java.util.Date输出 ISO 8601 UTC 字符串比实际日期少了 8 小时，显示成了前一天的 UTC 时间
        List<Map<String, Object>> dailyFixed = dailyCounts.stream().map(row -> {
            Map<String, Object> newRow = new LinkedHashMap<>();
            Object dateObj = row.get("date");
            if (dateObj instanceof java.sql.Date sqlDate) {
                // 将 java.sql.Date 转为本地日期字符串 "yyyy-MM-dd"
                // 方法1：直接截取前10位（因为 toString() 返回 "yyyy-MM-dd"）
                newRow.put("date", sqlDate.toString());
                // 或者方法2：转为 LocalDate
                // newRow.put("date", sqlDate.toLocalDate().toString());
            } else {
                newRow.put("date", dateObj);
            }
            newRow.put("count", row.get("count"));
            return newRow;
        }).toList();

        Map<String,Object> stats=new LinkedHashMap<>();
        stats.put("completed",completed);
        stats.put("uncompleted",uncompleted);
        stats.put("daily",dailyFixed);
        return stats;
    }
}
