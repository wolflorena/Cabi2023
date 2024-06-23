import Avatar from "../../src/components/AvatarImage.vue";

describe("<Avatar />", () => {
    it("mounts with default props", () => {
        cy.mount(Avatar);
        cy.get(".avatar").should("be.visible");
        cy.get("img").should("have.attr", "src", "");
    });

    it("renders with given src and alt attributes", () => {
        const src = "https://via.placeholder.com/180";
        const alt = "Avatar Image";

        cy.mount(Avatar, {
            props: {
                src,
                alt,
            },
        });

        cy.get("img").should("have.attr", "src", src);
    });

    it("has correct styling", () => {
        cy.mount(Avatar);

        cy.get(".avatar").should("have.css", "width", "180px")
            .and("have.css", "height", "180px")
            .and("have.css", "border-radius", "50%")
            .and("have.css", "border", "2px solid rgb(204, 204, 204)")
            .and("have.css", "display", "flex")
            .and("have.css", "align-items", "center")
            .and("have.css", "justify-content", "center")
            .and("have.css", "overflow", "hidden");

        cy.get("img").should("have.css", "width", "180px")
            .and("have.css", "height", "180px")
            .and("have.css", "object-fit", "cover");
    });
});
