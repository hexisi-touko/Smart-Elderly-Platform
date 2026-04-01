<template>
  <view class="exam-container">
    <!-- 顶部操作栏 -->
    <view class="top-bar">
      <text class="page-title">体检预约</text>
      <button class="add-btn" @click="openForm">+ 预约体检</button>
    </view>

    <!-- 状态筛选 -->
    <view class="filter-bar">
      <view
        v-for="(tab, index) in tabs"
        :key="index"
        class="filter-item"
        :class="{ active: currentTab === tab.value }"
        @click="handleTabChange(tab.value)"
      >
        <text>{{ tab.label }}</text>
      </view>
    </view>

    <!-- 预约列表 -->
    <view class="exam-list">
      <view class="exam-card" v-for="(item, index) in examList" :key="index">
        <view class="card-top">
          <view class="exam-type-tag">{{ item.examType || '常规体检' }}</view>
          <view class="status-tag" :class="item.statusClass">
            {{ item.statusLabel }}
          </view>
        </view>

        <view class="card-body">
          <view class="info-row">
            <text class="label">体检机构</text>
            <text class="value">{{ item.examInstitution || '待指定' }}</text>
          </view>
          <view class="info-row">
            <text class="label">预约时间</text>
            <text class="value">{{ formatDate(item.reservationTime) }}</text>
          </view>
          <view class="info-row" v-if="item.examAddress">
            <text class="label">体检地址</text>
            <text class="value">{{ item.examAddress }}</text>
          </view>
          <view class="info-row" v-if="item.examItems">
            <text class="label">体检项目</text>
            <text class="value">{{ item.examItems }}</text>
          </view>
        </view>

        <view class="card-footer">
          <!-- 报告状态 -->
          <view class="report-tag" v-if="item.reportStatus === 1">
            <text class="report-text">📄 报告已出</text>
          </view>
          <view class="report-tag pending" v-else-if="item.reservationStatus !== 3">
            <text class="report-text">⏳ 待出报告</text>
          </view>

          <!-- 操作按钮 -->
          <button
            class="cancel-btn"
            v-if="item.reservationStatus === 0 || item.reservationStatus === 1"
            @click="handleCancel(item)"
          >
            取消预约
          </button>
        </view>
      </view>

      <!-- 空状态 -->
      <view v-if="examList.length === 0 && !loading" class="empty-box">
        <text class="empty-icon">🏥</text>
        <text class="empty-text">暂无体检预约</text>
        <text class="empty-tip">点击上方「预约体检」开始预约</text>
      </view>
    </view>

    <!-- 预约弹窗 -->
    <view class="overlay" v-if="showForm" @click="closeForm"></view>
    <view class="form-drawer" v-if="showForm" :class="{'drawer-show': showForm}">
      <view class="drawer-header">
        <text class="drawer-title">预约体检</text>
        <view class="close-icon" @click="closeForm">
          <uni-icons type="closeempty" size="24" color="#999"></uni-icons>
        </view>
      </view>

      <scroll-view scroll-y class="drawer-body">
        <view class="form-item">
          <text class="form-label">体检类型 <text class="required">*</text></text>
          <picker :range="examTypeOptions" @change="onExamTypeChange" :value="examTypeIndex">
            <view class="form-input picker-display">
              {{ form.examType || '请选择体检类型' }}
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="form-label">体检机构 <text class="required">*</text></text>
          <input class="form-input" v-model="form.examInstitution" placeholder="请输入体检机构名称" />
        </view>

        <view class="form-item">
          <text class="form-label">预约日期 <text class="required">*</text></text>
          <picker mode="date" :value="form.reservationTimeStr" :start="today" @change="onDateChange">
            <view class="form-input picker-display">
              {{ form.reservationTimeStr || '请选择预约日期' }}
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="form-label">体检地址</text>
          <input class="form-input" v-model="form.examAddress" placeholder="请输入体检地址" />
        </view>

        <view class="form-item">
          <text class="form-label">体检项目</text>
          <textarea
            class="form-textarea"
            v-model="form.examItems"
            placeholder="如：血常规、肝功能、心电图等"
            maxlength="500"
          />
        </view>

        <button class="submit-btn" @click="handleSubmit">确认预约</button>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { listExam, addExam, cancelExam } from '@/api/health/exam'

