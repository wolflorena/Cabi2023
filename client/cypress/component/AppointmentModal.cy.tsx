import AddOrUpdateAppointmentModal from "../../src/components/AddOrUpdateAppointmentModal.vue";
import CustomModal from "../../src/components/CustomModal.vue";
import LoadingSpinner from "../../src/components/LoadingSpinner.vue";
import { createTestingPinia } from '@pinia/testing';

describe("<AddOrUpdateAppointmentModal />", () => {
    beforeEach(() => {
        cy.mount(AddOrUpdateAppointmentModal, {
            global: {
                components: {
                    CustomModal,
                    LoadingSpinner,
                },
                plugins: [createTestingPinia({ createSpy: cy.spy })],
            },
            props: {
                visible: true,
            },
            data() {
                return {
                    doctors: [
                        { id: 1, firstName: "Ionel", lastName: "Popescu" },
                        { id: 2, firstName: "Doctor", lastName: "2" }
                    ],
                    services: [
                        { serviceId: 1, name: "Detartraj" },
                        { serviceId: 2, name: "Service 2" }
                    ],
                    patients: [
                        { customerId: 1, firstName: "Alice", lastName: "Johnson" },
                        { customerId: 2, firstName: "Bob", lastName: "Brown" }
                    ],
                    availableDates: ["2024-06-16", "2024-06-17"],
                    availableHours: ["10:00 AM", "11:00 AM"]
                };
            },
        });
    });

    it("mounts", () => {
        cy.get("div.selection").should("be.visible");
    });

    it('emits "close" when closeModal is called', () => {
        cy.get('button').contains('Cancel').click();
        cy.wrap(Cypress.vueWrapper).then((wrapper) => {
            expect(wrapper.emitted('close')).to.have.length(1);
        });
    });

    it('displays doctors, services, and patients', () => {
        cy.get('select').eq(0).find('option').should('have.length', 1);
        cy.get('select').eq(1).find('option').should('have.length', 9);
        cy.get('select').eq(4).find('option').should('have.length', 1);
    });


});
