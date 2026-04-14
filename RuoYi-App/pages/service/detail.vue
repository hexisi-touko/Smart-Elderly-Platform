<template>
  <view class="service-detail">
    <!-- 顶部横幅 -->
    <view class="banner">
      <view class="banner-content">
        <text class="service-name">{{ detail.itemName || '加载中...' }}</text>
        <view class="price-row">
          <text class="price">¥{{ detail.price || '--' }}</text>
          <text class="price-unit">/次</text>
        </view>
      </view>
    </view>

    <!-- 基本信息 -->
    <view class="info-card">
      <view class="card-title">服务信息</view>
      <view class="info-row">
        <text class="label">服务分类</text>
        <text class="value">{{ detail.categoryName || detail.categoryId || '-' }}</text>
      </view>
      <view class="info-row" v-if="detail.serviceDuration">
        <text class="label">服务时长</text>
        <text class="value">{{ detail.serviceDuration }}分钟</text>
      </view>
      <view class="info-row" v-if="detail.applicableObject">
        <text class="label">适用对象</text>
        <text class="value">{{ detail.applicableObject }}</text>
      </view>
      <view class="info-row">
        <text class="label">服务状态</text>
        <text class="value" :class="detail.status == 1 ? 'on' : 'off'">
          {{ detail.status == 1 ? '可预约' : '暂停服务' }}
        </text>
      </view>
    </view>

    <!-- 服务描述 -->
    <view class="info-card" v-if="detail.description">
      <view class="card-title">服务详情</view>
      <text class="desc-text">{{ detail.description }}</text>
    </view>

    <!-- 服务商信息 -->
    <view class="info-card" v-if="detail.providerName">
      <view class="card-title">服务商</view>
      <view class="provider-info">
        <view class="provider-left">
          <text class="provider-name">{{ detail.providerName }}</text>
          <text class="provider-desc" v-if="detail.providerAddress">📍 {{ detail.providerAddress }}</text>
        </view>
      </view>
    </view>

    <!-- 底部预约按钮 -->
    <view class="bottom-bar">
      <button class="book-btn" :disabled="detail.status != 1" @click="showBooking = true">
        {{ detail.status == 1 ? '立即预约' : '暂停服务' }}
      </button>
    </view>

    <!-- 预约弹窗 -->
    <uni-popup ref="bookPopup" type="bottom" :show="showBooking" @change="onPopupChange">
      <view class="booking-popup">
        <view class="popup-header">
          <text class="popup-title">预约服务</text>
          <uni-icons type="closeempty" size="22" color="#999" @click="showBooking = false"></uni-icons>
        </view>
        <view class="popup-body">
          <view class="form-item">
            <text class="form-label">服务日期</text>
            <picker mode="date" :start="todayStr" @change="onDateChange">
              <view class="picker-value">{{ bookForm.serviceDate || '请选择日期' }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="form-label">服务地址</text>
            <input class="form-input" v-model="bookForm.serviceAddress" placeholder="请输入服务地址" />
          </view>
          <view class="form-item">
            <text class="form-label">备注</text>
            <textarea class="form-textarea" v-model="bookForm.serviceRequirements" placeholder="其他需求说明（选填）"></textarea>
          </view>
        </view>
        <button class="submit-btn" @click="submitBooking" :loading="submitting">确认预约</button>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import { getServiceItem } from '@/api/service/serviceItem'
import { addServiceOrder } from '@/api/order/serviceOrder'

export default {
  data() {
    return {
      itemId: null,
      detail: {},
      showBooking: false,
      submitting: false,
      bookForm: {
        serviceDate: '',
        serviceAddress: '',
        serviceRequirements: ''
      },
      todayStr: ''
    }
  },
  onLoad(options) {
    if (options.itemId) {
      this.itemId = options.itemId
      this.loadDetail()
    }
    const d = new Date()
    this.todayStr = `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`
  },
  methods: {
    loadDetail() {
      getServiceItem(this.itemId).then(res => {
        if (res.code === 200) {
          this.detail = res.data || {}
        }
      })
    },
    onPopupChange(e) {
      if (!e.show) this.showBooking = false
    },
    onDateChange(e) {
      this.bookForm.serviceDate = e.detail.value
    },
    submitBooking() {
      if (!this.bookForm.serviceDate) {
        return uni.showToast({ title: '请选择服务日期', icon: 'none' })
      }
      if (!this.bookForm.serviceAddress) {
        return uni.showToast({ title: '请输入服务地址', icon: 'none' })
      }
      this.submitting = true
      addServiceOrder({
        serviceItemId: this.detail.itemId,
        providerId: this.detail.providerId,
        serviceTime: this.bookForm.serviceDate,
        serviceAddress: this.bookForm.serviceAddress,
        serviceRequirements: this.bookForm.serviceRequirements
      }).then(res => {
        if (res.code === 200) {
          uni.showToast({ title: '预约成功！', icon: 'success' })
          this.showBooking = false
          setTimeout(() => {
            uni.navigateTo({ url: '/pages/work/index' })
          }, 1500)
        }
      }).finally(() => {
        this.submitting = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.service-detail {
  min-height: 100vh;
  background: #f5f6fa;
  padding-bottom: 130rpx;
}

.banner {
  background: linear-gradient(135deg, #1976d2, #42a5f5);
  padding: 60rpx 32rpx 40rpx;
  .banner-content {
    .service-name {
      font-size: 40rpx;
      font-weight: bold;
      color: #fff;
      display: block;
      margin-bottom: 12rpx;
    }
    .price-row {
      display: flex;
      align-items: baseline;
    }
    .price {
      font-size: 52rpx;
      font-weight: bold;
      color: #ffeb3b;
    }
    .price-unit {
      font-size: 24rpx;
      color: rgba(255,255,255,0.8);
      margin-left: 6rpx;
    }
  }
}

.info-card {
  background: #fff;
  margin: 20rpx 24rpx;
  border-radius: 20rpx;
  padding: 28rpx 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05);
  .card-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    padding-bottom: 16rpx;
    border-bottom: 1rpx solid #f0f0f0;
  }
}

.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16rpx;
  .label { font-size: 26rpx; color: #999; }
  .value { font-size: 26rpx; color: #333; }
  .value.on { color: #43a047; font-weight: bold; }
  .value.off { color: #e53935; }
}

.desc-text {
  font-size: 28rpx;
  color: #555;
  line-height: 1.8;
}

.provider-info {
  display: flex;
  align-items: center;
  .provider-name { font-size: 30rpx; font-weight: bold; color: #333; display: block; }
  .provider-desc { font-size: 24rpx; color: #999; margin-top: 8rpx; display: block; }
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 20rpx 32rpx;
  background: #fff;
  box-shadow: 0 -2rpx 12rpx rgba(0,0,0,0.06);
  box-sizing: border-box;
}
.book-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #f57c00, #ffa726);
  color: #fff;
  font-size: 34rpx;
  font-weight: bold;
  border-radius: 45rpx;
  border: none;
  line-height: 90rpx;
  &[disabled] {
    background: #ccc;
  }
}

/* 预约弹窗 */
.booking-popup {
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding: 30rpx;
  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    .popup-title { font-size: 34rpx; font-weight: bold; }
  }
  .popup-body { margin-bottom: 30rpx; }
  .form-item { margin-bottom: 24rpx; }
  .form-label {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 12rpx;
    display: block;
  }
  .picker-value, .form-input {
    width: 100%;
    height: 80rpx;
    line-height: 80rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    padding: 0 20rpx;
    font-size: 28rpx;
    box-sizing: border-box;
  }
  .form-textarea {
    width: 100%;
    height: 160rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    padding: 20rpx;
    font-size: 28rpx;
    box-sizing: border-box;
  }
  .submit-btn {
    width: 100%;
    height: 88rpx;
    background: linear-gradient(135deg, #43a047, #66bb6a);
    color: #fff;
    font-size: 32rpx;
    border-radius: 44rpx;
    border: none;
    line-height: 88rpx;
  }
}
</style>
