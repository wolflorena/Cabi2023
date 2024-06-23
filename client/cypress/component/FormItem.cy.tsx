import FormItem from "@/components/FormItem.vue";

describe("FormItem Component", () => {
  it("should render name and date correctly", () => {
    const name = "John Doe";
    const date = "2024-06-22";

    cy.mount(FormItem, {
      props: {
        name,
        date,
      },
    });

    cy.get("li span:first-child").should("contain.text", name);
    cy.get("li span:last-child").should("contain.text", date);
  });

  it("should handle missing props gracefully", () => {
    cy.mount(FormItem);

    cy.get("li span:first-child").should("have.text", "");
    cy.get("li span:last-child").should("have.text", "");
  });
});
