<template>
  <div class="main">
    <div style="margin: 10px 0">
      <el-input
          style="width: 30vw"
          placeholder="按上传者查询"
          prefix-icon="el-icon-search"
          v-model="search">
      </el-input>
      <el-button @click="load" type="primary" style="margin: 10px 10px;">查询</el-button>
      <el-button @click="addNotice" type="primary" style="margin-left: 50px;">发布公告</el-button>
    </div>
    <el-table
        border
        :data="tableData"
        style="width: 80%"
        :row-class-name="tableRowClassName">
      <el-table-column
          prop="notice_id"
          label="公告编号"
          sortable>
      </el-table-column>
      <el-table-column
          prop="uploader"
          label="上传者">
      </el-table-column>
      <el-table-column
          prop="notice_content"
          label="公告内容">
      </el-table-column>
      <el-table-column
          prop="notice_date"
          label="发布时间">
      </el-table-column>
      <el-table-column
          prop="notice_state"
          label="公告状态">
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="handleEdit(scope.row)" size="mini" plain>编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="handleDelete(scope.row.notice_id)"
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

      <el-dialog title="发布公告" v-model="dialogFormVisible">
        <el-form :model="form">
          <el-form-item label="公告内容" :label-width="formLabelWidth">
            <el-input v-model="form.notice_content" type="textarea" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="公告状态" :label-width="formLabelWidth">
            <el-switch
                style="display: block"
                v-model="form.notice_state"
                active-color="#13ce66"
                inactive-color="#00000030"
                active-value="正在公示"
                inactive-value="停止公示"
                active-text="开启公示"
                inactive-text="结束公示"
            >
            </el-switch>
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="commitAddNotice">确 定</el-button>
        </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import FDate from "@/utils/dateFormat";

export default {
  data(){
    return{
      form:{
        notice_date:'',
        notice_content:'',
        uploader:'',
        notice_state:''
      },
      dialogFormVisible:false,
      formLabelWidth: '120px',
      search:'',
      pageNum:1,
      pageSize:10,
      totalPage:10,
      tableData:[]
    }
  },
  created() {
    this.load()
  },

  methods:{
    load(){
      request.get("/notice/find-by-uploader",{
        params:{
          pageNumber:this.currentPage,
          pageSize:this.pageSize,
          search:this.search
        }
      }).then(res=>{
        console.log(res)
        this.tableData = res.data.records
      })
    },
    addNotice(){
      this.dialogFormVisible = true;
    },
    commitAddNotice(){
      this.dialogFormVisible = false;
      // server操作
      if(this.form.notice_id){
        request.put("notice/edit-notice",this.form).then(res=>{
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "修改成功"
            })
            this.load()
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
        })
      }else {
        this.form.notice_date = FDate.getFormatDate()
        this.form.uploader=JSON.parse(sessionStorage.getItem("user")).username
        console.log(this.form)
        request.post("notice/add-notice",this.form).then(res=>{
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "添加成功"
            })
            this.load()
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
        })
      }
      this.form = {}
    },

    handleEdit(row){
      // 深拷贝
      this.form = JSON.parse(JSON.stringify(row))
      this.dialog_title = '修改公告内容'
      this.dialogFormVisible = true
    },
    handleDelete(id){
      console.log(id)
      request.delete("notice/"+id).then(res=>{
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