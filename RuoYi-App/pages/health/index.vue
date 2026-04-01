<template>
  <view class="health-container">
    <!-- 顶部状态预览 -->
    <view class="health-header">
      <view class="user-info">
        <text class="title">今日健康汇总</text>
        <text class="date">{{ todayDate }}</text>
      </view>
    </view>

    <!-- 健康建议卡片 -->
    <view class="advice-card" v-if="healthAdvice">
      <view class="advice-header">
        <uni-icons type="info-filled" size="20" color="#0081ff"></uni-icons>
        <text class="advice-title">健康周建议</text>
      </view>
      <text class="advice-content">{{ healthAdvice }}</text>
    </view>

    <!-- 核心指标卡片 - 大字版 -->
    <view class="data-section">
      <!-- 血压卡片 -->
      <view class="data-card bp" :class="{ 'warning-border': isBpCritical }" @click="goDetail('bp')">
        <view class="card-left">
          <uni-icons type="heart-filled" size="40" :color="isBpCritical ? '#e54d42' : '#e54d42'"></uni-icons>
          <text class="card-label">血压 (mmHg)</text>
        </view>
        <view class="card-right">
          <view class="value-row">
            <text class="main-value" :class="{ 'alarm-text': isBpCritical }">{{ latestHealth.systolicBp ||'--' }}/{{ latestHealth.diastolicBp ||'--' }}</text>
          </view>
          <text class="status-tag" :class="getBpStatusClass">{{ getBpStatusText }}</text>
        </view>
      </view>

      <!-- 心率卡片 -->
      <view class="data-card hr" @click="goDetail('hr')">
        <view class="card-left">
          <uni-icons type="pulse" size="40" color="#0081ff"></uni-icons>
          <text class="card-label">心率 (次/分)</text>
        </view>
        <view class="card-right">
          <text class="main-value">{{ latestHealth.heartRate || '--' }}</text>
          <text class="status-tag" :class="getHrStatusClass">{{ getHrStatusText }}</text>
        </view>
      </view>

      <!-- 血糖卡片 -->
      <view class="data-card bs" @click="goDetail('bs')">
        <view class="card-left">
          <uni-icons type="fire-filled" size="40" color="#f37b1d"></uni-icons>
          <text class="card-label">血糖 (mmol/L)</text>
        </view>
        <view class="card-right">
          <text class="main-value">{{ latestHealth.bloodSugar || '--' }}</text>
          <text class="status-tag" :class="getBsStatusClass">{{ getBsStatusText }}</text>
        </view>
      </view>
    </view>

    <!-- 健康服务快捷入口 -->
    <view class="service-nav">
      <view class="nav-item" @click="navTo('/pages/health/medication')">
        <view class="nav-icon med-icon-bg">
          <text class="nav-emoji">💊</text>
        </view>
        <text class="nav-label">用药提醒</text>
      </view>
      <view class="nav-item" @click="navTo('/pages/health/exam')">
        <view class="nav-icon exam-icon-bg">
          <text class="nav-emoji">🏥</text>
        </view>
        <text class="nav-label">体检预约</text>
      </view>
      <view class="nav-item" @click="navTo('/pages/health/alert_list')">
        <view class="nav-icon alert-icon-bg">
          <text class="nav-emoji">🚨</text>
        </view>
        <text class="nav-label">预警历史</text>
      </view>
    </view>

    <!-- 数据趋势 -->
    <view class="chart-section">
      <view class="section-header">
        <text class="section-title">趋势分析</text>
        <view class="range-selector">
          <text v-for="(item, index) in rangeOptions" :key="index" 
            :class="['range-item', currentRange === item.value ? 'active' : '']"
            @click="changeRange(item.value)">{{ item.label }}</text>
        </view>
      </view>

      <!-- 指标切换按钮组 -->
      <scroll-view class="metric-selector" scroll-x>
        <view v-for="(m, i) in metrics" :key="i"
          :class="['metric-item', currentMetric === m.key ? 'active' : '']"
          @click="changeMetric(m.key)">
          <uni-icons :type="m.icon" size="24" :color="currentMetric === m.key ? '#fff' : '#666'"></uni-icons>
          <text class="metric-name">{{ m.name }}</text>
        </view>
      </scroll-view>

      <!-- 图表区域 -->
      <view class="chart-box">
        <view v-if="loadingChart" class="chart-loading">
          <uni-load-more status="loading" :content-text="{contentrefresh: '加载趋势中...'}"></uni-load-more>
        </view>
        <view v-else-if="chartData.length === 0" class="empty-chart">
          <text>近{{ getRangeName }}暂无{{ getMetricName }}数据</text>
        </view>
        <view v-else class="chart-wrap">
          <canvas canvas-id="trendCanvas" id="trendCanvas" class="trend-canvas"></canvas>
          <text class="chart-tip">红色点表示数据状态异常（超标或剧变）</text>
        </view>
      </view>
    </view>

    <!-- 模拟上报按钮 (仅用于演示 Heath 2.0 算法效果) -->
    <view class="debug-section" v-if="isDebugMode">
      <button class="btn-mock" @click="mockReport">模拟突变数据上报(演示20%突变)</button>
    </view>

    <view class="bottom-tips">
      <text>注：数据仅供参考，不作为医疗诊断依据。</text>
    </view>
  </view>
