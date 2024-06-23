import FormDetails from "@/components/FormDetails.vue";

describe("FormDetails Component", () => {
  const patientsList = [
    {
      customerFirstName: "John",
      customerLastName: "Doe",
      timestamp: "2024-06-20T14:30:00Z",
    },
    {
      customerFirstName: "Jane",
      customerLastName: "Smith",
      timestamp: "2024-06-21T10:00:00Z",
    },
  ];

  it("should toggle patients list visibility correctly", () => {
    cy.mount(FormDetails, {
      props: {
        label: "Patients",
        total: patientsList.length,
        patientsList,
      },
    });

    cy.get(".detail-shortcut button").click();
    cy.get(".patients-list").should("be.visible");

    cy.get(".detail-shortcut button").click();
  });

  it("should render patient list items correctly", () => {
    cy.mount(FormDetails, {
      props: {
        label: "Patients",
        total: patientsList.length,
        patientsList,
      },
    });

    cy.get(".detail-shortcut button").click();
    patientsList.forEach((patient, index) => {
      cy.get(".patients-list ul li")
        .eq(index)
        .contains(patient.customerFirstName);
      cy.get(".patients-list ul li")
        .eq(index)
        .contains(patient.customerLastName);
    });
  });
});
