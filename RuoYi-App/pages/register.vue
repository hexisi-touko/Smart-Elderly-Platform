<template>
  <view class="normal-login-container">
    <view class="logo-content align-center justify-center flex">
      <image style="width: 100rpx;height: 100rpx;" :src="globalConfig.appInfo.logo" mode="widthFix">
      </image>
      <text class="title">智慧居家养老注册</text>
    </view>
    <view class="login-form-content">
      <view class="input-item flex align-center">
        <view class="iconfont icon-user icon"></view>
        <input v-model="registerForm.phone" class="input" type="number" placeholder="请输入手机号" maxlength="11" />
      </view>
      <view class="input-item flex align-center">
        <view class="iconfont icon-user icon"></view>
        <input v-model="registerForm.realName" class="input" type="text" placeholder="请输入真实姓名" maxlength="20" />
      </view>
      <view class="input-item flex align-center">
        <view class="iconfont icon-password icon"></view>
        <input v-model="registerForm.password" :password="true" class="input" placeholder="请输入密码" maxlength="20" />
      </view>
      <view class="input-item flex align-center">
        <view class="iconfont icon-password icon"></view>
        <input v-model="registerForm.confirmPassword" :password="true" class="input" placeholder="请再次输入密码" maxlength="20" />
      </view>
      <view class="input-item flex align-center">
        <view class="iconfont icon-user icon"></view>
        <picker v-model="registerForm.userType" :range="userTypeOptions" range-key="label" @change="handleUserTypeChange">
          <view class="input" :style="{ color: registerForm.userType ? '#333' : '#999' }">
            {{ userTypeLabel || '请选择用户类型' }}
          </view>
        </picker>
      </view>
      <view class="action-btn">
        <button @click="handleRegister()" class="register-btn cu-btn block bg-blue lg round">注册</button>
      </view>
    </view>
    <view class="xieyi text-center">
      <text @click="handleUserLogin" class="text-blue">使用已有账号登录</text>
    </view>
  </view>
</template>

<script>
  import { register } from '@/api/login'

  export default {
    data() {
      return {
        globalConfig: getApp().globalData.config,
        userTypeOptions: [
          { label: '老人', value: 'elderly' },
          { label: '监护人', value: 'guardian' }
        ],
        registerForm: {
          phone: "",
          realName: "",
          password: "",
          confirmPassword: "",
          userType: ""
        }
      }
    },
    computed: {
      userTypeLabel() {
        const found = this.userTypeOptions.find(opt => opt.value === this.registerForm.userType)
        return found ? found.label : ''
      }
    },
    methods: {
      // 返回登录
      handleUserLogin() {
        this.$tab.navigateTo(`/pages/login`)
      },
      // 用户类型选择
      handleUserTypeChange(e) {
        this.registerForm.userType = this.userTypeOptions[e.detail.value].value
      },
      // 注册方法
      async handleRegister() {
        if (this.registerForm.phone === "") {
          this.$modal.msgError("请输入手机号")
        } else if (this.registerForm.phone.length !== 11) {
          this.$modal.msgError("请输入正确的手机号")
        } else if (this.registerForm.realName === "") {
          this.$modal.msgError("请输入真实姓名")
        } else if (this.registerForm.password === "") {
          this.$modal.msgError("请输入密码")
        } else if (this.registerForm.confirmPassword === "") {
          this.$modal.msgError("请再次输入密码")
        } else if (this.registerForm.password !== this.registerForm.confirmPassword) {
          this.$modal.msgError("两次输入的密码不一致")
        } else if (this.registerForm.userType === "") {
          this.$modal.msgError("请选择用户类型")
        } else {
          this.$modal.loading("注册中，请耐心等待...")
          this.doRegister()
        }
      },
      // 执行注册
      async doRegister() {
        register(this.registerForm).then(res => {
          this.$modal.closeLoading()
          uni.showModal({
            title: "系统提示",
            content: "恭喜你，手机号 " + this.registerForm.phone + " 注册成功！",
            success: function (res) {
              if (res.confirm) {
                uni.redirectTo({ url: `/pages/login` });
              }
            }
          })
        }).catch(() => {
          this.$modal.closeLoading()
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  page {
    background-color: #ffffff;
  }

  .normal-login-container {
    width: 100%;

    .logo-content {
      width: 100%;
      font-size: 21px;
      text-align: center;
      padding-top: 15%;

      image {
        border-radius: 4px;
      }

      .title {
        margin-left: 10px;
      }
    }

    .login-form-content {
      text-align: center;
      margin: 20px auto;
      margin-top: 15%;
      width: 80%;

      .input-item {
        margin: 20px auto;
        background-color: #f5f6f7;
        height: 45px;
        border-radius: 20px;

        .icon {
          font-size: 38rpx;
          margin-left: 10px;
          color: #999;
        }

        .input {
          width: 100%;
          font-size: 14px;
          line-height: 20px;
          text-align: left;
          padding-left: 15px;
        }

      }

      .register-btn {
        margin-top: 40px;
        height: 45px;
      }

      .xieyi {
        color: #333;
        margin-top: 20px;
      }
    }
  }

</style>
