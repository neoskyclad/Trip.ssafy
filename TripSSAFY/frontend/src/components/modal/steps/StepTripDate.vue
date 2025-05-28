<template>
  <div class="flex flex-col gap-6">
    <h1 class="text-lg font-bold">Step 2.</h1>
    <h2 class="text-xl font-semibold">여행 날짜를 선택해주세요</h2>

    <div class="grid grid-cols-2 gap-6">
      <!-- 시작일 -->
      <div class="flex flex-col">
        <label class="mb-2 text-sm text-gray-600">시작일</label>
        <Datepicker
          class="h-12 px-4 w-52 rounded-md bg-gray-100 text-gray-800"
          v-model="startDate"
          placeholder="YYYY-MM-DD"
        />
      </div>

      <!-- 종료일 -->
      <div class="flex flex-col">
        <label class="mb-2 text-sm text-gray-600">종료일</label>
        <Datepicker
          class="h-12 px-4 w-52 rounded-md bg-gray-100 text-gray-800"
          v-model="endDate"
          placeholder="YYYY-MM-DD"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import Datepicker from "vue3-datepicker";
const startDate = defineModel("startDate");
const endDate = defineModel("endDate");
</script>
