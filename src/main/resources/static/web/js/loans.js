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
      interestRate: 0,
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
      this.remainingAmount = this.totalAmount * (this.interesRate / 12);
    },
    loanType(newLoanType) {
      // Cada vez que loanType cambie de tipo de loan(id String), el selectedLoan se va actualizar y va a obtener el loan con relacion al id.
      // El comparador no es estricto ya que el LoanType es un string. Y el newLoanType es el valor de loanType
      this.selectedLoan = this.loans.find((loan) => loan.id == newLoanType);
      this.interestRate = this.selectedLoan.interestRate;
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
    createLoan() {
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
          const loanBody = {
            loanId: this.loanType,
            amount: this.amount,
            payments: this.payments,
            toAccount: this.toAccount,
          };

          axios
            .post("/api/loans", loanBody)
            .then((response) => {
              console.log("Loan " + response);
              Swal.fire({
                icon: "success",
                title: "Loan requested",
                text: "Your loan request has been sent successfully.",
                confirmButtonColor: "#003f80",
                background: "#1c2754",
              });
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