export default {
  data() {
    return {
      loading: false,
      examList: [],
      showForm: false,
      currentTab: null,
      tabs: [
        { label: '全部', value: null },
        { label: '待确认', value: 0 },
        { label: '已确认', value: 1 },
        { label: '已完成', value: 2 },
        { label: '已取消', value: 3 }
      ],
      form: {},
      examTypeOptions: ['常规体检', '入职体检', '老年专项', '慢病随访', '其他'],
      examTypeIndex: 0,
      today: new Date().toISOString().slice(0, 10)
    }
  },
  onShow() {
    this.loadList()
  },
  methods: {
    /** 加载列表 */
    loadList() {
      this.loading = true
      const query = {}
      if (this.currentTab !== null) {
        query.reservationStatus = this.currentTab
      }
      listExam(query).then(res => {
        const rows = res.rows || []
        // 预处理状态样式（uni-app Vue2 不支持 :class 中调用方法）
        const statusClassMap = { 0: 'status-pending', 1: 'status-confirmed', 2: 'status-done', 3: 'status-cancelled' }
        const statusTextMap = { 0: '待确认', 1: '已确认', 2: '已完成', 3: '已取消' }
        this.examList = rows.map(item => ({
          ...item,
          statusClass: statusClassMap[item.reservationStatus] || 'status-pending',
          statusLabel: statusTextMap[item.reservationStatus] || '待确认'
        }))
      }).finally(() => {
        this.loading = false
      })
    },

    /** 切换状态 */
    handleTabChange(value) {
      this.currentTab = value
      this.loadList()
    },

    /** 打开表单 */
    openForm() {
      this.form = {
        examType: '',
        examInstitution: '',
        reservationTimeStr: '',
        examAddress: '',
        examItems: ''
      }
      this.examTypeIndex = 0
      this.showForm = true
    },
    closeForm() {
      this.showForm = false
    },

    /** 选择器事件 */
    onExamTypeChange(e) {
      this.examTypeIndex = e.detail.value
      this.form.examType = this.examTypeOptions[this.examTypeIndex]
    },
    onDateChange(e) {
      this.form.reservationTimeStr = e.detail.value
    },

    /** 提交预约 */
    handleSubmit() {
      if (!this.form.examType) {
        return this.$modal.showToast('请选择体检类型')
      }
      if (!this.form.examInstitution) {
        return this.$modal.showToast('请输入体检机构')
      }
      if (!this.form.reservationTimeStr) {
        return this.$modal.showToast('请选择预约日期')
      }

      const data = { ...this.form }
      data.reservationTime = data.reservationTimeStr

      addExam(data).then(res => {
        if (res.code === 200) {
          this.$modal.showToast('预约成功')
          this.closeForm()
          this.loadList()
        } else {
          this.$modal.showToast(res.msg || '操作失败')
        }
      })
    },

    /** 取消预约 */
    handleCancel(item) {
      uni.showModal({
        title: '提示',
        content: '确定要取消该体检预约吗？',
        success: (res) => {
          if (res.confirm) {
            cancelExam(item.reservationId).then(res => {
              if (res.code === 200) {
                this.$modal.showToast('已取消预约')
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
    }
  }
}
</script>

<style lang="scss" scoped>
.exam-container {
  min-height: 100vh;
  background: #f5f6fa;
  padding-bottom: 40rpx;
}

/* 顶部 */
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
  background: linear-gradient(135deg, #43a047, #66bb6a);
  border: none;
  border-radius: 40rpx;
  padding: 14rpx 36rpx;
  line-height: 1.4;
}

/* 筛选栏 */
.filter-bar {
  display: flex;
  background: #fff;
  padding: 16rpx 32rpx 20rpx;
  gap: 16rpx;
  border-bottom: 1rpx solid #f0f0f0;
}
.filter-item {
  font-size: 26rpx;
  color: #666;
  padding: 10rpx 24rpx;
  border-radius: 30rpx;
  background: #f5f5f5;
}
.filter-item.active {
  color: #fff;
  background: linear-gradient(135deg, #43a047, #66bb6a);
}

/* 列表 */
.exam-list {
  padding: 20rpx 24rpx;
}
.exam-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}
.card-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}
.exam-type-tag {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}
.status-tag {
  font-size: 22rpx;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}
.status-pending { background: #fff3e0; color: #ef6c00; }
.status-confirmed { background: #e3f2fd; color: #1976d2; }
.status-done { background: #e8f5e9; color: #43a047; }
.status-cancelled { background: #f5f5f5; color: #999; }

.card-body {
  margin-bottom: 16rpx;
}
.info-row {
  display: flex;
  margin-bottom: 12rpx;
}
.info-row .label {
  font-size: 26rpx;
  color: #999;
  width: 150rpx;
  flex-shrink: 0;
}
.info-row .value {
  font-size: 26rpx;
  color: #333;
  flex: 1;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16rpx;
  border-top: 1rpx solid #f5f5f5;
}
.report-tag {
  display: flex;
  align-items: center;
}
.report-text {
  font-size: 24rpx;
  color: #43a047;
}
.report-tag.pending .report-text {
  color: #999;
}
.cancel-btn {
  font-size: 26rpx;
  color: #e53935;
  background: #fff;
  border: 1rpx solid #e53935;
  border-radius: 30rpx;
  padding: 10rpx 28rpx;
  line-height: 1.4;
}

/* 空状态 */
.empty-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 120rpx 0;
}
.empty-icon { font-size: 80rpx; margin-bottom: 20rpx; }
.empty-text { font-size: 32rpx; color: #999; margin-bottom: 12rpx; }
.empty-tip { font-size: 26rpx; color: #ccc; }

/* 弹窗 */
.overlay {
  position: fixed; top: 0; left: 0; width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.4); z-index: 998;
}
.form-drawer {
  position: fixed; left: 0; bottom: 0; width: 100%;
  max-height: 85vh; background: #fff;
  border-radius: 32rpx 32rpx 0 0; z-index: 999;
  transform: translateY(100%); transition: transform 0.3s ease;
}
.form-drawer.drawer-show { transform: translateY(0); }
.drawer-header {
  display: flex; justify-content: space-between; align-items: center;
  padding: 30rpx 32rpx 20rpx; border-bottom: 1rpx solid #f0f0f0;
}
.drawer-title { font-size: 34rpx; font-weight: bold; color: #333; }
.drawer-body { padding: 20rpx 32rpx 40rpx; max-height: 70vh; }
.form-item { margin-bottom: 28rpx; }
.form-label { font-size: 28rpx; color: #333; margin-bottom: 12rpx; display: block; }
.required { color: #e53935; }
.form-input {
  width: 100%; height: 80rpx;
  border: 1rpx solid #e0e0e0; border-radius: 12rpx;
  padding: 0 24rpx; font-size: 28rpx;
  box-sizing: border-box; background: #fafafa;
}
.picker-display {
  display: flex; justify-content: space-between; align-items: center;
  line-height: 80rpx; color: #333;
}
.picker-arrow { font-size: 22rpx; color: #ccc; }
.form-textarea {
  width: 100%; height: 160rpx;
  border: 1rpx solid #e0e0e0; border-radius: 12rpx;
  padding: 20rpx 24rpx; font-size: 28rpx;
  box-sizing: border-box; background: #fafafa;
}
.submit-btn {
  width: 100%; height: 88rpx; font-size: 32rpx;
  color: #fff; background: linear-gradient(135deg, #43a047, #66bb6a);
  border: none; border-radius: 44rpx; margin-top: 20rpx; line-height: 88rpx;
}
</style>
