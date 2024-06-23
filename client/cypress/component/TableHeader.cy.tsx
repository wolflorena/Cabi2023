import TheTableHeader from "@/components/TableHeader.vue";

describe("TheTableHeader Component Tests", () => {
  it("renders with given columns and props", () => {
    const columns = ["Name", "Price", "Quantity"];

    cy.mount(TheTableHeader, {
      props: {
        columns,
        isMain: true,
        hasEmptyRow: true,
        hasActionButton: true,
      },
    });

    cy.get("th").should("have.length", columns.length + 2);
    cy.get("tr").first().should("have.class", "isMain");
    cy.get("button").contains("Add new product").should("exist");
  });

  it("renders without empty row and action button", () => {
    const columns = ["Name", "Price", "Quantity"];

    cy.mount(TheTableHeader, {
      props: {
        columns,
        isMain: false,
        hasEmptyRow: false,
        hasActionButton: false,
      },
    });

    cy.get("th").should("have.length", columns.length + 1);
    cy.get("tr").first().should("not.have.class", "isMain");
    cy.get("button").should("not.exist");
    cy.get("tr").should("have.length", 1);
  });

  it("emits actionTriggered event on button click", () => {
    const columns = ["Name", "Price", "Quantity"];
    const onActionTriggered = cy.spy().as("actionTriggeredSpy");

    cy.mount(TheTableHeader, {
      props: {
        columns,
        hasActionButton: true,
      },
      listeners: {
        actionTriggered: onActionTriggered,
      },
    });

    cy.get("button").click();
  });
});
