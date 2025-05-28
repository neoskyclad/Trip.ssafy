<template>
  <header
    class="flex justify-between items-center px-10 py-3 bg-white shadow-m border-b border-gray-200 font-regular"
  >
    <!-- 왼쪽: 로고 -->
    <div class="relative flex items-center space-x-2">
      <router-link
        to="/"
        class="flex items-center"
        @mouseover="triggerAnimation"
      >
        <img
          ref="logo"
          src="@/assets/logos/header_logo.png"
          alt="Logo"
          width="200"
        />
      </router-link>

      <!-- ✈️ 비행기 애니메이션 (로고 뒤로 배치됨) -->
      <span
        v-show="showPlane"
        class="absolute left-0 top-0 text-3xl animate-fly-plane z-0"
        @animationend="resetAnimation"
      >
        ✈️
      </span>
    </div>

    <!-- 오른쪽: 네비게이션 -->
    <nav class="flex items-center space-x-3 relative">
      <router-link
        to="/"
        class="flex items-center gap-2 py-1.5 px-2 rounded-md hover:bg-gray-100 transition"
      >
        <img
          src="@/assets/icons/home-vector-icon.png"
          alt="홈"
          class="w-5 h-5"
        />
        홈으로
      </router-link>
      <router-link to="/post" class="flex items-center gap-2 py-1.5 px-2 rounded-md hover:bg-gray-100">
        <img
          src="@/assets/icons/clipboard-vector-icon.png"
          alt="게시판"
          class="w-5 h-5"
        />
        게시판
      </router-link>
      <router-link
        to="/search"
        class="flex items-center gap-2 py-1.5 px-2 rounded-md hover:bg-gray-100"
      >
        <img
          src="@/assets/icons/promotion-vector-icon.png"
          alt="검색"
          class="w-6 h-6"
        />
        광고
      </router-link>
      <router-link
        to="/recommend"
        class="flex items-center gap-2 py-1.5 px-2 rounded-md hover:bg-gray-100 transition"
      >
        <img
          src="@/assets/icons/ai-vector-icon.png"
          alt="AI 추천"
          class="w-6 h-6"
        />
        AI 추천
      </router-link>

      <template v-if="userStore.isLoggedIn">
        <!-- 프로필 버튼 -->
        <div class="py-1.5 px-2 relative">
          <img
            :src="userStore.profileImage"
            alt="프로필"
            class="w-9 h-9 rounded-full object-cover ml-2 cursor-pointer"
            @click="toggleDropdown"
          />

          <!-- 드롭다운 메뉴 -->
          <div
            v-if="isDropdownOpen"
            class="absolute right-0 mt-2 w-36 bg-white border border-gray-200 rounded shadow-md z-50"
          >
            <router-link
              to="/mypage"
              class="block px-4 py-2 hover:bg-gray-100 transition"
              >마이페이지</router-link
            >
            <button
              @click="logout"
              class="block w-full text-left px-4 py-2 text-red-600 hover:bg-red-100 transition"
            >
              로그아웃
            </button>
          </div>
        </div>

        <!-- 여행 계획하기 버튼 -->
        <router-link
          to="/room"
          class="bg-ssafy_blue text-white font-light py-1.5 px-4 rounded-md border border-ssafy_blue hover:bg-blue-700 transition"
        >
          여행 계획하기
        </router-link>
      </template>

      <template v-else>
        <button
          @click="openLoginModal"
          class="bg-ssafy_blue text-white font-light py-1.5 px-4 rounded-md border border-ssafy_blue hover:bg-blue-700 transition"
        >
          로그인 / 회원가입
        </button>
      </template>
    </nav>

    <LoginModal
      v-if="isLoginOpen"
      @close="closeLoginModal"
      @register="isRegisterOpen = true"
      :openRoleModal="openRoleModal"
    />
    <ChooseRoleModal
      v-if="isRoleOpen"
      @close="closeRoleModal"
      :openRegisterModal="openRegisterModal"
    />
    <RegisterModal
      v-if="isRegisterOpen"
      @close="closeRegisterModal"
      :role="selectedRole"
    />
  </header>
</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/userStore";
import LoginModal from "@/components/modal/LoginModal.vue";
import RegisterModal from "@/components/modal/RegisterModal.vue";
import ChooseRoleModal from "@/components/modal/ChooseRoleModal.vue";

const emit = defineProps(["login", "register"]);

const userStore = useUserStore();

const isLoginOpen = ref(false);
const isRoleOpen = ref(false);
const isRegisterOpen = ref(false);
const selectedRole = ref("ROLE_USER");
const isDropdownOpen = ref(false);

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const openLoginModal = () => {
  isLoginOpen.value = true;
};

defineExpose({
  openLoginModal,
});

const closeLoginModal = () => {
  isLoginOpen.value = false;
};

const openRoleModal = () => {
  isLoginOpen.value = false;
  isRoleOpen.value = true;
};
const closeRoleModal = () => {
  isRoleOpen.value = false;
};
const openRegisterModal = (role) => {
  selectedRole.value = role;
  isRoleOpen.value = false;
  isRegisterOpen.value = true;
};

const closeRegisterModal = () => {
  isRegisterOpen.value = false;
};

const logout = () => {
  userStore.logout();
  window.location.href = "/";
};

// 로고 애니메이션
const showPlane = ref(false);
const isAnimating = ref(false);

const triggerAnimation = () => {
  if (isAnimating.value) return; // 애니메이션 중이면 클릭 무시

  isAnimating.value = true;
  showPlane.value = true;
};

const resetAnimation = () => {
  showPlane.value = false;
  isAnimating.value = false;
};
</script>

<style scoped>
@keyframes fly-plane {
  0% {
    transform: translate(-200px, 100px) rotate(0deg);
    opacity: 1;
  }

  100% {
    transform: translate(400px, -100px) rotate(45deg);
    opacity: 0;
  }
}

.animate-fly-plane {
  animation: fly-plane 2s ease-out forwards;
  pointer-events: none;
  z-index: 0;
  /* 로고보다 뒤로 */
}
</style>
