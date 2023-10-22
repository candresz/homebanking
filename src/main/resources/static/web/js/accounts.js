const { createApp } = Vue;

createApp({
  data() {
    return {
      client: [],
      accounts: [],
    };
  },
  created() {
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
  methods: {
    logOut() {
      axios.post("/api/logout").then((response) => {
        console.log("Signed out");
        location.pathname = "/web/index.html"; // Redirige al usuario a la página de inicio.
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
