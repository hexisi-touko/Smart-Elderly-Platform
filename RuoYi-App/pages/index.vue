<template>
  <view class="container">
    <view class="header">
      <text class="title">居家养老服务</text>
      <text class="subtitle">用心陪伴您的每一天</text>
    </view>

    <view class="service-list">
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

    <!-- 预约弹层 -->
    <view class="overlay" v-if="showPopup" @click="closeOrderPopup"></view>
    <view class="popup-box" v-if="showPopup">
      <view class="popup-header">
        <text class="popup-title">呼叫：{{ currentService.itemName }}</text>
        <text class="close-btn" @click="closeOrderPopup">关闭</text>
      </view>
      
      <view class="popup-body">
        <view class="form-group">
          <text class="label">服务日期</text>
          <picker mode="date" :value="orderForm.serviceTime" @change="bindDateChange">
            <view class="input-large picker-view">{{ orderForm.serviceTime || '请点击这里选择日期' }}</view>
          </picker>
        </view>
        <view class="form-group">
          <text class="label">上门地址</text>
          <input class="input-large" type="text" v-model="orderForm.serviceAddress" placeholder="输入具体的上门楼栋门牌号" />
        </view>
        <view class="form-group">
          <text class="label">照顾要求(选填)</text>
          <input class="input-large" type="text" v-model="orderForm.serviceRequirements" placeholder="如：需要带轮椅、行动不便等..." />
        </view>
      </view>
      
      <button class="submit-btn" @click="submitOrder" :disabled="submitting">确认下订单</button>
    </view>
    <!-- SOS 悬浮呼叫按钮 -->
    <view class="sos-btn" @click="handleSOS">
      <text>SOS</text>
    </view>
  </view>
</template>

<script>
  import { listServiceItem } from '@/api/service/item'
  import { addServiceOrder } from '@/api/order/serviceOrder'
  import { triggerSOS } from '@/api/safety/alert'

  export default {
    data() {
      return {
        serviceList: [],
        currentService: {},
        orderForm: {
          serviceAddress: '',
          serviceTime: '',
          serviceRequirements: ''
        },
        showPopup: false,
        submitting: false
      }
    },
    onShow() {
      this.getServiceList()
    },
    methods: {
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
        this.$modal.loading('正在为您派发订单...');
        
        const data = {
          serviceItemId: this.currentService.itemId,
          providerId: this.currentService.providerId,
          orderAmount: this.currentService.standardPrice,
          serviceTime: this.orderForm.serviceTime, // Backend is expecting date pattern "yyyy-MM-dd"
          serviceAddress: this.orderForm.serviceAddress,
          serviceRequirements: this.orderForm.serviceRequirements
        };
        
        addServiceOrder(data).then(res => {
          this.$modal.closeLoading();
          this.submitting = false;
          this.closeOrderPopup();
          this.$modal.msgSuccess('预约成功，等待平台服务人员接单！');
        }).catch(() => {
          this.$modal.closeLoading();
          this.submitting = false;
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
              this.$modal.loading('正在发送紧急求救信号...');
              triggerSOS().then(() => {
                this.$modal.closeLoading();
                this.$modal.msgSuccess('报警已发出！请保持电话通畅！');
              }).catch(() => {
                this.$modal.closeLoading();
              });
            }
          }
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

  .service-list {
    padding: 20px;
    margin-top: -20px;
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

  /* 弹层样式 */
  .overlay {
    position: fixed;
    top: 0; left: 0; right: 0; bottom: 0;
    background: rgba(0,0,0,0.6);
    z-index: 998;
  }

  .popup-box {
    position: fixed;
    left: 0; right: 0; bottom: 0;
    background: #fff;
    z-index: 999;
    border-top-left-radius: 24px;
    border-top-right-radius: 24px;
    padding: 25px;
    animation: slideUp 0.3s ease-out;

    .popup-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 25px;
      
      .popup-title {
        font-size: 22px;
        font-weight: bold;
        color: #333;
      }
      .close-btn {
        font-size: 18px;
        color: #999;
        padding: 5px;
      }
    }

    .form-group {
      margin-bottom: 20px;
      .label {
        font-size: 18px;
        color: #333;
        font-weight: bold;
        display: block;
        margin-bottom: 10px;
      }
      
      .input-large {
        width: 100%;
        height: 56px;
        line-height: normal; /* Fix line jumping */
        background-color: #f5f6f7;
        border-radius: 12px;
        padding: 0 15px;
        font-size: 20px;
        color: #333;
        /* Align text in picker */
        display: flex;
        align-items: center; 
      }
    }

    .submit-btn {
      margin-top: 30px;
      background-color: #e54d42; /* 紧急感红色 */
      color: #fff;
      font-size: 22px;
      font-weight: bold;
      height: 60px;
      line-height: 60px;
      border-radius: 30px;
      width: 100%;
    }
  }

  @keyframes slideUp {
    from { transform: translateY(100%); }
    to { transform: translateY(0); }
  }

  /* SOS 悬浮按钮，采用绝对的震撼红色与大阴影 */
  .sos-btn {
    position: fixed;
    right: 20px;
    bottom: 50px;
    width: 80px;
    height: 80px;
    background: radial-gradient(circle, #ff4d4f, #cf1322);
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    box-shadow: 0 4px 15px rgba(229, 77, 66, 0.6);
    z-index: 100;
    animation: pulse 2s infinite;

    text {
      color: white;
      font-size: 26px;
      font-weight: 900;
      letter-spacing: 2px;
    }
  }

  /* 心跳动画增加紧急感 */
  @keyframes pulse {
    0% { transform: scale(1); box-shadow: 0 0 0 0 rgba(229, 77, 66, 0.7); }
    70% { transform: scale(1.05); box-shadow: 0 0 0 15px rgba(229, 77, 66, 0); }
    100% { transform: scale(1); box-shadow: 0 0 0 0 rgba(229, 77, 66, 0); }
  }
</style>
