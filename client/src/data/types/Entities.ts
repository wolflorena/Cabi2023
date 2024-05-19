export type Doctor = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNo: string;
  address: string;
  dateOfEmployment: string;
  appointments: Appointment[];
};

export type SelectedDoctor = Doctor & { checked: boolean; color: string };

type AppointmentBasic = {
  appointmentId: number;
  date: string;
  time: string;
  status: string;
  doctorId: number;
  customerId: number;
};

export type Appointment = AppointmentBasic & {
  finalDuration: number;
  serviceId: number;
};

export type AppointmentCalendar = AppointmentBasic & {
  finalDuration: number;
  customerFirstName: string;
  customerLastName: string;
  serviceName: string;
};

export type AppointmentAdmin = AppointmentBasic & {
  customerFirstName: string;
  customerLastName: string;
  doctorFirstName: string;
  doctorLastName: string;
  serviceId: number;
};

export type AppointmentDetail = AppointmentBasic & {
  customerFirstName: string;
  customerLastName: string;
  doctorFirstName: string;
  doctorLastName: string;
  serviceName: string;
};

export type PatientAdmin = {
  customerId: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNo: string;
};

export type Patient = {
  customerId: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNo: string;
  occupation: string;
  dateOfBirth: string;
};

export type Service = {
  serviceId: number;
  name: string;
};

export type Product = {
  inventoryId: number;
  product: string;
  quantity: number;
};

export type Form = {
  formId: number;
  title: string;
  description: string;
  editedTime: string;
  editedDate: string;
  visibility: boolean;
};

export type FormEvent = {
  customerFirstName: string;
  customerLastName: string;
  timestamp: string;
};

export type UserDetails = {
  firstName: string;
  secondName: string;
  email: string;
  phoneNo: string;
  dateOfBirth: string;
  occupation: string;
};
