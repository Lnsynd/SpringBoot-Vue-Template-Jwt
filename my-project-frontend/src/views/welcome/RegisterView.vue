<script setup>
import {reactive} from "vue";
import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})

const rule = {}

</script>

<template>
  <div style="text-align: center;margin: 0 20px;">
    <div style="margin-top: 100px;">
      <div style="font-size: 25px;font-weight: bold">注册新用户</div>
      <div style="font-size: 14px;color: gray">欢迎注册，请在下方填写信息</div>
    </div>
    <div style="margin-top: 50px;">
      <el-form :model="form" :rules="rule">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名/邮箱">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" maxlength="10" type="password" placeholder="密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" maxlength="10" type="password" placeholder="重复密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.email" type="email" placeholder="电子邮箱">
            <template #prefix>
              <el-icon>
                <Message/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-row :gutter="10" style="width: 100%">
            <el-col :span="17">
              <el-input v-model="form.code" maxlength="6" type="text" placeholder="验证码">
                <template #prefix>
                  <el-icon>
                    <EditPen/>
                  </el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="5">
              <el-button type="success">
                获取验证码
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 80px;">
      <el-button type="warning" plain style="width: 270px">立即注册</el-button>
    </div>
    <div style="margin-top: 20px;">
      <span style="font-size: 14px;">已有账号？</span>
      <el-link style="translate: 0 -1px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>
</template>

<style scoped>

</style>