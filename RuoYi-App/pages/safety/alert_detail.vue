<template>
  <view class="detail-container">
    <!-- 顶部状态卡片 -->
    <view class="status-header" :class="headerClass">
      <view class="status-icon-wrap">
        <text class="status-emoji">{{ statusEmoji }}</text>
      </view>
      <text class="status-title">{{ detail.alertType || '安全预警' }}</text>
      <text class="status-desc">{{ statusText }}</text>
    </view>

    <!-- 基本信息卡片 -->
    <view class="info-card">
      <view class="card-title">预警信息</view>
      <view class="info-row">
        <text class="label">预警编号</text>
        <text class="value">#{{ detail.alertId }}</text>
      </view>
      <view class="info-row">
        <text class="label">预警类型</text>
        <text class="value">{{ detail.alertType }}</text>
      </view>
      <view class="info-row">
        <text class="label">触发时间</text>
        <text class="value">{{ formatDateTime(detail.alertTime) }}</text>
      </view>
      <view class="info-row" v-if="detail.alertAddress">
        <text class="label">预警位置</text>
        <text class="value">{{ detail.alertAddress }}</text>
      </view>
      <view class="info-row" v-if="detail.elderlyName">
        <text class="label">关联老人</text>
        <text class="value">{{ detail.elderlyName }}</text>
      </view>
    </view>

    <!-- 处置时间线 -->
    <view class="timeline-card">
      <view class="card-title">处置进度</view>
      <view class="timeline">
        <!-- 步骤1：预警触发 -->
        <view class="timeline-item done">
          <view class="dot-wrap">
            <view class="dot active"></view>
            <view class="line" :class="{ 'line-active': detail.responseTime }"></view>
          </view>
          <view class="step-content">
            <text class="step-title">预警触发</text>
            <text class="step-time">{{ formatDateTime(detail.alertTime) }}</text>
            <text class="step-desc">系统已接收到预警信号</text>
          </view>
        </view>

        <!-- 步骤2：响应接单 -->
        <view class="timeline-item" :class="{ done: detail.responseTime }">
          <view class="dot-wrap">
            <view class="dot" :class="{ active: detail.responseTime }"></view>
            <view class="line" :class="{ 'line-active': detail.completeTime }"></view>
          </view>
          <view class="step-content">
            <text class="step-title">响应处理</text>
            <text class="step-time" v-if="detail.responseTime">{{ formatDateTime(detail.responseTime) }}</text>
            <text class="step-time waiting" v-else>等待响应中...</text>
            <text class="step-desc" v-if="detail.handlerId">处理人已指派</text>
          </view>
        </view>

        <!-- 步骤3：处置完成 -->
        <view class="timeline-item" :class="{ done: detail.completeTime }">
          <view class="dot-wrap">
            <view class="dot" :class="{ active: detail.completeTime }"></view>
          </view>
          <view class="step-content">
            <text class="step-title">处置完成</text>
            <text class="step-time" v-if="detail.completeTime">{{ formatDateTime(detail.completeTime) }}</text>
            <text class="step-time waiting" v-else>尚未完成</text>
            <text class="step-desc" v-if="detail.handleResult">{{ detail.handleResult }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 位置信息（如果有经纬度） -->
    <view class="map-card" v-if="detail.alertLng && detail.alertLng !== 0">
      <view class="card-title">预警位置</view>
      <map
        class="alert-map"
        :latitude="detail.alertLat"
        :longitude="detail.alertLng"
        :markers="markers"
        :scale="15"
        show-location
      ></map>
    </view>

    <!-- 底部操作 -->
    <view class="bottom-actions" v-if="detail.alertStatus === 0">
      <text class="action-tip">如情况紧急，请直接拨打 120</text>
      <button class="call-btn" @click="callEmergency">📞 拨打急救电话</button>
    </view>
  </view>
</template>

<script>
import { getAlertDetail } from '@/api/safety/alert'

export default {
  data() {
    return {
      alertId: null,
      detail: {},
      markers: []
    }
  },
  computed: {
    /** 计算属性：状态相关样式（避免在 :class 中调用方法） */
    headerClass() {
      const map = { 0: 'header-danger', 1: 'header-warning', 2: 'header-success', 3: 'header-closed' }
      return map[this.detail.alertStatus] || 'header-danger'
    },
    statusEmoji() {
      const map = { 0: '🚨', 1: '🔧', 2: '✅', 3: '📁' }
      return map[this.detail.alertStatus] || '🚨'
    },
    statusText() {
      const map = { 0: '待响应', 1: '处理中', 2: '已完成', 3: '已关闭' }
      return map[this.detail.alertStatus] || '待响应'
    }
  },
  onLoad(options) {
    if (options.alertId) {
      this.alertId = options.alertId
      this.loadDetail()
    }
  },
  methods: {
    /** 加载预警详情 */
    loadDetail() {
      getAlertDetail(this.alertId).then(res => {
        if (res.code === 200) {
          this.detail = res.data || {}
          // 构建地图标记
          if (this.detail.alertLng && this.detail.alertLng !== 0) {
            this.markers = [{
              id: 1,
              latitude: this.detail.alertLat,
              longitude: this.detail.alertLng,
              title: this.detail.alertAddress || '预警位置',
              iconPath: '/static/images/tabbar/work.png',
              width: 30,
              height: 30
            }]
          }
        }
      })
    },

    /** 拨打急救电话 */
    callEmergency() {
      uni.makePhoneCall({
        phoneNumber: '120',
        fail: () => {
          this.$modal.showToast('拨打失败，请手动拨打120')
        }
      })
    },

    /** 格式化日期时间 */
    formatDateTime(dateStr) {
      if (!dateStr) return ''
      if (dateStr.length >= 16) return dateStr.substring(0, 16).replace('T', ' ')
      return dateStr.substring(0, 10)
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-container {
  min-height: 100vh;
  background: #f5f6fa;
  padding-bottom: 40rpx;
}

/* 顶部状态 */
.status-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 32rpx 40rpx;
  color: #fff;
}
.header-danger { background: linear-gradient(135deg, #e53935, #ef5350); }
.header-warning { background: linear-gradient(135deg, #f57c00, #ffa726); }
.header-success { background: linear-gradient(135deg, #43a047, #66bb6a); }
.header-closed { background: linear-gradient(135deg, #757575, #9e9e9e); }

.status-icon-wrap {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}
.status-emoji { font-size: 48rpx; }
.status-title { font-size: 36rpx; font-weight: bold; margin-bottom: 8rpx; }
.status-desc { font-size: 28rpx; opacity: 0.9; }

/* 信息卡片 */
.info-card, .timeline-card, .map-card {
  background: #fff;
  margin: 20rpx 24rpx;
  border-radius: 20rpx;
  padding: 28rpx 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}
.card-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  padding-bottom: 16rpx;
  border-bottom: 1rpx solid #f0f0f0;
}
.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16rpx;
}
.info-row .label {
  font-size: 26rpx;
  color: #999;
  flex-shrink: 0;
  width: 160rpx;
}
.info-row .value {
  font-size: 26rpx;
  color: #333;
  text-align: right;
  flex: 1;
}

/* 时间线 */
.timeline {
  padding-left: 8rpx;
}
.timeline-item {
  display: flex;
  min-height: 120rpx;
}
.dot-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 40rpx;
  margin-right: 20rpx;
}
.dot {
  width: 24rpx;
  height: 24rpx;
  border-radius: 50%;
  background: #e0e0e0;
  flex-shrink: 0;
}
.dot.active {
  background: #43a047;
  box-shadow: 0 0 8rpx rgba(67, 160, 71, 0.4);
}
.line {
  width: 4rpx;
  flex: 1;
  background: #e0e0e0;
  margin: 6rpx 0;
}
.line.line-active {
  background: #43a047;
}
.step-content {
  flex: 1;
  padding-bottom: 24rpx;
}
.step-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 6rpx;
}
.step-time {
  font-size: 24rpx;
  color: #999;
  display: block;
  margin-bottom: 4rpx;
}
.step-time.waiting {
  color: #f57c00;
}
.step-desc {
  font-size: 24rpx;
  color: #666;
  display: block;
}

/* 地图 */
.alert-map {
  width: 100%;
  height: 400rpx;
  border-radius: 12rpx;
}

/* 底部操作 */
.bottom-actions {
  padding: 20rpx 24rpx 40rpx;
  text-align: center;
}
.action-tip {
  font-size: 24rpx;
  color: #999;
  display: block;
  margin-bottom: 20rpx;
}
.call-btn {
  width: 80%;
  height: 88rpx;
  font-size: 32rpx;
  color: #fff;
  background: linear-gradient(135deg, #e53935, #ef5350);
  border: none;
  border-radius: 44rpx;
  line-height: 88rpx;
}
</style>
