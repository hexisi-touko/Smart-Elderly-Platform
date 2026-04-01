<template>
  <view class="alert-container">
    <view class="header">
      <text class="title">预警历史</text>
      <text class="desc">共 {{ total }} 条记录</text>
    </view>

    <!-- 列表区域 -->
    <view class="list-wrapper">
      <view class="alert-item" v-for="(item, index) in alertList" :key="index" @click="goDetail(item)">
        <view class="item-header">
          <view class="type-box" :class="item.typeClass">
            <text>{{ item.alertType || '安全预警' }}</text>
          </view>
          <text class="status-label" :class="item.statusClass">{{ item.statusText }}</text>
        </view>
        
        <view class="item-content">
          <view class="info-line">
            <uni-icons type="calendar" size="16" color="#999"></uni-icons>
            <text class="label">时间：</text>
            <text class="text">{{ item.alertTime || item.createTime || '-' }}</text>
          </view>
          <view class="info-line">
            <uni-icons type="location" size="16" color="#999"></uni-icons>
            <text class="label">地点：</text>
            <text class="text address">{{ item.alertAddress || '地址确认中' }}</text>
          </view>
        </view>
        
        <view class="item-footer" v-if="item.handleResult">
          <text class="footer-title">处置：</text>
          <text class="footer-val">{{ item.handleResult }}</text>
        </view>
      </view>

      <!-- 状态提示 -->
      <view v-if="alertList.length === 0 && !loading" class="empty-box">
        <uni-icons type="info" size="60" color="#ddd"></uni-icons>
        <view class="empty-text">暂无报警记录</view>
      </view>
      
      <view v-if="loading" class="loading-box">
        <text>加载中...</text>
      </view>
      
      <view class="no-more" v-if="finished && alertList.length > 0">
        <text>没有更多记录了</text>
      </view>
    </view>
  </view>
</template>

<script>
  import { listMyAlerts } from '@/api/safety/alert'

  export default {
    data() {
      return {
        alertList: [],
        total: 0,
        queryParams: {
          pageNum: 1,
          pageSize: 10
        },
        loading: false,
        finished: false
      }
    },
    onShow() {
      this.refreshData();
    },
    onPullDownRefresh() {
      this.refreshData();
    },
    onReachBottom() {
      if (!this.finished && !this.loading) {
        this.queryParams.pageNum++;
        this.getList();
      }
    },
    methods: {
      refreshData() {
        this.queryParams.pageNum = 1;
        this.alertList = [];
        this.finished = false;
        this.getList();
      },
      getList() {
        this.loading = true;
        console.log('Fetching Alerts with query:', this.queryParams);
        
        listMyAlerts(this.queryParams).then(res => {
          console.log('Raw Backend Response:', res);
          
          // 健壮性处理：支持直接返回数组或嵌套在rows中
          let rows = [];
          if (res.rows) {
            rows = res.rows;
          } else if (Array.isArray(res)) {
            rows = res;
          } else if (res.data && Array.isArray(res.data)) {
            rows = res.data;
          }
          
          this.total = res.total || rows.length;
          
          // 预处理每行数据，方便模板展示
          const processed = rows.map(item => {
            return {
              ...item,
              ...this.parseAlertStyles(item)
            }
          });
          
          this.alertList = [...this.alertList, ...processed];
          console.log('Processed alertList length:', this.alertList.length);
          
          this.loading = false;
          if (this.alertList.length >= this.total) {
            this.finished = true;
          }
          uni.stopPullDownRefresh();
        }).catch(err => {
          console.error('Fetch Alert Error:', err);
          this.loading = false;
          uni.stopPullDownRefresh();
        });
      },
      parseAlertStyles(item) {
        // 类型样式
        let typeClass = 'type-default';
        if (item.alertType && item.alertType.includes('SOS')) typeClass = 'type-sos';
        if (item.alertType && item.alertType.includes('跌倒')) typeClass = 'type-fall';
        
        // 状态样式与文字
        const statusMap = {
          0: { text: '待确认', class: 's-waiting' },
          1: { text: '处理中', class: 's-process' },
          2: { text: '已完成', class: 's-done' }
        };
        const statusData = statusMap[item.alertStatus] || { text: '已收到', class: 's-waiting' };
        
        return {
          typeClass,
          statusText: statusData.text,
          statusClass: statusData.class
        };
      },
      /** 跳转预警详情 */
      goDetail(item) {
        uni.navigateTo({
          url: '/pages/safety/alert_detail?alertId=' + item.alertId
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .alert-container {
    background-color: #f5f7fa;
    min-height: 100vh;
    padding-bottom: 50rpx;
  }

  .header {
    background-color: #fff;
    padding: 60rpx 40rpx;
    border-bottom: 1rpx solid #efefef;
    
    .title {
      font-size: 52rpx;
      font-weight: bold;
      color: #333;
      display: block;
    }
    .desc {
      font-size: 28rpx;
      color: #999;
      margin-top: 10rpx;
    }
  }

  .list-wrapper {
    padding: 30rpx;
  }

  .alert-item {
    background-color: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 30rpx;
    box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.06);

    .item-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24rpx;
      border-bottom: 1rpx solid #f9f9f9;
      padding-bottom: 20rpx;
    }

    .type-box {
      padding: 6rpx 20rpx;
      border-radius: 8rpx;
      font-size: 24rpx;
      font-weight: bold;
      
      &.type-sos { background-color: #ffeef0; color: #ff4d4f; }
      &.type-fall { background-color: #fff7e6; color: #faad14; }
      &.type-default { background-color: #e6f7ff; color: #1890ff; }
    }

    .status-label {
      font-size: 26rpx;
      &.s-waiting { color: #ff4d4f; }
      &.s-process { color: #1890ff; }
      &.s-done { color: #52c41a; }
    }

    .item-content {
      .info-line {
        display: flex;
        align-items: flex-start;
        margin-bottom: 16rpx;
        
        .label {
          color: #888;
          font-size: 28rpx;
          margin-left: 10rpx;
          flex-shrink: 0;
        }
        .text {
          color: #333;
          font-size: 28rpx;
          flex: 1;
          word-break: break-all;
          
          &.address {
            color: #d4380d;
            font-weight: 500;
          }
        }
      }
    }

    .item-footer {
      margin-top: 20rpx;
      padding-top: 20rpx;
      background-color: #fdfdfd;
      border-top: 1rpx dashed #eee;
      display: flex;
      
      .footer-title { font-size: 26rpx; color: #999; }
      .footer-val { font-size: 26rpx; color: #666; flex: 1; }
    }
  }

  .empty-box {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 150rpx;
    .empty-text { color: #999; font-size: 32rpx; margin-top: 20rpx; }
  }

  .loading-box, .no-more {
    text-align: center;
    padding: 30rpx;
    color: #999;
    font-size: 26rpx;
  }
</style>
