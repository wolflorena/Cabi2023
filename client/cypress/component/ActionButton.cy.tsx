import ActionButton from "../../src/components/ActionButton.vue";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faCoffee } from "@fortawesome/free-solid-svg-icons";

library.add(faCoffee);

describe("<ActionButton />", () => {
  it("mounts", () => {
    cy.mount(ActionButton, {
      props: {
        iconToken: "coffee",
      },
    });
    cy.get("button").should("be.visible");
  });

  it('emits "actionTriggered" when clicked', () => {
    cy.mount(ActionButton, {
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
