<template>
  <nav class="flex justify-center mt-6">
    <ul class="inline-flex items-center space-x-2">
      <li>
        <button
          class="px-3 py-1 border rounded-md text-sm text-gray-700 hover:bg-gray-100"
          :disabled="currentPage <= 1"
          @click="changePage(currentPage - 1)"
        >
          이전
        </button>
      </li>

      <li v-for="page in visiblePages" :key="page">
        <button
          @click="changePage(page)"
          :class="[
            'px-3 py-1 border rounded-md text-sm',
            page === currentPage
              ? 'bg-ssafy_blue text-white border-ssafy_blue'
              : 'text-gray-700 hover:bg-gray-100',
          ]"
        >
          {{ page }}
        </button>
      </li>

      <li>
        <button
          class="px-3 py-1 border rounded-md text-sm text-gray-700 hover:bg-gray-100"
          :disabled="currentPage >= totalPages"
          @click="changePage(currentPage + 1)"
        >
          다음
        </button>
      </li>
    </ul>
  </nav>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  currentPage: {
    type: Number,
    required: true,
  },
  totalPages: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(["update:currentPage"]);

const changePage = (page) => {
  if (page >= 1 && page <= props.totalPages) {
    emit("update:currentPage", page);
  }
};

// 👇 최대 5개까지만 페이지 버튼 보여주는 로직
const visiblePages = computed(() => {
  const pages = [];
  const total = props.totalPages;
  const current = props.currentPage;
  const maxVisible = 5;

  let start = Math.max(1, current - Math.floor(maxVisible / 2));
  let end = start + maxVisible - 1;

  if (end > total) {
    end = total;
    start = Math.max(1, end - maxVisible + 1);
  }

  for (let i = start; i <= end; i++) {
    pages.push(i);
  }

  return pages;
});
</script>
