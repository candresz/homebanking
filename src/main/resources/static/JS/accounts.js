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
      .get("http://localhost:8080/api/clients/1")
      .then((response) => {
        this.client = response.data;
        this.accounts = response.data.accounts;
      })
      .catch((error) => {
        error;
      });
  },
  methods: {},
  computed: {
    totalBalance() {
      return this.accounts.reduce(
        (total, account) => total + account.balance,
        0
      );
    },
  },
}).mount("#app");
