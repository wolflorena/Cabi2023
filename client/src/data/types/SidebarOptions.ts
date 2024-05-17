export type SidebarOption = {
  path: string;
  text: string;
  icon: string;
};

export type SidebarOptionsWithRole = {
  options: SidebarOption[];
  role: string;
};

export const AdminSidebarOptions: SidebarOptionsWithRole = {
  role: "admin",
  options: [
    {
      path: "/dashboard",
      text: "Dashboard",
      icon: "table-columns",
    },
    {
      path: "/appointments",
      text: "Appointments",
      icon: "calendar-check",
    },
    { path: "/patients", text: "Patients", icon: "hospital-user" },
    {
      path: "/inventory",
      text: "Inventory",
      icon: "cart-flatbed",
    },
    { path: "/forms", text: "Forms", icon: "highlighter" },
  ],
};

export const DoctorSidebarOptions: SidebarOptionsWithRole = {
  options: [
    {
      path: "/dashboard",
      text: "Dashboard",
      icon: "table-columns",
    },
    {
      path: "/appointments",
      text: "Appointments",
      icon: "calendar-check",
    },
    {
      path: "/patients",
      text: "Patients",
      icon: "hospital-user",
    },
    {
      path: "/inventory",
      text: "Inventory",
      icon: "cart-flatbed",
    },
    { path: "/forms", text: "Forms", icon: "highlighter" },
  ],
  role: "doctor",
};

export const CustomerSidebarOptions: SidebarOptionsWithRole = {
  options: [
    {
      path: "/profile",
      text: "Profile",
      icon: "table-columns",
    },
    {
      path: "/appointment",
      text: "Appointment",
      icon: "calendar-check",
    },
    {
      path: "/history",
      text: "History",
      icon: "hospital-user",
    },
    {
      path: "/forms",
      text: "Forms",
      icon: "cart-flatbed",
    },
  ],
  role: "customer",
};
