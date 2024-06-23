
import { mount } from '@cypress/vue';
import CustomInput from '../../src/components/CustomInput.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

describe('CustomInput component', () => {
    beforeEach(() => {
        mount(CustomInput, {
            props: {
                labelText: 'Username',
                type: 'text',
                pattern: undefined,
                iconToken: 'faUser',
                modelValue: '',
            },
            global: {
                components: {
                    FontAwesomeIcon,
                },
            },
        });
    });

    it('does not render the icon when iconToken prop is not provided', () => {
        mount(CustomInput, {
            props: {
                labelText: 'Password',
                type: 'password',
                pattern: undefined,
                iconToken: undefined,
                modelValue: '',
            },
            global: {
                components: {
                    FontAwesomeIcon,
                },
            },
        });

        cy.get('#icon').should('not.exist');
    });


});
