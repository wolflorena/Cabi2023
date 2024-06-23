import TableRow from "@/components/TableRow.vue";
import DateAndTimeSpan from "@/components/DateAndTimeSpan.vue";

describe("TableRow", () => {
  it("renders with columns and highlights even rows", () => {
    const columns = ["Column1", "Column2", "Column3"];

    cy.mount(TableRow, {
      props: {
        index: 0,
        columns,
        highlight: "even",
      },
      global: {
        components: {
          DateAndTimeSpan,
        },
      },
    });

    cy.get("td").should("have.length", columns.length + 1);
    cy.get("tr").should("have.class", "highlight");
  });

  it("renders with columns and highlights odd rows", () => {
    const columns = ["Column1", "Column2", "Column3"];

    cy.mount(TableRow, {
      props: {
        index: 1,
        columns,
        highlight: "odd",
      },
      global: {
        components: {
          DateAndTimeSpan,
        },
      },
    });

    cy.get("td").should("have.length", columns.length + 1);
    cy.get("tr").should("have.class", "highlight");
  });

  it("renders with an empty column", () => {
    const columns = ["Column1", "Column2"];

    cy.mount(TableRow, {
      props: {
        index: 0,
        columns,
        hasEmptyColumn: true,
      },
      global: {
        components: {
          DateAndTimeSpan,
        },
      },
    });

    cy.get("td").should("have.length", columns.length + 2);
    cy.get("td.empty-column").should("exist");
  });
});
