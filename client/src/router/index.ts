import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import SignupView from "../views/SignupView.vue";
import LoginView from "../views/LoginView.vue";
import AdminDashboardView from "../views/AdminDashboardView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "default",
    component: LoginView,
  },
  {
    path: "/register",
    name: "register",
    component: SignupView,
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
  },
  {
    path: "/dashboard",
    name: "dashboard",
    component: AdminDashboardView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
