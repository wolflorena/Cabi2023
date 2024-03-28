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
