const { createApp } = Vue;

createApp({
  data() {
    return {
      client: [],
      cards: [],
      creditCards: [],
      debitCards: [],
    };
  },
  created() {
    axios
      .get("http://localhost:8080/api/clients/1")
      .then((response) => {
        this.client = response.data;
        this.cards = response.data.cards;
        this.creditCards = this.cards.filter(
          (card) => card.cardType == "CREDIT"
        );
        this.debitCards = this.cards.filter((card) => card.cardType == "DEBIT");
        setTimeout(() => (this.loading = false), 800);
        console.log(this.debitCards);
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
