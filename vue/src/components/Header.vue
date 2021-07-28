<template>
  <div
    style="height: 60px; line-height: 60px; border-bottom: 1px solid #ccc; display: flex"
  >
    <div
      v-if="user_info.user_type == '管理员'"
      style="width: 120px; padding-left: 42px; font-weight: bold; color: dodgerblue"
    >
      后台管理
    </div>
    <div v-if="user_info.user_type != '管理员'" style="flex: 0.6; padding-left: 20px;">
      <el-menu
        style="height:60px; line-height: 60px; font-size:34px; font-weight: 800;"
        :default-active="headMenuIndex"
        active-text-color="#00A1D6"
        class="el-menu-demo"
        mode="horizontal"
        @select="chooseMenu"
      >
        <el-menu-item v-if="user_info.user_type == '服务员'" index="/show-food">点菜菜单</el-menu-item>
        <el-menu-item v-if="user_info.user_type == '服务员'" index="/manage-order">当前订单管理</el-menu-item>
        <el-menu-item v-if="user_info.user_type == '后厨人员'" index="/show-order">前台订单处理</el-menu-item>
        <el-menu-item index="/show-notice">公告中心</el-menu-item>
      </el-menu>
    </div>
    <div style="flex: 1;"></div>
    <div style="width: 200px;">
      <el-dropdown>
        <span class="el-dropdown-link">
          <el-image style="width: 38px; height: 38px; border-radius:19px; margin-top:10px;margin-right: 10px;"
                    :src="'http://localhost:9090/static/'+user_info.user_avator" />
          <span class="username">{{user_info.username}}</span>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="userinfo">个人信息</el-dropdown-item>
            <el-dropdown-item @click="editPsw">修改密码</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/login')"
              >退出系统</el-dropdown-item
            >
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dialog
        title="个人信息"
        v-model="dialogFormVisible"
        append-to-body
        width="30%"
      >
        <el-form :model="form">
          <el-form-item label="当前用户名:" :label-width="formLabelWidth">
            {{user_info.username}}
          </el-form-item>
          <el-form-item label="修改用户名:" :label-width="formLabelWidth">
            <el-input
              v-model="form.username"
              placeholder="若为空则保留原用户名"
              style="width:80%;"
              type="text"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="当前头像:" :label-width="formLabelWidth">
            <el-image
                style="width: 120px; height: 120px; border-radius: 5px"
                :src="'http://localhost:9090/static/'+user_info.user_avator"
                :fit="fit">
            </el-image>
          </el-form-item>
          <el-form-item label="修改头像:" :label-width="formLabelWidth">
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
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="closeEditUserinfoDialog">返 回</el-button>
            <el-popconfirm
              title="确定修改吗？"
              @confirm="saveUserInfo"
            >
              <template #reference>
                <el-button type="primary">确 定</el-button>
              </template>
            </el-popconfirm>
          </span>
        </template>
      </el-dialog>

      <el-dialog title="修改密码" v-model="editPswDialog" center top="20vh" width="30%">
        <el-input placeholder="请输入旧密码" v-model="user_psw" show-password></el-input>
        <el-input clearable placeholder="请输入新密码" v-model="new_user_psw" show-password></el-input>
        <el-input clearable placeholder="请再次输入新密码" v-model="new_user_psw2" show-password></el-input>
        <template #footer>
            <span class="dialog-footer">
            <el-button @click="closeEditPswDialog">取 消</el-button>
            <el-button style="margin-left:10%" type="primary" @click="save">确 定 修 改</el-button>
            </span>
        </template>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Header",
  data() {
    return {
      user_info: {},
      headMenuIndex:'/show-notice',
      user_psw:'',
      new_user_psw:'',
      new_user_psw2:'',
      formLabelWidth: "120px",
      dialogFormVisible: false,
      editPswDialog: false,
      form: {},
      user: {},
      imgUploadUrl:'http://localhost:9090/user/avator',
      imageUrl:'',
      tempUrl:'',
    };
  },
  created() {
    // console.log("user:",sessionStorage.getItem("user"))
    this.user_info=JSON.parse(sessionStorage.getItem("user"))
  },
  methods: {
    save(){
      if(this.user_psw.length===0||this.new_user_psw===0||this.new_user_psw2===0){alert("密码不能为空");return}
      if(this.new_user_psw!==this.new_user_psw2){alert("两次密码不一致");return}
      this.form={
        user_id:this.user_info.user_id,
        password:this.user_psw
      }
      // console.log(this.form)
      request.put("user/edit-password/"+this.new_user_psw,this.form).then(res=>{
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "修改密码成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
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
    editPsw(){
      this.editPswDialog = true
    },
    closeEditPswDialog(){
      this.editPswDialog = false
      this.user_psw=''
      this.new_user_psw=''
      this.new_user_psw2=''
    },
    userinfo() {
      this.dialogFormVisible = true;
    },
    closeEditUserinfoDialog(){
      this.dialogFormVisible = false;
      this.form={}
      this.tempUrl=''
    },
    saveUserInfo() {
      // console.log("img url:",this.imageUrl)
      this.tempUrl=''
      if(!("username" in this.form)&&this.imageUrl.length===0){
        this.$message({
          type: "error",
          message: "至少修改一项！"
        })
        return
      }
      if(this.imageUrl){
        this.form.user_avator = this.imageUrl
      }
      this.form.user_id = this.user_info.user_id
      request.put("user/edit-user",this.form).then(res=>{
        console.log(res)
        request.get("user/find-by-id",{params:{
          search:this.user_info.user_id
          }}).then(reload_res=>{
          this.user_info = reload_res.data.records[0]
          console.log('newinfo:',this.user_info)
          sessionStorage.setItem("user", JSON.stringify(this.user_info))  // 缓存用户信息
          window.location.reload()
        })
        this.dialogFormVisible=false
        this.form={}
      })
    },
    chooseMenu(index, indexPath) {
      console.log(index, indexPath);
      this.headMenuIndex=index
      this.$router.push({ path: index });
    },
  },
};
</script>

<style>
.username{
  display:inline-block;
  height: 60px;
  margin-top: -8px;
  line-height: 60px;
  font-size: 25px;
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
