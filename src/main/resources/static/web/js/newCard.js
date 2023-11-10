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
        .get("/api/clients/current")
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
      Swal.fire({
        title: "Are you sure you want to request the card?",
        icon: "warning",
        iconColor: "#fff",
        background: "#0056b3",
        showCancelButton: true,
        confirmButtonColor: "#003f80",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, request card",
        cancelButtonText: "Cancel",
        customClass: {
          icon: "custom-icon-class", // Clase personalizada para el icono
        },
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .post(
              "/api/clients/current/cards",
              `cardColor=${this.cardColor}&cardType=${this.cardType}`
            )
            .then((response) => {
              location.pathname = "/web/pages/cards.html";
            })
            .catch((error) => {
              this.errorMessage(error.response.data);
            });
        }
      });
    },
    errorMessage(message) {
      Swal.fire({
        icon: "error",
        iconColor: "#fff",
        title: "An error has occurred",
        text: message,
        confirmButtonColor: "#17acc9",
        background: "#0056b3",
      });
    },
    logOut() {
      axios.post("/api/logout").then((response) => {
        console.log("Signed out");
        location.pathname = "/web/index.html"; // Redirige al usuario a la página de inicio.
      });
    },
  },
}).mount("#app");
