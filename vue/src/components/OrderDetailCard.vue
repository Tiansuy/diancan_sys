<template>
  <div style="display:flex;width:900px;">
    <el-descriptions
    style="flex:1"
      :column="2"
      :size="size"
      border
    >
      <el-descriptions-item>
        <template #label>
          <i class="el-icon-user"></i>
          发布者
        </template>
        {{dataList.uploader}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <i class="el-icon-mobile-phone"></i>
          发布时间
        </template>
        {{dataList.notice_date}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <i class="el-icon-location-outline"></i>
          内容
        </template>
        {{dataList.notice_content}}
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
export default {
  name: "OrderDetailCard",
  data() {
    return {
      data_item: {},
    };
  },
  props:[
    'dataList'
  ],
  methods: {},
};
</script>
