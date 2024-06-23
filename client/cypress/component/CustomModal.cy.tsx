
import { mount } from '@cypress/vue';
import CustomModal from '../../src/components/CustomModal.vue';

describe('CustomModal component', () => {
    beforeEach(() => {
        mount(CustomModal, {
            props: {
                show: true,
                title: 'Test Modal',
                button1Text: 'Save',
                button2Text: 'Cancel',
                oneButton: false,
                variant: 'DARK',
            },
        });
    });

    it('renders modal with correct initial props', () => {
        cy.get('.modal-mask').should('be.visible');
        cy.get('.modal-container').should('have.class', 'dark');
        cy.get('.modal-header h1').should('have.text', 'Test Modal');
        cy.get('.modal-button1').should('have.text', 'Save');
        cy.get('.modal-button2').should('have.text', 'Cancel');
    });

    it('emits button1 event on button1 click', () => {
        cy.get('.modal-button1').click();
    });

    it('emits button2 event on button2 click', () => {
        cy.get('.modal-button2').click();
    });

    it('does not render button2 when oneButton prop is true', () => {
        mount(CustomModal, {
            props: {
                show: true,
                title: 'Test Modal',
                button1Text: 'Save',
                oneButton: true,
                variant: 'DARK',
            },
        });

        cy.get('.modal-button1').should('exist');
        cy.get('.modal-button2').should('not.exist');
    });

    it('changes modal style based on variant prop', () => {
        mount(CustomModal, {
            props: {
                show: true,
                title: 'Light Variant Modal',
                button1Text: 'OK',
                button2Text: '',
                oneButton: true,
                variant: 'LIGHT',
            },
        });

        cy.get('.modal-container').should('have.class', 'light');
        cy.get('.modal-header h1').should('have.text', 'Light Variant Modal');
        cy.get('.modal-button1').should('have.text', 'OK');
        cy.get('.modal-button2').should('not.exist');
    });
});
