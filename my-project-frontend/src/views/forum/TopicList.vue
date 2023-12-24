<script setup>


import LightCard from "@/components/LightCard.vue";
import {
  Calendar,
  Clock,
  CollectionTag, Compass,
  Document,
  Edit,
  EditPen,
  Link,
  Microphone,
  Picture
} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed, reactive, ref, watch} from "vue";
import {get} from "@/net";
import {useStore} from "@/store";
import {ElMessage} from "element-plus";
import TopicEditor from "@/components/TopicEditor.vue";
import axios from "axios";
import ColorDot from "@/components/ColorDot.vue";


const store = useStore()
get('api/user/ip', (data) => {
  store.ip = data
})

const weather = reactive({
  location: {},
  now: {},
  hourly: [],
  success: false
})

const editor = ref(false)
const topics = reactive({
  list: [],
  top: [],
  type: 0,
  page: 0,
  end: false
})

watch(() => topics.type, () => {
      resetList()
    }, {immediate: true}
)

const today = computed(() => {
  const date = new Date()
  // 日期有问题
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
})

get('/api/forum/types', data => {
  const array = []
  array.push({name: '全部', id: 0, color: 'linear-gradient(45deg,white,red,orange,gold,green,blue)'})
  data.forEach(d => array.push(d))
  store.forum.types = array
})

get('/api/forum/top-topic', data => topics.top = data)

function updateList() {
  if (topics.end) return
  get(`/api/forum/list-topic?page=${topics.page}&type=${topics.type}`, data => {
    if (data) {
      data.forEach(d => topics.list.push(d))
      topics.page++
    }
    if (!data || data.length < 10) {
      topics.end = true
    }
  })
}

function onTopicCreate() {
  editor.value = false
  resetList()
}

function resetList() {
  topics.page = 0
  topics.end = false
  topics.list = []
  updateList()
}

function showCommit() {
  console.log('已触发')
}

updateList()

navigator.geolocation.getCurrentPosition(position => {
  const longitude = position.coords.longitude
  const latitude = position.coords.latitude
  get(`/api/forum/weather?longitude=${longitude}&latitude=${latitude}`, (data) => {
    Object.assign(weather, data)
    weather.success = true
  })
}, error => {
  ElMessage.warning("位置信息获取超时，请检查网络设置")
  get('/api/forum/weather?longitude=116.40529&latitude=39.90499', data => {
    Object.assign(weather, data)
    weather.success = true
  })
}, {
  timeout: 300,
  enableHighAccuracy: true
})

</script>

