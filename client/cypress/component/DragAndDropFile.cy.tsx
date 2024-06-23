import FileUpload from "@/components/DragAndDropFile.vue";

describe("FileUpload Component Props Test", () => {
  beforeEach(() => {
    cy.mount(FileUpload, {
      props: {
        patientId: 1,
      },
    });
  });

  it("renders with default props", () => {
    cy.get(".drag-drop-area").should("exist");

    cy.get('input[type="file"]').should("have.css", "display", "none");
    cy.get(".loading-spinner").should("not.exist");
  });

  it("allows file upload through drag and drop", () => {
    cy.get(".drag-drop-area")
      .trigger("dragover")
      .trigger("drop", {
        dataTransfer: {
          files: [{ name: "test.pdf", type: "application/pdf" }],
        },
      });

    cy.get(".loading-spinner").should("not.exist");
    cy.get(".file-list .file-card").should("have.length", 8);
  });
});
