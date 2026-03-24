<template>
  <view class="health-container">
    <!-- 顶部状态预览 -->
    <view class="health-header">
      <view class="user-info">
        <text class="title">今日健康汇总</text>
        <text class="date">{{ todayDate }}</text>
      </view>
    </view>

    <!-- 核心指标卡片 - 大字版 -->
    <view class="data-section">
      <!-- 血压卡片 -->
      <view class="data-card bp" @click="goDetail('bp')">
        <view class="card-left">
          <uni-icons type="heart-filled" size="40" color="#e54d42"></uni-icons>
          <text class="card-label">血压 (mmHg)</text>
        </view>
        <view class="card-right">
          <text class="main-value">{{ latestHealth.systolicBp ||'--' }}/{{ latestHealth.diastolicBp ||'--' }}</text>
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

      <!-- 体温卡片 -->
      <view class="data-card temp" @click="goDetail('temp')">
        <view class="card-left">
          <uni-icons type="staff-filled" size="40" color="#ff9700"></uni-icons>
          <text class="card-label">体温 (℃)</text>
        </view>
        <view class="card-right">
          <text class="main-value">{{ latestHealth.temperature || '--' }}</text>
          <text class="status-tag" :class="getTempStatusClass">{{ getTempStatusText }}</text>
        </view>
      </view>

      <!-- 血氧卡片 -->
      <view class="data-card oxy" @click="goDetail('oxy')">
        <view class="card-left">
          <uni-icons type="paperplane-filled" size="40" color="#39b54a"></uni-icons>
          <text class="card-label">血氧 (%)</text>
        </view>
        <view class="card-right">
          <text class="main-value">{{ latestHealth.bloodOxygen || '--' }}</text>
          <text class="status-tag" :class="getOxyStatusClass">{{ getOxyStatusText }}</text>
        </view>
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
          <canvas canvas-id="trendCanvas" id="trendCanvas" class="trend-canvas" @error="canvasError"></canvas>
          <text class="chart-tip">已获取到 {{ chartData.length }} 条时间序列数据</text>
        </view>
      </view>
    </view>

    <view class="bottom-tips">
      <text>注：数据仅供参考，不作为医疗诊断依据。</text>
    </view>
  </view>
</template>

