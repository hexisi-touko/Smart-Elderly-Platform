<template>
  <view class="zone-container">
    <!-- 标题 -->
    <view class="page-header">
      <text class="header-title">安全区域设置</text>
      <text class="header-desc">当老人离开设定区域时将触发预警</text>
    </view>

    <!-- 区域列表 -->
    <view class="zone-list">
      <view class="zone-card" v-for="(item, index) in zoneList" :key="index">
        <view class="zone-info">
          <view class="zone-top">
            <text class="zone-name">{{ item.zoneName }}</text>
            <view class="zone-status" :class="item.isActive == 1 ? 'active' : 'inactive'">
              <text>{{ item.isActive == 1 ? '已启用' : '已停用' }}</text>
            </view>
          </view>
          <view class="zone-detail">
            <text class="detail-item">📍 {{ item.zoneAddress || '未设置地址' }}</text>
            <text class="detail-item">📏 安全半径：{{ item.radius || 0 }}米</text>
          </view>
        </view>
        <view class="zone-actions">
          <button class="action-btn edit" @click="editZone(item)">编辑</button>
          <button class="action-btn toggle" @click="toggleZone(item)">
            {{ item.isActive == 1 ? '停用' : '启用' }}
          </button>
          <button class="action-btn delete" @click="removeZone(item)">删除</button>
        </view>
      </view>

      <view v-if="zoneList.length === 0 && !loading" class="empty-box">
        <uni-icons type="location" size="80" color="#ddd"></uni-icons>
        <text class="empty-text">暂未设置安全区域</text>
        <text class="empty-tip">添加安全区域后，老人离开区域时将自动预警</text>
      </view>
    </view>

    <!-- 底部新增按钮 -->
    <view class="bottom-bar">
      <button class="add-btn" @click="openForm()">＋ 新增安全区域</button>
    </view>

    <!-- 新增/编辑弹窗 -->
    <uni-popup ref="formPopup" type="bottom">
      <view class="form-popup">
        <view class="popup-header">
          <text class="popup-title">{{ isEdit ? '编辑区域' : '新增区域' }}</text>
          <uni-icons type="closeempty" size="22" color="#999" @click="closeForm"></uni-icons>
        </view>
        <view class="popup-body">
          <view class="form-item">
            <text class="form-label">区域名称</text>
            <input class="form-input" v-model="form.zoneName" placeholder="如：家、社区活动中心" />
          </view>
          <view class="form-item">
            <text class="form-label">区域地址</text>
            <input class="form-input" v-model="form.zoneAddress" placeholder="请输入详细地址" />
          </view>
          <view class="form-item">
            <text class="form-label">安全半径（米）</text>
            <input class="form-input" type="number" v-model="form.radius" placeholder="建议 500-2000" />
          </view>
          <view class="form-item">
            <text class="form-label">中心经度</text>
            <view class="input-row">
              <input class="form-input flex1" type="digit" v-model="form.centerLng" placeholder="经度" />
              <button class="loc-btn" @click="getMyLocation">📍定位</button>
            </view>
          </view>
          <view class="form-item">
            <text class="form-label">中心纬度</text>
            <input class="form-input" type="digit" v-model="form.centerLat" placeholder="纬度" />
          </view>
        </view>
        <button class="submit-btn" @click="submitForm" :loading="submitting">
          {{ isEdit ? '保存修改' : '确认添加' }}
        </button>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import { listMyZones, addZone, updateZone, deleteZone } from '@/api/safety/zone'

