<template>
  <view class="container">
    <!-- 老人/监护人视图 -->
    <block v-if="userType !== 'worker'">
      <view class="header">
        <text class="title">居家养老服务</text>
        <text class="subtitle">用心陪伴您的每一天</text>
      </view>

      <!-- 快捷看板入口 - 磁贴样式 -->
      <view class="quick-nav">
        <view class="nav-card health" @click="navToHealth">
          <view class="nav-content">
            <uni-icons type="heart-filled" size="36" color="#fff"></uni-icons>
            <view class="nav-text">
              <text class="nav-title">健康看板</text>
              <text class="nav-desc">血压心率</text>
            </view>
          </view>
        </view>
        <view class="nav-card safety" @click="navToAlert">
          <view class="nav-content">
            <uni-icons type="notification-filled" size="36" color="#fff"></uni-icons>
            <view class="nav-text">
              <text class="nav-title">预警历史</text>
              <text class="nav-desc">紧急呼救</text>
            </view>
          </view>
        </view>
        <view class="nav-card zone-nav" @click="navToZone">
          <view class="nav-content">
            <uni-icons type="location-filled" size="36" color="#fff"></uni-icons>
            <view class="nav-text">
              <text class="nav-title">安全区域</text>
              <text class="nav-desc">电子围栏</text>
            </view>
          </view>
        </view>
      </view>

      <view class="service-list">
        <view class="section-header">
          <text class="section-title">预约服务</text>
        </view>
        <view class="service-card" v-for="(item, index) in serviceList" :key="index" @click="openOrderPopup(item)">
          <view class="service-info">
            <text class="service-name">{{ item.itemName }}</text>
            <text class="service-desc">{{ item.itemDescription }}</text>
            <view class="service-price-wrap">
              <text class="price-val">￥{{ item.standardPrice || 0 }}</text>
              <text class="price-unit"> / {{ item.serviceUnit || '次' }}</text>
            </view>
          </view>
          <button class="book-btn">立即预约</button>
        </view>
        <view v-if="serviceList.length === 0" class="empty-state">
           <text>暂无上架的服务项目，去管理后台添加一点吧！</text>
        </view>
      </view>

      <!-- SOS 悬浮呼叫按钮 -->
      <view class="sos-btn" @click="handleSOS">
        <view class="sos-pulse"></view>
        <text>SOS</text>
      </view>

      <!-- 预约弹层 (底部抽屉样式) -->
      <view class="overlay" v-if="showPopup" @click="closeOrderPopup"></view>
      <view class="drawer-box" v-if="showPopup" :class="{'drawer-show': showPopup}">
        <view class="drawer-header">
          <view class="header-line"></view>
          <text class="drawer-title">预约服务</text>
          <view class="close-icon" @click="closeOrderPopup">
            <uni-icons type="closeempty" size="24" color="#999"></uni-icons>
          </view>
        </view>
        
        <view class="drawer-body">
          <view class="service-summary">
            <view class="summary-left">
              <text class="item-name">{{ currentService.itemName }}</text>
              <text class="item-type">{{ currentService.providerName || '专业服务' }}</text>
            </view>
            <view class="summary-right">
              <text class="price-label">预计费用</text>
              <text class="price-val">￥{{ currentService.standardPrice }}</text>
            </view>
          </view>

          <view class="booking-form">
            <view class="form-item">
              <view class="item-label">
                <uni-icons type="calendar" size="18" color="#0081ff"></uni-icons>
                <text>服务预约日期</text>
              </view>
              <picker mode="date" :value="orderForm.serviceTime" @change="bindDateChange">
                <view class="picker-content">
                  <text>{{ orderForm.serviceTime || '点击选择日期' }}</text>
                  <uni-icons type="right" size="16" color="#ccc"></uni-icons>
                </view>
              </picker>
            </view>
            
            <view class="form-item">
              <view class="item-label">
                <uni-icons type="location-filled" size="18" color="#0081ff"></uni-icons>
                <text>具体上门地址</text>
              </view>
              <textarea 
                class="address-input" 
                v-model="orderForm.serviceAddress" 
                placeholder="请输入楼栋、门牌号等详细信息"
                :auto-height="true"
              />
            </view>

            <view class="form-item">
              <view class="item-label">
                <uni-icons type="chat-filled" size="18" color="#0081ff"></uni-icons>
                <text>特殊要求说明 (选填)</text>
              </view>
              <input 
                class="memo-input" 
                type="text" 
                v-model="orderForm.serviceRequirements" 
                placeholder="如有轮椅需求、忌口等请注明" 
              />
            </view>
          </view>
        </view>
        
        <view class="drawer-footer">
          <button class="booking-btn" @click="submitOrder" :disabled="submitting">
            {{ submitting ? '正在为您呼叫...' : '立即预约服务' }}
          </button>
        </view>
      </view>

      <!-- 预约成功反馈卡片 -->
      <view class="overlay" v-if="showSuccessCard" @click="closeSuccessCard"></view>
      <view class="success-card" v-if="showSuccessCard">
        <view class="success-icon">
          <uni-icons type="checkbox-filled" size="80" color="#52c41a"></uni-icons>
        </view>
        <text class="success-title">预约成功！</text>
        <text class="success-desc">我们已为您通知专业服务人员，请保持电话畅通。</text>
        
        <view class="card-btns">
          <button class="btn-check" @click="navToOrder">查看订单</button>
          <button class="btn-close" @click="closeSuccessCard">返回首页</button>
        </view>
      </view>
    </block>

    <!-- 服务人员(Worker)视图 -->
    <block v-else>
      <view class="header staff-header">
        <text class="title">助老员工作台</text>
        <text class="subtitle">让关爱更专业，服务更高效</text>
      </view>
      <worker-dashboard ref="workerDashboard"></worker-dashboard>
    </block>

  </view>
