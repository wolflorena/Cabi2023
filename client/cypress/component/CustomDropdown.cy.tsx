import { mount } from '@cypress/vue';
import CustomDropdown from '../../src/components/CustomDropdown.vue';
import { Doctor } from '@/data/types/Entities';

const doctors: Doctor[] = [
    {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        phoneNo: '123-456-7890',
        address: '123 Main St, City, Country',
        dateOfEmployment: '2020-01-01',
        appointments: [],
    },
    {
        id: 2,
        firstName: 'Jane',
        lastName: 'Smith',
        email: 'jane.smith@example.com',
        phoneNo: '098-765-4321',
        address: '456 Elm St, City, Country',
        dateOfEmployment: '2019-05-15',
        appointments: [],
    },
    {
        id: 3,
        firstName: 'Jim',
        lastName: 'Beam',
        email: 'jim.beam@example.com',
        phoneNo: '555-555-5555',
        address: '789 Oak St, City, Country',
        dateOfEmployment: '2018-08-23',
        appointments: [],
    },
];

describe('<CustomDropdown />', () => {
    it('renders with default props', () => {
        mount(CustomDropdown, {
            props: {
                doctors,
                label: 'Select Doctor',
                placeholder: 'Search doctor...',
            },
        });

        cy.get('.add-calendar label').contains('Select Doctor');
        cy.get('input').should('have.attr', 'placeholder', 'Search doctor...');
    });

    it('toggles dropdown visibility on input click', () => {
        mount(CustomDropdown, {
            props: {
                doctors,
                label: 'Select Doctor',
                placeholder: 'Search doctor...',
            },
        });

        cy.get('input').click();
        cy.get('.dropdown').should('be.visible');
    });

    it('filters doctors based on search query', () => {
        mount(CustomDropdown, {
            props: {
                doctors,
                label: 'Select Doctor',
                placeholder: 'Search doctor...',
            },
        });

        cy.get('input').click();
        cy.get('.dropdown').should('be.visible');
        cy.get('input').type('Jane');
        cy.get('.element').should('have.length', 1).contains('Dr. Jane Smith');
    });

    it('emits select event with doctor details on click', () => {
        const selectSpy = cy.spy();
        mount(CustomDropdown, {
            props: {
                doctors,
                label: 'Select Doctor',
                placeholder: 'Search doctor...',
            },
            emits: {
                select: selectSpy,
            },
        });

        cy.get('input').click();
        cy.get('.dropdown').should('be.visible');
        cy.get('.element').first().click().then(() => {
            expect(selectSpy).to.be.calledWith(doctors[0]);
        });
    });

    it('hides dropdown on doctor selection', () => {
        mount(CustomDropdown, {
            props: {
                doctors,
                label: 'Select Doctor',
                placeholder: 'Search doctor...',
            },
        });

        cy.get('input').click();
        cy.get('.dropdown').should('be.visible');
        cy.get('.element').first().click();
        cy.get('.dropdown').should('not.exist');
    });

    it('does not display dropdown if no matching doctors', () => {
        mount(CustomDropdown, {
            props: {
                doctors,
                label: 'Select Doctor',
                placeholder: 'Search doctor...',
            },
        });

        cy.get('input').click().type('XYZ');
    });
});
