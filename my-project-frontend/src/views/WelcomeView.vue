<script setup>

</script>

<template>
<div style="width: 100vw;height: 100vh;overflow: hidden;display: flex;">
  <div style="flex: 1;background-color:#46dc5f;">
    <el-image style="width: 100%;height: 100%" fit="cover"
              src="src/img/welcome.jpeg"/>
  </div>
  <div class="welcome-title">
    <div style="font-size: 30px;font-weight: bold;">欢迎来到我的网站！</div>
    <div style="float: right;font-size: 18px;">-----一个拉米-阿弥诺斯</div>
  </div>
  <div class="right-card">
    <router-view v-slot="{Component}">
      <transition name="el-fade-in-linear" mode="out-in">
        <component :is="Component"/>
      </transition>
    </router-view>
  </div>
</div>
</template>

<style scoped>
.welcome-title{
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
.right-card{
  width: 400px;
  z-index: 1;
  background-color: #ffffff;
}
</style>