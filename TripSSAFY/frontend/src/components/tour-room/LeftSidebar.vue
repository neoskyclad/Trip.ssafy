<template>
  <aside
    :class="['transition-all duration-300', isCollapsed ? 'w-16' : 'w-64']"
    class="bg-white p-4 relative"
  >
    <!-- 접기 버튼 -->
    <button @click="isCollapsed = !isCollapsed" class="mb-4">≡</button>

    <!-- 홈으로 버튼 (≡ 버튼 바로 아래, 좌측 정렬) -->
    <div v-if="!isCollapsed" class="mb-6">
      <router-link
        to="/"
        class="flex items-center gap-1 text-base text-left text-gray-500 hover:text-gray-700 transition mb-6 ml-1 w-fit"
      >
        <img src="@/assets/icons/previous-icon.png" alt="뒤로가기" class="w-4 h-4" />
        홈으로
      </router-link>

      <!-- 프로필 -->
      <div class="mb-6">
        <img
          :src="userStore.profileImage"
          alt="프로필 이미지"
          class="w-32 h-32 rounded-full mx-auto shadow-lg"
        />
        <p class="text-center text-2xl mt-4">{{ userStore.nickname }}</p>
      </div>

      <!-- 여행 방 리스트 -->
      <span
        class="text-lg text-center bg-ssafy_orange shadow text-white font-medium block mb-2 px-4 py-2 rounded w-full"
      >
        여행 방
      </span>
      <div v-if="triproom.length > 0" v-for="room in triproom">
        <button
          class="block w-full mb-2 p-2 bg-white border rounded hover:bg-gray-100 transition"
          @click="handleRoomDetail(room.id)"
        >
          {{ room.title }}
        </button>
      </div>
      <div v-else class="text-center py-4">여행 방이 없습니다.</div>
    </div>
  </aside>
</template>

<script setup>
import { ref, inject } from "vue";

const userStore = inject("userStore");

const props = defineProps({
  triproom: {
    type: Array,
  },
  handleRoomDetail: {
    type: Function,
  },
});
const isCollapsed = ref(false);
</script>
