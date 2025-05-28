<template>
  <div class="input-box">
    <input
      type="text"
      v-model="text"
      @keydown.enter="emitMessage"
      placeholder="메시지를 입력하세요"
    />
    <button @click="emitMessage">전송</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
const emit = defineEmits(['send']);
const text = ref('');

const emitMessage = () => {
  if (text.value.trim()) {
    emit('send', text.value);
    text.value = '';
  }
};
</script>

<style scoped>
.input-box {
  display: flex;
  padding: 8px;
  border-top: 1px solid #ccc;
}

input {
  flex: 1;
  padding: 8px;
  border: none;
  outline: none;
}

button {
  padding: 0 16px;
}
</style>
