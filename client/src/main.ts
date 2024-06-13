import { library } from "@fortawesome/fontawesome-svg-core";
import {
  faBars,
  faCalendarCheck,
  faCaretLeft,
  faCaretRight,
  faCartFlatbed,
  faCheck,
  faChevronDown,
  faChevronLeft,
  faChevronRight,
  faChevronUp,
  faCircleChevronDown,
  faCircleChevronUp,
  faCirclePlus,
  faPlus,
  faEye,
  faEyeSlash,
  faHighlighter,
  faHospitalUser,
  faInfoCircle,
  faKey,
  faPen,
  faTableColumns,
  faTrashCan,
  faUser,
  faXmark,
  faFilePdf,
  faDownload,
} from "@fortawesome/free-solid-svg-icons";
import {
  faCircleCheck,
  faXmarkCircle,
} from "@fortawesome/free-regular-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import "@vuepic/vue-datepicker/dist/main.css";
import { createApp } from "vue";
import VueSweetalert2 from "vue-sweetalert2";
import App from "./App.vue";
import "./assets/main.less";
import router from "./router";

library.add(
  faInfoCircle,
  faXmarkCircle,
  faTableColumns,
  faCircleCheck,
  faCalendarCheck,
  faHospitalUser,
  faCartFlatbed,
  faHighlighter,
  faCaretLeft,
  faCaretRight,
  faPlus,
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
  faXmark,
  faFilePdf,
  faDownload
);

createApp(App)
  .component("font-awesome-icon", FontAwesomeIcon)
  .use(router)
  .use(VueSweetalert2)
  .mount("#app");
