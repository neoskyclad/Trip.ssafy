<template>
  <div class="min-h-screen flex flex-col">
    <Header @login="isLoginOpen = true" ref="headerRef" />

    <main>
      <!-- 첫 화면 (고정) -->
      <div
        class="h-screen w-full sticky top-0 z-0 overflow-hidden"
        :style="{ filter: `brightness(${imageBrightness})` }"
      >
        <div
          class="absolute inset-0 flex justify-center items-center bg-blue-50"
        >
          <img
            :src="backgroundImage"
            alt="Background"
            class="w-auto h-full object-cover opacity-90 brightness-95 contrast-110 transition-filter-long blur-transition"
            :class="{ 'blur-active': isBlurred }"
          />

          <div class="absolute top-1/4 left-[20%] text-left z-10">
            <h1
              class="text-5xl font-extrabold text-white drop-shadow-lg"
              :class="{ 'animate-slide-fade-in-1': isIntroVisible }"
            >
              Trip.ssafy에 오신 걸 환영합니다.
            </h1>
            <p
              class="mt-6 text-2xl text-white drop-shadow"
              :class="{ 'animate-slide-fade-in-2': isIntroVisible }"
            >
              당신의 여행 플래너, 지금부터 함께해요.
            </p>
          </div>
        </div>
      </div>

      <!-- 기능 설명 섹션 (GIF + 텍스트 교차 배치) -->
      <div
        v-for="(section, index) in sections"
        :key="index"
        ref="sectionRefs"
        class="section bg-white min-h-screen px-8 py-12 flex flex-col lg:flex-row items-center justify-center gap-16 shadow-xl z-10 mb-4 border-b border-gray-300 rounded-3xl"
      >
        <!-- 영상 영역 (왼쪽) -->
        <div class="lg:w-1/2 w-full flex justify-center">
          <video
            :src="section.video"
            autoplay
            muted
            loop
            playsinline
            class="rounded-xl shadow-lg max-w-[90%] lg:max-w-full"
          >
            브라우저가 비디오를 지원하지 않습니다.
          </video>
        </div>

        <!-- 텍스트 영역 (오른쪽) -->
        <div
          class="lg:w-1/2 w-full text-center lg:text-left flex flex-col justify-center items-center lg:items-start"
        >
          <h2 class="text-3xl font-bold text-gray-800 mb-4">
            {{ section.title }}
          </h2>
          <p class="text-lg text-gray-600 leading-relaxed">
            {{ section.content }}
          </p>

          <button
            v-if="index === 2"
            @click="handleClickStart"
            class="px-8 py-4 mt-8 bg-ssafy_blue text-white rounded-lg hover:bg-blue-700 transition font-semibold shadow"
          >
            지금 바로 시작하기
          </button>
        </div>
      </div>
    </main>

    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import { useRoute, useRouter } from "vue-router";
import ScrollMagic from "scrollmagic";

import { useUserStore } from "@/stores/userStore";
import Header from "@/components/common/Header.vue";
import Footer from "@/components/common/Footer.vue";
import backgroundImage from "@/assets/images/home_page_main.png";
import { showToastSuccess } from "@/api/toastMsg";

const userStore = useUserStore();
const route = useRoute();
const router = useRouter();

const imageBrightness = ref(1); // 기본 밝기 1 (100%)
const isIntroVisible = ref(false);
const isBlurred = ref(false);

const handleScroll = () => {
  const scrollY = window.scrollY;
  const start = window.innerHeight * 0.2;
  const end = window.innerHeight * 1.2;
  const progress = Math.min(1, Math.max(0, (scrollY - start) / (end - start)));

  // 1 ~ 0.4 사이 값으로 어두워지게
  imageBrightness.value = 1 - progress * 0.8;
};

const headerRef = ref(null);
const sectionRefs = ref([]);
const sections = ref([
  {
    title: "📝 여행 계획을 손쉽게",
    content:
      "여행 날짜, 장소, 동행자만 입력하세요. 캘린더 기반 인터페이스로 손쉽게 일정을 구성할 수 있어요.",
    video: "/assets/videos/add_room.mp4",
  },
  {
    title: "➕ 일정을 자유롭게 추가/편집",
    content:
      "드래그 앤 드롭으로 일정을 손쉽게 추가하거나, 지도 기반으로 장소를 빠르게 찾을 수 있어요.",
    video: "/assets/videos/add_schedule.mp4",
  },
  {
    title: "🧠 AI가 추천하는 여행지",
    content:
      "여행 장소와 스타일을 입력하면 AI가 딱 맞는 여행지를 추천해드려요.",
    video: "/assets/videos/ai_recommendation.mp4",
  },
]);

onMounted(() => {
  const token = route.query.token;
  if (token) {
    userStore.setToken(token);
    showToastSuccess("로그인 성공!");
    setTimeout(() => router.replace({ path: "/" }), 1500);
  }

  // 애니메이션 클래스 지연 적용
  requestAnimationFrame(() => {
    isIntroVisible.value = true;

    setTimeout(() => {
      isBlurred.value = true;
    }, 500);
  });

  window.addEventListener("scroll", handleScroll);
});

onBeforeUnmount(() => {
  window.removeEventListener("scroll", handleScroll);
});

const handleClickStart = () => {
  if (userStore.isLoggedIn) {
    // 여행 시작
    router.push("/room");
  } else {
    // 로그인 모달 띄우기
    headerRef.value?.openLoginModal();
  }
};
</script>

<style scoped>
@keyframes slideFadeUp {
  0% {
    opacity: 0;
    transform: translateY(40px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.slide-fade-up {
  animation: slideFadeUp 0.6s ease-out forwards;
}

.section {
  opacity: 1;
  transform: translateY(0);
  transition: none;
}

/* 환영 메시지: 먼저 나옴 */
.animate-slide-fade-in-1 {
  animation: slide-fade-in 1s ease-out forwards;
}

/* 소제목: 약간 늦게 나옴 */
.animate-slide-fade-in-2 {
  animation: slide-fade-in 1s ease-out 1s forwards;
}

@keyframes slide-fade-in {
  0% {
    transform: translateY(20px);
    opacity: 0;
  }

  100% {
    transform: translateY(0);
    opacity: 1;
  }
}

/* 기본적으로 숨겨둔다 */
.text-5xl,
.text-2xl {
  opacity: 0;
}

.blur-transition {
  filter: blur(0);
  transition: filter 2.5s ease-in-out;
}

.blur-transition.blur-active {
  filter: blur(3px); /* blur-sm 정도 값 */
}
</style>
