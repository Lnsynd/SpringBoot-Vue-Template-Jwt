<template>
  <div class="main-content" v-loading="loading" element-loading-text="正在载入...">
    <el-container style="height: 100%" v-if="!loading">
      <el-header class="main-content-header">
        <el-image class="logo" src="https://www.nuc.edu.cn/images/xxgk_02.jpg"></el-image>
        <div style="flex: 1;text-align: center;padding:10px 10px 10px 300px;">
          <el-input v-model="searchInput.text" style="width: 100%;max-width: 500px" placeholder="搜索论坛相关内容...">
            <template #prefix>
              <el-icon>
                <Search/>
              </el-icon>
            </template>
            <template #append>
              <el-select style="width: 120px;" v-model="searchInput.type">
                <el-option value="1" label="帖子广场"/>
                <el-option value="2" label="校园活动"/>
                <el-option value="3" label="表白墙"/>
                <el-option value="4" label="教务通知"/>
              </el-select>
            </template>
          </el-input>
        </div>
        <div style="flex: 1" class="user-info">
          <div class="profile">
            <div>{{ store.user.username }}</div>
            <div>{{ store.user.email }}</div>
          </div>
          <el-dropdown>
            <el-avatar :src="store.avatarUrl"/>
            <template #dropdown>
              <el-dropdown-item>
                <el-icon>
                  <Operation/>
                </el-icon>
                个人设置
              </el-dropdown-item>
              <el-dropdown-item>
                <el-icon>
                  <Message/>
                </el-icon>
                消息列表
              </el-dropdown-item>
              <el-dropdown-item @click="userLogout" divided>
                <el-icon>
                  <Back/>
                </el-icon>
                退出登陆
              </el-dropdown-item>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-scrollbar style="height: calc(100vh - 55px)">
            <el-menu
                router
                :default-active="$route.path"
                :default-openeds="['1','2','3']"
                style="min-height: calc(100vh - 55px)"
            >
              <el-sub-menu index="1">
                <template #title>
                  <el-icon>
                    <Location/>
                  </el-icon>
                  <span><b>校园论坛</b></span>
                </template>
                <el-menu-item index="/index">
                  <template #title>
                    <el-icon>
                      <ChatDotSquare/>
                    </el-icon>
                    帖子广场
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Bell/>
                    </el-icon>
                    失物招领
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Notification/>
                    </el-icon>
                    校园活动
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Umbrella/>
                    </el-icon>
                    表白墙
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <School/>
                    </el-icon>
                    海文考研
                    <el-tag style="margin-left: 5px;" size="small">合作机构</el-tag>
                  </template>
                </el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="2">
                <template #title>
                  <el-icon>
                    <Position/>
                  </el-icon>
                  <span><b>发现</b></span>
                </template>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Search/>
                    </el-icon>
                    成绩查询
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Files/>
                    </el-icon>
                    班级课程表
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Monitor/>
                    </el-icon>
                    教务通知
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Collection/>
                    </el-icon>
                    在线图书馆
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <DataLine/>
                    </el-icon>
                    预约教室
                  </template>
                </el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="3">
                <template #title>
                  <el-icon>
                    <Operation/>
                  </el-icon>
                  <span><b>个人设置</b></span>
                </template>
                <el-menu-item index="/index/user-setting">
                  <template #title>
                    <el-icon>
                      <User/>
                    </el-icon>
                    个人信息设置
                  </template>
                </el-menu-item>
                <el-menu-item index="/index/privacy-setting">
                  <template #title>
                    <el-icon>
                      <Lock/>
                    </el-icon>
                    账号安全设置
                  </template>
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-scrollbar>
        </el-aside>
        <el-main class="main-content-page">
          <el-scrollbar style="height: calc(100vh - 55px)">
            <router-view v-slot="{ Component }">
              <transition name="el-fade-in-linear" mode="out-in">
                <component :is="Component" style="height: 100%"/>
              </transition>
            </router-view>
          </el-scrollbar>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import {get, logout} from "@/net";
import router from "@/router";
import {useStore} from "@/store";
import {ref, reactive} from "vue";
import {
  Back,
  Bell,
  ChatDotSquare, Collection, DataLine, Files,
  Location, Lock, Message, Monitor,
  Notification, Operation,
  Position,
  School,
  Search,
  Umbrella, User,
  View
} from "@element-plus/icons-vue";

const store = useStore()
const loading = ref(true)

const searchInput = reactive({
  type: '1',
  text: ''
})


get('api/user/info', (data) => {
  store.user = data
  loading.value = false
})

function userLogout() {
  logout(() => {
    router.push('/')
  })
}
</script>


<style lang="less" scoped>
.main-content-page{
  padding: 0;
  background-color: #f7f8fa;
}

// 暗黑模式下 主页的 背景颜色
.dark .main-content-page{
  background-color: #242628;
}
.main-content {
  height: 100vh;
  width: 100vw;
}

.main-content-header {
  border-bottom: solid 1px var(--el-border-color);
  height: 55px;
  display: flex;
  align-content: center;
  box-sizing: border-box;

  .logo {
    height: 54px;
  }

  .user-info {
    display: flex;
    justify-content: flex-end;
    align-items: center;

    .el-avatar:hover {
      cursor: pointer;
    }

    .profile {
      text-align: center;
      margin-right: 20px;

      :first-child {
        font-size: 18px;
        font-weight: bold;
        line-height: 20px;
      }

      :last-child {
        font-size: 11px;
        color: gray;
      }
    }
  }
}


</style>