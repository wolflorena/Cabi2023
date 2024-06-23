import DateInput from "../../src/components/DateInput.vue";

describe("DateInput component", () => {
  const label = "Test Label";
  const errorMessage = "Error message";

  it("renders correctly with given props", () => {
    cy.mount(DateInput, {
      props: {
        label,
        error: false,
      },
    });

    cy.get("label").should("have.text", label);
    cy.get("input").should("have.attr", "placeholder", "Select a date");
  });

  it("toggles the date picker visibility on input focus", () => {
    cy.mount(DateInput, {
      props: {
        label,
      },
    });

    cy.get(".date-picker-container").should("not.exist");

    cy.get("input").focus();
    cy.get(".date-picker-container").should("exist");

    cy.get("body").click(0, 0);
  });

  it("displays error message correctly when error prop is set", () => {
    cy.mount(DateInput, {
      props: {
        label,
        error: true,
        errorMessage,
      },
    });

    cy.get(".error-message").should("have.text", errorMessage);
    cy.get("input").should("have.class", "error-border");
  });
});
