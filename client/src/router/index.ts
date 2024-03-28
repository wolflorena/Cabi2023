import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import SignupView from "../views/SignupView.vue";
import LoginView from "../views/admin/LoginView.vue";
import AdminDashboardView from "../views/admin/AdminDashboardView.vue";
import CustomerProfile from "@/views/customer/CustomerProfile.vue";

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
    path: "/admin/dashboard",
    name: "dashboard",
    component: AdminDashboardView,
  },
  {
    path: "/profile",
    name: "CustomerProfile",
    component: CustomerProfile,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
