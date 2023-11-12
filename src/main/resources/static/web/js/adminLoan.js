const { createApp } = Vue;

createApp({
  data() {
    return {
      payments: 0,
      maxAmount: 0,
      loanType: "",
      interestRate: 0,
    };
  },
  created() {},

  watch: {},
  methods: {
    logOut() {
      axios.post("/api/logout").then((response) => {
        console.log("Signed out");
        location.pathname = "/web/index.html"; // Redirige al usuario a la página de inicio.
      });
    },
    createLoan() {
      Swal.fire({
        title: "Are you sure you want to create the loan?",
        icon: "warning",
        iconColor: "#fff",
        showCancelButton: true,
        background: "#0056b3",
        confirmButtonColor: "#003f80",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, create loan",
        cancelButtonText: "Cancel",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .post(
              "/api/loans/create",
              `loanType=${this.loanType}&payments=${this.payments}&maxAmount=${this.maxAmount}&interestRate=${this.interestRate}`
            )
            .then((response) => {
              console.log("Loan " + response);
              Swal.fire({
                icon: "success",
                title: "Loan requested",
                text: "Your loan request has been sent successfully.",
                confirmButtonColor: "#003f80",
                background: "#1c2754",
              });
              location.pathname = "web/pages/admin-loan.html";
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
