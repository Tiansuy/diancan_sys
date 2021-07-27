<template>
  <div>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 62px);"
        :default-active="sideMenuIndex"
        class="el-menu-vertical-demo"
        @select="chooseMenu">

      <el-menu-item index="/notice">
        <i class="el-icon-menu"></i>
        <template #title>公告管理</template>
      </el-menu-item>

      <el-menu-item index="/user">
        <i class="el-icon-user-solid"></i>
        <template #title>用户管理</template>
      </el-menu-item>

      <el-menu-item index="/order">
        <i class="el-icon-dish"></i>
        <template #title>餐单管理</template>
      </el-menu-item>
      <el-menu-item index="/food">
        <i class="el-icon-food"></i>
        <template #title>菜品管理</template>
      </el-menu-item>

    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside",
  data(){
    return{
      sideMenuIndex:'/notice',
    }
  },
  methods:{
    chooseMenu(index, indexPath) {
      console.log(index, indexPath);
      this.sideMenuIndex=index
      this.$router.push({ path: index });
    },
  }
}
</script>

<style scoped>

</style>