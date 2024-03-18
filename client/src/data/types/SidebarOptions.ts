export type SidebarOption = {
  path: string;
  text: string;
  icon: string;
};

export const AdminSidebarOptions: SidebarOption[] = [
  { path: "/dashboard", text: "Dashboard", icon: "table-columns" },
  { path: "/appointments", text: "Appointments", icon: "calendar-check" },
  { path: "/patients", text: "Patients", icon: "hospital-user" },
  { path: "/inventory", text: "Inventory", icon: "cart-flatbed" },
  { path: "/forms", text: "Forms", icon: "highlighter" },
];

export const DoctorSidebarOptions: SidebarOption[] = [
  { path: "/dashboard", text: "Dashboard", icon: "table-columns" },
  { path: "/appointments", text: "Appointments", icon: "calendar-check" },
  { path: "/patients", text: "Patients", icon: "hospital-user" },
  { path: "/inventory", text: "Inventory", icon: "cart-flatbed" },
  { path: "/forms", text: "Forms", icon: "highlighter" },
];

export const CustomerSidebarOptions: SidebarOption[] = [
  { path: "/dashboard", text: "Dashboard", icon: "table-columns" },
  { path: "/appointments", text: "Appointments", icon: "calendar-check" },
  { path: "/patients", text: "Patients", icon: "hospital-user" },
  { path: "/inventory", text: "Inventory", icon: "cart-flatbed" },
  { path: "/forms", text: "Forms", icon: "highlighter" },
];
