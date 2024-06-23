import CustomButton from "../../src/components/CustomButton.vue";
import { mount } from "@cypress/vue";

describe("<CustomButton />", () => {
    beforeEach(() => {
        mount(CustomButton);
    });

    it("renders with default props", () => {
        cy.get("button")
            .should("have.css", "background-color", "rgb(239, 75, 76)")
            .and("have.css", "height", "2px")
            .and("have.css", "width", "12px")
            .and("have.css", "font-size", "16px")
            .and("have.css", "cursor", "pointer")
            .and("not.have.class", "disabled")
    });

    it("emits 'actionTriggered' event on click", () => {
        const spy = cy.spy();
        mount(CustomButton, {
            emits: {
                actionTriggered: spy,
            },
        });
        cy.get("button").click().then(() => {
            expect(spy).to.be.calledOnce;
        });
    });

    it("applies custom styles when props are provided", () => {
        const customStyles = {
            isMain: false,
            height: "40px",
            width: "120px",
            fontSize: "18px",
            color: "#00bcd4",
            disabled: true,
        };

        mount(CustomButton, { props: customStyles });

        cy.get("button")
            .should("have.css", "background-color", "rgb(0, 188, 212)")
            .and("have.css", "height", "2px")
            .and("have.css", "width", "12px")
            .and("have.css", "font-size", "15px")
            .and("have.css", "cursor", "not-allowed")
            .and("have.class", "disabled");
    });

    it("renders with custom text prop", () => {
        const buttonText = "Click Me";
        mount(CustomButton, {
            props: {
                text: buttonText,
            },
        });
        cy.get("button").contains(buttonText);
    });

    it("applies default color when 'isMain' is false and 'color' prop is not provided", () => {
        mount(CustomButton, {
            props: {
                isMain: false,
            },
        });
        cy.get("button").should("have.css", "background-color", "rgb(67, 80, 108)");
    });

    it("applies hover opacity on mouseover", () => {
        cy.get("button")
            .trigger("mouseover")
            .should("have.css", "opacity", "1");
    });

    it("removes hover opacity on mouseout", () => {
        cy.get("button")
            .trigger("mouseover")
            .should("have.css", "opacity", "1")
            .trigger("mouseout")
            .should("have.css", "opacity", "1");
    });
});
