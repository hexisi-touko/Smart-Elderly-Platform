<template>
  <view class="container">
    <view class="header">
      <text class="title">完善老人档案</text>
      <text class="subtitle">为了更好地为您服务，请填写真实有效的信息</text>
    </view>

    <view class="form-content">
      <view class="input-group">
        <text class="label">真实姓名 <text class="text-red">*</text></text>
        <input class="input-field" v-model="form.name" type="text" placeholder="请输入您的姓名" maxlength="20" />
      </view>

      <view class="input-group">
        <text class="label">手机号码 <text class="text-red">*</text></text>
        <input class="input-field" v-model="form.phone" type="number" placeholder="请输入您的手机号" maxlength="11" />
      </view>

      <view class="input-group">
        <text class="label">身份证号 <text class="text-red">*</text></text>
        <input class="input-field" v-model="form.idCard" type="idcard" placeholder="请输入您的身份证号" maxlength="18" />
      </view>

      <view class="input-group">
        <text class="label">紧急联系人 <text class="text-red">*</text></text>
        <input class="input-field" v-model="form.emergencyContactName" type="text" placeholder="请输入紧急联系人姓名" maxlength="20" />
      </view>

      <view class="input-group">
        <text class="label">紧急电话 <text class="text-red">*</text></text>
        <input class="input-field" v-model="form.emergencyContactPhone" type="number" placeholder="请输入紧急联系人手机号" maxlength="11" />
      </view>

      <button class="submit-btn" @click="submitProfile">保存并进入主页</button>
    </view>
  </view>
</template>

<script>
  import { addElderly } from '@/api/elderly/elderly'

  export default {
    data() {
      return {
        form: {
          name: '',
          phone: '',
          idCard: '',
          emergencyContactName: '',
          emergencyContactPhone: ''
        }
      }
    },
    onShow() {
      // 从 store 读取注册时的基本信息
      const user = this.$store.state.user
      this.form.name = user.name
      // TODO: the original phone is used for login, we can get it from storage if needed or require user to re-input for confirmation
      // Currently the TAppUser has no phone exposed by default to vuex, but the backend requires it.
      // We'll let them input it or we can fetch it via another API. For now, we allow them to input their own phone.
    },
    methods: {
      submitProfile() {
        if (!this.form.name) return this.$modal.msgError('请输入真实姓名')
        if (!this.form.phone) return this.$modal.msgError('请输入手机号')
        if (!this.form.idCard) return this.$modal.msgError('请输入身份证号')
        if (!this.form.emergencyContactName) return this.$modal.msgError('请输入紧急联系人姓名')
        if (!this.form.emergencyContactPhone) return this.$modal.msgError('请输入紧急电话')

        this.$modal.loading("提交资料中...")
        addElderly(this.form).then(res => {
          this.$modal.closeLoading()
          this.$modal.msgSuccess("资料完善成功")
          // 重新获取一下用户信息，更新 profileCompleted 标识
          this.$store.dispatch('GetInfo').then(() => {
            this.$tab.reLaunch('/pages/index')
          })
        }).catch(() => {
          this.$modal.closeLoading()
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  /* 贯彻适老化设计：大字体、高对比、大按钮 */
  .container {
    padding: 20px;
    background-color: #f5f6f7;
    min-height: 100vh;
  }

  .header {
    margin-bottom: 30px;
    text-align: center;
    .title {
      font-size: 24px;
      font-weight: bold;
      color: #333333;
      display: block;
      margin-bottom: 10px;
    }
    .subtitle {
      font-size: 16px;
      color: #666666;
    }
  }

  .form-content {
    background-color: #ffffff;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.05);

    .input-group {
      margin-bottom: 25px;

      .label {
        font-size: 18px;
        color: #333333;
        font-weight: 500;
        margin-bottom: 10px;
        display: block;
      }

      .text-red {
        color: #e53935;
      }

      .input-field {
        width: 100%;
        height: 52px;
        background-color: #f8f9fa;
        border-radius: 8px;
        padding: 0 15px;
        font-size: 18px;
        border: 1px solid #e0e0e0;
        color: #333;
      }
      
      .hint-text {
         font-size: 14px;
         color: #999;
         margin-top: 5px;
         display: block;
      }
    }

    .submit-btn {
      margin-top: 40px;
      background-color: #0081ff;
      color: #ffffff;
      height: 56px;
      line-height: 56px;
      border-radius: 28px;
      font-size: 20px;
      font-weight: bold;
      width: 100%;
    }
  }
</style>
