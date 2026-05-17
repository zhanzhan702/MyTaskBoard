package com.example.mtbbackend.controller;

import com.example.mtbbackend.entity.Task;
import com.example.mtbbackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    //获取所有任务
    @GetMapping
    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }

    //创建任务，前端传JSON自动转换为Task对象
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    //更新任务，包含标记完成
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable String id,@RequestBody Task task) {
        task.setId(id);
        return taskService.updateTask(task);
    }

    //删除任务
    @PutMapping("/{id}")
    public Map<String,String> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        Map<String,String> result = new HashMap<>();
        result.put("message","删除成功");
        return result;
    }

    //获取统计数据
    public Map<String, Object> stats(){
        return taskService.getStats();
    }
}
