<template>
  <view class="order-container">
    <!-- 状态切换栏 - 适老化大尺寸 -->
    <view class="status-tabs">
      <view 
        v-for="(tab, index) in tabs" 
        :key="index" 
        class="tab-item" 
        :class="{ active: currentTab === tab.value }"
        @click="handleTabChange(tab.value)"
      >
        <text class="tab-text">{{ tab.label }}</text>
        <view class="active-line" v-if="currentTab === tab.value"></view>
      </view>
    </view>

    <!-- 订单列表区 -->
    <view class="order-list">
      <view class="order-card" v-for="(item, index) in orderList" :key="index" @click="goDetail(item)">
        <view class="card-header">
          <text class="order-no">订单号：{{ item.orderNo }}</text>
          <text class="status-tag" :class="item.statusClass">{{ item.statusText }}</text>
        </view>
        
        <view class="card-body">
          <view class="info-row">
            <text class="label">服务项目：</text>
            <text class="value bold">{{ item.itemName || '加载中...' }}</text>
          </view>
          <view class="info-row">
            <text class="label">预约时间：</text>
            <text class="value">{{ item.serviceTime }}</text>
          </view>
          <view class="info-row">
            <text class="label">服务地址：</text>
            <text class="value">{{ item.serviceAddress }}</text>
          </view>
          <view class="price-row">
            <text class="price-label">合计：</text>
            <text class="price-value">￥{{ item.orderAmount }}</text>
          </view>
        </view>
        
        <view class="card-footer">
          <!-- 老人/监护人操作 -->
          <template v-if="userType !== 'worker'">
            <button class="footer-btn plain" v-if="item.orderStatus === 0 || item.orderStatus === 1" @click="handleCancel(item)">取消订单</button>
            <button class="footer-btn primary" v-if="item.orderStatus === 3" @click="handleEval(item)">去评价</button>
            <text class="footer-tip" v-if="item.orderStatus === 5">感谢评价！</text>
          </template>
          
          <!-- 服务人员（员工）操作 -->
          <template v-else>
            <text class="footer-tip grey" v-if="item.orderStatus === 3">等待客户评价</text>
            <button class="footer-btn line-blue" v-if="item.orderStatus === 5" @click="goDetail(item)">查看评价</button>
          </template>
        </view>
      </view>

      <!-- 缺省状态 -->
      <view v-if="orderList.length === 0 && !loading" class="empty-box">
        <uni-icons type="info-filled" size="80" color="#ddd"></uni-icons>
        <text class="empty-text">暂无相关订单记录</text>
      </view>
      
      <!-- 加载更多提示 -->
      <view class="load-more" v-if="orderList.length > 0">
        <text>{{ finished ? '没有更多订单了' : '正在加载中...' }}</text>
      </view>
    </view>

    <!-- 评价弹窗 -->
    <uni-popup ref="evalPopup" type="bottom">
      <view class="eval-popup-content">
        <view class="eval-header">
          <text class="title">服务评价</text>
          <uni-icons type="closeempty" size="24" color="#999" @click="$refs.evalPopup.close()"></uni-icons>
        </view>
        <view class="eval-body">
          <view class="rate-section">
            <text class="rate-label">服务满意度</text>
            <uni-rate v-model="evalForm.starLevel" :size="40" active-color="#ff9900" />
          </view>
          <view class="input-section">
            <textarea 
              v-model="evalForm.evaluationContent" 
              placeholder="请留下您的意见或建议..." 
              class="eval-textarea"
              maxlength="200"
            />
          </view>
          <button class="submit-btn" @click="submitEval" :loading="submitting">提交评价</button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
  import { listMyOrders, cancelServiceOrder } from "@/api/order/serviceOrder"
  import { addEvaluation } from "@/api/order/evaluation"

  export default {
    data() {
      return {
        tabs: [
          { label: '全部', value: '' },
          { label: '待派单', value: '1' },
          { label: '服务中', value: '2' },
          { label: '已完成', value: '3' }
        ],
        currentTab: '',
        orderList: [],
        queryParams: {
          pageNum: 1,
          pageSize: 10
        },
        loading: false,
        finished: false,
        submitting: false,
        evalForm: {
          orderId: null,
          starLevel: 5,
          evaluationContent: ''
        }
      }
    },
    computed: {
      userType() {
        return this.$store.state.user.userType;
      }
    },
    onShow() {
      this.refreshList()
    },
    // 下拉刷新
    onPullDownRefresh() {
      this.refreshList()
    },
    // 触底加载
    onReachBottom() {
      if (!this.finished && !this.loading) {
        this.queryParams.pageNum++
        this.getList()
      }
    },
    methods: {
      handleTabChange(val) {
        this.currentTab = val
        this.refreshList()
      },
      refreshList() {
        this.queryParams.pageNum = 1
        this.orderList = []
        this.finished = false
        this.getList()
      },
      getList() {
        this.loading = true
        const params = {
          ...this.queryParams,
          orderStatus: this.currentTab
        }
        listMyOrders(params).then(res => {
          const rows = res.rows || []
          // 处理状态显示，避开模板内函数调用
          const processedRows = rows.map(item => {
            return {
              ...item,
              ...this.formatStatus(item.orderStatus)
            }
          })
          
          this.orderList = [...this.orderList, ...processedRows]
          this.loading = false
          if (this.orderList.length >= res.total) {
            this.finished = true
          }
          uni.stopPullDownRefresh()
        }).catch(() => {
          this.loading = false
          uni.stopPullDownRefresh()
        })
      },
      /** 跳转订单详情 */
      goDetail(item) {
        uni.navigateTo({
          url: '/pages/order/detail?orderId=' + item.orderId
        })
      },
      formatStatus(status) {
        // 0:待接单, 1:服务中, 2:已完成, 3:已取消
        const map = {
          0: { text: '待支付', class: 'status-pending' },
          1: { text: '待派单', class: 'status-pending' },
          2: { text: '服务中', class: 'status-process' },
          3: { text: '已完成', class: 'status-success' },
          4: { text: '已取消', class: 'status-cancel' },
          5: { text: '已评价', class: 'status-success' },
          6: { text: '退款中', class: 'status-pending' },
          7: { text: '已退款', class: 'status-cancel' }
        }
        return {
          statusText: map[status]?.text || '未知',
          statusClass: map[status]?.class || ''
        }
      },
      handleCancel(item) {
        this.$modal.confirm('确定要取消这个订单吗？').then(() => {
          this.$modal.loading("正在取消订单...");
          cancelServiceOrder(item.orderId).then(res => {
            this.$modal.closeLoading();
            this.$modal.msgSuccess('订单已取消');
            this.refreshList();
          }).catch(() => {
            this.$modal.closeLoading();
          });
        })
      },
      handleEval(item) {
        this.evalForm.orderId = item.orderId
        this.evalForm.starLevel = 5
        this.evalForm.evaluationContent = ''
        this.$refs.evalPopup.open()
      },
      submitEval() {
        if (!this.evalForm.starLevel) {
          return this.$modal.msgError('请选择评分')
        }
        this.submitting = true
        addEvaluation(this.evalForm).then(res => {
          this.$modal.msgSuccess('感谢您的评价！')
          this.$refs.evalPopup.close()
          this.submitting = false
          this.refreshList()
        }).catch(() => {
          this.submitting = false
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .order-container {
    background-color: #f7f8fa;
    min-height: 100vh;
    padding-top: 100rpx; /* 为吸顶tab留位 */
  }

  .status-tabs {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 10;
    display: flex;
    background-color: #fff;
    height: 100rpx;
    box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);

    .tab-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      position: relative;

      .tab-text {
        font-size: 32rpx;
        color: #666;
        font-weight: 500;
      }

      &.active {
        .tab-text {
          color: #0081ff;
          font-weight: bold;
          font-size: 34rpx;
        }
        .active-line {
          position: absolute;
          bottom: 10rpx;
          width: 40rpx;
          height: 6rpx;
          background-color: #0081ff;
          border-radius: 3rpx;
        }
      }
    }
  }

  .order-list {
    padding: 20rpx;
  }

  .order-card {
    background-color: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 25rpx;
    box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.04);

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 20rpx;
      border-bottom: 2rpx solid #f2f2f2;
      margin-bottom: 20rpx;

      .order-no {
        font-size: 26rpx;
        color: #999;
      }

      .status-tag {
        font-size: 28rpx;
        font-weight: bold;
        padding: 6rpx 16rpx;
        border-radius: 8rpx;
        
        &.status-pending { color: #f37b1d; background-color: #fdf6ec; }
        &.status-process { color: #0081ff; background-color: #e8f4ff; }
        &.status-success { color: #39b54a; background-color: #e7f7e9; }
        &.status-cancel { color: #aaaaaa; background-color: #f0f0f0; }
      }
    }

    .card-body {
      .info-row {
        margin-bottom: 15rpx;
        display: flex;
        font-size: 30rpx;
        line-height: 1.4;

        .label {
          color: #777;
          width: 160rpx;
          flex-shrink: 0;
        }
        .value {
          color: #333;
          &.bold {
            font-weight: bold;
            font-size: 32rpx;
          }
        }
      }

      .price-row {
        margin-top: 25rpx;
        text-align: right;
        .price-label {
          font-size: 28rpx;
          color: #666;
        }
        .price-value {
          font-size: 40rpx;
          color: #e54d42;
          font-weight: bold;
        }
      }
    }

    .card-footer {
      margin-top: 30rpx;
      padding-top: 25rpx;
      border-top: 2rpx solid #f2f2f2;
      display: flex;
      justify-content: flex-end;

      .footer-btn {
        margin: 0;
        margin-left: 20rpx;
        height: 70rpx;
        line-height: 70rpx;
        font-size: 28rpx;
        border-radius: 35rpx;
        padding: 0 40rpx;
        
        &.plain {
          border: 2rpx solid #ddd;
          background: #fff;
          color: #666;
        }
        
        &.primary {
          background-color: #0081ff;
          color: #fff;
        }
      }

      .footer-tip {
        font-size: 26rpx;
        color: #39b54a;
        font-style: italic;
      }
    }
  }

  /* 评价弹窗样式 */
  .eval-popup-content {
    background-color: #fff;
    border-top-left-radius: 40rpx;
    border-top-right-radius: 40rpx;
    padding: 40rpx;
    
    .eval-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 40rpx;
      .title {
        font-size: 36rpx;
        font-weight: bold;
        color: #333;
      }
    }
    
    .eval-body {
      .rate-section {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 40rpx;
        .rate-label {
          font-size: 32rpx;
          color: #666;
          margin-bottom: 20rpx;
        }
      }
      
      .input-section {
        background-color: #f8f9fb;
        border-radius: 16rpx;
        padding: 20rpx;
        margin-bottom: 40rpx;
        .eval-textarea {
          width: 100%;
          height: 200rpx;
          font-size: 30rpx;
          color: #333;
        }
      }
      
      .submit-btn {
        background-color: #0081ff;
        color: #fff;
        height: 100rpx;
        line-height: 100rpx;
        border-radius: 50rpx;
        font-size: 34rpx;
        font-weight: bold;
      }
    }
  }

  .empty-box {
    padding-top: 200rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .empty-img {
      width: 240rpx;
      height: 240rpx;
      opacity: 0.5;
    }
    .empty-text {
      margin-top: 30rpx;
      color: #999;
      font-size: 30rpx;
    }
  }

  .load-more {
    text-align: center;
    padding: 30rpx;
    color: #999;
    font-size: 26rpx;
  }
</style>
