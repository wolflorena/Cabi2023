import SidebarButton from "@/components/SidebarButton.vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

describe("SidebarButton Component Tests", () => {
  it("renders with given props", () => {
    cy.mount(SidebarButton, {
      props: {
        text: "Dashboard",
        selected: true,
        icon: "home",
      },
      global: {
        components: {
          FontAwesomeIcon,
        },
      },
    });

    cy.get("button").should("have.class", "selected");
    cy.get("span").contains("Dashboard").should("exist");
  });

  it("emits selectionChanged event on click", () => {
    const onSelectionChanged = cy.spy().as("selectionChangedSpy");

    cy.mount(SidebarButton, {
      props: {
        text: "Dashboard",
        selected: false,
        icon: "home",
      },
      global: {
        components: {
          FontAwesomeIcon,
        },
      },
      listeners: {
        selectionChanged: onSelectionChanged,
      },
    });

    cy.get("button").click();
  });
});
