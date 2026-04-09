<template>
  <view class="report-container">
    <!-- 顶部标题 -->
    <view class="report-header">
      <text class="title">健康报告</text>
      <text class="sub-title">{{ rangeName }} · {{ todayDate }}</text>
    </view>

    <!-- 时间范围切换 -->
    <view class="range-bar">
      <view v-for="(r, i) in ranges" :key="i"
        :class="['range-tab', currentRange === r.value ? 'active' : '']"
        @click="changeRange(r.value)">
        <text>{{ r.label }}</text>
      </view>
    </view>

    <!-- 指标摘要卡片组 -->
    <view class="summary-section">
      <view class="summary-card" v-for="(m, i) in metricSummaries" :key="i"
        :class="{ 'card-active': currentMetric === m.key }"
        @click="changeMetric(m.key)">
        <text class="summary-label">{{ m.name }}</text>
        <view class="summary-stats">
          <view class="stat-item">
            <text class="stat-val">{{ m.avg || '--' }}</text>
            <text class="stat-name">平均</text>
          </view>
          <view class="stat-item">
            <text class="stat-val high">{{ m.max || '--' }}</text>
            <text class="stat-name">最高</text>
          </view>
          <view class="stat-item">
            <text class="stat-val low">{{ m.min || '--' }}</text>
            <text class="stat-name">最低</text>
          </view>
          <view class="stat-item">
            <text class="stat-val warn" v-if="m.abnormal > 0">{{ m.abnormal }}</text>
            <text class="stat-val" v-else>0</text>
            <text class="stat-name">异常</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 趋势图 -->
    <view class="chart-section">
      <view class="chart-title">
        <text>{{ getMetricName }} 趋势</text>
      </view>
      <view class="chart-box">
        <view v-if="loading" class="chart-loading">
          <uni-load-more status="loading"></uni-load-more>
        </view>
        <view v-else-if="chartData.length === 0" class="empty-chart">
          <text>暂无{{ getMetricName }}数据</text>
        </view>
        <view v-else class="chart-wrap">
          <canvas canvas-id="reportCanvas" id="reportCanvas" class="report-canvas"></canvas>
        </view>
      </view>
    </view>

    <!-- 健康建议 -->
    <view class="advice-section">
      <view class="advice-header">
        <text class="advice-icon">💡</text>
        <text class="advice-title">健康建议</text>
      </view>
      <text class="advice-text">{{ advice }}</text>
    </view>

    <view class="footer-tip">
      <text>以上数据仅供参考，不作为医疗诊断依据。</text>
    </view>
  </view>
</template>

<script>
import { listHealthRecords } from '@/api/health/record'

