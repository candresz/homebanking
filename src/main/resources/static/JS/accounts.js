const { createApp } = Vue;

createApp({
  data() {
    return {
      client: [],
    };
  },
  created() {
    axios
      .get("http://localhost:8080/api/clients/1")
      .then((response) => {
        this.client = response.data;
        console.log(this.client);
      })
      .catch((error) => {
        console.error(error);
      });
  },
  methods: {},
}).mount("#app");
