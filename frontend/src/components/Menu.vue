<template>

<v-data-table
    :headers="headers"
    :items="menu"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'Menu',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
        ],
        menu : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/menus')

      this.menu = temp.data._embedded.menus;

    },
    methods: {
    }
  }
</script>