export default {
  data() {
    return {
      loading: false,
      zoneList: [],
      isEdit: false,
      submitting: false,
      form: {
        zoneId: null,
        zoneName: '',
        zoneAddress: '',
        radius: 1000,
        centerLng: '',
        centerLat: '',
        isActive: 1
      }
    }
  },
  onShow() {
    this.loadList()
  },
  methods: {
    loadList() {
      this.loading = true
      listMyZones().then(res => {
        this.zoneList = res.rows || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    openForm() {
      this.isEdit = false
      this.form = { zoneId: null, zoneName: '', zoneAddress: '', radius: 1000, centerLng: '', centerLat: '', isActive: 1 }
      this.$refs.formPopup.open()
    },
    editZone(item) {
      this.isEdit = true
      this.form = { ...item }
      this.$refs.formPopup.open()
    },
    closeForm() {
      this.$refs.formPopup.close()
    },
    getMyLocation() {
      uni.getLocation({
        type: 'wgs84',
        success: (loc) => {
          this.form.centerLng = loc.longitude.toFixed(6)
          this.form.centerLat = loc.latitude.toFixed(6)
          uni.showToast({ title: '定位成功', icon: 'success' })
        },
        fail: () => {
          uni.showToast({ title: '定位失败', icon: 'none' })
        }
      })
    },
    submitForm() {
      if (!this.form.zoneName) return uni.showToast({ title: '请输入区域名称', icon: 'none' })
      if (!this.form.zoneAddress) return uni.showToast({ title: '请输入区域地址', icon: 'none' })
      if (!this.form.radius) return uni.showToast({ title: '请输入安全半径', icon: 'none' })

      this.submitting = true
      const api = this.isEdit ? updateZone(this.form) : addZone(this.form)
      api.then(res => {
        if (res.code === 200) {
          uni.showToast({ title: this.isEdit ? '修改成功' : '添加成功', icon: 'success' })
          this.closeForm()
          this.loadList()
        }
      }).finally(() => {
        this.submitting = false
      })
    },
    toggleZone(item) {
      const newStatus = item.isActive == 1 ? 0 : 1
      updateZone({ zoneId: item.zoneId, isActive: newStatus }).then(res => {
        if (res.code === 200) {
          uni.showToast({ title: newStatus == 1 ? '已启用' : '已停用', icon: 'success' })
          this.loadList()
        }
      })
    },
    removeZone(item) {
      uni.showModal({
        title: '提示',
        content: `确定删除安全区域"${item.zoneName}"吗？`,
        success: (res) => {
          if (res.confirm) {
            deleteZone(item.zoneId).then(res => {
              if (res.code === 200) {
                uni.showToast({ title: '已删除', icon: 'success' })
                this.loadList()
              }
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.zone-container {
  min-height: 100vh;
  background: #f5f6fa;
  padding-bottom: 130rpx;
}

.page-header {
  background: linear-gradient(135deg, #1976d2, #42a5f5);
  padding: 50rpx 32rpx 36rpx;
  .header-title { font-size: 38rpx; font-weight: bold; color: #fff; display: block; }
  .header-desc { font-size: 24rpx; color: rgba(255,255,255,0.8); margin-top: 8rpx; display: block; }
}

.zone-list {
  padding: 20rpx 24rpx;
}

.zone-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05);
}
.zone-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}
.zone-name { font-size: 32rpx; font-weight: bold; color: #333; }
.zone-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  &.active { background: #e8f5e9; color: #43a047; }
  &.inactive { background: #fce4ec; color: #e53935; }
}
.zone-detail {
  .detail-item { font-size: 26rpx; color: #666; display: block; margin-bottom: 8rpx; }
}
.zone-actions {
  display: flex;
  gap: 12rpx;
  margin-top: 20rpx;
  padding-top: 16rpx;
  border-top: 1rpx solid #f0f0f0;
}
.action-btn {
  flex: 1;
  height: 64rpx;
  font-size: 24rpx;
  border-radius: 32rpx;
  line-height: 64rpx;
  border: none;
  &.edit { background: #e3f2fd; color: #1976d2; }
  &.toggle { background: #fff3e0; color: #f57c00; }
  &.delete { background: #fce4ec; color: #e53935; }
}

.empty-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
  .empty-text { font-size: 28rpx; color: #999; margin-top: 20rpx; }
  .empty-tip { font-size: 24rpx; color: #ccc; margin-top: 8rpx; }
}

.bottom-bar {
  position: fixed;
  bottom: 0; left: 0; width: 100%;
  padding: 20rpx 32rpx;
  background: #fff;
  box-shadow: 0 -2rpx 12rpx rgba(0,0,0,0.06);
  box-sizing: border-box;
}
.add-btn {
  width: 100%; height: 90rpx;
  background: linear-gradient(135deg, #1976d2, #42a5f5);
  color: #fff; font-size: 32rpx; font-weight: bold;
  border-radius: 45rpx; border: none; line-height: 90rpx;
}

/* 弹窗 */
.form-popup {
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding: 30rpx;
  .popup-header {
    display: flex; justify-content: space-between; align-items: center;
    margin-bottom: 30rpx;
    .popup-title { font-size: 34rpx; font-weight: bold; }
  }
  .form-item { margin-bottom: 24rpx; }
  .form-label { font-size: 28rpx; color: #666; margin-bottom: 12rpx; display: block; }
  .form-input {
    width: 100%; height: 80rpx; line-height: 80rpx;
    background: #f5f5f5; border-radius: 12rpx;
    padding: 0 20rpx; font-size: 28rpx; box-sizing: border-box;
  }
  .input-row { display: flex; gap: 12rpx; }
  .flex1 { flex: 1; }
  .loc-btn {
    width: 140rpx; height: 80rpx; line-height: 80rpx;
    background: #e3f2fd; color: #1976d2; font-size: 26rpx;
    border-radius: 12rpx; border: none;
  }
  .submit-btn {
    width: 100%; height: 88rpx;
    background: linear-gradient(135deg, #43a047, #66bb6a);
    color: #fff; font-size: 32rpx;
    border-radius: 44rpx; border: none; line-height: 88rpx;
    margin-top: 20rpx;
  }
}
</style>
