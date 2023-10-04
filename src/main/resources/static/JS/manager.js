const { createApp } = Vue;

createApp({
  data() {
    return {
      firstName: "",
      lastName: "",
      email: "",
      clientsInformation: [],
    };
  },
  created() {
    axios
      .get("http://localhost:8080/rest/clients/")
      .then((response) => {
        this.clientsInformation = response.data._embedded.clients;
        console.log(response.data);
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
        .post("http://localhost:8080/rest/clients", userData)
        .then(function (response) {
          // Maneja la respuesta después de agregar un usuario
        })
        .catch(function (error) {
          // Maneja los errores si ocurren al agregar un usuario
        });
    },
  },
}).mount("#app");
