<template>
  <view class="detail-container">
    <!-- 顶部状态 -->
    <view class="status-header" :class="headerClass">
      <text class="status-emoji">{{ statusEmoji }}</text>
      <text class="status-title">{{ statusText }}</text>
      <text class="order-no">订单号：{{ detail.orderNo }}</text>
    </view>

    <!-- 服务信息 -->
    <view class="info-card">
      <view class="card-title">服务信息</view>
      <view class="info-row">
        <text class="label">服务项目</text>
        <text class="value highlight">{{ detail.itemName || '-' }}</text>
      </view>
      <view class="info-row">
        <text class="label">服务商</text>
        <text class="value">{{ detail.providerName || '-' }}</text>
      </view>
      <view class="info-row">
        <text class="label">服务时间</text>
        <text class="value">{{ formatDate(detail.serviceTime) }}</text>
      </view>
      <view class="info-row">
        <text class="label">服务地址</text>
        <text class="value">{{ detail.serviceAddress || '-' }}</text>
      </view>
      <view class="info-row" v-if="detail.serviceRequirements">
        <text class="label">服务要求</text>
        <text class="value">{{ detail.serviceRequirements }}</text>
      </view>
      <view class="info-row">
        <text class="label">订单金额</text>
        <text class="value price">¥{{ detail.orderAmount || '0.00' }}</text>
      </view>
    </view>

    <!-- 订单进度时间线 -->
    <view class="timeline-card">
      <view class="card-title">订单进度</view>
      <view class="timeline">
        <!-- 下单 -->
        <view class="timeline-item done">
          <view class="dot-wrap">
            <view class="dot active"></view>
            <view class="line" :class="{ 'line-active': detail.acceptTime }"></view>
          </view>
          <view class="step-content">
            <text class="step-title">已下单</text>
            <text class="step-time">{{ formatDateTime(detail.createTime) }}</text>
          </view>
        </view>

        <!-- 已接单 -->
        <view class="timeline-item" :class="{ done: detail.acceptTime }">
          <view class="dot-wrap">
            <view class="dot" :class="{ active: detail.acceptTime }"></view>
            <view class="line" :class="{ 'line-active': detail.startTime }"></view>
          </view>
          <view class="step-content">
            <text class="step-title">已接单</text>
            <text class="step-time" v-if="detail.acceptTime">{{ formatDateTime(detail.acceptTime) }}</text>
            <text class="step-time waiting" v-else>等待服务商接单...</text>
          </view>
        </view>

        <!-- 服务中 -->
        <view class="timeline-item" :class="{ done: detail.startTime }">
          <view class="dot-wrap">
            <view class="dot" :class="{ active: detail.startTime }"></view>
            <view class="line" :class="{ 'line-active': detail.completeTime }"></view>
          </view>
          <view class="step-content">
            <text class="step-title">服务中</text>
            <text class="step-time" v-if="detail.startTime">{{ formatDateTime(detail.startTime) }}</text>
            <text class="step-time waiting" v-else>待服务人员上门</text>
          </view>
        </view>

        <!-- 已完成 -->
        <view class="timeline-item" :class="{ done: detail.completeTime }">
          <view class="dot-wrap">
            <view class="dot" :class="{ active: detail.completeTime }"></view>
          </view>
          <view class="step-content">
            <text class="step-title">已完成</text>
            <text class="step-time" v-if="detail.completeTime">{{ formatDateTime(detail.completeTime) }}</text>
            <text class="step-time waiting" v-else>服务完成后自动更新</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 取消信息 -->
    <view class="info-card" v-if="detail.cancelReason">
      <view class="card-title">取消原因</view>
      <text class="cancel-text">{{ detail.cancelReason }}</text>
    </view>

    <!-- 底部操作 -->
    <view class="bottom-actions">
      <button class="action-btn cancel-btn"
        v-if="detail.orderStatus === 0 || detail.orderStatus === 1"
        @click="handleCancel"
      >取消订单</button>
      <button class="action-btn eval-btn"
        v-if="detail.orderStatus === 3"
        @click="goEvaluate"
      >去评价</button>
    </view>
  </view>
</template>

<script>
import { getOrderDetail, cancelServiceOrder } from '@/api/order/serviceOrder'

