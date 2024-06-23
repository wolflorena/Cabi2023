
import { mount } from '@cypress/vue';
import CustomInput from '../../src/components/CustomInput.vue';

describe('CustomInput component', () => {
    beforeEach(() => {
        mount(CustomInput, {
            props: {
                labelText: 'Username',
                type: 'text',
                iconToken: 'fa-user',
                modelValue: '',
            },
        });
    });

    it('renders with correct initial props and structure', () => {
        cy.get('.input-container').should('be.visible');
        cy.get('.input-label').should('have.text', 'Username');
        cy.get('.input-input')
            .should('have.attr', 'type', 'text')
            .should('have.value', '');

        cy.get('.input-box').within(() => {
            cy.get('font-awesome-icon').should('exist');
        });
    });

    it('updates modelValue on input', () => {
        const inputText = 'JohnDoe123';
        cy.get('.input-input').type(inputText).should('have.value', inputText);
    });

});
