import LoadingSpinner from "@/components/LoadingSpinner.vue";

describe("LoadingSpinner Component", () => {
  it("renders with default color", () => {
    cy.mount(LoadingSpinner);

    cy.get(".loader").should("exist");
    cy.get(".loader").should(
      "have.css",
      "border-top-color",
      "rgb(255, 255, 255)"
    );
  });

  it("renders with custom color", () => {
    const customColor = "#00ff00";
    cy.mount(LoadingSpinner, {
      propsData: {
        color: customColor,
      },
    });
    cy.get(".loader").should("exist");
    cy.get(".loader").should(
      "have.css",
      "border-top-color",
      "rgb(255, 255, 255)"
    );
  });
});
