<template>
  <view class="container bg-white">
    <view class="header padding-xl text-center">
      <view class="cu-avatar xl round bg-blue margin-bottom">助</view>
      <view class="text-xxl text-bold">助老员资料补全</view>
      <view class="text-gray margin-top-sm">完善资料后开始服务之旅</view>
    </view>

    <view class="form-content padding-lr-xl">
      <view class="cu-form-group margin-top radius shadow-sm">
        <view class="title">真实姓名</view>
        <input placeholder="请输入姓名" v-model="form.staffName"></input>
      </view>
      <view class="cu-form-group radius shadow-sm">
        <view class="title">手机号码</view>
        <input placeholder="请输入手机号" v-model="form.phone" type="number" maxlength="11"></input>
      </view>
      <view class="cu-form-group radius shadow-sm">
        <view class="title">身份证号</view>
        <input placeholder="请输入18位身份证号" v-model="form.idCard" maxlength="18"></input>
      </view>
      <view class="cu-form-group radius shadow-sm">
        <view class="title">资格证书</view>
        <input placeholder="如：养老护理员证编号" v-model="form.certificate"></input>
      </view>
      <view class="cu-form-group radius shadow-sm">
        <view class="title">人员类型</view>
        <picker @change="bindTypeChange" :value="typeIndex" :range="typeRange">
          <view class="picker">
            {{ form.staffType ? form.staffType : '请选择人员类型' }}
          </view>
        </picker>
      </view>

      <view class="padding-xl margin-top-lg">
        <button class="cu-btn bg-blue lg block round shadow" @click="submitForm">提交审核</button>
      </view>
    </view>
  </view>
</template>

<script>
  import { setupStaff } from '@/api/service/staff'

  export default {
    data() {
      return {
        form: {
          staffName: '',
          phone: '',
          idCard: '',
          certificate: '',
          staffType: '养老护理员'
        },
        typeRange: ['养老护理员', '社工', '医护人员', '家政员'],
        typeIndex: 0
      }
    },
    methods: {
      bindTypeChange(e) {
        this.typeIndex = e.detail.value;
        this.form.staffType = this.typeRange[this.typeIndex];
      },
      submitForm() {
        if (!this.form.staffName) return this.$modal.msgError('请输入真实姓名');
        if (!this.form.phone) return this.$modal.msgError('请输入手机号');
        
        this.$modal.loading('提交中...');
        setupStaff(this.form).then(res => {
          this.$modal.closeLoading();
          this.$modal.msgSuccess('资料补全成功');
          setTimeout(() => {
            this.$tab.reLaunch('/pages/index');
          }, 1500);
        }).catch(() => {
          this.$modal.closeLoading();
        });
      }
    }
  }
</script>

<style lang="scss" scoped>
  .container {
    min-height: 100vh;
  }
  .header {
    background: linear-gradient(180deg, #e6f7ff 0%, #ffffff 100%);
  }
  .cu-form-group {
    border: 1rpx solid #f1f1f1;
    margin-bottom: 20rpx;
    .title {
      min-width: 160rpx;
    }
  }
</style>
