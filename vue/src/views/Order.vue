<template>
  <div class="main">
    <div style="margin: 10px 0">
      <el-input
          style="width: 30vw"
          placeholder="按发起者查询"
          prefix-icon="el-icon-search"
          v-model="search">
      </el-input>
      <el-button @click="load" type="primary" style="margin: 10px 10px;">查询</el-button>
    </div>
    <el-table
        bbill
        :data="tableData"
        style="width: 90%"
        :row-class-name="tableRowClassName">
      <el-table-column
          prop="bill_id"
          label="订单编号"
          sortable>
      </el-table-column>
      <el-table-column
          prop="uploader"
          label="发起者">
      </el-table-column>
      <el-table-column
          prop="bill_price"
          label="订单总价">
      </el-table-column>
      <el-table-column
          prop="table_number"
          label="订单桌号">
      </el-table-column>
      <el-table-column
          prop="bill_state"
          label="订单状态">
      </el-table-column>
      <el-table-column
          prop="bill_date"
          label="订单日期"
          sortable>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <el-popconfirm
              title="确定删除吗？"
              @confirm="handleDelete(scope.row.bill_id)"
          >
            <template #reference>
              <el-button type="danger" size="mini" plain>删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-left: 25%; margin-top: 20px">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5,10,20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalPage">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name:"Order",
  data(){
    return{
      form:{},
      dialogFormVisible:false,
      formLabelWidth: '120px',
      search:'',
      pageSize:10,
      pageNum:1,
      totalPage:30,
      tableData:[{
        bill_id:1,
        uploader:'aa',
        bill_price:20,
      }]
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      request.get("bill/find-by-uploader",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          search:this.search,
        }
      }).then(res=>{
        console.log(res);
        this.tableData = res.data.records
        this.totalPage = res.data.total
      })
    },
    add_bill(){
      this.form.bill_id = 2
      this.form.bill_price = 20
      this.form.uploader = '吴亦凡'
      request.post("bill/add-bill",this.form).then(res=>{
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "添加成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load() // 刷新表格的数据
      })
    },
    handleDelete(id){
      request.delete("bill/"+id).then(res=>{
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()  // 删除之后重新加载表格的数据
      })
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>

<style scoped>
.main{
  width: calc(100vw - 52px);
  padding: 10px;
}
</style>