const { createApp } = Vue;

createApp({
  data() {
    return {
      payments: 0,
      amount: 0,
      clientLoans: [],
      clientLoanId: 0,
      accounts: [],
      client: [],
      accountId: 0,
      description: "",
      interestRate: 0,
    };
  },
  created() {
    this.getClient();
    this.getLoans();
  },

  methods: {
    getClient() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
          this.accounts = response.data.accounts;
          this.clientLoans = response.data.loans;
          setTimeout(() => (this.loading = false), 800);
        })
        .catch((error) => {
          error;
        });
    },
    getLoans(id) {
      axios
        .get("/api/loans")
        .then((response) => {
          this.loans = response.data;
          this.interestRate = response.data.interestRate;
          setTimeout(() => (this.loading = false), 800);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    logOut() {
      axios.post("/api/logout").then((response) => {
        console.log("Signed out");
        location.pathname = "/web/index.html"; // Redirige al usuario a la página de inicio.
      });
    },
    payLoan() {
      Swal.fire({
        title: "Are you sure you want to make this payment?",
        icon: "warning",
        iconColor: "#fff",
        showCancelButton: true,
        background: "#0056b3",
        confirmButtonColor: "#003f80",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, make payment",
        cancelButtonText: "Cancel",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .post(
              "/api/loans/payments",
              `clientLoanId=${this.clientLoanId}&accountId=${this.accountId}&amount=${this.amount}&payments=${this.payments}&description=${this.description}`
            )
            .then((response) => {
              Swal.fire({
                icon: "success",
                title: "Payment successfully",
                background: "#0056b3",
                iconColor: "#fff",
                text: "Loan paid successfully.",
                confirmButtonColor: "#003f80",
              }).then(() => {
                location.pathname = "web/pages/accounts.html";
              });
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
