import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import SignupView from "../views/SignupView.vue";
import LoginView from "../views/admin/LoginView.vue";
import AdminDashboardView from "@/views/admin/AdminDashboardView.vue";
import AdminAppointmentsView from "@/views/admin/AdminAppointmentsView.vue";
import CustomerProfile from "@/views/customer/CustomerProfile.vue";
import AdminPatientsView from "@/views/admin/AdminPatientsView.vue";
import AdminPatientDetailsView from "@/views/admin/AdminPatientDetailsView.vue";
import AdminEditPatientView from "@/views/admin/AdminEditPatientView.vue";
import AdminInventoryView from "@/views/admin/AdminInventoryView.vue";
import AdminFormsView from "@/views/admin/AdminFormsView.vue";
import AdminAddFormView from "@/views/admin/AdminAddFormView.vue";

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
    component: AdminAddFormView,
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
