<template>
  <div style="width: 100%; height: 100vh;background-color: darkslateblue; overflow: hidden">
    <div style="width: 400px; margin: 100px auto">
      <div style="color: #cccccc; font-size: 30px; text-align: center; padding: 30px 0">欢迎登录</div>
      <el-form ref="form" :model="form" size="normal" :rules="rules">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user-solid" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="form.user_type">
            <el-radio label="管理员" style="color: #eee">管理员</el-radio>
            <el-radio label="服务员" style="color: #eee">服务员</el-radio>
            <el-radio label="后厨人员" style="color: #eee">后厨人员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Login",
  data() {
    return {
      form: {user_type:"管理员"},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
      },
    }
  },
  created() {
    sessionStorage.removeItem("user")
  },
  methods: {
    
    login() {
      // console.log(this.form)
      let that = this
      this.$refs['form'].validate((valid) => {
        if(valid){
          request.post("user/login",this.form).then(res=>{
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "登录成功"
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))  // 缓存用户信息
              if(that.form.user_type=="管理员"){
                that.$router.push("/notice")
              }else {
                that.$router.push("/non-admin")
              }
              that.form={}
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
