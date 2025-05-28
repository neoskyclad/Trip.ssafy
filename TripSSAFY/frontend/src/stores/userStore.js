import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "@/api/axios";
import router from "@/router/index.js";
import { showToastError } from "@/api/toastMsg";

export const useUserStore = defineStore("user", () => {
  const accessToken = ref(localStorage.getItem("accessToken") || "");
  const profileImage = ref(
    localStorage.getItem("profileImage") || "/default_profile.png"
  );
  const isLoggedIn = computed(() => !!accessToken.value);

  const setToken = async (token) => {
    accessToken.value = token;
    localStorage.setItem("accessToken", token);

    await fetchUserInfo();
  };

  const setProfileImage = (image) => {
    profileImage.value = image;
    localStorage.setItem("profileImage", image);
  };

  const username = ref("");
  const nickname = ref("");
  const createdAt = ref("");
  const role = ref("");
  // profileImage는 이미 선언되어 있음

  const fetchUserInfo = async () => {
    try {
      const response = await fetch("http://localhost:8080/user/info", {
        headers: {
          Authorization: `Bearer ${accessToken.value}`,
        },
      });
      if (!response.ok) throw new Error("유저 정보 요청 실패");
      const data = await response.json();

      username.value = data.username;
      nickname.value = data.nickname;
      createdAt.value = data.createdAt;
      role.value = data.role;
      setProfileImage(data.profileImage ?? "/default_profile.png");
    } catch (error) {
      logout();
      console.error("유저 정보 요청 실패:", error);
      showToastError("세션이 만료되었습니다. 다시 로그인 해주세요.");
      setTimeout(() => router.push("/"), 1500);
    }
  };

  const logout = () => {
    accessToken.value = "";
    profileImage.value = "/default_profile.png"; // 기본 이미지로 초기화
    localStorage.removeItem("accessToken");
    localStorage.removeItem("profileImage");
  };

  return {
    accessToken,
    isLoggedIn,
    profileImage,
    username,
    nickname,
    createdAt,
    role,
    setToken,
    setProfileImage,
    logout,
    fetchUserInfo,
  };
});
