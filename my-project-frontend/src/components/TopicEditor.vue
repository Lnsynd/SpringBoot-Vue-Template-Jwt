<script setup>
import {computed, reactive, ref} from "vue";
import {Check, Document} from "@element-plus/icons-vue/global";
import {Delta, Quill, QuillEditor} from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import ImageResize from "quill-image-resize-vue";
import {ImageExtend, QuillWatch} from "quill-image-super-solution-module";
import axios from "axios";
import {accessHeader, get, post} from "@/net";
import {ElMessage} from "element-plus";
import ColorDot from "@/components/ColorDot.vue";
import {useStore} from "@/store";


Quill.register('modules/imageResize', ImageResize)
Quill.register('modules/imageExtend', ImageExtend)

const props = defineProps({
  show: Boolean,
  defaultTitle: {
    default: '',
    type: String
  },
  defaultText: {
    default: '',
    type: String
  },
  defaultType: {
    default: 1,
    type: Number
  },
  submitButton: {
    default: '立即发布主题  ',
    type: String
  },
  submit: {
    default: (editor, success) => {
      post('/api/forum/create-topic', {
        type: editor.type,
        title: editor.title,
        content: editor.text
      }, () => {
        ElMessage.success('帖子发表成功')
        success()
      })
    },
    type: Function
  }
})

const store = useStore();
const emit = defineEmits(['close', 'success'])

const editor = reactive({
  type: null,
  title: '',
  text: '',
  loading: false
})

const editorRef = ref()

function initEditor() {
  if (props.defaultText) {
    editor.text = new Delta(JSON.parse(props.defaultText))
  } else {
    editorRef.value.setContents('', 'user')
  }
  editor.title = props.defaultTitle
  editor.type = findTypeById(props.defaultType)
}

const editorOption = {
  modules: {
    toolbar: {
      container: [
        "bold", "italic", "underline", "strike", "clean",
        {color: []}, {'background': []},
        {size: ['small', false, "large", "huge"]},
        {header: [1, 2, 3, 4, 5, 6, false]},
        {list: "ordered"}, {list: "bullet"}, {align: []},
        "blockquote", "code-block", "link", "image",
        {indent: '-1'}, {indent: '+1'}
      ],
      handlers: {
        'image': function () {
          QuillWatch.emit(this.quill.id)
        }
      }
    },
    imageResize: {
      modules: ['Resize', 'DisplaySize']
    },
    imageExtend: {
      action: axios.defaults.baseURL + '/api/image/cache',
      name: 'file',
      size: 5,
      loading: true,
      accept: 'image/png,image/jpg',
      response: (resp) => {
        if (resp.data) {
          return axios.defaults.baseURL + '/images' + resp.data
        } else {
          return null
        }
      },
      methods: 'POST',
      headers: xhr => {
        xhr.setRequestHeader('Authorization', accessHeader().Authorization)
      },
      start: () => editor.uploading = true,
      success: () => {
        ElMessage.success('图片上传成功')
        editor.uploading = false
      },
      error: () => {
        ElMessage.warning('图片上传失败，请联系管理员!')
        editor.uploading = false
      }
    }
  }
}

function deltaToText(delta) {
  if (!delta.ops) return ""
  let str = ""
  for (let op of delta.ops) {
    str += op.insert
  }
  return str.replace(/\s/g, "")
}

function submitTopic() {
  const text = deltaToText(editor.text)
  if (text.length > 20000) {
    ElMessage.warning('字数超出限制！无法发表主题')
    return
  }
  if (!editor.title) {
    ElMessage.warning('请填写标题')
    return
  }
  if (!editor.type) {
    ElMessage.warning('请选择一个合适的帖子类型')
    return
  }
  props.submit(editor, () => emit('success'))
}

const contentLength = computed(() => deltaToText(editor.text).length)

function findTypeById(id) {
  for (let type of store.forum.types) {
    if (type.id === id)
      return type
  }
}
</script>

<template>
  <div>
    <el-drawer :model-value="show"
               direction="btt"
               :size="600"
               @open="initEditor"
               :close-on-click-modal="false"
               @close="emit('close')">
      <template #header>
        <div>
          <div style="font-weight: bold">发表新的帖子</div>
          <div style="font-size: 13px;margin-top: 5px;">发表帖子前，注意不要乱写</div>
        </div>
      </template>
      <div style="display: flex;gap: 10px">
        <div style="width: 150px">
          <el-select placeholder="选择主题类型" value-key="id" v-model="editor.type"
                     :disabled="!store.forum.types.length">
            <el-option v-for="item in store.forum.types.filter(type => type.id > 0)" :value="item" :label="item.name">
              <div>
                <color-dot :color="item.color"/>
                <span style="margin-left: 10px">{{ item.name }}</span>
              </div>
            </el-option>
          </el-select>
        </div>
        <div style="flex: 1">
          <el-input placeholder="请输入帖子标题..."
                    v-model="editor.title"
                    :prefix-icon="Document"
                    style="height: 100%"
                    maxlength="30"
          />
        </div>
      </div>
      <div style="margin-top: 10px;font-size: 13px;color:gray">
        <color-dot :color="editor.type? editor.type.color :'#FFFFFF'"/>
        {{ editor.type ? editor.type.desc : '请选择一个帖子类型' }}
      </div>
      <div style="margin: 15px 0 0 10px;height: 380px;border-radius: 5px"
           v-loading="editor.uploading" element-loading-text="正在上传图片，请稍后...">
        <quill-editor v-model:content="editor.text"
                      style="height: calc(100% - 45px)"
                      placeholder="今天想分享点什么呢？"
                      :options="editorOption"
                      content-type="delta"
                      ref="editorRef"
        />
      </div>
      <div style="display: flex;justify-content: space-between;margin-top: 10px;">
        <div style="color: gray;font-size: 12px">当前字数{{ contentLength }}</div>
        <div>
          <el-button type="success" :icon="Check" @click="submitTopic" plain>
            {{ submitButton }}
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>
:deep(.el-drawer) {
  width: 1000px;
  margin: auto;
  border-radius: 10px 10px 0 0;
}

:deep(.el-drawer__header) {
  margin: 0;
}

:deep(.ql-container) {
  border-radius: 0 0 5px 5px;
  border-color: var(--el-border-color);
}

:deep(.ql-editor) {
  font-size: 14px;
}

:deep(.ql-editor.ql-blank::before) {
  color: var(--el-text-color-placeholder);
  font-style: normal;
}
</style>