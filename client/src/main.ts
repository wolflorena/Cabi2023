import { createApp } from "vue";
import "./assets/main.less";
import App from "./App.vue";
import router from "./router";

createApp(App).use(router).mount("#app");
