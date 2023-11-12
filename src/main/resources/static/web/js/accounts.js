const { createApp } = Vue;

createApp({
  data() {
    return {
      client: [],
      accounts: [],
      accountType: "",
    };
  },
  created() {
    this.getClient(); // Es util tener el metodo a la mano, para poder actualizar datos.
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
    logOut() {
      axios.post("/api/logout").then((response) => {
        console.log("Signed out");
        location.pathname = "/web/index.html"; // Redirige al usuario a la página de inicio.
      });
    },
    createAccount() {
      Swal.fire({
        title: "Are you sure you want to request a new account?",
        icon: "warning",
        iconColor: "#fff",
        background: "#0056b3",
        showCancelButton: true,
        confirmButtonColor: "#003f80",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, request account",
        cancelButtonText: "Cancel",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .post(
              "/api/clients/current/accounts",
              `accountType=${this.accountType}`
            )
            .then((response) => {
              this.getClient(); // Una vez creada la cuenta, hacemos una solicitud get nuevamente para obtener los datos actualizados.
              location.pathname = "/web/pages/accounts.html";
            })
            .catch((error) => {
              console.error("Error:", error);
              this.errorMessage(error.response.data);
            });
        }
      });
    },
    errorMessage(message) {
      Swal.fire({
        icon: "error",
        iconColor: "#fff",
        title: "An error has occurred",
        text: message,
        confirmButtonColor: "#17acc9",
        background: "#0056b3",
      });
    },
  },
  computed: {
    totalBalance() {
      return this.accounts.reduce(
        (total, account) => total + account.balance,
        0
      );
    },
  },
}).mount("#app");
