import DateDisplay from "../../src/components/DateAndTimeSpan.vue";
import { formatTime, formatDateForTable } from "../../src/utils/helpers";

describe("DateDisplay component", () => {
  it("renders correctly with given props", () => {
    const time = "12:34:56";
    const date = "2024-06-18";
    const formattedDate = formatDateForTable(date);

    cy.mount(DateDisplay, {
      props: {
        time,
        date,
      },
    });

    cy.get("#date").should("have.text", formattedDate);
  });

  it("renders with default props", () => {
    cy.mount(DateDisplay);

    cy.get("#time").should("be.empty");
    cy.get("#date").should("be.empty");
  });
});
