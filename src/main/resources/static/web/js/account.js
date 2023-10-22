const { createApp } = Vue;

createApp({
  data() {
    return {
      account: [],
      transactions: [],
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
    const id = parametersKeyValue.get("id");

    axios
      .get(`/api/accounts/${id}`)
      .then((response) => {
        this.account = response.data;
        this.transactions = response.data.transactions;
        this.transactions.sort((a, b) => b.id - a.id);
        setTimeout(() => (this.loading = false), 300);
      })
      .catch((error) => {
        console.error(error);
      });
  },
  methods: {},
}).mount("#app");
