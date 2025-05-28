// src/axios.js
import axios from "axios";
import router from "@/router/index.js"; // 👉 useRouter() 대신 직접 import
import { useUserStore } from "@/stores/userStore";
import { showToastError } from "./toastMsg";
const api = axios.create({
  baseURL: "http://localhost:8080",
});

api.interceptors.request.use(
  (config) => {
    const userStore = useUserStore();
    if (userStore.accessToken) {
      config.headers.Authorization = `Bearer ${userStore.accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const userStore = useUserStore();

    if (error.response && error.response.status === 401) {
      userStore.logout?.();
      console.error("유저 정보 요청 실패:", error);
      showToastError("세션이 만료되었습니다. 다시 로그인 해주세요.");
      setTimeout(() => {
        window.location.href = "/";
      }, 1500);
    }

    return Promise.reject(error);
  }
);

export default api;
