const { createApp } = Vue;

createApp({
  data() {
    return {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
    };
  },
  created() {},
  methods: {
    login() {
      axios
        .post("/api/login", `email=${this.email}&password=${this.password}`)
        .then((response) => {
          console.log("Signed in");
          location.pathname = "/web/pages/accounts.html";
        })
        .catch((error) => {
          console.log(error);
        });
    },
    register() {
      axios
        .post(
          "/api/clients",
          `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`
          // { headers: { "content-type": "application/x-www-form-urlencoded" } }
        )
        .then((response) => {
          console.log("User registered");
          axios
            .post("/api/login", `email=${this.email}&password=${this.password}`)
            .then((response) => {
              console.log("Signed in");
              location.pathname = "/web/pages/accounts.html";
            })
            .catch((error) => {
              console.log(error);
            });
        })
        .catch((error) => {
          console.error("Error registering user:", error);
        });
    },
  },
}).mount("#app");
