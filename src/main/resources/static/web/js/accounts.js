const { createApp } = Vue;

createApp({
  data() {
    return {
      client: [],
      accounts: [],
    };
  },
  created() {
    this.getClient();
  },
  methods: {
    getClient() {
      axios
        .get("/api/clients/currents")
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
      axios
        .post("/api/clients/current/accounts")
        .then((response) => {
          this.getClient();
        })
        .catch((error) => {
          console.error("Error:", error);
          if (error.response.status === 403) {
            Swal.fire({
              icon: "error",
              title: "Oops...",
              text: "You already have the maximum amount of accounts (3)",
              color: "#fff",
            });
          }
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
