const { createApp } = Vue;

createApp({
  data() {
    return {
      account: [],
      transactions: [],
    };
  },
  created() {
    const parameters = location.search;
    const parametersKeyValue = new URLSearchParams(parameters);
    const id = parametersKeyValue.get("id");

    axios
      .get(`http://localhost:8080/api/accounts/${id}`)
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
