<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
  >
    <div class="bg-white p-6 rounded-md shadow-lg w-80 relative" @click.stop>
      <!-- X 닫기 버튼 -->
      <button
        @click="closeModal"
        class="absolute top-2 right-3 text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>

      <h2 class="text-xl font-semibold mb-4 text-center">로그인</h2>

      <form @submit.prevent="handleLogin">
        <!-- 아이디 입력 -->
        <div class="mb-4">
          <label for="username" class="block text-gray-700">아이디</label>
          <input
            type="text"
            id="username"
            v-model="username"
            class="w-full p-2 border border-gray-300 rounded"
            required
          />
        </div>

        <!-- 비밀번호 입력 -->
        <div class="mb-4">
          <label for="password" class="block text-gray-700">비밀번호</label>
          <input
            type="password"
            id="password"
            v-model="password"
            class="w-full p-2 border border-gray-300 rounded"
            required
          />
        </div>

        <!-- 로그인 저장 체크박스 -->
        <div class="mb-4 flex items-center">
          <input
            type="checkbox"
            id="saveLogin"
            v-model="saveLogin"
            class="mr-2"
          />
          <label for="saveLogin" class="text-sm text-gray-700"
            >아이디 저장</label
          >
        </div>

        <!-- 로그인 버튼 -->
        <div class="mb-4flex justify-center">
          <button
            type="submit"
            class="bg-ssafy_blue hover:bg-blue-700 transition text-white px-4 py-2 rounded w-full"
          >
            로그인
          </button>
        </div>

        <!-- 회원가입 유도 문구 -->
        <div class="text-left text-sm mt-1 mb-4">
          <button
            type="button"
            @click="goToRegister"
            class="text-ssafy_blue hover:underline ml-1"
          >
            아직 회원이 아니신가요?
          </button>
        </div>
      </form>

      <!-- 외부 로그인 -->
      <span class="mb-8 flex justify-center text-gray-500 font-light"
        >다른 로그인 방식</span
      >
      <div class="flex justify-center">
        <button class="px-4" @click.prevent="handleOAuthLogin('kakao')">
          <img
            src="@/assets/logos/kakao_login_logo.png"
            width="60"
            alt="카카오 로그인 버튼"
          />
        </button>
        <button class="px-4" @click.prevent="handleOAuthLogin('naver')">
          <img
            src="@/assets/logos/naver_login_logo.png"
            width="60"
            alt="네이버 로그인 버튼"
          />
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useUserStore } from "@/stores/userStore";
import api from "@/api/axios";
import qs from "qs";
import { showToastError, showToastSuccess } from "@/api/toastMsg";

const userStore = useUserStore();
const saveLogin = ref(false);

const emit = defineEmits(["close", "register"]);
const props = defineProps({
  openRoleModal: Function,
});

const username = ref("");
const password = ref("");

const closeModal = () => {
  emit("close");
};

const goToRegister = () => {
  closeModal();
  props.openRoleModal();
};

const handleLogin = async () => {
  try {
    const response = await api.post(
      "/auth/login",
      qs.stringify({
        username: username.value,
        password: password.value,
      }),
      {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      }
    );

    const token = response.headers["authorization"].replace("Bearer ", "");
    userStore.setToken(token);
    if (saveLogin.value) {
      localStorage.setItem("savedUsername", username.value);
    } else {
      localStorage.removeItem("savedUsername");
    }
    showToastSuccess("로그인 성공");
    emit("close");
    // setTimeout(() => (window.location.href = "/"), 1000);
  } catch (error) {
    console.error(error);
    showToastError("아이디 또는 비밀번호가 틀렸습니다.");
  }
};

const handleOAuthLogin = (provider) => {
  window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
};
onMounted(() => {
  // 이전에 저장된 아이디가 있다면 자동 입력
  const savedUsername = localStorage.getItem("savedUsername");
  if (savedUsername) {
    username.value = savedUsername;
    saveLogin.value = true;
  }

  const kakaoScript = document.createElement("script");
  kakaoScript.src = "https://t1.kakaocdn.net/kakao_js_sdk/2.7.5/kakao.min.js";
  kakaoScript.integrity =
    "sha384-dok87au0gKqJdxs7msEdBPNnKSRT+/mhTVzq+qOhcL464zXwvcrpjeWvyj1kCdq6";
  kakaoScript.crossOrigin = "anonymous";
  document.head.appendChild(kakaoScript);
});
</script>
