import axios from 'axios'

//创建axios实例，统一配置基础路径
const api = axios.create({
  baseURL: 'http://localhost:8080/api', //后端地址
  timeout: 5000,
})

//导出任务相关的API方法
export const taskApi = {
  //获取所有任务
  getAll: () => api.get('/tasks'),
  //创建任务，参数是一个对象，包含了title，description，completed，createTime
  create: (task) => api.post('/tasks', task),
  //更新任务，id是任务id，task是更新的字段
  update: (id, task) => api.put(`/tasks/${id}`, task),
  //删除任务
  delete: (id) => api.delete(`/tasks/${id}`),
  //获取统计数据
  getStats: () => api.get('/tasks/stats'),
}
