<template>
  <view class="medication-container">
    <!-- 顶部操作栏 -->
    <view class="top-bar">
      <text class="page-title">我的用药提醒</text>
      <button class="add-btn" @click="openForm(null)">+ 添加药品</button>
    </view>

    <!-- 用药提醒列表 -->
    <view class="med-list">
      <view class="med-card" v-for="(item, index) in medList" :key="index">
        <view class="card-left">
          <view class="med-icon" :class="item.status === 1 ? 'active' : 'inactive'">
            <text class="icon-text">💊</text>
          </view>
        </view>
        <view class="card-center">
          <text class="med-name">{{ item.medicationName }}</text>
          <text class="med-dosage">{{ item.dosage }} · {{ item.frequency }}</text>
          <text class="med-time">提醒时间：{{ formatTime(item.reminderTime) }}</text>
          <text class="med-period" v-if="item.startDate">
            {{ formatDate(item.startDate) }} 至 {{ formatDate(item.endDate) }}
          </text>
        </view>
        <view class="card-right">
          <view class="status-tag" :class="item.status === 1 ? 'tag-on' : 'tag-off'">
            {{ item.status === 1 ? '进行中' : '已停用' }}
          </view>
          <view class="action-row">
            <text class="action-btn edit" @click="openForm(item)">编辑</text>
            <text class="action-btn delete" @click="handleDelete(item)">删除</text>
          </view>
        </view>
      </view>

      <!-- 空状态 -->
      <view v-if="medList.length === 0 && !loading" class="empty-box">
        <text class="empty-icon">📋</text>
        <text class="empty-text">暂无用药提醒</text>
        <text class="empty-tip">点击上方「添加药品」开始记录</text>
      </view>
    </view>

    <!-- 新增/编辑弹窗 -->
    <view class="overlay" v-if="showForm" @click="closeForm"></view>
    <view class="form-drawer" v-if="showForm" :class="{'drawer-show': showForm}">
      <view class="drawer-header">
        <text class="drawer-title">{{ isEdit ? '编辑药品' : '添加药品' }}</text>
        <view class="close-icon" @click="closeForm">
          <uni-icons type="closeempty" size="24" color="#999"></uni-icons>
        </view>
      </view>

      <scroll-view scroll-y class="drawer-body">
        <view class="form-item">
          <text class="form-label">药品名称 <text class="required">*</text></text>
          <input class="form-input" v-model="form.medicationName" placeholder="请输入药品名称" />
        </view>

        <view class="form-item">
          <text class="form-label">药品类型</text>
          <picker :range="typeOptions" @change="onTypeChange" :value="typeIndex">
            <view class="form-input picker-display">
              {{ form.medicationType || '请选择药品类型' }}
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="form-label">用药剂量 <text class="required">*</text></text>
          <input class="form-input" v-model="form.dosage" placeholder="如：每次2片" />
        </view>

        <view class="form-item">
          <text class="form-label">用药频率 <text class="required">*</text></text>
          <picker :range="freqOptions" @change="onFreqChange" :value="freqIndex">
            <view class="form-input picker-display">
              {{ form.frequency || '请选择用药频率' }}
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="form-label">提醒时间 <text class="required">*</text></text>
          <picker mode="time" :value="form.reminderTimeStr" @change="onTimeChange">
            <view class="form-input picker-display">
              {{ form.reminderTimeStr || '请选择提醒时间' }}
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="form-label">开始日期</text>
          <picker mode="date" :value="form.startDateStr" @change="onStartDateChange">
            <view class="form-input picker-display">
              {{ form.startDateStr || '请选择开始日期' }}
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="form-label">结束日期</text>
          <picker mode="date" :value="form.endDateStr" @change="onEndDateChange">
            <view class="form-input picker-display">
              {{ form.endDateStr || '请选择结束日期' }}
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="form-label">备注说明</text>
          <textarea class="form-textarea" v-model="form.note" placeholder="如服药注意事项" maxlength="200" />
        </view>

        <button class="submit-btn" @click="handleSubmit">
          {{ isEdit ? '保存修改' : '确认添加' }}
        </button>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { listMedication, addMedication, updateMedication, delMedication } from '@/api/health/medication'

