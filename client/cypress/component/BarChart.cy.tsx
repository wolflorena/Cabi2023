import BarChart from "../../src/components/BarChart.vue";
import { mount } from "@cypress/vue";
import { AppointmentWeekly } from "../../src/data/types/Entities";

describe("<BarChart />", () => {
    const sampleData: AppointmentWeekly[] = [
        { week: "Week 1", count: 5 },
        { week: "Week 2", count: 8 },
        { week: "Week 3", count: 2 },
        { week: "Week 4", count: 6 },
    ];

    beforeEach(() => {
        mount(BarChart, {
            props: {
                data: sampleData,
            },
        });
    });

    it("renders with no data", () => {
        mount(BarChart, {
            props: {
                data: [],
            },
        });
        cy.get(".chart-container.empty").should("contain.text", "No appointments this month");
    });

    it("renders with data", () => {
        cy.get(".chart-container").should("be.visible");
        cy.get(".bar").should("have.length", sampleData.length);
        sampleData.forEach((item, index) => {
            cy.get(".bar").eq(index).should("contain.text", item.week);
            cy.get(".bar-inner").eq(index).invoke("height").should("be.gt", 0);
        });
    });

    it("updates chart when data prop changes", () => {
        const updatedData: AppointmentWeekly[] = [
            { week: "Week 1", count: 10 },
            { week: "Week 2", count: 15 },
            { week: "Week 3", count: 5 },
            { week: "Week 4", count: 20 },
        ];

        mount(BarChart, {
            props: {
                data: sampleData,
            },
        }).then(({ component }) => {
            component.$emit("update:data", updatedData);

            cy.get(".bar").should("have.length", updatedData.length);
            updatedData.forEach((item, index) => {
                cy.get(".bar").eq(index).should("contain.text", item.week);
                cy.get(".bar-inner").eq(index).invoke("height").should("be.gt", 0);
            });
        });
    });

    it("displays tooltip on bar hover", () => {
        cy.get(".bar").first().trigger("mousemove");
        cy.get(".tooltip").should("contain.text", "5 appointments");

        cy.get(".bar").first().trigger("mouseout");
        cy.get(".tooltip").should("not.be.visible");
    });
});
