import FileCard from "@/components/FileCard.vue";

describe("FileCard Component", () => {
  it("should appear", () => {
    const fileId = 1;
    const fileName = "sample-file.pdf";

    cy.mount(FileCard, {
      props: {
        fileId,
        fileName,
      },
    });

    cy.window().then((win) => {
      cy.stub(win, "open").as("openStub");
    });
  });
});
