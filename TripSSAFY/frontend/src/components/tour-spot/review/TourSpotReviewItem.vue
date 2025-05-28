<template>
  <div class="p-4 shadow-sm relative">
    <!-- 사용자 정보 -->
    <div class="flex items-center space-x-3">
      <img
        v-if="review.profileImg"
        :src="review.profileImg"
        alt="Profile"
        class="w-10 h-10 rounded-full object-cover"
      />
      <img
        v-else
        src="/default_profile.png"
        alt="Profile"
        class="w-10 h-10 rounded-full object-cover"
      />
      <span class="font-semibold">{{ review.nickname }}</span>

      <!-- 드롭다운 버튼 (내 게시글일 경우) -->
      <div v-if="review.isMyPost" class="ml-auto relative">
        <button @click="toggleDropdown" class="text-gray-500 hover:text-black">
          ⋮
        </button>
        <div
          v-if="showDropdown"
          class="absolute right-0 mt-2 w-24 bg-white border rounded shadow z-10"
        >
          <button
            @click="onEditButtonClick"
            class="block w-full px-3 py-2 text-left hover:bg-gray-100 transition"
          >
            수정
          </button>
          <button
            @click="onDelete"
            class="block w-full px-3 py-2 text-left text-red-500 hover:bg-red-100 transition"
          >
            삭제
          </button>
        </div>
      </div>
    </div>

    <!-- 생성/수정 날짜 -->
    <div class="text-sm text-gray-500 mt-1">
      <span>생성: {{ formatDateTime(review.createdAt) }}</span>
      <span v-if="review.updatedAt" class="ml-4">
        수정: {{ formatDateTime(review.updatedAt) }}
      </span>
    </div>

    <!-- 제목 -->
    <div class="text-lg font-bold mt-2">{{ review.title }}</div>

    <!-- 평점 -->
    <div class="flex items-center mt-2 space-x-2">
      <span class="text-lg flont-bold">{{ review.rating }}</span>
      <div class="flex items-center space-x-1">
        <svg
          v-for="star in 5"
          :key="star"
          class="w-5 h-5"
          :class="{
            'text-ssafy_orange': star <= Math.round(review.rating),
            'text-gray-300': star > Math.round(review.rating),
          }"
          fill="currentColor"
          viewBox="0 0 20 20"
        >
          <path
            d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.967a1
        1 0 00.95.69h4.175c.969 0 1.371 1.24.588
        1.81l-3.38 2.455a1 1 0 00-.364 1.118l1.287
        3.966c.3.922-.755 1.688-1.54 1.118l-3.38-2.455a1
        1 0 00-1.176 0l-3.38 2.455c-.784.57-1.838-.196-1.54-1.118l1.287-3.966a1
        1 0 00-.364-1.118L2.05 9.394c-.783-.57-.38-1.81.588-1.81h4.175a1
        1 0 00.95-.69l1.286-3.967z"
          />
        </svg>
      </div>
    </div>

    <!-- 본문 -->
    <img v-if="review.img" :src="review.img" width="100px" />
    <div class="mt-3 text-gray-800">
      {{ review.content }}
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps } from "vue";
import api from "@/api/axios";
import { useUserStore } from "@/stores/userStore";
const userStore = useUserStore();

const { review } = defineProps({
  review: Object,
});

const showDropdown = ref(false);

function toggleDropdown() {
  showDropdown.value = !showDropdown.value;
}

const emit = defineEmits(["delete", "update"]);
const onEditButtonClick = () => {
  emit("update", { ...review });
};

const onDelete = async () => {
  try {
    const response = await api.delete(`/post/${review.id}`);
    emit("delete");
  } catch (err) {
    console.log(err);
  }
};
function formatDateTime(input) {
  const date = new Date(input); // input은 ISO 문자열이나 Date 객체

  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, "0"); // 월은 0부터 시작
  const dd = String(date.getDate()).padStart(2, "0");

  const hh = String(date.getHours()).padStart(2, "0");
  const min = String(date.getMinutes()).padStart(2, "0");

  return `${yyyy}-${mm}-${dd} ${hh}:${min}`;
}
</script>

<style scoped>
/* 필요시 추가 스타일 */
</style>
