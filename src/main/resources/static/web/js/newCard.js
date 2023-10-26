const { createApp } = Vue;

createApp({
  data() {
    return {
      client: [],
      cardType: [],
      cardColor: [],
    };
  },
  created() {},
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
    newCard() {
      axios
        .post(
          "/api/clients/currents/cards",
          `cardColor=${this.cardColor}&cardType=${this.cardType}`
        )
        .then((response) => {
          location.pathname = "/web/pages/cards.html";
        })
        .catch((error) => {
          error;
        });
    },
  },
  computed: {},
}).mount("#app");
