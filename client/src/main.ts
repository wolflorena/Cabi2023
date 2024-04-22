import { createApp } from "vue";
import "./assets/main.less";
import App from "./App.vue";
import router from "./router";
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
  faHighlighter,
  faEye,
  faPen,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";

import { faTrashCan } from "@fortawesome/free-solid-svg-icons";

library.add(
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
  faPen,
  faTrashCan
);

createApp(App)
  .component("font-awesome-icon", FontAwesomeIcon)
  .use(router)
  .mount("#app");
