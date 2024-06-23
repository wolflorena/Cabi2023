import Add from "@/components/InputDropdown.vue";

describe("Add Component", () => {
  const array = ["Option 1", "Option 2", "Option 3"];
  const label = "Select Options";
  const placeholder = "Select an option";

  it("renders with  selection", () => {
    cy.mount(Add, {
      props: {
        array,
        label,
        placeholder,
        multiple: false,
      },
    });

    cy.get("input").click();

    cy.get(".dropdown").should("be.visible");
  });
});
