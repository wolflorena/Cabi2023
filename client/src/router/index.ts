import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
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
import CustomerAppointmentView from "@/views/customer/CustomerAppointmentView.vue";
import DoctorProfileView from "@/views/doctor/DoctorProfileView.vue";
import DoctorDashboardView from "@/views/doctor/DoctorDashboardView.vue";
import DoctorPatientsView from "@/views/doctor/DoctorPatientsView.vue";
import DoctorCalendarView from "@/views/doctor/DoctorCalendarView.vue";
import {
  getUserRole,
  isAuthenticated,
  isTokenValid,
} from "@/services/authentication_service";
import LoginView from "@/views/Login-Register/LoginView.vue";
import SignupView from "@/views/Login-Register/SignupView.vue";
import HistoryView from "@/views/customer/HistoryView.vue";
import FormsView from "@/views/customer/FormsView.vue";
import DoctorPatientDetailsView from "@/views/doctor/DoctorPatientDetailsView.vue";

type RouteMeta = {
  requiresAuth: boolean;
  roles?: string[];
};

const routes: Array<RouteRecordRaw> = [
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
    name: "adminForms",
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
    path: "/doctor/profile",
    name: "doctorProfile",
    component: DoctorProfileView,
  },
  {
    path: "/doctor/dashboard",
    name: "doctorDashboard",
    component: DoctorDashboardView,
  },
  {
    path: "/doctor/calendar",
    name: "doctorCalendar",
    component: DoctorCalendarView,
  },
  {
    path: "/doctor/patients",
    name: "doctorPatients",
    component: DoctorPatientsView,
  },
  {
    path: "/doctor/patients/:id",
    name: "patientDetails",
    component: DoctorPatientDetailsView,
  },
  {
    path: "/recovery",
    name: "recovery",
    component: LoginView,
  },
  {
    path: "/profile",
    name: "profile",
    component: CustomerProfileView,
    meta: { requiresAuth: true, roles: ["ROLE_CUSTOMER"] } as RouteMeta,
  },
  {
    path: "/appointment",
    name: "appointment",
    component: CustomerAppointmentView,
    meta: { requiresAuth: true, roles: ["ROLE_CUSTOMER"] } as RouteMeta,
  },
  {
    path: "/history",
    name: "history",
    component: HistoryView,
    meta: { requiresAuth: true, roles: ["ROLE_CUSTOMER"] } as RouteMeta,
  },
  {
    path: "/forms",
    name: "forms",
    component: FormsView,
    meta: { requiresAuth: true, roles: ["ROLE_CUSTOMER"] } as RouteMeta,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);

  if (requiresAuth && !isAuthenticated()) {
    next("/login");
  } else {
    const userRole = getUserRole();
    const routeRoles = to.meta.roles as string[];

    if (requiresAuth && !routeRoles.includes(userRole)) {
      next("/404_NOT_FOUND");
    } else {
      next();
    }
  }
});

export default router;
