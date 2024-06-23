import TimeInput from "@/components/TimeInput.vue";
import TimePicker from "@/components/TimePicker.vue";

describe("TimeInput Component Tests", () => {
  it("should toggle the time picker visibility", () => {
    cy.mount(TimeInput, {
      props: {
        label: "Test Time Input",
      },
      global: {
        components: {
          TimePicker,
        },
      },
    });

    cy.get(".time-picker-container").should("not.exist");

    cy.get("input").focus();

    cy.get(".time-picker-container").should("be.visible");

    cy.get("body").click();
  });

  it("should update selected time on time selection", () => {
    cy.mount(TimeInput, {
      props: {
        label: "Test Time Input",
      },
      global: {
        components: {
          TimePicker,
        },
      },
    });

    cy.get("input").focus();
  });

  it("should display error message when error prop is true", () => {
    const errorMessage = "This field is required";

    cy.mount(TimeInput, {
      props: {
        label: "Test Time Input",
        error: true,
        errorMessage,
      },
      global: {
        components: {
          TimePicker,
        },
      },
    });
    cy.get(".error-message").should("contain.text", errorMessage);
  });
});
