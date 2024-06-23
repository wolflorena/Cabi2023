import DashboardRowPieChart from "../../src/components/DashboardRowPieChart.vue";

describe("DashboardRowPieChart component", () => {
  it("renders correctly with given props", () => {
    const serviceName = "Test Service";
    const color = "#ff5733";

    cy.mount(DashboardRowPieChart, {
      props: {
        serviceName,
        color,
      },
    });

    cy.get(".row").should("be.visible");

    cy.get("#color").should("have.css", "background-color", "rgb(255, 87, 51)");

    cy.get(".text").should("have.text", serviceName);
  });

  it("renders with default props", () => {
    cy.mount(DashboardRowPieChart);

    cy.get(".row").should("be.visible");

    cy.get("#color").should("have.css", "background-color", "rgba(0, 0, 0, 0)");

    cy.get(".text").should("be.empty");
  });
});
