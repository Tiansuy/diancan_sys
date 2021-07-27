<template>
  <div class="main">
    <span class="title">当前订单</span>
    <span v-if="allBills.length==0" style="margin-top: 2vh;color: #00000080;font-size: 18px">暂无订单</span>
    <el-card v-for="(item,i) in allBills" :key="i" class="box-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>订单号{{item.bill_id}}</span>
          <el-button @click="save(item)" class="button">结账</el-button>
        </div>
      </template>
      <div class="col">
        <div class="food-name">
          菜品名
        </div>
        <div class="food-num">
          数量
        </div>
        <div class="food-price">
          价格
        </div>
      </div>
      <div v-for="(food_item,o) in item.item_res" :key="o" class="text item">
        <div class="col">
          <div class="food-name">
            {{food_item.food_name}}
          </div>
          <div class="food-num">
            x{{food_item.food_num}}
          </div>
          <div class="food-price">
            ￥{{food_item.item_price}}
          </div>
        </div>
      </div>
    </el-card>
    <div style="height: 100px">

    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "ManageOrder",
  data() {
    return {
      form:{},
      tableData:[],
      allBills:[],
      total:0,
    };
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      let that=this
      request.get("bill/find-unpay",{params:{search:"已结账"}}).then(res=>{
        console.log(res);
        that.allBills = res.data.records
        for(let i=0;i<that.allBills.length;i++){
          request.get("item",{params:{search:that.allBills[i].bill_id}}).then(item_res=>{
            that.allBills[i]["item_res"]=item_res.data.records
            if(i===that.allBills.length-1){
              console.log(that.allBills)
            }
          })
        }
      })
    },
    save(item){
      this.clearButtonColor()
      this.form.bill_state="已结账"
      this.form.bill_id=item.bill_id
      request.put("bill",this.form).then(res=>{
        console.log(res)
        this.form={}
        this.load()
      })
    },
    clearButtonColor(){
      let target = event.target;
      if(target.nodeName == "I" || target.nodeName == "SPAN"){
        target = event.target.parentNode;
      }
      target.blur();
    },
  }
};
</script>

<style scoped>
.main{
  display: flex;
  flex-direction: column;
  align-items: center;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title{
  margin-top: 20px;
  margin-left: 0;
  height: 50px;
  width: 30vw;
  border-bottom: 1px solid #00000030;
  font-size: 30px;
  color: #00000090;
}

.box-card {
  margin-top: 2vw;
  width: 35vw;
}

.item{

}

.col{
  height: 40px;
  line-height: 40px;
  font-size: 15px;
  color: #00000080;
  border-bottom: 1px solid #00000020;
  display: flex;
}

.food-name{
  flex: 0.4;
}
.food-num {
  flex: 0.1;
}
.food-price{
  flex:0.2;
}
</style>