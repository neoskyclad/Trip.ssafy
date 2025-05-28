<template>
  <div class="flex flex-col gap-4">
    <h1 class="text-lg font-bold">Step 1.</h1>
    <h2 class="text-xl font-semibold">방 제목을 입력해주세요.</h2>
    <input v-model="title" class="h-12 px-4 rounded-md bg-gray-100" placeholder="방 제목 입력" required/>
  </div>
</template>

<script setup>
const title = defineModel('title') // v-model:title로 전달된 데이터 바인딩
</script>
