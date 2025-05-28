<template>
  <div :class="['message', message.sender === 'user' ? 'user' : 'bot']">
    <div class="bubble">{{ message.text }}</div>
  </div>
</template>

<script setup>
defineProps({
  message: Object
});
</script>

<style scoped>
.message {
  display: flex;
  margin-bottom: 8px;
}

.message.user {
  justify-content: flex-end;
}

.message.bot {
  justify-content: flex-start;
}

.bubble {
  max-width: 70%;
  padding: 10px;
  border-radius: 16px;
  background: #eee;
}

.user .bubble {
  background: #d3f0ff;
}
</style>
