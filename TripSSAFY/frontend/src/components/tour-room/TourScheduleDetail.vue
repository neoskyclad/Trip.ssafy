<template>
  <div
    class="bg-white rounded-2xl shadow-md p-4 mb-4 border border-gray-100 flex items-start gap-4 justify-between"
  >
    <!-- 인덱스 + 내용 -->
    <div class="flex gap-4">
      <!-- 강조된 인덱스 번호 -->
      <div
        class="w-8 h-8 flex items-center justify-center rounded-full bg-ssafy_blue text-white font-bold text-sm mt-1"
      >
        {{ index + 1 }}
      </div>

      <!-- 장소 정보 -->
      <div class="flex-1">
        <div class="text-lg text-black font-medium">
          {{ place.placeName }}
        </div>
        <div v-if="place.memo" class="text-sm text-gray-500 mt-1">
          {{ place.memo }}
        </div>
      </div>
    </div>

    <div class="flex gap-2 items-center">
      <!-- 메모 버튼 -->
      <button @click.stop.prevent="$emit('memo', index)" class="mt-1">
        <img
          src="@/assets/icons/memo.png"
          alt="메모"
          class="w-6 h-6 opacity-50 hover:opacity-100 transition"
        />
      </button>
      <button @click.stop.prevent="openExpenseModal = true" class="mt-1">
        <img
          src="@/assets/icons/calculator-icon.png"
          alt="정산"
          class="w-5 h-5 opacity-50 hover:opacity-100 transition"
        />
      </button>
      <!-- 삭제 버튼 -->
      <button @click.stop.prevent="$emit('delete', index)" class="mt-1">
        <img
          src="@/assets/icons/trash-can.png"
          alt="삭제"
          class="w-5 h-5 opacity-50 hover:opacity-100 transition"
        />
      </button>
      <ExpenseModal
        v-if="openExpenseModal"
        :place-id="place.id"
        @close="openExpenseModal = false"
      />
    </div>
  </div>
</template>

<script setup>
import ExpenseModal from "../expense/ExpenseModal.vue";
import { ref } from "vue";
const props = defineProps({
  place: {
    type: Object,
    required: true,
  },
  index: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(["memo", "delete"]);
const openExpenseModal = ref(false);
</script>
