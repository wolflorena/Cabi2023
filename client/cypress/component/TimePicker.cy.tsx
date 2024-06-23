import TimePicker from "@/components/TimePicker.vue";

describe("TimePicker Component Test", () => {
  it("should render hours and minutes options", () => {
    cy.mount(TimePicker, {
      props: {
        initialHour: "09",
        initialMinute: "00",
      },
    });

    cy.get("select")
      .first()
      .within(() => {
        cy.get("option").should("have.length", 10);
        cy.get("option").eq(1).should("have.text", "09");
      });

    cy.get("select")
      .last()
      .within(() => {
        cy.get("option").should("have.length", 3);
        cy.get("option").eq(1).should("have.text", "00");
      });
  });
});
