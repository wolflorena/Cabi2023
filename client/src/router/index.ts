import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import SignupView from "../views/SignupView.vue";
import LoginView from "../views/admin/LoginView.vue";
import AdminDashboardView from "@/views/admin/AdminDashboardView.vue";
import AdminAppointmentsView from "@/views/admin/AdminAppointmentsView.vue";
import AdminPatientsView from "@/views/admin/AdminPatientsView.vue";
import AdminPatientDetailsView from "@/views/admin/AdminPatientDetailsView.vue";
import AdminEditPatientView from "@/views/admin/AdminEditPatientView.vue";
import AdminInventoryView from "@/views/admin/AdminInventoryView.vue";
import AdminFormsView from "@/views/admin/AdminFormsView.vue";
import AdminFormView from "@/views/admin/AdminFormView.vue";
import AdminFormEventsView from "@/views/admin/AdminFormEventsView.vue";
import CustomerProfileView from "@/views/customer/CustomerProfileView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/register",
    name: "register",
    component: SignupView,
  },
  {
    path: "/admin/login",
    name: "login",
    component: LoginView,
  },
  {
    path: "/admin/dashboard",
    name: "dashboard",
    component: AdminDashboardView,
  },
  {
    path: "/admin/appointments",
    name: "appointments",
    component: AdminAppointmentsView,
  },
  {
    path: "/admin/patients",
    name: "patients",
    component: AdminPatientsView,
  },
  {
    path: "/admin/patients/:id",
    name: "patientsDetails",
    component: AdminPatientDetailsView,
  },
  {
    path: "/admin/patients/edit/:id",
    name: "editPatientsDetails",
    component: AdminEditPatientView,
  },
  {
    path: "/admin/inventory",
    name: "inventory",
    component: AdminInventoryView,
  },
  {
    path: "/admin/forms",
    name: "forms",
    component: AdminFormsView,
  },
  {
    path: "/admin/forms/create",
    name: "formsAddition",
    component: AdminFormView,
  },
  {
    path: "/admin/forms/edit/:id",
    name: "editForm",
    component: AdminFormView,
  },
  {
    path: "/admin/forms/:id",
    name: "viewForm",
    component: AdminFormEventsView,
  },
  {
    path: "/profile",
    name: "profile",
    component: CustomerProfileView,
  },
  {
    path: "/appointment",
    name: "appointment",
    component: CustomerProfileView,
  },
  {
    path: "/history",
    name: "history",
    component: CustomerProfileView,
  },
  {
    path: "/forms",
    name: "forms",
    component: CustomerProfileView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
