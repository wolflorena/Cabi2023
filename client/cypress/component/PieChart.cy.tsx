import PieChart from "@/components/PieChart.vue";
import { TreatmentType } from "@/data/types/Entities";

describe("Pie Chart Component Props Test", () => {
  const treatments: TreatmentType[] = [
    { serviceName: "Service A", count: 10 },
    { serviceName: "Service B", count: 20 },
    { serviceName: "Service C", count: 15 },
  ];
  const colors: string[] = ["#FF5733", "#33FF57", "#5733FF"];

  beforeEach(() => {
    cy.mount(PieChart, {
      props: {
        treatments,
        colors,
      },
    });
  });

  it("renders with default props", () => {
    cy.get(".pie-chart").should("exist");
    cy.get(".tooltip").should("not.be.visible");
  });

  it("displays correct conic gradient based on props", () => {
    cy.get(".pie-chart")
      .should("have.css", "background-image")
      .and("match", /conic-gradient/);
  });
});
