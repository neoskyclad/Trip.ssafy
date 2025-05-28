<template>
  <div class="modal bg-white rounded-xl shadow p-6 max-w-xl mx-auto w-full">
    <div class="flex justify-between items-center mb-4">
      <p></p>
      <button class="text-gray-500 hover:text-black" @click="$emit('close')">
        ✕
      </button>
    </div>
    <div v-if="step <= 3" class="mb-2">
      <h1 v-if="step === 1" class="text-lg font-bold">Step 1.</h1>
      <h1 v-if="step === 2" class="text-lg font-bold">Step 2.</h1>
      <h1 v-if="step === 3" class="text-lg font-bold">Step 3.</h1>
    </div>

    <!-- Step 1~3: 입력 단계 -->
    <div v-if="step <= 3" class="text-center space-y-4">
      <!-- 대표 이미지 -->
      <img
        v-if="step === 1"
        src="@/assets/images/region.png"
        alt="지역 선택"
        class="w-32 h-32 mx-auto"
      />
      <img
        v-else-if="step === 2"
        src="@/assets/images/companion.png"
        alt="동행자 선택"
        class="w-32 h-32 mx-auto"
      />
      <img
        v-else
        src="@/assets/images/style.png"
        alt="스타일 선택"
        class="w-32 h-32 mx-auto"
      />

      <!-- 질문형 문구 -->
      <h2 class="text-lg font-semibold">
        {{
          step === 1
            ? "가고 싶은 지역이 어디신가요?"
            : step === 2
            ? "누구와 함께 떠나시나요?"
            : "어떤 스타일의 여행을 원하시나요?"
        }}
      </h2>

      <!-- 안내 설명 -->
      <p class="text-sm text-gray-600">
        {{
          step === 1
            ? "선호하는 여행 지역을 입력해주세요 (예: 제주도)"
            : step === 2
            ? "함께하는 사람을 입력해주세요 (예: 친구, 가족, 혼자)"
            : "힐링, 액티비티, 맛집 탐방 등 스타일을 입력해주세요"
        }}
      </p>

      <!-- 입력 필드 -->
      <input
        v-model="currentInput"
        class="input"
        :placeholder="placeholders[step - 1]"
      />

      <!-- 버튼 -->
      <div class="flex justify-between mt-4">
        <button v-if="step > 1" @click="prevStep" class="btn-prev">이전</button>
        <button v-if="step < 3" @click="nextStep" class="btn ml-auto">
          다음
        </button>
        <button v-else @click="nextStep" class="btn-submit ml-auto">
          완료
        </button>
      </div>
    </div>

    <!-- Step 4: 로딩 -->
    <div v-else-if="step === 4" class="flex flex-col items-center">
      <p class="mb-2 text-gray-700 pt-2 pb-4">AI가 여행지를 찾는 중입니다...</p>
      <img
        src="@/assets/gifs/loading-trip.gif"
        alt="로딩 중"
        class="w-24 h-24"
      />
    </div>

    <!-- Step 5: 결과 -->
    <div v-else-if="step === 5">
      <h3 class="text-lg font-semibold mb-2">추천 여행지 ✨</h3>

      <!-- 마크다운으로 파싱된 HTML 삽입 -->
      <div
        v-html="parsedRecommendation"
        class="prose max-w-none mb-4 leading-relaxed"
      ></div>

      <div
        class="bg-blue-50 border border-blue-200 p-4 rounded-xl text-sm flex items-center justify-between shadow-sm"
      >
        <div class="text-blue-800 font-medium">
          추천된 여행지를 여행 계획에 추가해보세요!
        </div>
        <button
          @click="$emit('recommend', keywords)"
          class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg transition text-sm ml-4"
        >
          장소 추가
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import api from "@/api/axios";
import { marked } from "marked";
import { showToastError } from "@/api/toastMsg";

const emit = defineEmits(["close", "recommend"]);

const step = ref(1);
const region = ref("");
const companion = ref("");
const style = ref("");
const recommendation = ref("");

const placeholders = ["예: 서울", "예: 친구", "예: 힐링"];
const currentInput = ref("");

const prevStep = () => {
  step.value--;
  if (step.value === 1) currentInput.value = region.value;
  else if (step.value === 2) currentInput.value = companion.value;
  else if (step.value === 3) currentInput.value = style.value;
};

const nextStep = async () => {
  if (step.value === 1) region.value = currentInput.value;
  else if (step.value === 2) companion.value = currentInput.value;
  else if (step.value === 3) {
    style.value = currentInput.value;
    await requestRecommendation();
    return;
  }

  step.value++;
  if (step.value === 2) currentInput.value = companion.value;
  else if (step.value === 3) currentInput.value = style.value;
};

const keywords = ref([]);

const extractBoldKeywords = (markdownText) => {
  const boldPattern = /\*\*(.+?)\*\*/g; // **텍스트** 패턴
  const matches = [...markdownText.matchAll(boldPattern)];
  return matches.map((m) => m[1]); // "텍스트"만 추출
};

const requestRecommendation = async () => {
  step.value = 4;

  try {
    const res = await api.post("/chat/start", {
      region: region.value,
      companion: companion.value,
      style: style.value,
    });

    recommendation.value = res.data;

    // ✅ 추천 결과에서 키워드 추출
    keywords.value = extractBoldKeywords(recommendation.value);

    console.log("추출된 여행지 키워드:", keywords.value);

    step.value = 5;
  } catch (err) {
    console.error("AI 추천 실패:", err);
    showToastError("추천에 실패했습니다.");
    step.value = 3;
  }
};

const parsedRecommendation = computed(() => {
  return marked.parse(recommendation.value);
});
</script>

<style scoped>
.input {
  @apply w-full border border-gray-300 p-2 rounded mt-2;
}

.btn-prev {
  @apply bg-gray-200 hover:bg-gray-300 transition rounded px-4 py-2;
}

.btn {
  @apply bg-ssafy_blue hover:bg-blue-700 transition text-white px-4 py-2 rounded;
}

.btn-submit {
  @apply bg-ssafy_orange hover:bg-orange-400 transition text-white px-4 py-2 rounded;
}
</style>
