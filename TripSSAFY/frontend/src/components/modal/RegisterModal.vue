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

      <h2 class="text-xl font-semibold mb-4 text-center">회원가입</h2>

      <form @submit.prevent="handleRegister">
        <!-- 아이디 -->
        <div class="mb-3">
          <label for="username" class="block text-gray-700">아이디</label>
          <input
            type="text"
            id="username"
            v-model="username"
            class="w-full p-2 border border-gray-300 rounded"
            required
          />
        </div>

        <!-- 비밀번호 -->
        <div class="mb-3">
          <label for="password" class="block text-gray-700">비밀번호</label>
          <input
            type="password"
            id="password"
            v-model="password"
            class="w-full p-2 border border-gray-300 rounded"
            required
          />
          <p
            v-if="password && !passwordRegex.test(password)"
            class="text-red-500 text-sm"
          >
            비밀번호는 8~20자 사이의 영문자, 숫자, 특수문자를 포함해야 합니다.
          </p>
        </div>

        <!-- 🔒 비밀번호 확인 -->
        <div class="mb-3">
          <label for="confirmPassword" class="block text-gray-700"
            >비밀번호 확인</label
          >
          <input
            type="password"
            id="confirmPassword"
            v-model="confirmPassword"
            class="w-full p-2 border border-gray-300 rounded"
            required
          />
          <p
            v-if="password && confirmPassword && password !== confirmPassword"
            class="text-red-500 text-sm"
          >
            비밀번호가 다릅니다.
          </p>
        </div>

        <!-- 닉네임 -->
        <div class="mb-4">
          <label for="nickname" class="block text-gray-700">닉네임</label>
          <input
            type="text"
            id="nickname"
            v-model="nickname"
            class="w-full p-2 border border-gray-300 rounded"
            required
          />
        </div>

        <!-- 에러 메시지 -->
        <div v-if="errorMessage" class="text-red-500 text-sm mb-2 text-center">
          {{ errorMessage }}
        </div>

        <!-- 회원가입 버튼 -->
        <button
          type="submit"
          class="bg-ssafy_blue hover:bg-blue-700 transition text-white px-4 py-2 rounded w-full"
        >
          회원가입
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import api from "@/api/axios";
import qs from "qs";
import { showToastSuccess } from "@/api/toastMsg";

// 상태
const username = ref("");
const password = ref("");
const confirmPassword = ref("");
const nickname = ref("");
const errorMessage = ref("");

const props = defineProps({
  role: String,
});

// 정규식
const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$/;

// 이벤트
const emit = defineEmits(["close"]);
const closeModal = () => emit("close");

// 회원가입 처리
const handleRegister = async () => {
  if (!passwordRegex.test(password.value)) {
    errorMessage.value =
      "비밀번호는 8~20자 사이의 영문자, 숫자, 특수문자를 포함해야 합니다.";
    return;
  }

  if (password.value !== confirmPassword.value) {
    errorMessage.value = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
    return;
  }

  try {
    await api.post(
      "/auth/signup",
      qs.stringify({
        username: username.value,
        password: password.value,
        nickname: nickname.value,
        role: props.role,
      }),
      {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      }
    );

    showToastSuccess("회원가입 성공! 로그인해주세요.");
    emit("close");
  } catch (error) {
    console.error(error);
    if (error.response?.data?.errorMsg) {
      errorMessage.value = error.response.data.errorMsg;
    } else {
      errorMessage.value = "회원가입 중 오류가 발생했습니다.";
    }
  }
};
</script>
