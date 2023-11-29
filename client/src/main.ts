import { createApp } from "vue";
import "./assets/main.less";
import App from "./App.vue";
import router from "./router";
import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faTableColumns } from "@fortawesome/free-solid-svg-icons";
import { faCalendarCheck } from "@fortawesome/free-solid-svg-icons";
import { faHospitalUser } from "@fortawesome/free-solid-svg-icons";
import { faCartFlatbed } from "@fortawesome/free-solid-svg-icons";
import { faHighlighter } from "@fortawesome/free-solid-svg-icons";

library.add(
  faTableColumns,
  faCalendarCheck,
  faHospitalUser,
  faCartFlatbed,
  faHighlighter
);

createApp(App)
  .component("font-awesome-icon", FontAwesomeIcon)
  .use(router)
  .mount("#app");
