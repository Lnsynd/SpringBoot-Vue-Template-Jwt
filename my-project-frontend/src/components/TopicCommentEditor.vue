<script setup>
import {Delta, Quill, QuillEditor} from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import {ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";


const props = defineProps({
  show: Boolean,
  tid: String,
  quote: Number
})

const content = ref()

const emit = defineEmits(['close', 'comment'])

const init = () => content.value = new Delta()

function submitComment() {
  post('/api/forum/add-comment', {
    tid: props.tid,
    quote: props.quote,
    content: JSON.stringify(content.value)
  }, () => {
    ElMessage.success('发表评论成功')
    emit('comment')
  })
}
</script>

<template>
  <div>
    <el-drawer :model-value="show"
               title="发表评论"
               @open="init"
               @close="emit('close')"
               direction="btt" :size="270"
               :close-on-click-modal="false">
      <div>
        <div>
          <quill-editor style="height: 100px" v-model:content="content"
                        placeholder="请发表友善的评论"/>
        </div>
        <div style="margin-top: 10px;text-align: right">
          <el-button type="success" plain @click="submitComment">发表评论</el-button>
        </div>
      </div>

    </el-drawer>
  </div>
</template>

<style lang="less" scoped>
:deep(.el-drawer) {
  width: 1000px;
  margin: 20px auto;
  border-radius: 10px;
}

:deep(.el-drawer__header) {
  margin: 0;
}

</style>