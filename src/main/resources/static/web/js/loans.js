const { createApp } = Vue;

createApp({
  data() {
    return {
      toAccount: "",
      payments: [],
      amount: "",
      loans: [],
      loanType: "",
      accounts: [],
      client: [],
      paymentsTableArray: [],
      remainingAmount: 0,
      selectedLoan: null,
    };
  },
  created() {
    this.getClient();
    this.getLoans();
  },
  watch: {
    payments: function (newPayments) {
      // Aquí actualiza paymentsTableArray cuando payments cambie
      const paymentsLength = parseInt(newPayments);
      this.paymentsTableArray = Array.from(
        { length: paymentsLength },
        (_, index) => index + 1
      );
      this.remainingAmount = this.totalAmount * 1.2;
    },
    loanType(newLoanType) {
      this.selectedLoan = this.loans.find((loan) => loan.id == newLoanType);
      console.log(this.selectedLoan);
    },
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
    getLoans() {
      axios
        .get("/api/loans")
        .then((response) => {
          this.loans = response.data;

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
    createLoan() {
      if (this.loanType === "") {
        Swal.fire("Please complete 'Loan Type'");
      } else if (this.amount === "") {
        Swal.fire("Please complete 'Amount'");
      } else if (this.payments === "") {
        Swal.fire("Please complete 'Payments'");
      } else if (this.toAccount === "") {
        Swal.fire("Please complete 'To Account'");
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
            // El usuario eligió enviar la solicitud de préstamo
            const newLoan = {
              loanId: this.loanType,
              amount: this.amount,
              payments: this.payments,
              toAccount: this.toAccount,
            };
            axios
              .post("/api/loans", newLoan)
              .then((response) => {
                this.getClient();
                location.pathname = "/web/pages/accounts.html";
              })
              .catch((error) => {
                console.log("Error registering loan:", error);
              });
          } else if (result.isDismissed) {
            // El usuario eligio no enviar la solicitud de prestamo
          }
        });
      }
    },
  },
  computed: {},
}).mount("#app");
