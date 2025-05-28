<template>
  <!-- 여행 방 헤더 -->
  <div class="flex justify-between">
    <h2 class="text-4xl font-bold text-ssafy_blue">
      {{ selectedRoom.tripRoom.title }}
    </h2>
    <!-- 여행 방 수정 버튼 -->
    <div class="relative inline-block text-left" ref="dropdownRef">
      <!-- 점 3개 버튼 -->
      <button
        @click="toggleDropdown"
        class="text-gray-500 hover:text-black text-2xl p-2"
      >
        ⋮
      </button>

      <!-- 드롭다운 메뉴 -->
      <div
        v-if="isDropdownOpen"
        class="absolute right-0 mt-2 w-32 bg-white rounded-md shadow-lg ring-1 ring-black/5 z-10"
      >
        <button
          @click="editRoom"
          class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition"
        >
          수정
        </button>
        <button
          @click="deleteRoomConfirm"
          class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-100 transition"
        >
          삭제
        </button>
      </div>
    </div>
  </div>
  <!-- 여행 날짜 표시 -->
  <p class="text-lg">
    {{ formatDate(selectedRoom.tripRoom.startDate) }} ~
    {{ formatDate(selectedRoom.tripRoom.endDate) }}
  </p>
  <!-- 프로필 이미지 표시 -->
  <div class="flex gap-3 mt-2">
    <img
      v-for="user in selectedRoom.memberList"
      :key="user.nickname"
      :src="user.profileImg || '/default_profile.png'"
      class="w-10 h-10 rounded-full border border-gray-300 shadow-md object-cover"
      alt="프로필 이미지"
    />
  </div>
</template>

<script setup>
import { ref, defineProps, onMounted, onBeforeUnmount, inject } from "vue";

const props = defineProps({
  selectedRoom: {
    type: Object,
    required: true,
  },
  formatDate: {
    type: Function,
    required: true,
  },
  editRoom: {
    type: Function,
    required: true,
  },
  deleteRoomConfirm: {
    type: Function,
    required: true,
  },
});

const dropdownRef = ref(null);
const isDropdownOpen = inject("isDropdownOpen");

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const handleClickOutside = (event) => {
  if (dropdownRef.value && !dropdownRef.value.contains(event.target)) {
    isDropdownOpen.value = false;
  }
};
</script>
