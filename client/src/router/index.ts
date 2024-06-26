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
import HistoryView from "@/views/customer/HistoryView.vue";
import FormsView from "@/views/customer/FormsView.vue";
import DoctorPatientDetailsView from "@/views/doctor/DoctorPatientDetailsView.vue";
import HomePage from "@/views/Login-Register-Home/HomePage.vue";
import SignupView from "@/views/Login-Register-Home/SignupView.vue";
import LoginView from "@/views/Login-Register-Home/LoginView.vue";
import NotFoundView from "@/views/NotFoundView.vue";
import ForgotPassword from "@/views/Login-Register-Home/ForgotPassword.vue";

type RouteMeta = {
  requiresAuth: boolean;
  roles?: string[];
};

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect: "/home",
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
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/admin/appointments",
    name: "appointments",
    component: AdminAppointmentsView,
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/admin/patients",
    name: "patients",
    component: AdminPatientsView,
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/admin/patients/:id",
    name: "patientsDetails",
    component: AdminPatientDetailsView,
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/admin/patients/edit/:id",
    name: "editPatientsDetails",
    component: AdminEditPatientView,
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/admin/inventory",
    name: "inventory",
    component: AdminInventoryView,
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/admin/forms",
    name: "adminForms",
    component: AdminFormsView,
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/admin/forms/create",
    name: "formsAddition",
    component: AdminFormView,
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/admin/forms/edit/:id",
    name: "editForm",
    component: AdminFormView,
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/admin/forms/:id",
    name: "viewForm",
    component: AdminFormEventsView,
    meta: { requiresAuth: true, roles: ["ROLE_ADMIN"] } as RouteMeta,
  },
  {
    path: "/doctor/profile",
    name: "doctorProfile",
    component: DoctorProfileView,
    meta: { requiresAuth: true, roles: ["ROLE_DOCTOR"] } as RouteMeta,
  },
  {
    path: "/doctor/dashboard",
    name: "doctorDashboard",
    component: DoctorDashboardView,
    meta: { requiresAuth: true, roles: ["ROLE_DOCTOR"] } as RouteMeta,
  },
  {
    path: "/doctor/calendar",
    name: "doctorCalendar",
    component: DoctorCalendarView,
    meta: { requiresAuth: true, roles: ["ROLE_DOCTOR"] } as RouteMeta,
  },
  {
    path: "/doctor/patients",
    name: "doctorPatients",
    component: DoctorPatientsView,
    meta: { requiresAuth: true, roles: ["ROLE_DOCTOR"] } as RouteMeta,
  },
  {
    path: "/doctor/patients/:id",
    name: "patientDetails",
    component: DoctorPatientDetailsView,
    meta: { requiresAuth: true, roles: ["ROLE_DOCTOR"] } as RouteMeta,
  },
  {
    path: "/recovery",
    name: "recovery",
    component: ForgotPassword,
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
  {
    path: "/home",
    name: "home",
    component: HomePage,
  },
  {
    path: "/404_NOT_FOUND",
    name: "notFound",
    component: NotFoundView,
  },
  {
    path: "/:catchAll(.*)",
    name: "catchAll",
    component: NotFoundView,
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
