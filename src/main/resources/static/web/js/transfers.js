const { createApp } = Vue;

createApp({
  data() {
    return {
      toAccount: "",
      fromAccount: "",
      amount: "",
      transactionType: "",
      description: "",
      accounts: [],
      client: [],
    };
  },
  created() {
    this.getClient(); // Es util tener el metodo a la mano, para poder actualizar datos.
  },
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
    logOut() {
      axios.post("/api/logout").then((response) => {
        console.log("Signed out");
        location.pathname = "/web/index.html"; // Redirige al usuario a la página de inicio.
      });
    },
    createTransfer() {
      Swal.fire({
        title: "Are you sure you want to request the loan?",
        icon: "warning",
        iconColor: "#fff",
        showCancelButton: true,
        background: "#0056b3",
        confirmButtonColor: "#003f80",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, request loan",
        cancelButtonText: "Cancel",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .post(
              `/api/clients/current/transaction?amount=${this.amount}&description=${this.description}&fromAccount=${this.fromAccount}&toAccount=${this.toAccount}`
            )
            .then((response) => {
              this.getClient(); // Una vez creada la transferencia, hacemos una solicitud get nuevamente para obtener los datos actualizados.
              location.pathname = "/web/pages/accounts.html";
            })
            .catch((error) => {
              console.log(error);
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
        color: "#fff",
        background: "#0056b3",
        confirmButtonColor: "#17acc9",
      });
    },
  },
}).mount("#app");
