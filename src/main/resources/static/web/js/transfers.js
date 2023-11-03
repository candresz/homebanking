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
      if (this.transactionType === "") {
        Swal.fire("Please complete 'Transaction Type'");
      } else if (this.fromAccount === "") {
        Swal.fire("Please complete 'From Account'");
      } else if (this.toAccount === "") {
        Swal.fire("Please complete 'To Account'");
      } else if (this.amount === "") {
        Swal.fire("Please complete 'Amount'");
      } else if (this.amount <= 0) {
        Swal.fire("Please 'Amount must not be zero'");
      } else if (this.description === "") {
        Swal.fire("Please complete 'Description'");
      } else {
        Swal.fire({
          title: "Loan Application Confirmation",
          text: "Do you want to submit the loan application?",
          icon: "question",
          showCancelButton: true,
          confirmButtonText: "Yes",
          cancelButtonText: "No",
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
                console.error("Error:", error);
              });
          }
        });
      }
    },
  },
  computed: {},
}).mount("#app");
