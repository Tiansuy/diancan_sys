<template>
	<div class="main">
		<span class="title">前台订单</span>
		<span v-if="allBills.length==0" style="margin-top: 2vh;color: #00000080;font-size: 18px">暂无订单</span>
		<el-card v-for="(item,i) in allBills" :key="i" class="box-card" shadow="hover">
			<template #header>
				<div class="card-header">
					<span>订单号{{item.bill_id}}</span>
          <el-button @click="save(item)" class="button">结束订单</el-button>
				</div>
			</template>
			<div class="col">
                    <div class="food-name">
						菜品名
					</div>
					<div class="food-num">
						数量
					</div>
					<div class="food-state">
						状态
					</div>
					<div class="food-button">
						操作
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
					<div class="food-state">
						{{food_item.item_state}}
					</div>
					<div v-if="food_item.item_state!='完成烹饪'" class="food-button">
						<el-button @click="changeItemState(food_item)">{{getButtonText(food_item)}}</el-button>
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
	name: "OrderList",
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
      request.get("bill/find-by-state",{params:{search:"未完成"}}).then(res=>{
        console.log(res);
        that.allBills = res.data.records
        for(let i=0;i<that.allBills.length;i++){
          request.get("item",{params:{search:that.allBills[i].bill_id}}).then(item_res=>{
            that.allBills[i]["item_res"]=item_res.data.records
            if(i==that.allBills.length-1){
              console.log(that.allBills)
            }
          })
        }
      })
    },
    save(item){
      this.clearButtonColor()
      for(let i=0;i<item.item_res.length;i++){
        if(item.item_res[i].item_state!="完成烹饪"){
          alert("还有未完成烹饪的菜品")
          return
        }
      }
		this.form.bill_state="已烹饪"
		this.form.bill_id=item.bill_id
		request.put("bill",this.form).then(res=>{
			console.log(res)
			this.load()
			this.form={}
		})
    },
    getButtonText(food_item){
      switch (food_item.item_state){
        case "待烹饪": return "开始制作";
        case "正在烹饪": return "完成制作";
        case "完成烹饪": return "";
      }
    },
    clearButtonColor(){
      let target = event.target;
      if(target.nodeName == "I" || target.nodeName == "SPAN"){
        target = event.target.parentNode;
      }
      target.blur();
    },
    changeItemState(food_item){
      this.clearButtonColor()
      switch (food_item.item_state){
        case "待烹饪": food_item.item_state="正在烹饪";return;
        case "正在烹饪": food_item.item_state="完成烹饪";return;
      }
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
.food-state{
	flex:0.2;
}
.food-button{
	flex: 0.3;
}
</style>