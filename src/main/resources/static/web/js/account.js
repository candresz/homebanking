const { createApp } = Vue;

createApp({
  data() {
    return {
      account: [],
      transactions: [],
      id: "",
      balance: 0,
    };
  },
  methods: {
    logOut() {
      axios.post("/api/logout").then((response) => {
        console.log("Signed out");
        location.pathname = "/web/index.html"; // Redirige al usuario a la página de inicio.
      });
    },
  },
  created() {
    const parameters = location.search;
    const parametersKeyValue = new URLSearchParams(parameters);
    this.id = parametersKeyValue.get("id");

    axios
      .get(`/api/clients/current/accounts`)
      .then(({ data }) => {
        this.account = data.find((account) => account.id == this.id);
        this.transactions = this.account.transactions;
        this.transactions.sort((a, b) => b.id - a.id);
        setTimeout(() => (this.loading = false), 300);
      })
      .catch((error) => {
        console.error(error);
      });
  },
  methods: {},
}).mount("#app");
