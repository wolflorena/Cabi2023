import MessageDisplay from "@/components/ErrorMessage.vue";

describe("MessageDisplay Component", () => {
  it("should display the correct message passed via props", () => {
    const testMessage = "Hello, Cypress!";

    cy.mount(MessageDisplay, {
      props: {
        message: testMessage,
      },
    });

    cy.get("span").should("have.text", testMessage);
  });
});