export default {
  data() {
    return {
      orderId: null,
      detail: {}
    }
  },
  computed: {
    /** 状态相关计算属性 */
    headerClass() {
      const map = { 0: 'header-pending', 1: 'header-accepted', 2: 'header-serving', 3: 'header-done', 4: 'header-cancelled' }
      return map[this.detail.orderStatus] || 'header-pending'
    },
    statusEmoji() {
      const map = { 0: '📋', 1: '✅', 2: '🔧', 3: '🎉', 4: '❌' }
      return map[this.detail.orderStatus] || '📋'
    },
    statusText() {
      const map = { 0: '待接单', 1: '已接单', 2: '服务中', 3: '已完成', 4: '已取消' }
      return map[this.detail.orderStatus] || '待接单'
    }
  },
  onLoad(options) {
    if (options.orderId) {
      this.orderId = options.orderId
      this.loadDetail()
    }
  },
  methods: {
    /** 加载订单详情 */
    loadDetail() {
      getOrderDetail(this.orderId).then(res => {
        if (res.code === 200) {
          this.detail = res.data || {}
        }
      })
    },

    /** 取消订单 */
    handleCancel() {
      uni.showModal({
        title: '提示',
        content: '确定要取消该订单吗？',
        success: (res) => {
          if (res.confirm) {
            cancelServiceOrder(this.orderId).then(res => {
              if (res.code === 200) {
                this.$modal.showToast('订单已取消')
                this.loadDetail()
              }
            })
          }
        }
      })
    },

    /** 去评价 */
    goEvaluate() {
      // 跳转到评价功能（复用 work/index 中的评价逻辑）
      uni.navigateBack()
    },

    /** 格式化 */
    formatDate(dateStr) {
      if (!dateStr) return ''
      return dateStr.substring(0, 10)
    },
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
  padding-bottom: 120rpx;
}

/* 顶部状态 */
.status-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 50rpx 32rpx 36rpx;
  color: #fff;
}
.header-pending { background: linear-gradient(135deg, #f57c00, #ffa726); }
.header-accepted { background: linear-gradient(135deg, #1976d2, #42a5f5); }
.header-serving { background: linear-gradient(135deg, #7b1fa2, #ab47bc); }
.header-done { background: linear-gradient(135deg, #43a047, #66bb6a); }
.header-cancelled { background: linear-gradient(135deg, #757575, #9e9e9e); }

.status-emoji { font-size: 48rpx; margin-bottom: 12rpx; }
.status-title { font-size: 36rpx; font-weight: bold; margin-bottom: 8rpx; }
.order-no { font-size: 24rpx; opacity: 0.85; }

/* 卡片 */
.info-card, .timeline-card {
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
.info-row .value.highlight {
  color: #1976d2;
  font-weight: bold;
}
.info-row .value.price {
  color: #e53935;
  font-weight: bold;
  font-size: 30rpx;
}
.cancel-text {
  font-size: 26rpx;
  color: #999;
}

/* 时间线 */
.timeline { padding-left: 8rpx; }
.timeline-item { display: flex; min-height: 110rpx; }
.dot-wrap {
  display: flex; flex-direction: column; align-items: center;
  width: 40rpx; margin-right: 20rpx;
}
.dot {
  width: 24rpx; height: 24rpx; border-radius: 50%;
  background: #e0e0e0; flex-shrink: 0;
}
.dot.active {
  background: #43a047;
  box-shadow: 0 0 8rpx rgba(67, 160, 71, 0.4);
}
.line {
  width: 4rpx; flex: 1; background: #e0e0e0; margin: 6rpx 0;
}
.line.line-active { background: #43a047; }
.step-content { flex: 1; padding-bottom: 20rpx; }
.step-title {
  font-size: 28rpx; font-weight: bold; color: #333;
  display: block; margin-bottom: 4rpx;
}
.step-time { font-size: 24rpx; color: #999; display: block; }
.step-time.waiting { color: #f57c00; }

/* 底部操作 */
.bottom-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 20rpx 32rpx;
  background: #fff;
  box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.06);
  box-sizing: border-box;
}
.action-btn {
  width: 100%;
  height: 88rpx;
  font-size: 32rpx;
  border-radius: 44rpx;
  border: none;
  line-height: 88rpx;
}
.cancel-btn {
  color: #e53935;
  background: #fff;
  border: 2rpx solid #e53935;
}
.eval-btn {
  color: #fff;
  background: linear-gradient(135deg, #f57c00, #ffa726);
}
</style>
