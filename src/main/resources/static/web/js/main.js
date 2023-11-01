const { createApp } = Vue;

createApp({
  data() {
    return {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      isLoggedIn: false,
      client: [],
    };
  },
  created() {
    this.getClient(); // Es util tener el metodo a la mano, para poder actualizar datos.
    this.isLoggedIn = JSON.parse(localStorage.getItem("login")) || false;
  },
  methods: {
    getClient() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
          this.accounts = response.data.accounts;
          setTimeout(() => (this.loading = false), 800);
        })
        .catch((error) => {
          error;
        });
    },
    login() {
      axios
        .post("/api/login", `email=${this.email}&password=${this.password}`)
        .then((response) => {
          this.isLoggedIn = true;
          localStorage.setItem("login", JSON.stringify(this.isLoggedIn));
          location.pathname = "/web/pages/accounts.html";
          console.log("Signed in");
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
    logOut() {
      axios.post("/api/logout").then((response) => {
        this.isLoggedIn = false;
        localStorage.setItem("login", JSON.parse(this.isLoggedIn));
        console.log("Signed out");
      });
    },
  },
}).mount("#app");