export default {
  data() {
    return {
      currentRange: 'week',
      currentMetric: 'blood_pressure',
      ranges: [
        { label: '最近一周', value: 'week' },
        { label: '最近一月', value: 'month' },
        { label: '最近三月', value: 'quarter' }
      ],
      metrics: [
        { name: '血压', key: 'blood_pressure' },
        { name: '心率', key: 'heart_rate' },
        { name: '血糖', key: 'blood_sugar' },
        { name: '体温', key: 'temperature' },
        { name: '血氧', key: 'blood_oxygen' }
      ],
      chartData: [],
      allRecords: [],
      loading: false,
      todayDate: '',
      advice: ''
    }
  },
  onLoad(options) {
    if (options.metric) {
      // 从看板卡片点击进入时，映射为对应的 recordType
      const typeMap = { bp: 'blood_pressure', hr: 'heart_rate', bs: 'blood_sugar', temp: 'temperature', spo2: 'blood_oxygen' }
      this.currentMetric = typeMap[options.metric] || options.metric
    }
    const d = new Date()
    this.todayDate = `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`
    this.loadData()
  },
  computed: {
    rangeName() {
      return this.ranges.find(r => r.value === this.currentRange)?.label || ''
    },
    getMetricName() {
      return this.metrics.find(m => m.key === this.currentMetric)?.name || ''
    },
    metricSummaries() {
      return this.metrics.map(m => {
        const values = this.allRecords.map(r => {
          if (m.key === 'blood_pressure') return parseFloat(r.systolicBp) || 0
          if (m.key === 'heart_rate') return parseFloat(r.heartRate) || 0
          if (m.key === 'blood_sugar') return parseFloat(r.bloodSugar) || 0
          if (m.key === 'temperature') return parseFloat(r.temperature) || 0
          if (m.key === 'blood_oxygen') return parseFloat(r.bloodOxygen) || 0
          return 0
        }).filter(v => v > 0)

        if (values.length === 0) return { ...m, avg: null, max: null, min: null, abnormal: 0 }

        const isDecimal = m.key === 'blood_sugar' || m.key === 'temperature'
        const avg = (values.reduce((a, b) => a + b, 0) / values.length)
        const abnormalCount = this.allRecords.filter(r => r.dataStatus > 0).length

        return {
          ...m,
          avg: isDecimal ? avg.toFixed(1) : Math.round(avg),
          max: isDecimal ? Math.max(...values).toFixed(1) : Math.max(...values),
          min: isDecimal ? Math.min(...values).toFixed(1) : Math.min(...values),
          abnormal: m.key === this.currentMetric ? abnormalCount : 0
        }
      })
    }
  },
  methods: {
    getBeginTime() {
      const now = new Date()
      let d = new Date(now)
      if (this.currentRange === 'week') d.setDate(d.getDate() - 7)
      else if (this.currentRange === 'month') d.setMonth(d.getMonth() - 1)
      else if (this.currentRange === 'quarter') d.setMonth(d.getMonth() - 3)
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
    },
    loadData() {
      this.loading = true
      listHealthRecords({
        beginTime: this.getBeginTime(),
        pageSize: 100
      }).then(res => {
        this.allRecords = res.rows || []
        this.filterChart()
        this.generateAdvice()
      }).finally(() => {
        this.loading = false
      })
    },
    filterChart() {
      this.chartData = this.allRecords.map(row => {
        let val = 0
        if (this.currentMetric === 'blood_pressure') val = row.systolicBp
        else if (this.currentMetric === 'heart_rate') val = row.heartRate
        else if (this.currentMetric === 'blood_sugar') val = row.bloodSugar
        else if (this.currentMetric === 'temperature') val = row.temperature
        else if (this.currentMetric === 'blood_oxygen') val = row.bloodOxygen
        return {
          val: parseFloat(val) || 0,
          date: (row.collectTime || row.createTime || '').split(' ')[0].substring(5),
          status: row.dataStatus
        }
      }).filter(p => p.val > 0).reverse()

      this.$nextTick(() => { this.drawChart() })
    },
    changeRange(val) {
      this.currentRange = val
      this.loadData()
    },
    changeMetric(key) {
      this.currentMetric = key
      this.filterChart()
      this.generateAdvice()
    },
    generateAdvice() {
      const abnormals = this.allRecords.filter(r => r.dataStatus > 0)
      if (abnormals.length > 3) {
        this.advice = `在${this.rangeName}内检测到 ${abnormals.length} 次${this.getMetricName}异常记录，建议尽快就诊或调整用药方案，并每日定时自测。`
      } else if (abnormals.length > 0) {
        this.advice = `${this.rangeName}内有 ${abnormals.length} 次轻微异常，总体趋势尚可，建议继续保持规律作息和饮食。`
      } else {
        this.advice = `${this.rangeName}内各项指标平稳正常，请继续保持健康的生活习惯，适当参加社区活动。`
      }
    },
    drawChart() {
      if (this.chartData.length === 0) return
      const ctx = uni.createCanvasContext('reportCanvas', this)
      const width = uni.upx2px(660)
      const height = uni.upx2px(380)
      const pl = 70, pb = 50, pt = 20, pr = 20

      const points = this.chartData
      const vals = points.map(p => p.val)
      let maxV = Math.max(...vals), minV = Math.min(...vals)
      if (maxV === minV) { maxV += 10; minV -= 10 }
      const range = maxV - minV
      const cw = width - pl - pr, ch = height - pt - pb

      ctx.clearRect(0, 0, width, height)

      // Y 轴
      const ySteps = 4
      ctx.setFontSize(10)
      ctx.setFillStyle('#999')
      for (let i = 0; i <= ySteps; i++) {
        const yVal = minV + (range / ySteps) * i
        const yPos = height - pb - (ch / ySteps) * i
        const isDecimal = this.currentMetric === 'blood_sugar' || this.currentMetric === 'temperature'
        ctx.fillText(isDecimal ? yVal.toFixed(1) : Math.round(yVal).toString(), 4, yPos + 4)
        ctx.setStrokeStyle('#f0f0f0')
        ctx.setLineWidth(0.5)
        ctx.beginPath()
        ctx.moveTo(pl, yPos)
        ctx.lineTo(width - pr, yPos)
        ctx.stroke()
      }

      // 坐标轴
      ctx.setStrokeStyle('#ccc')
      ctx.setLineWidth(1)
      ctx.beginPath()
      ctx.moveTo(pl, pt)
      ctx.lineTo(pl, height - pb)
      ctx.lineTo(width - pr, height - pb)
      ctx.stroke()

      // 渐变填充
      const stepX = points.length > 1 ? cw / (points.length - 1) : 0
      ctx.beginPath()
      ctx.moveTo(pl, height - pb)
      points.forEach((p, i) => {
        const x = pl + i * stepX
        const y = height - pb - ((p.val - minV) / range) * ch
        ctx.lineTo(x, y)
      })
      ctx.lineTo(pl + (points.length - 1) * stepX, height - pb)
      ctx.closePath()
      ctx.setFillStyle('rgba(0,129,255,0.08)')
      ctx.fill()

      // 折线
      ctx.beginPath()
      ctx.setStrokeStyle('#0081ff')
      ctx.setLineWidth(2)
      points.forEach((p, i) => {
        const x = pl + i * stepX
        const y = height - pb - ((p.val - minV) / range) * ch
        if (i === 0) ctx.moveTo(x, y)
        else ctx.lineTo(x, y)
      })
      ctx.stroke()

      // 数据点 + X 轴
      const showEvery = points.length > 10 ? Math.ceil(points.length / 6) : 1
      points.forEach((p, i) => {
        const x = pl + i * stepX
        const y = height - pb - ((p.val - minV) / range) * ch
        ctx.beginPath()
        ctx.setFillStyle(p.status == 2 ? '#e54d42' : '#fff')
        ctx.setStrokeStyle(p.status == 2 ? '#e54d42' : '#0081ff')
        ctx.arc(x, y, p.status == 2 ? 5 : 3, 0, 2 * Math.PI)
        ctx.fill()
        ctx.stroke()
        if (i % showEvery === 0 || i === points.length - 1) {
          ctx.setFillStyle('#999')
          ctx.setFontSize(9)
          ctx.fillText(p.date, x - 14, height - pb + 16)
        }
      })
      ctx.draw()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-container {
  min-height: 100vh;
  background: #f5f6fa;
  padding: 30rpx;
}

.report-header {
  margin-bottom: 30rpx;
  .title { font-size: 44rpx; font-weight: bold; color: #222; }
  .sub-title { font-size: 28rpx; color: #999; margin-left: 16rpx; }
}

.range-bar {
  display: flex;
  gap: 16rpx;
  margin-bottom: 30rpx;
  .range-tab {
    flex: 1;
    text-align: center;
    padding: 16rpx 0;
    background: #fff;
    border-radius: 16rpx;
    font-size: 28rpx;
    color: #666;
    box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.04);
    &.active {
      background: linear-gradient(135deg, #0081ff, #1cbbb4);
      color: #fff;
      font-weight: bold;
    }
  }
}

.summary-section {
  margin-bottom: 30rpx;
}
.summary-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx 24rpx;
  margin-bottom: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.04);
  border-left: 6rpx solid transparent;
  &.card-active {
    border-left-color: #0081ff;
    background: linear-gradient(90deg, #f0f7ff, #fff);
  }
  .summary-label {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 16rpx;
    display: block;
  }
  .summary-stats {
    display: flex;
    justify-content: space-around;
  }
  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .stat-val {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    &.high { color: #e54d42; }
    &.low { color: #0081ff; }
    &.warn { color: #f37b1d; }
  }
  .stat-name {
    font-size: 22rpx;
    color: #999;
    margin-top: 6rpx;
  }
}

.chart-section {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx 20rpx;
  margin-bottom: 30rpx;
  .chart-title {
    font-size: 32rpx;
    font-weight: bold;
    margin-bottom: 20rpx;
    color: #333;
  }
  .chart-box {
    height: 420rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    .report-canvas {
      width: 660rpx;
      height: 380rpx;
    }
  }
  .empty-chart {
    color: #ccc;
    font-size: 28rpx;
  }
}

.advice-section {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 20rpx;
  padding: 30rpx;
  color: #fff;
  margin-bottom: 30rpx;
  .advice-header {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
    .advice-icon { font-size: 36rpx; margin-right: 10rpx; }
    .advice-title { font-size: 30rpx; font-weight: bold; }
  }
  .advice-text {
    font-size: 28rpx;
    line-height: 1.6;
  }
}

.footer-tip {
  text-align: center;
  padding: 40rpx 0;
  color: #ccc;
  font-size: 24rpx;
}
</style>