export default {
  data() {
    return {
      loading: false,
      medList: [],
      showForm: false,
      isEdit: false,
      form: {},
      typeOptions: ['西药', '中成药', '中草药', '保健品', '其他'],
      typeIndex: 0,
      freqOptions: ['每日一次', '每日两次', '每日三次', '隔日一次', '每周一次'],
      freqIndex: 0
    }
  },
  onShow() {
    this.loadList()
  },
  methods: {
    /** 加载列表 */
    loadList() {
      this.loading = true
      listMedication().then(res => {
        this.medList = res.rows || []
      }).finally(() => {
        this.loading = false
      })
    },

    /** 打开表单弹窗 */
    openForm(item) {
      if (item) {
        this.isEdit = true
        this.form = {
          ...item,
          reminderTimeStr: this.formatTime(item.reminderTime),
          startDateStr: this.formatDate(item.startDate),
          endDateStr: this.formatDate(item.endDate)
        }
        this.typeIndex = this.typeOptions.indexOf(item.medicationType) >= 0
          ? this.typeOptions.indexOf(item.medicationType) : 0
        this.freqIndex = this.freqOptions.indexOf(item.frequency) >= 0
          ? this.freqOptions.indexOf(item.frequency) : 0
      } else {
        this.isEdit = false
        this.form = {
          medicationName: '',
          medicationType: '',
          dosage: '',
          frequency: '',
          reminderTimeStr: '',
          startDateStr: '',
          endDateStr: '',
          note: ''
        }
      }
      this.showForm = true
    },

    /** 关闭表单 */
    closeForm() {
      this.showForm = false
    },

    /** 选择器事件 */
    onTypeChange(e) {
      this.typeIndex = e.detail.value
      this.form.medicationType = this.typeOptions[this.typeIndex]
    },
    onFreqChange(e) {
      this.freqIndex = e.detail.value
      this.form.frequency = this.freqOptions[this.freqIndex]
    },
    onTimeChange(e) {
      this.form.reminderTimeStr = e.detail.value
    },
    onStartDateChange(e) {
      this.form.startDateStr = e.detail.value
    },
    onEndDateChange(e) {
      this.form.endDateStr = e.detail.value
    },

    /** 提交表单 */
    handleSubmit() {
      if (!this.form.medicationName) {
        return this.$modal.showToast('请输入药品名称')
      }
      if (!this.form.dosage) {
        return this.$modal.showToast('请输入用药剂量')
      }
      if (!this.form.frequency) {
        return this.$modal.showToast('请选择用药频率')
      }

      const data = { ...this.form }
      // 时间字段转换
      if (data.reminderTimeStr) {
        data.reminderTime = data.startDateStr
          ? data.startDateStr + ' ' + data.reminderTimeStr + ':00'
          : new Date().toISOString().slice(0, 10) + ' ' + data.reminderTimeStr + ':00'
      }
      if (data.startDateStr) {
        data.startDate = data.startDateStr
      }
      if (data.endDateStr) {
        data.endDate = data.endDateStr
      }

      const action = this.isEdit ? updateMedication(data) : addMedication(data)
      action.then(res => {
        if (res.code === 200) {
          this.$modal.showToast(this.isEdit ? '修改成功' : '添加成功')
          this.closeForm()
          this.loadList()
        } else {
          this.$modal.showToast(res.msg || '操作失败')
        }
      })
    },

    /** 删除 */
    handleDelete(item) {
      uni.showModal({
        title: '提示',
        content: `确定要删除「${item.medicationName}」吗？`,
        success: (res) => {
          if (res.confirm) {
            delMedication(item.reminderId).then(res => {
              if (res.code === 200) {
                this.$modal.showToast('删除成功')
                this.loadList()
              }
            })
          }
        }
      })
    },

    /** 格式化日期 */
    formatDate(dateStr) {
      if (!dateStr) return ''
      return dateStr.substring(0, 10)
    },
    formatTime(dateStr) {
      if (!dateStr) return ''
      if (dateStr.length >= 16) {
        return dateStr.substring(11, 16)
      }
      return dateStr
    }
  }
}
</script>

