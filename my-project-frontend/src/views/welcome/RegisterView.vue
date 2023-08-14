<script setup>
import {computed, reactive, ref} from "vue";
import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {ElMessage} from "element-plus";
import {get, post} from "@/net";

const coldTime = ref(0)
const formRef = ref()

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9]+$/.test(value)) {
    callback(new Error('用户名只能是中英文字符'))
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const validateEmail = computed(
    () => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email)
)

const rule = {
  username: [
    {validator: validateUsername, trigger: ['blur', 'change']}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: ['blur']},
    {min: 3, max: 10, message: '密码的长度在3-10个字符之间', trigger: ['blur', 'change']}
  ],
  password_repeat: [
    {validator: validatePassword}
  ],
  email: [
    {required: true, message: '请输入邮件地址', trigger: ['blur']},
    {type: 'email', message: '请输入合法的邮件地址', trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: ['blur']}
  ]
}

function askCode() {
  if (validateEmail) {
    coldTime.value = 60
    get(`/api/auth/ask-code?email=${form.email}&type=register`, () => {
      ElMessage.success(`验证码已经发到邮箱:${form.email}`)
      setInterval(() => coldTime.value--, 1000)
    })
  } else {
    ElMessage.warning('请输入正确的电子邮件!')
  }
}

function register() {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/register', {...form}, () => {
        ElMessage.success('注册成功')
        router.push('/')
      })
    } else {
      ElMessage.warning('完整填写表单内容')
    }
  })
}


</script>

<template>
  <div style="text-align: center;margin: 0 20px;">
    <div style="margin-top: 100px;">
      <div style="font-size: 25px;font-weight: bold">注册新用户</div>
      <div style="font-size: 14px;color: gray">欢迎注册，请在下方填写信息</div>
    </div>
    <div style="margin-top: 50px;">
      <el-form :model="form" :rules="rule" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名">
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
        <el-form-item prop="email">
          <el-input v-model="form.email" type="email" placeholder="电子邮箱">
            <template #prefix>
              <el-icon>
                <Message/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
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
              <el-button type="success" @click="askCode" :disabled="coldTime||!validateEmail">
                {{ coldTime > 0 ? `等待${coldTime}秒` : '获取验证码' }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 80px;">
      <el-button type="warning" plain style="width: 270px" @click="register">立即注册</el-button>
    </div>
    <div style="margin-top: 20px;">
      <span style="font-size: 14px;">已有账号？</span>
      <el-link style="translate: 0 -1px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>
</template>

<style scoped>

</style>