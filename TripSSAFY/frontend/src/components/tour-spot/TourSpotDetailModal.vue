<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
  >
    <div
      class="bg-white p-6 rounded-md shadow-lg w-[1200px] max-h-[90vh] overflow-y-auto relative"
      @click.stop
    >
      <!-- 닫기 버튼 -->
      <button
        @click="closeModal"
        class="absolute top-2 right-3 text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>

      <h2 class="text-2xl font-bold mb-6">{{ spot.title }}</h2>

      <main class="flex gap-6">
        <!-- 왼쪽 정보 영역 -->
        <div class="w-1/2 space-y-4">
          <img
            :src="spot.firstimage"
            class="rounded-md w-full h-68 object-cover"
          />

          <div class="space-y-2 text-gray-700">
            <div class="flex items-start gap-2">
              <span class="text-ssafy_blue">📍</span>
              <p>{{ spot.addr1 }}</p>
            </div>
            <div class="flex items-start gap-2">
              <span class="text-ssafy_blue">🔗</span>
              <div
                v-html="spot.homepage"
                class="hover:underline text-ssafy_blue"
              ></div>
            </div>
            <div class="flex items-start gap-2">
              <span class="text-ssafy_blue">📞</span>
              <p>{{ spot.tel }}</p>
            </div>
          </div>
        </div>

        <!-- 오른쪽 개요 -->
        <div class="w-1/2">
          <!-- 탭 버튼 영역 -->
          <div class="flex border-b mb-2">
            <button
              @click="showDetail = false"
              :class="[
                'w-1/2 py-2 text-center font-medium',
                !showDetail
                  ? 'border-b-2 border-blue-500 text-blue-600 font-semibold'
                  : 'text-gray-500 hover:text-blue-600',
              ]"
            >
              개요
            </button>
            <button
              @click="showDetail = true"
              :class="[
                'w-1/2 py-2 text-center font-medium',
                showDetail
                  ? 'border-b-2 border-blue-500 text-blue-600 font-semibold'
                  : 'text-gray-500 hover:text-blue-600',
              ]"
            >
              상세
            </button>
          </div>
          <div
            class="p-4 border rounded max-h-[400px] overflow-y-auto"
            v-if="!showDetail"
          >
            <div
              v-html="spot.overview"
              class="text-gray-800 leading-relaxed"
            ></div>
          </div>
          <template v-else>
            <DetailInfo
              :contentId="contentId"
              :contentTypeId="spot.contenttypeid"
            />
          </template>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import DetailInfo from "./DetailInfo.vue";
import api from "@/api/axios";
const props = defineProps({
  contentId: Number,
});
const spot = ref({});
const showDetail = ref(false);
onMounted(async () => {
  try {
    const response = await api.get(`/tour-spot/${props.contentId}`);
    spot.value = response.data.response.body.items.item[0];
  } catch (err) {
    console.log(err);
  }
});

const emit = defineEmits();
const closeModal = () => {
  emit("close");
};
</script>

<style scoped></style>