</template>

<script>
  import { listServiceItem } from '@/api/service/item'
  import { addServiceOrder } from '@/api/order/serviceOrder'
  import { triggerSOS } from '@/api/safety/alert'
  import WorkerDashboard from './index/WorkerDashboard.vue'

  export default {
    components: {
      WorkerDashboard
    },
    data() {
      return {
        userType: '',
        serviceList: [],
        currentService: {},
        orderForm: {
          serviceAddress: '',
          serviceTime: '',
          serviceRequirements: ''
        },
        showPopup: false,
        showSuccessCard: false,
        submitting: false
      }
    },
    onShow() {
      this.checkRole()
      if (this.userType !== 'worker') {
        this.getServiceList()
      } else if (this.$refs.workerDashboard) {
        this.$refs.workerDashboard.loadData();
      }
    },
    methods: {
      checkRole() {
        this.userType = this.$store.state.user.userType || uni.getStorageSync('userType')
      },
      getServiceList() {
        listServiceItem().then(res => {
          this.serviceList = res.rows || []
        })
      },
      openOrderPopup(item) {
        this.currentService = item;
        // set default date to tomorrow
        let d = new Date();
        d.setDate(d.getDate() + 1);
        let month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();
        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;
        
        this.orderForm.serviceTime = [year, month, day].join('-');
        this.orderForm.serviceAddress = '';
        this.orderForm.serviceRequirements = '';
        
        this.showPopup = true;
      },
      closeOrderPopup() {
        this.showPopup = false;
      },
      bindDateChange(e) {
        this.orderForm.serviceTime = e.detail.value;
      },
      submitOrder() {
        if (!this.orderForm.serviceTime) return this.$modal.msgError('请选择服务日期');
        if (!this.orderForm.serviceAddress) return this.$modal.msgError('请输入服务地址');
        
        this.submitting = true;
        
        const data = {
          serviceItemId: this.currentService.itemId,
          providerId: this.currentService.providerId,
          orderAmount: this.currentService.standardPrice,
          serviceTime: this.orderForm.serviceTime, // Backend is expecting date pattern "yyyy-MM-dd"
          serviceAddress: this.orderForm.serviceAddress,
          serviceRequirements: this.orderForm.serviceRequirements
        };
        
        addServiceOrder(data).then(res => {
          this.submitting = false;
          this.showPopup = false;
          this.showSuccessCard = true;
        }).catch(() => {
          this.submitting = false;
        });
      },
      closeSuccessCard() {
        this.showSuccessCard = false;
      },
      navToOrder() {
        this.showSuccessCard = false;
        uni.switchTab({
          url: '/pages/work/index'
        });
      },
      handleSOS() {
        uni.showModal({
          title: '紧急报警',
          content: '是否确认发起SOS一键呼救？',
          confirmText: '确认呼救',
          confirmColor: '#e54d42',
          cancelText: '取消',
          success: (res) => {
            if (res.confirm) {
              this.$modal.loading('正在定位并发送求救信号...');
              // 获取定位并上报
              uni.getLocation({
                type: 'wgs84',
                success: (loc) => {
                  const data = {
                    alertLng: loc.longitude,
                    alertLat: loc.latitude,
                    alertAddress: '移动端App一键呼救位置'
                  };
                  triggerSOS(data).then(() => {
                    this.$modal.closeLoading();
                    this.$modal.msgSuccess('报警已发出！请保持电话通畅！');
                  }).catch(() => {
                    this.$modal.closeLoading();
                  });
                },
                fail: (err) => {
                  console.error('定位获取失败', err);
                  // 即使定位失败也强制发出报警信号
                  triggerSOS({ alertLng: 0, alertLat: 0, alertAddress: '定位失败，请速联系客户' }).then(() => {
                    this.$modal.closeLoading();
                    this.$modal.msgWarning('报警已发出，但定位获取失败');
                  }).catch(() => {
                    this.$modal.closeLoading();
                  });
                }
              });
            }
          }
        });
      },
      navToHealth() {
        uni.navigateTo({
          url: '/pages/health/index'
        });
      },
      navToAlert() {
        uni.navigateTo({
          url: '/pages/health/alert_list'
        });
      }
    }
  }
