<template>
  <div class="app-container dashboard">
    <!-- 核心指标卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :xs="12" :sm="6" v-for="(card, index) in statCards" :key="index">
        <el-card shadow="hover" :class="['stat-card', card.class]">
          <div class="stat-card-inner">
            <div class="stat-info">
              <div class="stat-label">{{ card.label }}</div>
              <div class="stat-value">{{ card.value }}</div>
            </div>
            <el-icon class="stat-icon" :size="48"><component :is="card.icon" /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：额外指标 -->
    <el-row :gutter="16" class="stat-cards" style="margin-top: 16px">
      <el-col :xs="12" :sm="6" v-for="(card, index) in statCards2" :key="index">
        <el-card shadow="hover" :class="['stat-card', card.class]">
          <div class="stat-card-inner">
            <div class="stat-info">
              <div class="stat-label">{{ card.label }}</div>
              <div class="stat-value">{{ card.value }}</div>
            </div>
            <el-icon class="stat-icon" :size="48"><component :is="card.icon" /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover">
          <template #header>
            <div class="chart-header">
              <span>订单趋势</span>
              <el-radio-group v-model="trendDays" size="small" @change="loadOrderTrend">
                <el-radio-button :value="7">近7天</el-radio-button>
                <el-radio-button :value="30">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="orderTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover">
          <template #header><span>预警类型分布</span></template>
          <div ref="alertChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover">
          <template #header><span>订单状态分布</span></template>
          <div ref="orderStatusChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover">
          <template #header><span>服务满意度分布</span></template>
          <div ref="satisfactionChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import { ref, reactive, onMounted, onBeforeUnmount, nextTick, computed, markRaw } from 'vue'
import * as echarts from 'echarts'
import { getOverview, getOrderTrend, getAlertStats, getOrderStatusDistribution, getSatisfactionDistribution } from '@/api/admin/dashboard'
import { User, OfficeBuilding, List, WarnTriangleFilled, Star, ShoppingCart } from '@element-plus/icons-vue'

// 图表 DOM 引用
const orderTrendChart = ref(null)
const alertChart = ref(null)
const orderStatusChart = ref(null)
const satisfactionChart = ref(null)

// 图表实例
let trendInstance = null
let alertInstance = null
let orderStatusInstance = null
let satisfactionInstance = null

// 概览数据
const overview = reactive({
  totalElderly: 0,
  totalProviders: 0,
  totalOrders: 0,
  todayOrders: 0,
  todayAlerts: 0,
  avgSatisfaction: 0
})

const trendDays = ref(7)

// 卡片数据
const statCards = computed(() => [
  { label: '老人总数', value: overview.totalElderly, icon: markRaw(User), class: 'card-primary' },
  { label: '服务商数', value: overview.totalProviders, icon: markRaw(OfficeBuilding), class: 'card-success' },
  { label: '订单总数', value: overview.totalOrders, icon: markRaw(List), class: 'card-warning' },
  { label: '今日订单', value: overview.todayOrders, icon: markRaw(ShoppingCart), class: 'card-info' }
])

const statCards2 = computed(() => [
  { label: '今日预警', value: overview.todayAlerts, icon: markRaw(WarnTriangleFilled), class: 'card-danger' },
  { label: '平均满意度', value: overview.avgSatisfaction + ' 分', icon: markRaw(Star), class: 'card-star' }
])

// 订单状态映射
const orderStatusMap = { 0: '待接单', 1: '已接单', 2: '服务中', 3: '已完成', 4: '已取消', 5: '已评价' }

// 加载概览数据
function loadOverview() {
  getOverview().then(res => {
    Object.assign(overview, res.data)
  })
}

// 加载订单趋势
function loadOrderTrend() {
  getOrderTrend(trendDays.value).then(res => {
    const data = res.data || []
    const dates = data.map(item => item.date)
    const counts = data.map(item => item.count)
    if (trendInstance) {
      trendInstance.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: dates, boundaryGap: false },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{
          name: '订单数',
          type: 'line',
          smooth: true,
          areaStyle: { opacity: 0.3 },
          itemStyle: { color: '#409EFF' },
          data: counts
        }]
      })
    }
  })
}

