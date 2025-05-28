<template></template>

<script setup>
import { onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import { useUserStore } from "@/stores/userStore";

const userStore = useUserStore();
const route = useRoute();
const router = useRouter();

onMounted(async () => {
  const code = route.query.code;
  try {
    // 백엔드가 code로 토큰 교환 및 유저 정보 반환
    // const response = await axios.get(`http://localhost:8080/oauth/kakao/callback?code=${code}`);
    const token = response.headers["authorization"];
    userStore.setToken(token);
    alert("로그인 성공");
    window.location.href = "/";
  } catch (error) {
    alert("로그인 실패");
    console.error(error);
    router.push("/");
  }
});
</script>
