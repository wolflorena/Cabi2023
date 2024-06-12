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
export type HistoryAppointmentCalendar = AppointmentBasic & {
  doctorFirstName: string;
  doctorLastName: string;
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

export type Patient = PatientAdmin & {
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

export type FormWithStatus = Form & {
  formEventType: string;
};

export type FormEvent = {
  customerFirstName: string;
  customerLastName: string;
  timestamp: string;
};

export type UserDetails = {
  firstName: string;
  lastName: string;
  email: string;
  phoneNo: string;
  dateOfBirth: string;
  occupation: string;
};

export type RegisterCustomerBody = {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNo: string;
  dateOfBirth: string;
};

export type TreatmentType = {
  serviceName: string;
  count: number;
};

export type jwtPayload = {
  user_id: number;
  role: string;
  sub: string;
  exp: number;
};

export type ChangePasswordBody = {
  currentPassword: string;
  newPassword: string;
};

export type AppointmentWeekly = {
  week: string;
  count: number;
};

export type Vacation = HasDoctorIdAndTime & {
  startDate: string;
  endDate: string;
  startTime: string;
  endTime: string;
  reason: string;
};

type HasDoctorIdAndTime = {
  doctorId: number;
};
