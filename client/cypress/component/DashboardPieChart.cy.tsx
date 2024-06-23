import TreatmentTypes from "../../src/components/DashboardPieChart.vue";
import { TreatmentType } from "../../src/data/types/Entities";

const mockTreatments: TreatmentType[] = [
  { serviceName: "Treatment A", count: 10 },
  { serviceName: "Treatment B", count: 20 },
  { serviceName: "Treatment C", count: 30 },
];

describe("TreatmentTypes component", () => {
  it("renders correctly with treatments", () => {
    cy.mount(TreatmentTypes, {
      props: {
        treatments: mockTreatments,
      },
    });

    cy.get(".container").should("be.visible");
    cy.get("h1").should("have.text", "Treatment Types");
  });
});
