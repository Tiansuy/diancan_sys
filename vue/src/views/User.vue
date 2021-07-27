<template>
  <div class="main">
    <div style="margin: 10px 0">
      <el-input
          style="width: 30vw"
          placeholder="按用户名查询"
          prefix-icon="el-icon-search"
          v-model="search">
      </el-input>
      <el-button @click="load" type="primary" style="margin: 10px 10px;">查询</el-button>
      <el-button @click="addUser" type="primary" style="margin-left: 50px;">新增用户</el-button>
    </div>
    <el-table
        border
        :data="tableData"
        style="width: 80%"
        :row-class-name="tableRowClassName">
      <el-table-column
          prop="user_id"
          label="用户编号"
          sortable>
      </el-table-column>
      <el-table-column
          prop="username"
          label="用户名">
      </el-table-column>
      <el-table-column
          prop="user_type"
          label="用户类型">
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="handleEdit(scope.row)" size="mini" plain>编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="handleDelete(scope.row.user_id)"
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

      <el-dialog :title="dialog_title" v-model="dialogFormVisible">
        <el-form :model="form">
          <el-form-item label="用户名" :label-width="formLabelWidth">
            <el-input v-model="form.username" style="width:50%;" type="text" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="用户类型">
          <el-radio v-model="form.user_type" label="管理员" >管理员</el-radio>
          <el-radio v-model="form.user_type" label="服务员" >服务员</el-radio>
          <el-radio v-model="form.user_type" label="后厨人员" >后厨人员</el-radio>
        </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="commitAddUser">确 定</el-button>
        </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name:"User",
  data(){
    return{
      form:{},
      dialog_title:'',
      dialogFormVisible:false,
      formLabelWidth: '120px',
      search:'',
      pageSize:10,
      pageNum:1,
      totalPage:10,
      tableData:[]
    }
  },
  created(){
    this.load()
  },
  methods:{
    load(){
      request.get("user",{
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

    addUser(){
      this.dialog_title = '新增用户'
      this.dialogFormVisible = true;
      
    },
    commitAddUser(){
      if(this.form.user_id){ // update
        request.put("user/edit-user",this.form).then(res=>{
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
         }
        this.load() // 刷新表格的数据
        })
      }else{  //insert
        request.post("user/add-user",this.form).then(res=>{
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
      }
      this.dialogFormVisible = false;
      this.form = {}
    },

    handleEdit(row){
      // 深拷贝
      this.form = JSON.parse(JSON.stringify(row))
      this.dialog_title = '修改用户信息'
      this.dialogFormVisible = true
    },
    handleDelete(id){
      request.delete("user/"+id).then(res=>{
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