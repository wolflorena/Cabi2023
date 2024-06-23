import { library } from "@fortawesome/fontawesome-svg-core";
import {
  faBars,
  faCalendarCheck,
  faCaretLeft,
  faCaretRight,
  faCartFlatbed,
  faCaretDown,
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
  faCircleUser,
  faStar,
  faUser,
  faXmark,
  faFilePdf,
  faCircleQuestion,
  faDownload,
  faCircleChevronRight,
  faLeftLong,
  faFolder,
  faFolderOpen,
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
  faLeftLong,
  faCircleCheck,
  faCalendarCheck,
  faHospitalUser,
  faCaretDown,
  faCartFlatbed,
  faStar,
  faHighlighter,
  faCaretLeft,
  faCaretRight,
  faPlus,
  faChevronRight,
  faChevronDown,
  faCircleUser,
  faChevronUp,
  faCircleQuestion,
  faChevronLeft,
  faCirclePlus,
  faBars,
  faEye,
  faCircleChevronDown,
  faCircleChevronRight,
  faEyeSlash,
  faPen,
  faTrashCan,
  faUser,
  faKey,
  faCheck,
  faCircleChevronUp,
  faXmark,
  faFilePdf,
  faDownload,
  faFolder,
  faFolderOpen
);

createApp(App)
  .component("font-awesome-icon", FontAwesomeIcon)
  .use(router)
  .use(VueSweetalert2)
  .mount("#app");
