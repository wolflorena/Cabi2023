import PasswordInput from "@/components/PasswordInput.vue";

describe("Password Input Component Props Test", () => {
  const labelText = "Password";
  const value = "password123";

  beforeEach(() => {
    cy.mount(PasswordInput, {
      props: {
        labelText,
        value,
      },
      emits: ["password-changed"],
    });
  });

  it("renders with default props", () => {
    cy.get("#password-input").should("exist");
    cy.get("button").should("exist");

    cy.contains("label", labelText).should("exist");
  });
});
