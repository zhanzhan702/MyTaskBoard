import { createRouter, createWebHistory } from 'vue-router'
import TaskList from '@/view/TaskList.vue'
import StatsView from '@/view/StatsView.vue'

const routes = [
  { path: '/', name: 'TaskList', component: TaskList },
  { path: '/stats', name: 'StatsView', component: StatsView },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
