import AppointmentCalendar from "@/components/DayScheduler.vue";

describe("Appointment Calendar Component", () => {
  const selectedCalendars = [
    {
      id: 1,
      checked: true,
      firstName: "Dr.",
      lastName: "Smith",
      color: "#ff0000",
    },
    {
      id: 2,
      checked: true,
      firstName: "Dr.",
      lastName: "Jones",
      color: "#00ff00",
    },
  ];
  const daySelected = new Date();

  it("should display the current day correctly", () => {
    const currentDay = new Date().toLocaleDateString("en-US", {
      weekday: "long",
      month: "long",
      day: "numeric",
    });
    cy.get(".current-date").contains(currentDay);
  });

  it("should navigate to the next day correctly", () => {
    cy.get(".header-button").contains("chevron-right").click();
    cy.get(".current-date")
      .invoke("text")
      .should("match", /^\w+, \w+ \d+\w{2}$/);
  });

  it("should navigate to the previous day correctly", () => {
    cy.get(".header-button").contains("chevron-left").click();
    cy.get(".current-date")
      .invoke("text")
      .should("match", /^\w+, \w+ \d+\w{2}$/);
  });

  it("should display appointments correctly", () => {
    const appointments = [
      {
        appointmentId: 1,
        time: "09:00",
        endTime: "09:30",
        serviceName: "Consultation",
        customerFirstName: "John",
        customerLastName: "Doe",
        date: new Date().toISOString().split("T")[0],
        doctorId: 1,
      },
    ];
    cy.intercept("GET", "/appointments", { body: appointments }).as(
      "getAppointments"
    );

    cy.wait("@getAppointments");

    cy.get(".appointment").should("have.length", 1);
    cy.get(".appointment").contains("Consultation");
    cy.get(".appointment").contains("Patient: John Doe");
  });

  it("should display vacations correctly", () => {
    const vacations = [
      {
        doctorId: 1,
        reason: "Holiday",
        startDate: new Date().toISOString().split("T")[0],
        endDate: new Date().toISOString().split("T")[0],
        startTime: "10:00",
        endTime: "12:00",
      },
    ];
    cy.intercept("GET", "/vacations", { body: vacations }).as("getVacations");

    cy.wait("@getVacations");

    cy.get(".vacation").should("have.length", 1);
    cy.get(".vacation").contains("Holiday");
  });

  it("should toggle calendar view", () => {
    cy.get(".calendar-pick").click();
    cy.get("@emitted").should("have.been.calledWith", "toggle-calendar");
  });
});