<template>
  <div style="display: flex;margin: 20px;gap: 20px;max-width: 1100px;">
    <div style="flex: 1;">
      <light-card>
        <div class="create-topic" @click="editor=true">
          <el-icon>
            <EditPen/>
          </el-icon>
          点击发表主题...
        </div>
        <div style="margin-top: 10px;display: flex;gap: 10px;font-size: 18px;color: gray">
          <el-icon>
            <Edit/>
          </el-icon>
          <el-icon>
            <Document/>
          </el-icon>
          <el-icon>
            <Compass/>
          </el-icon>
          <el-icon>
            <Picture/>
          </el-icon>
          <el-icon>
            <Microphone/>
          </el-icon>
        </div>
      </light-card>
      <light-card style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px">
        <div v-for="item in topics.top" class="top-topic">
          <el-tag type="info" size="small">置顶</el-tag>
          <div>{{ item.title }}</div>
          <div>{{ new Date(item.time).toLocaleString() }}</div>
        </div>
      </light-card>
      <light-card style="margin-top: 10px;display: flex;gap: 10px">
        <div :class="`type-select-card ${topics.type===item.id ? 'active' : ''}`"
             v-for="item in store.forum.types"
             @click="topics.type = item.id">
          <color-dot :color="item.color"/>
          <span style="margin-left: 10px"> {{ item.name }} </span>
        </div>
      </light-card>
      <transition name="el-fade-in" mode="out-in">
        <div v-if="topics.list.length">
          <div style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px;height: 100%"
               v-infinite-scroll="updateList">
            <light-card  v-for="item in topics.list" class="topic-card">
              <div style="display: flex">
                <div>
                  <el-avatar :size="30" :src="`${axios.defaults.baseURL}/images${item.avatar}`"/>
                </div>
                <div style="margin-left: 7px;transform: translateY(-2px)">
                  <div style="font-size: 13px;font-weight: bold;margin-bottom: 2px;"> {{ item.username }}</div>
                  <div style="font-size: 11px;color: gray">
                    <el-icon>
                      <Clock/>
                    </el-icon>
                    <span
                        style="margin-left: 2px;display: inline-block;transform: translateY(-1px)"> {{ new Date(item.time).toLocaleString() }}</span>
                  </div>
                </div>
              </div>
              <div style="margin-top: 10px;">
                <div class="topic-type"
                     :style="{color:store.findTypeById(item.type)?.color + 'EE',
                                'border-color': store.findTypeById(item.type)?.color + '77',
                                'background':store.findTypeById(item.type)?.color + '22'}">
                  {{ store.findTypeById(item.type)?.name }}
                </div>
                <span style="font-weight: bold">{{ item.title }}</span>
              </div>
              <div class="topic-content">{{ item.text }}</div>
              <div style="display: grid;grid-template-columns: repeat(3,1fr);grid-gap: 10px">
                <el-image class="topic-image" v-for="img in item.images" :src="img" fit="cover"></el-image>
              </div>
            </light-card>
          </div>
        </div>
      </transition>

    </div>
    <div style="width: 300px;">
      <div style="position: sticky;top: 20px">
        <light-card>
          <div style="font-weight: bold;">
            <el-icon>
              <CollectionTag/>
            </el-icon>
            论坛公告
          </div>
          <el-divider style="margin: 10px 0"/>
          <div style="font-size: 14px;margin: 10px;color:gray">
            尊敬的论坛会员们，大家好！
            为提高论坛质量，我们决定加强管理力度，禁止任何形式的不当言论和违规行为。
            请遵守社区规定，保持友好交流环境。对于违规行为将采取相应措施。
            感谢大家的支持与理解，共同努力维护论坛和谐氛围。祝大家在这里愉快交流，谢谢！
          </div>
        </light-card>
        <light-card style="margin-top: 10px">
          <div style="font-weight: bold">
            <el-icon>
              <Calendar/>
            </el-icon>
            天气信息
          </div>
          <el-divider style="margin: 10px 0"/>
          <weather :data="weather"/>
        </light-card>
        <light-card style="margin-top: 10px">
          <div class="info-text">
            <div>当前日期</div>
            <div>{{ today }}</div>
          </div>
          <div class="info-text">
            <div>当前IP地址</div>
            <div>
              {{ store.ip }}
            </div>
          </div>
        </light-card>
        <div style="font-size: 14px;margin-top: 10px;color: gray">
          <el-icon>
            <Link/>
          </el-icon>
          友情连接
          <el-divider style="margin: 10px 0"/>
        </div>
        <div style="display: grid;grid-template-columns: repeat(2,1fr);grid-gap: 10px;">
          <div class="friend-link">
            <el-image src="xxx"/>
          </div>
        </div>
      </div>
    </div>
    <topic-editor :show="editor" @close="editor = false" @success="onTopicCreate"/>
  </div>
</template>

<style lang="less" scoped>
.top-topic {
  display: flex;

  div:first-of-type {
    font-size: 14px;
    margin-left: 10px;
    font-weight: bold;
    opacity: 0.7;
    transition: color .3s;

    &:hover {
      color: gray;
    }
  }

  div:nth-of-type(2) {
    flex: 1;
    color: gray;
    font-size: 13px;
    text-align: right;

  }

  &:hover {
    cursor: pointer;
  }
}

.type-select-card {
  background-color: #f5f5f5;
  padding: 2px 7px;
  box-sizing: border-box;
  font-size: 14px;
  border-radius: 3px;

  &.active {
    border: solid 1px #1a1919;
  }

  &:hover {
    cursor: pointer;
    background-color: #efefef;
  }
}

.topic-image {
  width: 50%;
  height: 50%;
}

.topic-card {
  padding: 15px;
  transition: scale .3s;

  &:hover {
    scale: 1.01;
    cursor: pointer;
  }

  .topic-content {
    font-size: 13px;
    color: gray;
    margin: 5px 0;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .topic-type {
    display: inline-block;
    border: solid 1px gray;
    border-radius: 3px;
    font-size: 12px;
    padding: 0 5px;
    margin-right: 5px;
    height: 18px;
  }

  .topic-image {
    width: 100%;
    height: 100%;
    border-radius: 5px;
    max-height: 120px;
  }
}

.info-text {
  display: flex;
  justify-content: space-between;
  color: gray;
  font-size: 14px
}

.friend-link {
  border-radius: 5px;
  overflow: hidden;
}

.create-topic {
  background-color: #efefef;
  border-radius: 5px;
  height: 40px;
  font-size: 14px;
  line-height: 40px;
  padding: 0 10px;
  color: gray;

  &:hover {
    cursor: pointer;
  }
}

.dark {
  .create-topic {
    background-color: #464141;
  }

  .type-select-card {
    background-color: #464141;

    &.active {
      border: solid 1px #64594b;
    }

    &.hover {
      background-color: #5e5e5e;
    }
  }
}
</style>