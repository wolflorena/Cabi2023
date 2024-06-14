import { createApp, h } from "vue";

export function mountComponent(component: any, props: any) {
  const container = document.createElement("div");
  document.body.appendChild(container);

  const app = createApp({
    render() {
      return h(component, {
        ...props,
      });
    },
  });
  app.mount(container);

  return container;
}
