import CustomCheckbox from "../../src/components/CustomCheckbox.vue";

describe("<CustomCheckbox />", () => {
  it("renders with default props", () => {
    cy.mount(CustomCheckbox, {
      props: {
        uuid: "checkbox1",
      },
    });

    cy.get(".cbx").should("exist");
    cy.get(".inp-cbx").should("exist").and("not.be.checked");
    cy.get("input").should("have.attr", "id", "checkbox1");
    cy.get("label").should("have.css", "color", "rgb(0, 0, 0)");
    cy.get("span:first-child").should(
      "have.css",
      "border-color",
      "rgb(125, 134, 154)"
    );
  });
});