<script>
  import { listHealthRecords } from "@/api/health/record"

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
      initDate() {
        const d = new Date()
        this.todayDate = `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`
      },
      // 获取今日数据（严格过滤本日）
      getHealthData() {
        const todayStr = new Date().toISOString().split('T')[0]
        listHealthRecords({ pageSize: 100 }).then(res => {
          const allRows = res.rows || []
          // 1. 过滤出今日数据
          const todayRows = allRows.filter(row => {
            const rowDate = (row.collectTime || row.createTime || '').split(' ')[0]
            return rowDate === todayStr
          })

          // 2. 聚合本日指标最新值
          const latest = {
            systolicBp: null,
            diastolicBp: null,
            heartRate: null,
            bloodSugar: null,
            temperature: null,
            bloodOxygen: null
          }

          // 从旧到新遍历聚合，这样后面覆盖前面的
          todayRows.sort((a, b) => new Date(a.createTime) - new Date(b.createTime)).forEach(row => {
            if (row.systolicBp) latest.systolicBp = row.systolicBp
            if (row.diastolicBp) latest.diastolicBp = row.diastolicBp
            if (row.heartRate) latest.heartRate = row.heartRate
            if (row.bloodSugar) latest.bloodSugar = row.bloodSugar
            if (row.temperature) latest.temperature = row.temperature
            if (row.bloodOxygen) latest.bloodOxygen = row.bloodOxygen
          })

          this.latestHealth = latest
        })
      },
      // 获取趋势数据
      getTrendData() {
        this.loadingChart = true
        // 根据 currentRange 计算 beginTime (此处逻辑简化，实际需计算具体日期)
        listHealthRecords({ 
          recordType: this.currentMetric,
          pageSize: 50 
        }).then(res => {
          this.chartData = res.rows || []
          this.loadingChart = false
          this.$nextTick(() => {
            this.drawChart()
          })
        }).catch(() => {
          this.loadingChart = false
        })
      },
      changeMetric(key) {
        this.currentMetric = key
        this.getTrendData()
      },
      changeRange(val) {
        this.currentRange = val
        this.getTrendData()
      },
      // 绘制原生 Canvas 图表
      drawChart() {
        if (this.chartData.length === 0) return
        
        const ctx = uni.createCanvasContext('trendCanvas', this)
        const width = uni.upx2px(600)
        const height = uni.upx2px(350)
        const paddingLeft = 60 // 留出 Y 轴空间
        const paddingBottom = 40 // 留出 X 轴空间
        const paddingTop = 30
        const paddingRight = 30
        
        // 1. 数据准备 (提取数值并排序)
        const rawPoints = this.chartData.map(row => {
          let val = 0;
          if (this.currentMetric === 'blood_pressure') val = row.systolicBp;
          else if (this.currentMetric === 'heart_rate') val = row.heartRate;
          else if (this.currentMetric === 'blood_sugar') val = row.bloodSugar;
          else if (this.currentMetric === 'temperature') val = row.temperature;
          else if (this.currentMetric === 'blood_oxygen') val = row.bloodOxygen;
          
          return {
            val: parseFloat(val) || 0,
            date: (row.collectTime || row.createTime || '').split(' ')[0].substring(5) // 取 MM-DD
          };
        }).filter(p => p.val > 0).reverse(); // 时间正序

        if (rawPoints.length === 0) return
        
        const dataValues = rawPoints.map(p => p.val);
        const dates = rawPoints.map(p => p.date);

        let maxVal = Math.max(...dataValues)
        let minVal = Math.min(...dataValues)
        
        // 防止最大最小值相等导致除以0
        if (maxVal === minVal) {
          maxVal += 10;
          minVal -= 10;
        }
        
        const range = maxVal - minVal
        const chartWidth = width - paddingLeft - paddingRight
        const chartHeight = height - paddingTop - paddingBottom

        // 2. 绘制坐标轴
        ctx.setStrokeStyle('#ccc')
        ctx.setLineWidth(1)
        ctx.moveTo(paddingLeft, paddingTop)
        ctx.lineTo(paddingLeft, height - paddingBottom) // Y 轴
        ctx.lineTo(width - paddingRight, height - paddingBottom) // X 轴
        ctx.stroke()

        // 3. 绘制 Y 轴刻度与文字
        ctx.setFontSize(10)
        ctx.setFillStyle('#999')
        ctx.setTextAlign('right')
        const yLabels = [maxVal.toFixed(1), ((maxVal + minVal) / 2).toFixed(1), minVal.toFixed(1)]
        yLabels.forEach((label, i) => {
          const y = paddingTop + (chartHeight * i / 2)
          ctx.fillText(label, paddingLeft - 5, y + 4)
        })

        // 4. 绘制趋势线与区域
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

        // 5. 绘制数据锚点（小圆圈）
        rawPoints.forEach((p, i) => {
          const x = paddingLeft + i * stepX
          const y = height - paddingBottom - ((p.val - minVal) / range) * chartHeight
          ctx.beginPath()
          ctx.setFillStyle('#fff')
          ctx.setStrokeStyle('#0081ff')
          ctx.arc(x, y, 3, 0, 2 * Math.PI)
          ctx.fill()
          ctx.stroke()
          
          // X 轴日期标注 (抽样显示，避免太挤)
          if (i === 0 || i === rawPoints.length - 1 || (rawPoints.length > 3 && i === Math.floor(rawPoints.length/2))) {
            ctx.setFontSize(10)
            ctx.setFillStyle('#999')
            ctx.setTextAlign('center')
            ctx.fillText(p.date, x, height - 10)
          }
        })

        ctx.draw()
      },
      canvasError(e) {
        console.error('Canvas Error:', e)
      },
      goDetail(type) {
        // 后续扩展详情曲线页
        this.$modal.msg("详情功能即将上线")
      }
    },
    computed: {
      getRangeName() {
        return this.rangeOptions.find(o => o.value === this.currentRange)?.label || ''
      },
      getMetricName() {
        return this.metrics.find(m => m.key === this.currentMetric)?.name || ''
      },
      getBpStatusText() {
        if (!this.latestHealth.systolicBp) return '无数据'
        const s = this.latestHealth.systolicBp
        if (s > 140) return '偏高'
        if (s < 90) return '偏低'
        return '正常'
      },
      getBpStatusClass() {
        const text = this.getBpStatusText
        if (text === '偏高') return 'warn'
        if (text === '正常') return 'normal'
        return 'info'
      },
      getHrStatusText() {
        if (!this.latestHealth.heartRate) return '无数据'
        const h = this.latestHealth.heartRate
        if (h > 100) return '过快'
        if (h < 60) return '过慢'
        return '正常'
      },
      getHrStatusClass() {
        const text = this.getHrStatusText
        return text === '正常' ? 'normal' : 'warn'
      },
      getBsStatusText() {
        if (!this.latestHealth.bloodSugar) return '无数据'
        return '正常' // 演示简化
      },
      getBsStatusClass() {
        return 'normal'
      },
      getTempStatusText() {
        if (!this.latestHealth.temperature) return '无数据'
        const t = this.latestHealth.temperature
        if (t > 37.3) return '发热'
        if (t < 36.0) return '偏低'
        return '正常'
      },
      getTempStatusClass() {
        const text = this.getTempStatusText
        return text === '正常' ? 'normal' : 'warn'
      },
      getOxyStatusText() {
        if (!this.latestHealth.bloodOxygen) return '无数据'
        const o = this.latestHealth.bloodOxygen
        if (o < 95) return '偏低'
        return '正常'
      },
      getOxyStatusClass() {
        const text = this.getOxyStatusText
        return text === '正常' ? 'normal' : 'warn'
      },
      getTempStatusText() {
        if (!this.latestHealth.temperature) return '无数据'
        const t = this.latestHealth.temperature
        if (t > 37.3) return '发热'
        if (t < 36.0) return '偏低'
        return '正常'
      },
      getTempStatusClass() {
        const text = this.getTempStatusText
        return text === '正常' ? 'normal' : 'warn'
      },
      getOxyStatusText() {
        if (!this.latestHealth.bloodOxygen) return '无数据'
        const o = this.latestHealth.bloodOxygen
        if (o < 95) return '偏低'
        return '正常'
      },
      getOxyStatusClass() {
        const text = this.getOxyStatusText
        return text === '正常' ? 'normal' : 'warn'
      }
    }
  }
