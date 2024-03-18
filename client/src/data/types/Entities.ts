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

export type Appointment = {
  id: number;
  date: Date;
  time: Date;
  finalDuration: number;
  status: string;
  customerId: number;
  doctorId: number;
  serviceId: number;
};
