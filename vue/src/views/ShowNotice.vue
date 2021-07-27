<template>
    <div class="main">
        <div class="title"><span>公告中心</span></div>
        <span v-if="tableData.length==0" style="margin-top: 2vh;color: #00000080;font-size: 18px">暂无公告</span>
        <OrderDetailCard v-for="(item,index) in tableData" :key="index" :dataList="item" class="card" />
    </div>
</template>

<script>
import OrderDetailCard from '@/components/OrderDetailCard'
import request from "@/utils/request";

export default {
    name:"ShowNotice",
    components:{
        OrderDetailCard
    },
    data(){
        return{
          tableData:[],
          total:0,
        }
    },
  created() {
      this.load()
  },
  methods:{
        load(){
          request.get("notice/find-by-state",{params:{search:"正在公示"}}).then(res=>{
            console.log(res);
            this.tableData = res.data.records
            this.total = res.data.total
          })
        },
    }
}
</script>

<style scoped>
.main{
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
.card{
    margin-top:30px;
}
.title{
    text-align: center;
    margin: 60px 0;
    font-size: 30px;
    font-weight: 400;
    color: #00A1D6;
}
</style>