// 加载预警统计
function loadAlertStats() {
  getAlertStats().then(res => {
    const data = res.data || []
    if (alertInstance) {
      alertInstance.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: '0', left: 'center' },
        color: ['#F56C6C', '#E6A23C', '#409EFF', '#67C23A', '#909399'],
        series: [{
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: true,
          label: { show: true, formatter: '{b}\n{d}%' },
          data: data
        }]
      })
    }
  })
}

// 加载订单状态分布
function loadOrderStatus() {
  getOrderStatusDistribution().then(res => {
    const data = res.data || []
    const mapped = data.map(item => ({
      name: orderStatusMap[item.name] || ('状态' + item.name),
      value: item.value
    }))
    if (orderStatusInstance) {
      orderStatusInstance.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: '0', left: 'center' },
        color: ['#909399', '#409EFF', '#E6A23C', '#67C23A', '#F56C6C', '#a855f7'],
        series: [{
          type: 'pie',
          radius: '60%',
          center: ['50%', '45%'],
          label: { show: true, formatter: '{b}\n{c}' },
          data: mapped
        }]
      })
    }
  })
}

// 加载满意度分布
function loadSatisfaction() {
  getSatisfactionDistribution().then(res => {
    const data = res.data || []
    const ratings = data.map(item => item.name + '星')
    const counts = data.map(item => item.value)
    if (satisfactionInstance) {
      satisfactionInstance.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: ratings },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{
          type: 'bar',
          barWidth: '40%',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#67C23A' },
              { offset: 1, color: '#b3e19d' }
            ]),
            borderRadius: [4, 4, 0, 0]
          },
          data: counts
        }]
      })
    }
  })
}

// 窗口缩放自适应
function handleResize() {
  trendInstance && trendInstance.resize()
  alertInstance && alertInstance.resize()
  orderStatusInstance && orderStatusInstance.resize()
  satisfactionInstance && satisfactionInstance.resize()
}

onMounted(() => {
  loadOverview()
  nextTick(() => {
    trendInstance = echarts.init(orderTrendChart.value)
    alertInstance = echarts.init(alertChart.value)
    orderStatusInstance = echarts.init(orderStatusChart.value)
    satisfactionInstance = echarts.init(satisfactionChart.value)
    loadOrderTrend()
    loadAlertStats()
    loadOrderStatus()
    loadSatisfaction()
    window.addEventListener('resize', handleResize)
  })
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  trendInstance && trendInstance.dispose()
  alertInstance && alertInstance.dispose()
  orderStatusInstance && orderStatusInstance.dispose()
  satisfactionInstance && satisfactionInstance.dispose()
})
</script>

<style scoped>
.dashboard {
  padding: 16px;
}

.stat-cards .el-col {
  margin-bottom: 0;
}

.stat-card {
  border-radius: 8px;
  overflow: hidden;
}

.stat-card :deep(.el-card__body) {
  padding: 20px;
}

.stat-card-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.stat-icon {
  opacity: 0.15;
}

.card-primary .stat-value { color: #409EFF; }
.card-primary .stat-icon { color: #409EFF; }
.card-success .stat-value { color: #67C23A; }
.card-success .stat-icon { color: #67C23A; }
.card-warning .stat-value { color: #E6A23C; }
.card-warning .stat-icon { color: #E6A23C; }
.card-info .stat-value { color: #909399; }
.card-info .stat-icon { color: #909399; }
.card-danger .stat-value { color: #F56C6C; }
.card-danger .stat-icon { color: #F56C6C; }
.card-star .stat-value { color: #f59e0b; }
.card-star .stat-icon { color: #f59e0b; }

.chart-container {
  width: 100%;
  height: 350px;
}

.chart-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
