<template>
  <div class="flex flex-col gap-4 relative">
    <h1 class="text-lg font-bold">Step 3.</h1>
    <h2 class="text-xl font-semibold">같이 여행할 친구를 초대해주세요</h2>

    <input
      v-model="searchText"
      @input="handleInput"
      @focus="isDropdownOpen = true"
      @blur="handleBlur"
      class="h-12 px-4 rounded-md bg-gray-100"
      placeholder="아이디 또는 닉네임 입력"
    />

    <!-- 드롭다운 결과 -->
    <div
      v-if="isDropdownOpen && searchText.length > 0"
      class="absolute top-32 z-10 bg-white border border-gray-300 rounded-md w-full shadow-md"
    >
      <div v-if="isLoading" class="p-4 text-gray-500 text-sm">...</div>
      <div
        v-else-if="users.length === 0 && searchText.length > 0"
        class="p-4 text-gray-500 text-sm"
      >
        검색된 유저가 없습니다.
      </div>
      <ul v-else>
        <li
          v-for="user in users"
          :key="user.username"
          class="px-4 py-2 hover:bg-blue-100 cursor-pointer"
          @mousedown.prevent="selectUser(user)"
        >
          <img
            v-if="user.profileImage !== null"
            :src="user.profileImage"
            alt="User Image"
            class="w-8 h-8 rounded-full inline-block mr-2"
          />
          {{ user.username }} ({{ user.nickname }})
        </li>
      </ul>
    </div>

    <!-- 초대된 친구 목록 -->
    <div class="mt-4">
      <h3 class="text-md font-medium mb-2">초대된 친구 목록</h3>
      <div class="bg-gray-100 rounded-md p-4 h-32 overflow-y-auto">
        <div v-for="(user, index) in invitedUsers" :key="index" class="py-1">
          <img
            v-if="user.profileImage !== null"
            :src="user.profileImage"
            alt="User Image"
            class="w-8 h-8 rounded-full inline-block mr-2"
          />
          {{ user.username }} ({{ user.nickname }})
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import api from "@/api/axios";
import debounce from "lodash.debounce";
import { useUserStore } from "@/stores/userStore";
import { showToastError } from "@/api/toastMsg";

const userStore = useUserStore();

// 검색어
const searchText = ref("");
const users = ref([]);
const invitedUsers = defineModel("invitedUsers");
const isDropdownOpen = ref(false);
const isLoading = ref(false);

// 디바운스된 검색 함수
const searchUsers = debounce(async (text) => {
  isLoading.value = true;
  try {
    const res = await api.get("/user", {
      params: {
        username: text,
      },
    });
    users.value = res.data;
  } catch (err) {
    users.value = [];
  } finally {
    isLoading.value = false;
  }
}, 300);

const handleInput = () => {
  if (searchText.value.length === 0) {
    users.value = [];
    isLoading.value = false;
    return;
  }
  isLoading.value = true;
  if (searchText.value.trim().length < 2) {
    users.value = [];
    return;
  }
  searchUsers(searchText.value);
};

const selectUser = (user) => {
  if (
    user.username === userStore.username ||
    user.nickname === userStore.nickname
  ) {
    showToastError("초대할 수 없는 유저입니다.");
    return;
  }
  if (!invitedUsers.value.some((u) => u.username === user.username)) {
    invitedUsers.value.push(user);
  }
  searchText.value = "";
  users.value = [];
};

const handleBlur = () => {
  // 드롭다운 클릭 시 blur 되는 걸 방지하기 위해 약간의 delay
  setTimeout(() => {
    isDropdownOpen.value = false;
  }, 200);
};
</script>