<style lang="scss" scoped>
.medication-container {
  min-height: 100vh;
  background: #f5f6fa;
  padding-bottom: 40rpx;
}

/* 顶部操作栏 */
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx 32rpx 20rpx;
  background: #fff;
}
.page-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}
.add-btn {
  font-size: 28rpx;
  color: #fff;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  border-radius: 40rpx;
  padding: 14rpx 36rpx;
  line-height: 1.4;
}

/* 列表 */
.med-list {
  padding: 20rpx 24rpx;
}
.med-card {
  display: flex;
  align-items: flex-start;
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}
.card-left {
  margin-right: 20rpx;
}
.med-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.med-icon.active {
  background: linear-gradient(135deg, #e0f7fa, #b2ebf2);
}
.med-icon.inactive {
  background: #f0f0f0;
}
.icon-text {
  font-size: 36rpx;
}
.card-center {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.med-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}
.med-dosage {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 6rpx;
}
.med-time {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 4rpx;
}
.med-period {
  font-size: 22rpx;
  color: #bbb;
}
.card-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  min-width: 120rpx;
}
.status-tag {
  font-size: 22rpx;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  margin-bottom: 16rpx;
}
.tag-on {
  background: #e8f5e9;
  color: #43a047;
}
.tag-off {
  background: #f5f5f5;
  color: #999;
}
.action-row {
  display: flex;
  gap: 16rpx;
}
.action-btn {
  font-size: 24rpx;
  padding: 6rpx 12rpx;
}
.action-btn.edit {
  color: #667eea;
}
.action-btn.delete {
  color: #e53935;
}

/* 空状态 */
.empty-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 120rpx 0;
}
.empty-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}
.empty-text {
  font-size: 32rpx;
  color: #999;
  margin-bottom: 12rpx;
}
.empty-tip {
  font-size: 26rpx;
  color: #ccc;
}

/* 弹窗遮罩 */
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  z-index: 998;
}

/* 底部抽屉表单 */
.form-drawer {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  max-height: 85vh;
  background: #fff;
  border-radius: 32rpx 32rpx 0 0;
  z-index: 999;
  transform: translateY(100%);
  transition: transform 0.3s ease;
}
.form-drawer.drawer-show {
  transform: translateY(0);
}
.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx 32rpx 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}
.drawer-title {
  font-size: 34rpx;
  font-weight: bold;
  color: #333;
}
.drawer-body {
  padding: 20rpx 32rpx 40rpx;
  max-height: 70vh;
}
.form-item {
  margin-bottom: 28rpx;
}
.form-label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 12rpx;
  display: block;
}
.required {
  color: #e53935;
}
.form-input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  box-sizing: border-box;
  background: #fafafa;
}
.picker-display {
  display: flex;
  justify-content: space-between;
  align-items: center;
  line-height: 80rpx;
  color: #333;
}
.picker-arrow {
  font-size: 22rpx;
  color: #ccc;
}
.form-textarea {
  width: 100%;
  height: 160rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 12rpx;
  padding: 20rpx 24rpx;
  font-size: 28rpx;
  box-sizing: border-box;
  background: #fafafa;
}
.submit-btn {
  width: 100%;
  height: 88rpx;
  font-size: 32rpx;
  color: #fff;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  border-radius: 44rpx;
  margin-top: 20rpx;
  line-height: 88rpx;
}
</style>