</script>

<style lang="scss" scoped>
  /* 贯彻适老化设计：大字体、高对比、大按钮 */
  .container {
    background-color: #f0f2f5;
    min-height: 100vh;
    padding-bottom: 20px;
  }

  .header {
    background: linear-gradient(135deg, #0081ff, #1cbbb4);
    padding: 60px 20px 40px;
    border-bottom-left-radius: 20px;
    border-bottom-right-radius: 20px;
    color: #fff;
    
    .title {
      font-size: 32px;
      font-weight: bold;
      display: block;
      margin-bottom: 10px;
      text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
    }
    .subtitle {
      font-size: 18px;
      opacity: 0.9;
    }
  }

  .staff-header {
    background: linear-gradient(135deg, #39b54a, #8dc63f) !important;
  }

  .quick-nav {
    padding: 20px;
    margin-top: -20px;
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-gap: 15px;
    
    .nav-card {
      border-radius: 20px;
      padding: 20px 15px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);
      
      &.health {
        background: linear-gradient(135deg, #6739b6, #a5673f);
      }
      &.safety {
        background: linear-gradient(135deg, #e54d42, #f37b1d);
      }

      .nav-content {
        display: flex;
        flex-direction: column;
        align-items: center;
        
        .nav-text {
          margin-top: 10px;
          text-align: center;
          .nav-title {
            font-size: 32rpx;
            font-weight: bold;
            color: #fff;
            display: block;
          }
          .nav-desc {
            font-size: 24rpx;
            color: rgba(255,255,255,0.8);
            margin-top: 4rpx;
          }
        }
      }
    }
  }

  .service-list {
    padding: 20px;
    margin-top: 0;

    .section-header {
      margin-bottom: 15px;
      .section-title {
        font-size: 24px;
        font-weight: bold;
        color: #333;
      }
    }
  }

  .service-card {
    background-color: #ffffff;
    border-radius: 16px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
    
    .service-info {
      margin-bottom: 20px;
    }
    
    .service-name {
      font-size: 24px;
      font-weight: bold;
      color: #333;
      display: block;
      margin-bottom: 8px;
    }
    
    .service-desc {
      font-size: 16px;
      color: #666;
      line-height: 1.5;
      display: block;
      margin-bottom: 15px;
    }
    
    .service-price-wrap {
      .price-val {
        font-size: 28px;
        color: #e54d42;
        font-weight: bold;
      }
      .price-unit {
        font-size: 16px;
        color: #999;
      }
    }
    
    .book-btn {
      background-color: #0081ff;
      color: #fff;
      font-size: 20px;
      font-weight: bold;
      height: 56px;
      line-height: 56px;
      border-radius: 28px;
    }
  }

  .empty-state {
    text-align: center;
    padding: 50px 0;
    color: #999;
    font-size: 18px;
  }

  /* 抽屉式预约弹窗 */
  .drawer-box {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: #fff;
    border-radius: 40rpx 40rpx 0 0;
    z-index: 1000;
    padding-bottom: env(safe-area-inset-bottom);
    transform: translateY(100%);
    transition: transform 0.3s ease;
    
    &.drawer-show {
      transform: translateY(0);
    }

    .drawer-header {
      padding: 30rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      position: relative;
      
      .header-line {
        width: 80rpx;
        height: 8rpx;
        background-color: #eee;
        border-radius: 4rpx;
        margin-bottom: 20rpx;
      }
      
      .drawer-title {
        font-size: 36rpx;
        font-weight: bold;
        color: #333;
      }
      
      .close-icon {
        position: absolute;
        right: 30rpx;
        top: 30rpx;
      }
    }

    .drawer-body {
      padding: 0 40rpx 40rpx;

      .service-summary {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: #f8f9fa;
        padding: 30rpx;
        border-radius: 20rpx;
        margin-bottom: 40rpx;
        
        .item-name {
          font-size: 34rpx;
          font-weight: bold;
          color: #333;
          display: block;
        }
        .item-type {
          font-size: 26rpx;
          color: #999;
          margin-top: 5rpx;
        }
        
        .price-label {
          font-size: 24rpx;
          color: #999;
          display: block;
          text-align: right;
        }
        .price-val {
          font-size: 40rpx;
          color: #e54d42;
          font-weight: bold;
        }
      }

      .booking-form {
        .form-item {
          margin-bottom: 30rpx;
          
          .item-label {
            display: flex;
            align-items: center;
            margin-bottom: 15rpx;
            
            text {
              font-size: 30rpx;
              font-weight: bold;
              color: #333;
              margin-left: 10rpx;
            }
          }
          
          .picker-content {
            background-color: #f1f1f1;
            padding: 25rpx 30rpx;
            border-radius: 12rpx;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 32rpx;
          }
          
          .address-input {
            width: 100%;
            background-color: #f1f1f1;
            padding: 25rpx 30rpx;
            border-radius: 12rpx;
            font-size: 32rpx;
            box-sizing: border-box;
            min-height: 120rpx;
          }
          
          .memo-input {
            width: 100%;
            background-color: #f1f1f1;
            padding: 25rpx 30rpx;
            border-radius: 12rpx;
            font-size: 32rpx;
            box-sizing: border-box;
            height: 90rpx;
          }
        }
      }
    }

    .drawer-footer {
      padding: 0 40rpx 40rpx;
      
      .booking-btn {
        background-color: #0081ff;
        color: #fff;
        height: 100rpx;
        line-height: 100rpx;
        border-radius: 50rpx;
        font-size: 34rpx;
        font-weight: bold;
        box-shadow: 0 10rpx 20rpx rgba(0, 129, 255, 0.3);
        
        &[disabled] {
          opacity: 0.7;
          background-color: #ccc;
        }
      }
    }
  }

  /* 成功卡片 */
  .success-card {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 600rpx;
    background-color: #fff;
    border-radius: 40rpx;
    z-index: 1001;
    padding: 60rpx 40rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-shadow: 0 20rpx 60rpx rgba(0,0,0,0.2);
    
    .success-icon {
      margin-bottom: 30rpx;
      animation: scaleIn 0.5s ease-out;
    }
    
    .success-title {
      font-size: 44rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 20rpx;
    }
    
    .success-desc {
      font-size: 30rpx;
      color: #666;
      text-align: center;
      line-height: 1.5;
      margin-bottom: 50rpx;
    }
    
    .card-btns {
      width: 100%;
      display: flex;
      flex-direction: column;
      gap: 20rpx;
      
      button {
        width: 100%;
        height: 90rpx;
        line-height: 90rpx;
        border-radius: 45rpx;
        font-size: 32rpx;
        font-weight: bold;
      }
      
      .btn-check {
        background-color: #0081ff;
        color: #fff;
      }
      
      .btn-close {
        background-color: #f1f1f1;
        color: #666;
      }
    }
  }

  @keyframes scaleIn {
    0% { transform: scale(0); opacity: 0; }
    80% { transform: scale(1.1); }
    100% { transform: scale(1); opacity: 1; }
  }

  .overlay {
    position: fixed;
    top: 0; left: 0; right: 0; bottom: 0;
    background: rgba(0,0,0,0.6);
    z-index: 998;
  }

  .sos-btn {
    position: fixed;
    right: 40rpx;
    bottom: 60rpx;
    width: 140rpx;
    height: 140rpx;
    background: radial-gradient(circle, #ff4d4f, #cf1322);
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #fff;
    font-size: 40rpx;
    font-weight: bold;
    box-shadow: 0 10rpx 40rpx rgba(230, 0, 0, 0.4);
    z-index: 99;
    border: 4rpx solid rgba(255,255,255,0.3);

    .sos-pulse {
      position: absolute;
      width: 100%;
      height: 100%;
      background-color: #ff4d4f;
      border-radius: 50%;
      opacity: 0.6;
      animation: pulse 2s infinite;
      z-index: -1;
    }
  }

  @keyframes pulse {
    0% { transform: scale(1); opacity: 0.6; }
    100% { transform: scale(1.8); opacity: 0; }
  }
</style>
