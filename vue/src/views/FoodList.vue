<template>
    <div>
        <div class="menu">
            <div @click="chooseFoodType(item.food_type_name)" v-for="(item,index) in menuList" :key="index"
                 class="item">
                <div
                        v-if="index==choosed"
                        style="background-color: #77777760; color: #fff;">
                    <a style="color:#fff">
                        {{ item.food_type_name }}
                    </a>
                </div>

                <div v-else @click="chooseMenu(index)">
                    <a style="color:#000">
                        {{ item.food_type_name }}
                    </a>
                </div>
            </div>
        </div>


        <div v-for="(item,i) in tableData" :key="i" class="food-item">
            <el-image
                    class="food-img"
                    @click="showDetail(item)"
                    :src="'http://localhost:9090/static/'+item.food_img"
                    style="height: 137px"
                    fit="fill"></el-image>
            <div class="food-title">
                <span>{{item.food_name}}</span>
            </div>
            <div class="food-price">
                <span>{{'￥'+item.food_price}}</span>
            </div>
            <el-input-number v-model="item.select_num" @change="handleChange(item)" :min="0" :max="10"
                             label="描述文字"></el-input-number>
        </div>
        <div class="total" v-if="selectItems.length>0">
            <div @click="showOrderDetail" class="total-left">
                <span>￥{{getSelectionTotalPraice()}}</span>
            </div>
            <el-dialog title="购物车" v-model="dialogTableVisible" top="20vh" width="40%">
                <div v-for="(item,i) in selectItems" :key="i"
                     style="display:flex; height:42px;border:1px solid #00000020;margin-top:1px;">
                    <span style="flex:0.5;padding-left:15px;height:42px; line-height:42px; font-size:20px;">{{item.food_name}}</span>
                    <span style="flex:0.2;height:42px; line-height:42px;font-size:20px; color:orange;">￥{{item.food_price*item.select_num}}</span>
                    <el-input-number style="flex:0.2;" size="mini" v-model="item.select_num"
                                     @change="handleChange(item)" :min="0" :max="10" label="描述文字"></el-input-number>
                </div>
                <div style="font-size: 20px;color: #00A1D6">
                    总价：￥{{getSelectionTotalPraice()}}
                </div>
            </el-dialog>
            <div @click="chooseTable" class="total-right">
                <span>选好了</span>
                <el-dialog title="选择桌号" v-model="submitDialog" center top="20vh" width="30%">
                    <div style="margin-left:30%;">
                        <el-input-number v-model="table_number" size="medium" :min="1" :max="30"
                                         label="描述文字"></el-input-number>
                    </div>
                    <template #footer>
                        <span class="dialog-footer">
                        <el-button @click="centerDialogVisible = false">取 消</el-button>
                        <el-button style="margin-left:10%" type="primary" @click="save">确 定 下 单</el-button>
                        </span>
                    </template>
                </el-dialog>
            </div>
        </div>
        <el-dialog title="菜品详情" v-model="detailDialog" top="8vh" width="40%">
            <div class="food-detail">
                <div class="detail-name">
                    {{currentItem.food_name}}
                </div>
                <el-image
                        style="width: 450px; height: 380px; border-radius: 7px"
                        :src="'http://localhost:9090/static/'+currentItem.food_img"
                        :fit="fit">
                </el-image>
                <div class="detail-desc">
                    <div class="detail-desc-span">
              <span>
              {{currentItem.food_desc}}
            </span>
                    </div>
                </div>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import request from "@/utils/request";

    export default {
        name: "FoodList",
        data() {
            return {
                form: {},
                formList: [],
                allfood: [
                    {
                        foodname: '包子',
                        foodprice: 20,
                    }
                ],
                menuList: ['炒菜', '干锅', '甜品', '汤菜', '烧烤'],
                choosed: 0,
                dialogTableVisible: false,
                submitDialog: false,
                detailDialog: false,
                table_number: 1,
                list: [1, 2, 3, 4, 5, 6, 7],
                total: 0,
                tableData: [],
                allTableData: [],
                currentItem: {},
                selectItems: [],
            }
        },
        created() {
            this.loadType()
            this.load()
        },
        methods: {
            load() {
                request.get("food", {}).then(res => {
                    console.log(res);
                    this.tableData = res.data.records
                    for (let i = 0; i < this.tableData.length; i++) {
                        this.tableData[i]["select_num"] = 0
                    }
                    this.allTableData = this.tableData
                    this.total = res.data.total
                })
            },
            loadType() {
                request.get("food-type", {}).then(res => {
                    console.log(res);
                    this.menuList = res.data.records
                    this.menuList.push({
                        food_type_name: '全部'
                    })
                    this.choosed = this.menuList.length - 1
                })
            },
            getSelectionTotalPraice() {
                var totalPrice = 0
                for (let i = 0; i < this.selectItems.length; i++) {
                    totalPrice += this.selectItems[i].food_price * this.selectItems[i].select_num
                }
                return totalPrice
            },
            handleChange(item) {
                let exist = false
                for (let i = 0; i < this.selectItems.length; i++) {
                    if (item.food_id === this.selectItems[i].food_id) {
                        exist = true
                        if (this.selectItems[i].select_num === 0) {
                            this.selectItems.splice(i,1)
                            i--
                        }
                    }
                }
                // if(item.select_num===0){
                //     this.selectItems.pop(item)
                // }
                if (!exist) {
                    this.selectItems.push(item)
                }
                if(this.selectItems.length===0){
                    this.dialogTableVisible = false
                }
                console.log(this.selectItems)
            },
            showDetail(item) {
                this.currentItem = item
                this.detailDialog = true
            },
            chooseFoodType(foodType) {
                if (foodType == '全部') {
                    this.tableData = this.allTableData
                    return
                }
                let newFoodList = []
                for (let i = 0; i < this.allTableData.length; i++) {
                    if (this.allTableData[i].food_type == foodType) {
                        newFoodList.push(this.allTableData[i])
                    }
                }
                this.tableData = newFoodList
            },
            // 路由跳转
            chooseMenu(index) {
                console.log(index)
                this.choosed = index
            },
            save() {
                if (this.selectItems.length == 0) {
                    alert('至少选择一项菜品！')
                    this.submitDialog = false
                    return
                }
                let that = this
                let uploader = JSON.parse(sessionStorage.getItem("user")).username
                for (let i = 0; i < that.selectItems.length; i++) {
                    that.form = {
                        food_name: that.selectItems[i].food_name,
                        food_num: that.selectItems[i].select_num,
                        item_price: that.selectItems[i].food_price * that.selectItems[i].select_num
                    }
                    that.formList.push(that.form)
                }
                request.post("item/" + that.table_number + "/" + uploader, this.formList).then(res => {
                    if (res.code === '0') {
                        this.$message({
                            type: "success",
                            message: "添加成功"
                        })
                        that.load()
                        this.selectItems = []
                        this.form = {}
                        this.formList = []
                    } else {
                        this.$message({
                            type: "error",
                            message: res.msg
                        })
                        this.selectItems = []
                        this.form = {}
                        this.formList = []
                    }
                })
                this.table_number = 1
                this.submitDialog = false
            },
            showOrderDetail() {
                this.dialogTableVisible = true
            },
            chooseTable() {
                this.submitDialog = true
            }
        }
    }
