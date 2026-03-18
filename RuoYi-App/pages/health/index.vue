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
    </view>

    <!-- 趋势图表占位 - 提示用户去手动添加数据 -->
    <view class="chart-section" v-if="recordList.length === 0">
      <view class="empty-hint">
        <uni-icons type="help-filled" size="50" color="#999"></uni-icons>
        <text class="hint-text">请在 Web 端管理后台手动添加健康记录，即可在此查看实时趋势。</text>
      </view>
    </view>
    
    <view class="chart-section" v-else>
       <text class="section-title">数据趋势</text>
       <view class="chart-placeholder">
         <text>趋势图表正在对接中...</text>
         <text class="tip">（当前已检测到 {{ recordList.length }} 条历史数据）</text>
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
        loading: false
      }
    },
    onShow() {
      this.initDate()
      this.getHealthData()
    },
    methods: {
      initDate() {
        const d = new Date()
        this.todayDate = `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`
      },
      getHealthData() {
        this.loading = true
        // 查询最近的数据
        listHealthRecords({ pageSize: 20 }).then(res => {
          this.recordList = res.rows || []
          if (this.recordList.length > 0) {
            // 假设第一条是最新的
            this.latestHealth = this.recordList[0]
          }
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      goDetail(type) {
        // 后续扩展详情曲线页
        this.$modal.msg("详情功能即将上线")
      }
    },
    computed: {
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
    
    .section-title {
      font-size: 36rpx;
      font-weight: bold;
      margin-bottom: 30rpx;
      display: block;
    }

    .empty-hint {
      padding: 60rpx 0;
      display: flex;
      flex-direction: column;
      align-items: center;
      .hint-text {
        margin-top: 20rpx;
        color: #999;
        font-size: 28rpx;
        text-align: center;
        line-height: 1.6;
      }
    }

    .chart-placeholder {
      height: 300rpx;
      background-color: #fcfcfc;
      border: 2rpx dashed #eee;
      border-radius: 16rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #ccc;
      .tip { font-size: 24rpx; margin-top: 10rpx; }
    }
  }

  .bottom-tips {
    text-align: center;
    padding: 60rpx 0;
    color: #bbb;
    font-size: 26rpx;
  }
</style>
