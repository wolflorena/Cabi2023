import Pagination from "@/components/Pagination.vue";

describe("Pagination Component Props Test", () => {
  const totalPages = 10;
  const currentPage = 5;

  beforeEach(() => {
    cy.mount(Pagination, {
      props: {
        totalPages,
        currentPage,
      },
    });
  });

  it("renders with default props", () => {
    cy.get(".pagination-container").should("exist");

    cy.get(".page__btn").should("have.length", 2);
    cy.get(".page__numbers").should("have.length", 5);
  });

  it("displays correct pagination based on props", () => {
    if (totalPages > 5) {
      cy.get(".page__dots").should("exist");
    } else {
      cy.get(".page__dots").should("not.exist");
    }
    cy.get(".page__numbers").should(
      "have.length",
      totalPages <= 5 ? totalPages : 5
    );
  });
});