</script>

<style lang="scss" scoped>
  .health-container {
    background-color: #f8f9fb;
    min-height: 100vh;
    padding: 30rpx;
  }

  .health-header {
    margin-bottom: 40rpx;
    .title {
      font-size: 48rpx;
      font-weight: bold;
      color: #333;
      display: block;
    }
    .date {
      font-size: 32rpx;
      color: #999;
      margin-top: 10rpx;
    }
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
      box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.03);

      .card-left {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 200rpx;
        .card-label {
          font-size: 30rpx;
          color: #666;
          margin-top: 15rpx;
        }
      }

      .card-right {
        text-align: right;
        .main-value {
          font-size: 60rpx;
          font-weight: bold;
          color: #333;
          display: block;
          margin-bottom: 10rpx;
        }
        .status-tag {
          font-size: 28rpx;
          padding: 6rpx 20rpx;
          border-radius: 30rpx;
          &.normal { background-color: #e7f7e9; color: #39b54a; }
          &.warn { background-color: #fff1f0; color: #e54d42; }
          &.info { background-color: #f0f0f0; color: #999; }
        }
      }
    }
  }

  .chart-section {
    background-color: #fff;
    border-radius: 24rpx;
    padding: 30rpx;
    margin-top: 20rpx;
    
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 30rpx;
      
      .section-title {
        font-size: 36rpx;
        font-weight: bold;
      }
      
      .range-selector {
        display: flex;
        background-color: #f0f0f0;
        border-radius: 30rpx;
        padding: 4rpx;
        
        .range-item {
          padding: 8rpx 24rpx;
          font-size: 24rpx;
          color: #666;
          border-radius: 26rpx;
          &.active {
            background-color: #fff;
            color: #0081ff;
            font-weight: bold;
            box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
          }
        }
      }
    }

    .metric-selector {
      white-space: nowrap;
      margin-bottom: 30rpx;
      .metric-item {
        display: inline-flex;
        flex-direction: column;
        align-items: center;
        padding: 20rpx 30rpx;
        margin-right: 20rpx;
        background-color: #f8f9fb;
        border-radius: 20rpx;
        min-width: 120rpx;
        transition: all 0.3s;
        
        &.active {
          background-color: #0081ff;
          .metric-name { color: #fff; }
        }
        
        .metric-name {
          font-size: 24rpx;
          margin-top: 8rpx;
          color: #666;
        }
      }
    }

    .chart-box {
      height: 450rpx;
      background-color: #fcfcfc;
      border-radius: 16rpx;
      position: relative;
      
      .empty-chart {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #999;
        font-size: 28rpx;
      }
      
      .chart-loading {
        padding-top: 150rpx;
      }

      .chart-wrap {
        height: 100%;
        width: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        
        .trend-canvas {
          width: 600rpx;
          height: 350rpx;
          background-color: transparent;
        }

        .chart-tip {
          font-size: 24rpx;
          color: #999;
          margin-top: 10rpx;
        }
      }
    }
  }

  .bottom-tips {
    text-align: center;
    padding: 60rpx 0;
    color: #bbb;
    font-size: 26rpx;
  }
</style>
