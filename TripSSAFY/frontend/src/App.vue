<!-- src/App.vue -->
<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useUserStore } from "./stores/userStore";

const userStore = useUserStore();
onMounted(() => {
  if (userStore.isLoggedIn) {
    userStore.fetchUserInfo();
  }
});
</script>

<style>
/* 앱 스타일 */
</style>
