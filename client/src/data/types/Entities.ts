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
