<template>
  <div>
    <!--    头部-->
    <Header />

    <!--    主体-->
    <div style="display: flex">
        <div style="flex: 0.1"></div>
      <!--      内容区域-->
      <router-view style="flex: 0.8"/>
      <div style="flex: 0.1"></div>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "Layout",
  components: {
    Header
  },
  data(){
    return{
    }
  }
}
</script>

<style scoped>

</style>