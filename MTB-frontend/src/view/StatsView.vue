<template>
    <div class="stats-container">
        <h2>📊 任务统计看板</h2>
        <el-button @click="$router.push('/')">← 返回列表</el-button>

        <!-- Leafer 画布容器 -->
        <div ref="canvasContainer" class="canvas-box"></div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { taskApi } from '../api/taskApi'
// 引入 Leafer UI 核心类
import { Leafer, Rect, Text } from 'leafer-ui'

// 容器元素的引用
const canvasContainer = ref(null)

onMounted(async () => {
    // 1. 先获取统计数据
    let stats
    try {
        const response = await taskApi.getStats()
        stats = response.data
    } catch (error) {
        console.error('获取统计数据失败', error)
        return
    }

    // 2. 初始化 Leafer 应用
    const app = new Leafer({
        view: canvasContainer.value, // 挂载到DOM
        width: 800,
        height: 500,
        fill: '#f0f2f5' // 背景色
    })

    // 3. 绘制“已完成 vs 未完成”的简单柱状图
    drawCompletionChart(app, stats.completed, stats.uncompleted)

    // 4. 绘制“每日新增任务”折线图（用Rect模拟）
    drawDailyChart(app, stats.daily)
})

// 绘制完成情况柱状图
function drawCompletionChart(app, completed, uncompleted) {
    // 标题
    app.add(new Text({
        text: '任务完成情况',
        x: 300, y: 20,
        fontSize: 18,
        fill: '#333'
    }))

    const startX = 150, barWidth = 60, gap = 100
    const baseY = 300 // 柱子底部Y坐标

    // 已完成柱子
    app.add(new Rect({
        x: startX,
        y: baseY - completed * 20, // 每单位高度20px，可按比例缩放
        width: barWidth,
        height: completed * 20,
        fill: '#67C23A',
        cornerRadius: 4
    }))
    // 未完成柱子
    app.add(new Rect({
        x: startX + gap,
        y: baseY - uncompleted * 20,
        width: barWidth,
        height: uncompleted * 20,
        fill: '#909399',
        cornerRadius: 4
    }))

    // 数值标签
    app.add(new Text({ text: completed.toString(), x: startX, y: baseY - completed * 20 - 20, fontSize: 14, fill: '#67C23A' }))
    app.add(new Text({ text: uncompleted.toString(), x: startX + gap, y: baseY - uncompleted * 20 - 20, fontSize: 14, fill: '#909399' }))
    // 图例
    app.add(new Text({ text: '已完成', x: startX, y: baseY + 10, fontSize: 12 }))
    app.add(new Text({ text: '未完成', x: startX + gap, y: baseY + 10, fontSize: 12 }))
}

// 绘制每日任务统计（简易柱状图）
function drawDailyChart(app, dailyData) {
    if (!dailyData || dailyData.length === 0) return

    const startX = 150, barWidth = 30, gap = 40
    const baseY = 480 // 坐标轴底部
    // 找到最大count，用于缩放高度
    const maxCount = Math.max(...dailyData.map(d => d.count))
    const scale = 100 / maxCount // 高度缩放因子

    // 绘制坐标轴（简单线条）
    app.add(new Rect({ x: startX, y: baseY, width: dailyData.length * (barWidth + gap), height: 2, fill: '#333' }))

    dailyData.forEach((item, index) => {
        const x = startX + index * (barWidth + gap)
        const height = item.count * scale
        // 柱子
        app.add(new Rect({
            x,
            y: baseY - height,
            width: barWidth,
            height,
            fill: '#409EFF',
            cornerRadius: 2
        }))
        // 数值标签
        app.add(new Text({
            text: item.count.toString(),
            x,
            y: baseY - height - 16,
            fontSize: 12,
            fill: '#409EFF'
        }))
        // 日期标签（取后5位，原日期格式为'yyyy-mm-dd'）
        const dateShort = item.date ? item.date.substring(5) : ''
        app.add(new Text({
            text: dateShort,
            x,
            y: baseY + 5,
            fontSize: 10,
            fill: '#666',
            //rotation: -10 // 倾斜显示，避免重叠
        }))
    })
}
</script>

<style scoped>
.stats-container {
    padding: 20px;
    max-width: 900px;
    margin: 0 auto;
}

.canvas-box {
    width: 800px;
    height: 500px;
    margin-top: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    overflow: hidden;
}
</style>