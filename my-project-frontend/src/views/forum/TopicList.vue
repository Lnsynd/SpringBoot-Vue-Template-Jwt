<script setup>


import LightCard from "@/components/LightCard.vue";
import {Calendar, CollectionTag, EditPen, Link} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed, reactive} from "vue";
import {get} from "@/net";
import {useStore} from "@/store";
import {ElMessage} from "element-plus";

const today = computed(() => {
  const date = new Date()
  // 日期有问题
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
})

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

navigator.geolocation.getCurrentPosition(position => {
  const longitude = position.coords.longitude
  const latitude = position.coords.latitude
  get(`/api/forum/weather?longitude=${longitude}&latitude=${latitude}`, (data) => {
    Object.assign(weather, data)
    weather.success = true
  })
}, error => {
  console.log(error)
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
  <div style="display: flex;margin: 20px auto;gap: 20px;max-width: 1100px">
    <div style="flex: 1">
      <light-card>
        <div class="create-topic">
          <el-icon>
            <EditPen/>
          </el-icon>
          点击发表主题...
        </div>
      </light-card>
      <light-card style="margin-top: 10px;height: 50px">

      </light-card>
      <div style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px">
        <light-card style="height: 100px" v-for="item in 10">

        </light-card>
      </div>
    </div>
    <div style="width: 300px">
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
  </div>
</template>

<style scoped>
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
</style>