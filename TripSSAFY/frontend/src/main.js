// src/main.js
import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
import "./assets/css/index.css";
import ToastPlugin from "vue-toast-notification";
import "vue-toast-notification/dist/theme-bootstrap.css";

const pinia = createPinia();
createApp(App)
  .use(router) // <- router 연결 필수
  .use(pinia)
  .use(ToastPlugin)
  .mount("#app");
