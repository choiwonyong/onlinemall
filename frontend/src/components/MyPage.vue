<template>

<v-data-table
    :headers="headers"
    :items="myPage"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'MyPage',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
            { text: "qty", value: "qty" },
            { text: "productId", value: "productId" },
            { text: "productName", value: "productName" },
            { text: "unitPrice", value: "unitPrice" },
            { text: "customerId", value: "customerId" },
            { text: "status", value: "status" },
            { text: "orderId", value: "orderId" },
        ],
        myPage : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/mypages')

      this.myPage = temp.data._embedded.mypages;

    },
    methods: {
    }
  }
</script>

