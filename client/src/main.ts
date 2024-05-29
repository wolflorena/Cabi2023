import { createApp } from "vue";
import "./assets/main.less";
import App from "./App.vue";
import router from "./router";
import VueSweetalert2 from "vue-sweetalert2";
import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
  faBars,
  faChevronDown,
  faChevronLeft,
  faChevronRight,
  faChevronUp,
  faCirclePlus,
  faTableColumns,
  faCalendarCheck,
  faHospitalUser,
  faCartFlatbed,
  faCircleChevronDown,
  faCircleChevronUp,
  faHighlighter,
  faEye,
  faPen,
  faUser,
  faKey,
  faEyeSlash,
  faCheck,
  faXmark,
  faInfoCircle,
  faTrashCan,
} from "@fortawesome/free-solid-svg-icons";

library.add(
  faInfoCircle,
  faTableColumns,
  faCalendarCheck,
  faHospitalUser,
  faCartFlatbed,
  faHighlighter,
  faChevronRight,
  faChevronDown,
  faChevronUp,
  faChevronLeft,
  faCirclePlus,
  faBars,
  faEye,
  faCircleChevronDown,
  faEyeSlash,
  faPen,
  faTrashCan,
  faUser,
  faKey,
  faCheck,
  faCircleChevronUp,
  faXmark
);

createApp(App)
  .component("font-awesome-icon", FontAwesomeIcon)
  .use(router)
  .use(VueSweetalert2)
  .mount("#app");
