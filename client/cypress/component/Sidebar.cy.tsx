import Sidebar from "@/components/Sidebar.vue";
import { createRouter, createMemoryHistory } from "vue-router";

const routes = [
  { path: "/dashboard", component: { template: "Dashboard" } },
  { path: "/profile", component: { template: "Profile" } },
];

const router = createRouter({
  history: createMemoryHistory(),
  routes,
});

describe("Sidebar Component Tests", () => {
  const options = {
    role: "admin",
    options: [
      { text: "Dashboard", icon: "dashboard", path: "/dashboard" },
      { text: "Profile", icon: "user", path: "/profile" },
    ],
  };

  it("renders the Log out button", () => {
    cy.mount(Sidebar, {
      props: {
        options,
      },
      global: {
        plugins: [router],
      },
    });

    cy.contains("Log out").should("exist");
  });
});
