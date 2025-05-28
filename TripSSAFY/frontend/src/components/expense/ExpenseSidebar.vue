<template>
  <aside class="w-96 bg-white border shadow-xl overflow-y-auto">
    <div
      class="flex justify-between items-center bg-gray-100 px-5 py-4 border-b"
    >
      <span class="font-semibold text-gray-700 text-2xl"
        >💰 나의 지출 내역</span
      >
      <!-- 닫기 버튼 -->
      <button
        @click="$emit('close')"
        class="text-gray-500 hover:text-gray-800 text-2xl leading-none"
        type="button"
        aria-label="닫기"
      >
        &times;
      </button>
    </div>

    <!-- 총액 표시 -->
    <div class="px-6 py-5 border-b border-gray-200">
      <p class="text-gray-500 text-sm uppercase tracking-wide">총 지출</p>
      <p class="text-3xl font-extrabold text-orange-500 mt-1">
        &#8361;{{ totalPrice.toLocaleString() }}
      </p>
    </div>

    <!-- 지출 내역 리스트 -->
    <div class="flex-1 divide-y divide-gray-100 overflow-y-auto">
      <div
        v-for="(expense, index) in expenses"
        :key="index"
        class="px-6 py-4 hover:bg-gray-50 transition-colors cursor-pointer"
      >
        <div class="flex justify-between items-center">
          <p
            class="text-gray-800 font-semibold truncate max-w-[18rem]"
            title="지출 장소"
          >
            {{ expense.placeName }}
          </p>
          <p class="text-sm text-orange-600 font-bold whitespace-nowrap">
            &#8361;{{ expense.amount.toLocaleString() }}
          </p>
        </div>
        <p class="text-xs text-gray-400 mt-1" title="방문 날짜">
          {{ formatDate(expense.visitDate) }}
        </p>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import api from "@/api/axios";
import { showToastError } from "@/api/toastMsg";
const props = defineProps({
  roomId: Number,
});
const emit = defineEmits(["close"]);
const expenses = ref([]);
onMounted(async () => {
  try {
    const response = await api.get(`/expense/room/${props.roomId}`);
    expenses.value = response.data;
  } catch (err) {
    showToastError("가계부 로드 실패");
    console.log(err);
  }
});
const totalPrice = computed(() =>
  expenses.value.reduce((sum, e) => sum + (e.amount || 0), 0)
);
const formatDate = (dateStr) => {
  const date = new Date(dateStr);
  return date.toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
};
</script>

<style scoped></style>
