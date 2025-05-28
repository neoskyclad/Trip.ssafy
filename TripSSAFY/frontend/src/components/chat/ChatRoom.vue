<template>
  <div class="chat-room">
    <div class="messages" ref="messageContainer">
      <ChatMessage
        v-for="(msg, index) in messages"
        :key="index"
        :message="msg"
      />
    </div>

    <div v-if="isLoading" class="loading flex items-center justify-center">
      <img src="@/assets/gifs/loading.gif" class="w-8 h-8" />
    </div>

    <MessageInput @send="sendMessage" />
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import ChatMessage from '@/components/chat/ChatMessage.vue';
import MessageInput from '@/components/chat/MessageInput.vue';
import api from '@/api/axios';

const messages = ref([]);
const isLoading = ref(false);
const messageContainer = ref(null);

const sendMessage = async (userText) => {
  if (!userText.trim()) return;

  // 사용자 메시지 추가
  messages.value.push({ sender: 'user', text: userText });

  // 스크롤 아래로
  await nextTick(() => {
    messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
  });

  isLoading.value = true;

  try {
    const res = await api.post('/chatbot', { message: userText });
    messages.value.push({ sender: 'bot', text: res.data.reply });
  } catch (err) {
    messages.value.push({ sender: 'bot', text: '죄송합니다. 오류가 발생했어요.' });
  } finally {
    isLoading.value = false;
    await nextTick(() => {
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
    });
  }
};
</script>

<style scoped>
.chat-room {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f9f9f9;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}
</style>
