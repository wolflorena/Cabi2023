import CustomCheckbox from "@/components/CustomCheckbox.vue";

describe("CustomCheckbox Component Simple Tests", () => {
  it("should render ", () => {
    cy.mount(CustomCheckbox, {
      props: {
        uuid: "test-uuid",
        modelValue: false,
      },
    });
  });

  it("should update checked state and emit event when clicked", () => {});
});
