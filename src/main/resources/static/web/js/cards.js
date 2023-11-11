const { createApp } = Vue;

createApp({
  data() {
    return {
      client: [],
      cards: [],
      creditCards: [],
      debitCards: [],
      localDate: "",
    };
  },
  created() {
    axios
      .get("/api/clients/current")
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
    this.createLocalDate();
  },
  methods: {
    createLocalDate() {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, "0");
      const day = String(today.getDate()).padStart(2, "0");
      this.localDate = `${year}-${month}-${day}`;
    },
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