</template>

<script>
  import { listHealthRecords, reportHealthData } from "@/api/health/record"

  export default {
    data() {
      return {
        todayDate: '',
        latestHealth: {},
        recordList: [],
        loading: false,
        loadingChart: false,
        currentMetric: 'blood_pressure',
        currentRange: 'week',
        chartData: [],
        healthAdvice: '基于您过去一周的血压波动（平均155/90），建议减少盐分摄入，保持作息规律。',
        isDebugMode: true, 
        metrics: [
          { name: '血压', key: 'blood_pressure', icon: 'heart-filled' },
          { name: '心率', key: 'heart_rate', icon: 'pulse' },
          { name: '血糖', key: 'blood_sugar', icon: 'fire-filled' },
          { name: '体温', key: 'temperature', icon: 'staff-filled' },
          { name: '血氧', key: 'blood_oxygen', icon: 'paperplane-filled' }
        ],
        rangeOptions: [
          { label: '周', value: 'week' },
          { label: '月', value: 'month' },
          { label: '季', value: 'quarter' }
        ]
      }
    },
    onShow() {
      this.initDate()
      this.getHealthData()
      this.getTrendData()
    },
    methods: {
      /** 页面跳转 */
      navTo(url) {
        uni.navigateTo({ url })
      },
      initDate() {
        const d = new Date()
        this.todayDate = `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`
      },
      getHealthData() {
        listHealthRecords({ pageSize: 10 }).then(res => {
          const allRows = res.rows || []
          if (allRows.length > 0) {
            this.latestHealth = allRows[0] 
          }
        })
      },
      getTrendData() {
        this.loadingChart = true
        listHealthRecords({ 
          recordType: this.currentMetric,
          pageSize: 20 
        }).then(res => {
          this.chartData = res.rows || []
          this.generateAdvice()
          this.$nextTick(() => {
            this.drawChart()
          })
        }).finally(() => {
          this.loadingChart = false
        })
      },
      generateAdvice() {
        if (this.chartData.length > 0) {
          const bplist = this.chartData.filter(r => r.systolicBp > 140)
          if (bplist.length > 3) {
            this.healthAdvice = "近一周检测到您多次血压偏高，建议严格遵医嘱服用降压药，并每日早晚固定时间测量。"
          } else {
            this.healthAdvice = "您的各项指标目前相对平稳，请继续保持清淡饮食，适度参与社区活动。"
          }
        }
      },
      changeMetric(key) {
        this.currentMetric = key
        this.getTrendData()
      },
      changeRange(val) {
        this.currentRange = val
        this.getTrendData()
      },
      drawChart() {
        if (this.chartData.length === 0) return
        const ctx = uni.createCanvasContext('trendCanvas', this)
        const width = uni.upx2px(600)
        const height = uni.upx2px(350)
        const paddingLeft = 60 
        const paddingBottom = 40 
        const paddingTop = 30
        const paddingRight = 30
        
        const rawPoints = this.chartData.map(row => {
          let val = 0;
          if (this.currentMetric === 'blood_pressure') val = row.systolicBp;
          else if (this.currentMetric === 'heart_rate') val = row.heartRate;
          else if (this.currentMetric === 'blood_sugar') val = row.bloodSugar;
          else if (this.currentMetric === 'temperature') val = row.temperature;
          else if (this.currentMetric === 'blood_oxygen') val = row.bloodOxygen;
          
          return {
            val: parseFloat(val) || 0,
            date: (row.collectTime || row.createTime || '').split(' ')[0].substring(5),
            status: row.dataStatus 
          };
        }).filter(p => p.val > 0).reverse(); 

        if (rawPoints.length === 0) return
        const dataValues = rawPoints.map(p => p.val);
        let maxVal = Math.max(...dataValues)
        let minVal = Math.min(...dataValues)
        if (maxVal === minVal) { maxVal += 10; minVal -= 10; }
        const range = maxVal - minVal
        const chartWidth = width - paddingLeft - paddingRight
        const chartHeight = height - paddingTop - paddingBottom

        ctx.setStrokeStyle('#ccc')
        ctx.setLineWidth(1)
        ctx.moveTo(paddingLeft, paddingTop)
        ctx.lineTo(paddingLeft, height - paddingBottom) 
        ctx.lineTo(width - paddingRight, height - paddingBottom) 
        ctx.stroke()

        ctx.beginPath()
        ctx.setStrokeStyle('#0081ff')
        ctx.setLineWidth(2)
        const stepX = rawPoints.length > 1 ? chartWidth / (rawPoints.length - 1) : 0
        rawPoints.forEach((p, i) => {
          const x = paddingLeft + i * stepX
          const y = height - paddingBottom - ((p.val - minVal) / range) * chartHeight
          if (i === 0) ctx.moveTo(x, y)
          else ctx.lineTo(x, y)
        })
        ctx.stroke()

        rawPoints.forEach((p, i) => {
          const x = paddingLeft + i * stepX
          const y = height - paddingBottom - ((p.val - minVal) / range) * chartHeight
          ctx.beginPath()
          ctx.setFillStyle(p.status == 2 ? '#e54d42' : '#fff')
          ctx.setStrokeStyle(p.status == 2 ? '#e54d42' : '#0081ff')
          ctx.arc(x, y, p.status == 2 ? 5 : 3, 0, 2 * Math.PI)
          ctx.fill()
          ctx.stroke()
        })
        ctx.draw()
      },
      mockReport() {
        const mockData = {
          systolicBp: 158,
          diastolicBp: 95,
          heartRate: 88,
          recordType: 'blood_pressure',
          collectTime: new Date().toISOString()
        }
        reportHealthData(mockData).then(res => {
          uni.showToast({ title: res.msg, icon: 'none', duration: 3000 })
          setTimeout(() => {
            this.getHealthData()
            this.getTrendData()
          }, 1000)
        })
      },
      goDetail(type) {
        this.$modal.msg("详情功能即将上线")
      }
    },
    computed: {
      getRangeName() { return this.rangeOptions.find(o => o.value === this.currentRange)?.label || '' },
      getMetricName() { return this.metrics.find(m => m.key === this.currentMetric)?.name || '' },
      isBpCritical() { return this.latestHealth.dataStatus == 2 },
      getBpStatusText() {
        const status = this.latestHealth.dataStatus
        if (status == 2) return '⚠️ 数据异常剧变'
        if (status == 1) return '偏高提醒'
        return this.latestHealth.systolicBp ? '状态平稳' : '暂无数据'
      },
      getBpStatusClass() {
        const status = this.latestHealth.dataStatus
        if (status == 2) return 'warn pulse'
        if (status == 1) return 'warn'
        return 'normal'
      },
      getHrStatusText() { return this.latestHealth.heartRate ? (this.latestHealth.dataStatus > 0 ? '心率偏快' : '正常') : '无数据' },
      getHrStatusClass() { return this.latestHealth.dataStatus > 0 ? 'warn' : 'normal' },
      getBsStatusText() { return this.latestHealth.bloodSugar ? '正常' : '无数据' },
      getBsStatusClass() { return 'normal' }
    }
  }
