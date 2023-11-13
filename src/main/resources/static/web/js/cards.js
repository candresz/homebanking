const { createApp } = Vue;

createApp({
  data() {
    return {
      client: [],
      cards: [],
      creditCards: [],
      debitCards: [],
      localDate: "",
      cardId: 0,
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
    deleteCard(cardId) {
      Swal.fire({
        title: "Are you sure you want to delete the card?",
        icon: "warning",
        iconColor: "#fff",
        showCancelButton: true,
        background: "#0056b3",
        confirmButtonColor: "#003f80",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete card",
        cancelButtonText: "Cancel",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .post("/api/clients/current/cards/delete", `id=${cardId}`)
            .then(() => {
              Swal.fire({
                icon: "success",
                title: "Card deleted",
                background: "#0056b3",
                iconColor: "#fff",
                text: "Card deleted successfully.",
                confirmButtonColor: "#003f80",
              }).then(() => {
                location.pathname = "web/pages/cards.html";
              });
            })
            .catch((error) => {
              console.log(error);
              this.errorMessage(error.response.data);
            });
        }
      });
    },
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
