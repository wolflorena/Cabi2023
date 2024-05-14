import ActionTableButton from "../../src/components/ActionTableButton.vue";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faCoffee } from "@fortawesome/free-solid-svg-icons";

library.add(faCoffee);

describe("<ActionTableButton />", () => {
  it("mounts", () => {
    cy.mount(ActionTableButton, {
      props: {
        iconToken: "coffee",
      },
    });
    cy.get("button").should("be.visible");
  });

  it('emits "actionTriggered" when clicked', () => {
    cy.mount(ActionTableButton, {
      props: {
        iconToken: "coffee",
      },
    });

    cy.get("button").click();
    cy.then(() => {
      const events = Cypress.vueWrapper.emitted("actionTriggered");
      expect(events).to.have.length(1);
    });
  });
});
