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
          if (this.email == "" || this.password == "") {
            Swal.fire("Please complete all the required fields");
          } else {
            {
              Swal.fire("User not registered");
            }
          }
        });
    },
    register() {
      axios
        .post(
          "/api/clients",
          `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`
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
          console.log("Error registering user:", error);
          if (this.firstName == "") {
            Swal.fire("Please complete your name");
          }
          if (this.lastName == "") {
            Swal.fire("Please complete your last name");
          }
          if (this.email == "") {
            Swal.fire("Please complete your email");
          }
          if (this.password == "") {
            Swal.fire("Please complete your password");
          }
        });
    },
  },
}).mount("#app");
