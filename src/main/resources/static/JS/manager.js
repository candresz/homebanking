const { createApp } = Vue;

createApp({
  data() {
    return {
      firstName: "",
      lastName: "",
      email: "",
      clientsInformation: [],
      userData: [],
    };
  },
  created() {
    fetch("http://localhost:8080/clients")
      .then((res) => res.json())
      .then((data) => {
        this.clientsInformation = data._embedded.clients;
        console.log(data);
      })
      .catch((error) => console.log(error));
  },
  methods: {
    addUser() {
      const userData = {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
      };

      axios
        .post("http://localhost:8080/clients", userData)
        .then(function (response) {})
        .catch(function (error) {});
    },
  },
}).mount("#app");
