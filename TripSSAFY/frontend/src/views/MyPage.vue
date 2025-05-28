<template>
  <div class="min-h-screen flex flex-col">
    <!-- 헤더 -->
    <Header />

    <!-- 본문 -->
    <main class="flex-1 bg-white">
      <div class="max-w-2xl mx-auto bg-white p-6 rounded-lg shadow-md mt-10">
        <h2 class="text-2xl font-semibold mb-6 text-center">마이페이지</h2>

        <!-- 사용자 정보 -->
        <div class="flex flex-col items-center mb-6">
          <img
            :src="previewImage || user.profileImage"
            alt="프로필 이미지"
            class="w-24 h-24 rounded-full object-cover border border-gray-300 mb-4"
          />
          <label class="cursor-pointer text-ssafy_blue text-sm hover:underline">
            프로필 사진 변경
            <input
              type="file"
              accept="image/*"
              class="hidden"
              @change="onImageChange"
            />
          </label>
          <p class="text-lg font-medium text-gray-800 mt-4">
            {{ user.nickname }}
          </p>
          <p class="text-sm text-gray-500">{{ user.username }}</p>
        </div>

        <!-- 기본 정보 -->
        <div class="border-t pt-4 space-y-2 text-sm text-gray-700 mb-6 ml">
          <div class="flex justify-between">
            <span class="font-medium text-gray-600">아이디</span>
            <span>{{ user.username }}</span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium text-gray-600">닉네임</span>
            <span>{{ user.nickname }}</span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium text-gray-600">가입일</span>
            <span>{{ formattedDate }}</span>
          </div>
        </div>

        <!-- 비밀번호 수정 -->
        <form @submit.prevent="changePassword" class="space-y-4 border-t pt-4">
          <h3 class="text-lg font-semibold text-gray-800">비밀번호 수정</h3>
          <div>
            <label class="block text-sm font-medium text-gray-600"
              >현재 비밀번호</label
            >
            <input
              v-model="currentPassword"
              type="password"
              required
              class="w-full border rounded px-3 py-2 mt-1"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-600"
              >새 비밀번호</label
            >
            <input
              v-model="newPassword"
              type="password"
              required
              class="w-full border rounded px-3 py-2 mt-1"
            />
          </div>
          <button
            type="submit"
            class="w-full bg-ssafy_blue text-white py-2 px-4 rounded hover:bg-blue-700 transition"
          >
            비밀번호 변경
          </button>
        </form>

        <!-- 프로필 사진 저장 -->
        <div v-if="previewImage" class="mt-6">
          <button
            @click="uploadProfileImage"
            class="w-full bg-ssafy_orange text-white py-2 px-4 rounded hover:bg-orange-400 transition"
          >
            프로필 사진 저장
          </button>
        </div>
      </div>
    </main>

    <!-- 푸터 -->
    <Footer />
  </div>
</template>

<script setup>
import Header from "@/components/common/Header.vue";
import Footer from "@/components/common/Footer.vue";
import { useUserStore } from "@/stores/userStore";
import { computed, ref } from "vue";
import api from "@/api/axios";
import qs from "qs";
import { showToastError, showToastSuccess } from "@/api/toastMsg";

const userStore = useUserStore();

const user = computed(() => ({
  username: userStore.username,
  nickname: userStore.nickname,
  profileImage: userStore.profileImage,
  createdAt: userStore.createdAt || new Date().toISOString(),
}));

const formattedDate = computed(() => {
  if (!user.value.createdAt) return "";
  return new Date(user.value.createdAt).toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
});

// 비밀번호 수정
const currentPassword = ref("");
const newPassword = ref("");

const changePassword = async () => {
  try {
    await api.post(
      "/user/password",
      qs.stringify({
        currentPassword: currentPassword.value,
        newPassword: newPassword.value,
      })
    );

    showToastSuccess("비밀번호가 변경되었습니다.");
    currentPassword.value = "";
    newPassword.value = "";
  } catch (error) {
    showToastError("비밀번호 변경 실패");
    console.error(error);
  }
};

// 프로필 사진 업로드
const previewImage = ref(null);
const selectedImageFile = ref(null);
const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB 제한

const onImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    if (file.size > MAX_FILE_SIZE) {
      showToastError("파일 크기가 5MB를 초과할 수 없습니다.");
      event.target.value = ""; // 입력값 초기화 (파일 선택 취소)
      return;
    }

    selectedImageFile.value = file;
    previewImage.value = URL.createObjectURL(file);
  }
};

const uploadProfileImage = async () => {
  try {
    const file = new FormData();
    file.append("file", selectedImageFile.value);

    const response = await api.post("/user/profile-image", file, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    showToastSuccess("프로필 이미지가 저장되었습니다.");
    previewImage.value = null;

    userStore.setProfileImage(response.data);
    console.log(userStore.profileImage);
  } catch (error) {
    showToastError("프로필 이미지 저장 실패");
    console.error(error);
  }
};
</script>

<style scoped>
main {
  padding: 2rem;
}
</style>
