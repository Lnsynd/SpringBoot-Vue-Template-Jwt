<script setup>
import Card from "@/components/Card.vue";
import {Setting, Switch} from "@element-plus/icons-vue/global";
import {Lock} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";

const updatePasswordForm = reactive({
  password: '',
  new_password: '',
  repeat_new_password: ''
})

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== updatePasswordForm.new_password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  password: [
    {required: true, message: "请输入原来的密码", trigger: 'blur'}
  ],
  new_password: [
    {required: true, message: "请输入新的密码", trigger: 'blur'},
    {min: 6, max: 20, message: "密码的长度必须在6-16个字符之间", trigger: 'blur'}
  ],
  repeat_new_password: [
    {required: true, message: "请重复输入新的密码", trigger: 'blur'},
    {validator: validatePassword, trigger: ['blur', 'change']}
  ]
}

const formRef = ref()
const valid = ref(false)
const onValidate = (prop, isValid) => {
  valid.value = isValid
}

function resetPassword() {
  formRef.value.validate(valid => {
    if(valid){
      post('/api/user/change-password',updatePasswordForm,()=>{
        ElMessage.success("修改密码成功")
        updatePasswordForm.value.resetFields();
      })
    }
  })
}

</script>

<template>
  <div style="margin: auto;max-width: 600px;">
    <div style="margin-top: 20px;">
      <Card :icon="Setting" title="隐私设置" desc="在这里可以设置那些内容可以被其他人看到">
        <div class="checkbox-list">
          <el-checkbox>公开展示我的手机号</el-checkbox>
          <el-checkbox>公开展示我的邮件地址</el-checkbox>
          <el-checkbox>公开展示我的微信号</el-checkbox>
          <el-checkbox>公开展示我的QQ号码</el-checkbox>
          <el-checkbox>公开展示我的性别</el-checkbox>
        </div>
      </Card>
      <Card style="margin: 20px 0"
            :icon="Setting" title="修改密码" desc="在这里可以对你的密码进行修改...">
        <el-form label-width="100"
                 :model="updatePasswordForm"
                 :rules="rules"
                 ref="formRef"
                 @validate="onValidate"
        >
          <el-form-item label="当前密码" prop="password">
            <el-input :prefix-icon="Lock"
                      type="password"
                      v-model="updatePasswordForm.password"
                      placeholder="当前密码"
                      maxlength="16"/>
          </el-form-item>
          <el-form-item label="新密码" prop="new_password">
            <el-input :prefix-icon="Lock"
                      type="password"
                      v-model="updatePasswordForm.new_password"
                      placeholder="新密码"
                      maxlength="16"/>
          </el-form-item>
          <el-form-item label="重复密码" prop="repeat_new_password">
            <el-input :prefix-icon="Lock"
                      type="password"
                      v-model="updatePasswordForm.repeat_new_password"
                      placeholder="重新输入新密码" maxlength="16"/>
          </el-form-item>
          <div style="text-align: center">
            <el-button :icon="Switch" type="success" @click="resetPassword">
              立即重置密码
            </el-button>
          </div>
        </el-form>
      </Card>
    </div>
  </div>
</template>

<style scoped>
.checkbox-list {
  margin: 10px 0 0 10px;
  display: flex;
  flex-direction: column;
}
</style>