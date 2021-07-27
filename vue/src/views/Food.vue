<template>
  <div class="main">
    <div style="margin: 10px 0">
      <el-input
          style="width: 30vw"
          placeholder="按菜品名查询"
          prefix-icon="el-icon-search"
          v-model="search">
      </el-input>
      <el-button @click="load" type="primary" style="margin: 10px 10px;">查询</el-button>
      <el-button @click="addfood" type="primary" style="margin-left: 50px;">新增菜品</el-button>
    </div>
    <el-table
        border
        :data="tableData"
        style="width: 85%"
        :row-class-name="tableRowClassName">
      <el-table-column
          prop="food_id"
          label="菜品编号"
          sortable>
      </el-table-column>
      <el-table-column
          prop="food_name"
          label="菜品名">
      </el-table-column>
      <el-table-column
          prop="food_price"
          label="菜品价格">
      </el-table-column>
      <el-table-column
          prop="food_type"
          label="菜品分类">
      </el-table-column>
      <el-table-column
          prop="food_img"
          align="center"
          label="菜品图片">
        <template #default="scope">
          <el-image
              style="width: 120px; height: 120px; border-radius: 5px"
              :src="'http://localhost:9090/static/'+scope.row.food_img"
              :fit="fit">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column
          prop="food_desc"
          label="菜品简介">
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="handleEdit(scope.row)" size="mini" plain>编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="handleDelete(scope.row.food_id)"
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
          <el-form-item label="菜品名" :label-width="formLabelWidth">
            <el-input v-model="form.food_name" style="width:50%;" type="text" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="菜品价格" :label-width="formLabelWidth">
            <el-input v-model="form.food_price" style="width:50%;" type="text" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="菜品分类" :label-width="formLabelWidth">
            <el-select v-model="selectType" placeholder="请选择菜品分类">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="菜品图片" :label-width="formLabelWidth">
            <el-upload
                class="avatar-uploader"
                :action="imgUploadUrl"
                :show-file-list="false"
                :on-success="imgUploadSuccess"
                :before-upload="beforeAvatarUpload"
            >
              <img v-if="tempUrl" :src="tempUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="菜品简介" :label-width="formLabelWidth">
            <el-input v-model="form.food_desc" style="width:50%;" type="text" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="HanddleCancel">取 消</el-button>
          <el-button type="primary" @click="commitAddfood">确 定</el-button>
        </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
// import request from "@/utils/request";

import request from "@/utils/request";

export default {
  name:"Food",
  data(){
    return{
      options: [],
      form:{
        role:1,
      },
      dialogFormVisible:false,
      formLabelWidth: '70px',
      search:'',
      pageNum:1,
      pageSize: 10,
      totalPage:1,
      tableData:[],
      imgUploadUrl:'http://localhost:9090/food/upload-img',
      tempUrl:'',
      imageUrl:'',
      selectType:'',
    }
  },
  created() {
    this.load()
    this.loadType()
  },
  methods:{
    load(){
      request.get("food",{
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
    loadType(){
      request.get("food-type",{}).then(res=>{
        console.log(res);
        for(let i=0;i<res.data.records.length;i++){
          let type_item={
            label:res.data.records[i].food_type_name,
            value:res.data.records[i].food_type_name
          }
          this.options.push(type_item)
        }
      })
    },
    imgUploadSuccess(res, file) {
      this.tempUrl = URL.createObjectURL(file.raw);
      this.imageUrl = res.data
      console.log(res,'+',file)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    addfood(){
      this.dialogFormVisible = true;
      this.dialog_title = '新增菜品'
    },
    commitAddfood(){
      if(this.imageUrl){
        this.form.food_img = this.imageUrl
      }
      this.form.food_type=this.selectType
      if(this.form.food_id){ // update
        request.put("food/edit-food",this.form).then(res=>{
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
        request.post("food/add-food",this.form).then(res=>{
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
      this.form={}
      this.imageUrl=''
      this.tempUrl=''
    },
    HanddleCancel(){
      this.dialogFormVisible = false;
      this.tempUrl=''
    },
    handleEdit(row){
      // 深拷贝
      this.form = JSON.parse(JSON.stringify(row))
      this.dialog_title = '修改菜品信息'
      this.dialogFormVisible = true
    },
    handleDelete(id){
      request.delete("food/"+id).then(res=>{
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
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px !important;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>