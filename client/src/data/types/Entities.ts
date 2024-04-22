export type Doctor = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNo: string;
  address: string;
  dateOfEmployment: Date;
  appointments: Appointment[];
};

export type SelectedDoctor = Doctor & { checked: boolean; color: string };

export type Appointment = {
  id: number;
  date: string;
  time: string;
  finalDuration: number;
  status: string;
  customerId: number;
  doctorId: number;
  serviceId: number;
};

export type AppointmentCalendar = {
  id: number;
  date: string;
  time: string;
  finalDuration: number;
  status: string;
  customerFirstName: string;
  customerLastName: string;
  doctorId: number;
  serviceName: string;
};

export type AppointmentAdmin = {
  appointmentId: number;
  date: string;
  time: string;
  status: string;
  customerFirstName: string;
  customerLastName: string;
  doctorId: number;
  doctorFirstName: string;
  doctorLastName: string;
  serviceId: number;
};
