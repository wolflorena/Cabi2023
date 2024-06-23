import DatePicker from "../../src/components/DatePicker.vue";

describe("DatePicker component", () => {
  const currentDate = new Date();

  it("renders correctly with initial date", () => {
    cy.mount(DatePicker);

    cy.get(".header span").should(
      "contain.text",
      currentDate.toLocaleString("default", {
        month: "long",
        year: "numeric",
      })
    );

    const weekdays = ["MO", "TU", "WE", "TH", "FR", "SA", "SU"];
    cy.get(".weekdays .weekday").each((day, index) => {
      cy.wrap(day).should("have.text", weekdays[index]);
    });
  });

  it("navigates to the next month and previous month correctly", () => {
    cy.mount(DatePicker);

    const nextMonth = new Date(
      currentDate.getFullYear(),
      currentDate.getMonth() + 1,
      1
    );
    const prevMonth = new Date(
      currentDate.getFullYear(),
      currentDate.getMonth() - 1,
      1
    );

    cy.get(".header-controls .header-button").eq(1).click();
    cy.get(".header span").should(
      "contain.text",
      nextMonth.toLocaleString("default", {
        month: "long",
        year: "numeric",
      })
    );

    cy.get(".header-controls .header-button").eq(0).click();
    cy.get(".header span").should(
      "contain.text",
      currentDate.toLocaleString("default", {
        month: "long",
        year: "numeric",
      })
    );
    cy.get(".header-controls .header-button").eq(0).click();
    cy.get(".header span").should(
      "contain.text",
      prevMonth.toLocaleString("default", {
        month: "long",
        year: "numeric",
      })
    );
  });
});