</script>

<style scoped>
    .menu {
        display: flex;
        align-content: center;
        padding: 3vh 10px;
    }

    .detail-name {
        font-size: 33px;
        margin-bottom: 2vh;
        color: #00A1D6;
    }

    .detail-desc {
        width: 60%;
        min-height: 12vh;
        font-size: 24px;
        margin-top: 2vh;
        color: #8c939d;
        border: 1px solid #8c939d;
        border-radius: 8px;
    }

    .detail-desc-span {
        padding: 2vh 2vh;
    }

    .food-detail {
        margin-top: -3vh;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }

    .menu .item {
        width: 25%;
        margin-right: 1px;
        border: 1px solid #000;
        height: 30px;
        text-align: center;
        line-height: 30px;
    }

    .menu .item:hover {
        border: 1px solid peachpuff;
        box-shadow: 2px 2px 10px rgb(253, 237, 205), 5px 5px 20px #fff;
    }

    .food-item {
        float: left;
        width: 200px;
        height: 250px;
        z-index: 1;
        margin: 25px 40px;
        /* background-color: #00000060; */
        min-height: 120px;
        display: flex;
        flex-direction: column;
    }

    .food-img {
        border-radius: 10px;
    }

    .food-title {
        /* padding:8px 8px; */
        margin: 8px auto;
        color: #00000090;
        font-weight: 600;
    }

    .food-price {
        margin: 8px auto;
        color: rgb(255, 102, 0);
        font-weight: 600;
    }

    .food-item:hover > .el-image {
        border: 3px solid peachpuff;
        box-shadow: 2px 2px 10px rgb(253, 237, 205), 5px 5px 20px #fff;
    }

    .food-item:hover > .el-image + .food-title {
        color: rgb(0, 161, 214);
    }

    .el-input-number {
        margin: 2px auto;
    }

    .total {
        z-index: 2;
        border-radius: 40px;
        background-color: #12130ae3;
        height: 70px;
        line-height: 70px;
        font-size: 20px;
        color: #ffffff;
        width: 230px;
        /* position:absolute; */
        position: fixed;
        bottom: 5vh;
        margin: 0 calc((74vw - 230px) / 2);
        display: flex;
    }

    .total:hover {
        border: 3px solid peachpuff;
        box-shadow: 2px 2px 10px rgb(253, 237, 205), 5px 5px 20px #fff;
    }

    .total-left {
        flex: 0.6;
    }

    .total-left:hover {
        border: 1px solid wheat;
    }

    .total-left span {
        height: 60px;
        margin-left: 40px;
        /* padding-right: 70px; */
    }

    .total-right {
        flex: 0.4;
        background-color: dodgerblue;
        border-top-right-radius: 40px;
        border-bottom-right-radius: 40px;
    }

    .total-right:hover {
        border: 1px solid wheat;
    }

    .total-right span {
        height: 60px;
        margin: 0 14px;
    }
</style>