</script>

<style lang="scss" scoped>
  .health-container { background-color: #f8f9fb; min-height: 100vh; padding: 30rpx; }
  .health-header { margin-bottom: 30rpx; .title { font-size: 48rpx; font-weight: bold; } .date { font-size: 32rpx; color: #999; } }

  .advice-card {
    background: linear-gradient(135deg, #0081ff, #1cbbb4);
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 40rpx;
    color: #fff;
    box-shadow: 0 10rpx 30rpx rgba(0,129,255,0.2);
    .advice-header { display: flex; align-items: center; margin-bottom: 15rpx; .advice-title { margin-left: 10rpx; font-weight: bold; } }
    .advice-content { font-size: 28rpx; line-height: 1.6; }
  }

  .data-section {
    .data-card {
      background-color: #fff;
      border-radius: 24rpx;
      padding: 40rpx;
      margin-bottom: 30rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      transition: all 0.3s;
      &.warning-border { border: 2rpx solid #e54d42; box-shadow: 0 0 20rpx rgba(229,77,66,0.1); }
      .card-left { display: flex; flex-direction: column; align-items: center; width: 220rpx; .card-label { font-size: 30rpx; color: #666; margin-top: 15rpx; } }
      .card-right {
        text-align: right;
        .main-value { font-size: 60rpx; font-weight: bold; &.alarm-text { color: #e54d42; } }
        .status-tag { 
           margin-top: 10rpx; display: inline-block; padding: 6rpx 20rpx; border-radius: 30rpx; font-size: 24rpx;
           &.normal { background-color: #e7f7e9; color: #39b54a; }
           &.warn { background-color: #fff1f0; color: #e54d42; }
           &.pulse { animation: pulse-red 2s infinite; }
        }
      }
    }
  }

  @keyframes pulse-red {
    0% { transform: scale(1); box-shadow: 0 0 0 0 rgba(229,77,66,0.4); }
    70% { transform: scale(1.05); box-shadow: 0 0 0 10rpx rgba(229,77,66,0); }
    100% { transform: scale(1); box-shadow: 0 0 0 0 rgba(229,77,66,0); }
  }

  /* 健康服务快捷入口 */
  .service-nav {
    display: flex;
    justify-content: space-around;
    background: #fff;
    margin: 20rpx 0 40rpx;
    padding: 30rpx 16rpx;
    border-radius: 24rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
  }
  .nav-item {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .nav-icon {
    width: 90rpx;
    height: 90rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 12rpx;
  }
  .med-icon-bg { background: linear-gradient(135deg, #e8eaf6, #c5cae9); }
  .exam-icon-bg { background: linear-gradient(135deg, #e8f5e9, #c8e6c9); }
  .alert-icon-bg { background: linear-gradient(135deg, #fce4ec, #f8bbd0); }
  .nav-emoji { font-size: 38rpx; }
  .nav-label { font-size: 26rpx; color: #333; font-weight: 500; }

  .chart-section {
    background-color: #fff; border-radius: 24rpx; padding: 30rpx;
    .section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30rpx; .section-title { font-size: 36rpx; font-weight: bold; } }
    .metric-selector { white-space: nowrap; margin-bottom: 30rpx; .metric-item { display: inline-flex; flex-direction: column; align-items: center; padding: 20rpx 30rpx; margin-right: 20rpx; border-radius: 20rpx; &.active { background-color: #0081ff; .metric-name { color: #fff; } } } }
    .chart-box { height: 450rpx; .chart-wrap { width: 100%; display: flex; flex-direction: column; align-items: center; .trend-canvas { width: 600rpx; height: 350rpx; } .chart-tip { font-size: 20rpx; color: #999; margin-top: 10rpx; } } }
  }

  .debug-section { margin-top: 40rpx; .btn-mock { background-color: #333; color: #fff; border-radius: 50rpx; font-size: 28rpx; } }
  .bottom-tips { text-align: center; padding: 60rpx 0; color: #bbb; font-size: 26rpx; }
</style>
