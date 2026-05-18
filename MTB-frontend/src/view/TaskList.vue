<template>
    <div class="task-container">
        <h2>📋 任务管理</h2>

        <!-- 新增任务区域 -->
        <div>
            <div class="add-task">
                <el-input v-model="newTaskTitle" placeholder="输入任务标题" style="width: 300px; margin-right: 10px" />
                <el-button type="primary" @click="addTask">新增任务</el-button>
                <el-button @click="$router.push('/stats')">📊 查看统计</el-button>
            </div>
            <el-input v-model="newTaskDescription" placeholder="输入任务描述" type="textarea"
                style="width: 80%; margin-right: 10px" />
        </div>

        <!-- 任务列表表格 -->
        <el-table :data="tasks" style="margin-top: 20px" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="title" label="任务" />
            <el-table-column prop="description" label="内容" />
            <el-table-column label="状态">
                <template #default="{ row }">
                    <el-tag :type="row.completed ? 'success' : 'info'">
                        {{ row.completed ? '已完成' : '未完成' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建日期" width="120" />
            <el-table-column label="操作" width="180">
                <template #default="{ row }">
                    <el-button size="small" @click="toggleComplete(row)">
                        {{ row.completed ? '标记未完成' : '标记完成' }}
                    </el-button>
                    <el-button size="small" type="danger" @click="deleteTask(row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { taskApi } from '../api/taskApi'
import { descriptionItemProps } from 'element-plus'

// 响应式数据
const tasks = ref([])  // 任务列表
const newTaskTitle = ref('') // 新任务标题输入
const newTaskDescription = ref('')//新任务描述

// 页面加载时获取任务列表
onMounted(() => {
    fetchTasks()
})

// 获取所有任务
async function fetchTasks() {
    try {
        const response = await taskApi.getAll()
        tasks.value = response.data
    } catch (error) {
        console.error('获取任务失败:', error)
    }
}

// 新增任务
async function addTask() {
    if (!newTaskTitle.value.trim()) {
        alert('任务标题不能为空')
        return
    }
    try {
        const newTask = {
            title: newTaskTitle.value,
            description: newTaskDescription.value,
            completed: false,
            // createTime由后端自动设置为当前日期，也可以前端传 '2025-05-17'
        }
        await taskApi.create(newTask)
        newTaskTitle.value = '' // 清空输入框
        newTaskDescription.value = ''
        await fetchTasks()      // 刷新列表
    } catch (error) {
        console.error('创建任务失败:', error)
    }
}

// 切换完成状态
async function toggleComplete(task) {
    try {
        const updated = { ...task, completed: !task.completed }
        await taskApi.update(task.id, updated)
        await fetchTasks()
    } catch (error) {
        console.error('更新状态失败:', error)
    }
}

// 删除任务
async function deleteTask(id) {
    try {
        await taskApi.delete(id)
        await fetchTasks()
    } catch (error) {
        console.error('删除失败:', error)
    }
}
</script>

<style scoped>
.task-container {
    max-width: 900px;
    margin: 0 auto;
    padding: 20px;
}

.add-task {
    display: flex;
    align-items: center;
}
</style>