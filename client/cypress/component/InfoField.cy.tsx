import InfoFieldDefault from "@/components/InfoField.vue";

describe("InfoFieldDefault Component", () => {
  it("should render date input correctly", () => {
    const uuid = "dateInput";
    const label = "Date of Birth";

    cy.mount(InfoFieldDefault, {
      props: {
        uuid,
        label,
        type: "date",
        inputValue: "1990-01-01",
      },
    });

    cy.get(`#${uuid}`).should("have.value", "1990-01-01");
    cy.get(`#${uuid}`).should("have.value", "1990-01-01");
  });

  it("should render phone number input correctly", () => {
    const uuid = "phoneNoInput";
    const label = "Phone Number";

    cy.mount(InfoFieldDefault, {
      props: {
        uuid,
        label,
        type: "phoneNo",
        inputValue: "123-456-7890",
      },
    });
    cy.get(`#${uuid}`).should("have.value", "1234-567-890");
  });

  it("should render text input correctly", () => {
    const uuid = "textInput";
    const label = "Full Name";

    cy.mount(InfoFieldDefault, {
      props: {
        uuid,
        label,
        type: "text",
        inputValue: "John Doe",
      },
    });

    cy.get(`#${uuid}`).should("have.value", "John Doe");
  });

  it("should handle readonly state", () => {
    const uuid = "readonlyInput";
    const label = "Readonly Input";

    cy.mount(InfoFieldDefault, {
      props: {
        uuid,
        label,
        type: "text",
        inputValue: "Readonly Value",
        isReadonly: true,
      },
    });

    cy.get(`#${uuid}`).should("have.attr", "readonly");
    cy.get(`#${uuid}`).should("have.value", "Readonly Value");
  });
});
