<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="老人姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入老人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="老人手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入老人手机号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="queryParams.gender" placeholder="请选择性别" clearable>
          <el-option
            v-for="dict in sys_user_sex"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="健康状态" prop="healthStatus">
        <el-select v-model="queryParams.healthStatus" placeholder="请选择健康状态" clearable>
          <el-option
            v-for="dict in elderly_health_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['elderly:elderly:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['elderly:elderly:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['elderly:elderly:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['elderly:elderly:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="elderlyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="老人姓名" align="center" prop="name" />
      <el-table-column label="老人手机号" align="center" prop="phone" />
      <el-table-column label="性别" align="center" prop="gender">
        <template #default="scope">
          <dict-tag :options="sys_user_sex" :value="scope.row.gender"/>
        </template>
      </el-table-column>
      <el-table-column label="出生日期" align="center" prop="birthDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.birthDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="健康状态" align="center" prop="healthStatus">
        <template #default="scope">
          <dict-tag :options="elderly_health_status" :value="scope.row.healthStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="居住地址" align="center" prop="address" />
      <el-table-column label="紧急联系人姓名" align="center" prop="emergencyContactName" />
      <el-table-column label="紧急联系人电话" align="center" prop="emergencyContactPhone" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['elderly:elderly:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['elderly:elderly:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改老人基础信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="elderlyRef" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="老人姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入老人姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="老人手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入老人手机号" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别">
            <el-option
              v-for="dict in sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker clearable
            v-model="form.birthDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择出生日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="健康状态" prop="healthStatus">
          <el-select v-model="form.healthStatus" placeholder="请选择健康状态">
            <el-option
              v-for="dict in elderly_health_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="居住地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入居住地址" />
        </el-form-item>
        <el-form-item label="紧急联系人姓名" prop="emergencyContactName">
          <el-input v-model="form.emergencyContactName" placeholder="请输入紧急联系人姓名" />
        </el-form-item>
        <el-form-item label="紧急联系人电话" prop="emergencyContactPhone">
          <el-input v-model="form.emergencyContactPhone" placeholder="请输入紧急联系人电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Elderly">
import { listElderly, getElderly, delElderly, addElderly, updateElderly } from "@/api/elderly/elderly"

const { proxy } = getCurrentInstance()
const { sys_user_sex, elderly_health_status } = proxy.useDict('sys_user_sex', 'elderly_health_status')

const elderlyList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: null,
    name: null,
    phone: null,
    gender: null,
    healthStatus: null,
    createTime: null,
  },
  rules: {

    name: [
      { required: true, message: "老人姓名不能为空", trigger: "blur" }
    ],
    idCard: [
      { required: true, message: "身份证号不能为空", trigger: "blur" }
    ],
    phone: [
      { required: true, message: "老人手机号不能为空", trigger: "blur" }
    ],
    healthStatus: [
      { required: true, message: "健康状态不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询老人基础信息列表 */
function getList() {
  loading.value = true
  listElderly(queryParams.value).then(response => {
    elderlyList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    elderlyId: null,
    userId: null,
    name: null,
    idCard: null,
    phone: null,
    gender: null,
    birthDate: null,
    healthStatus: null,
    address: null,
    emergencyContactName: null,
    emergencyContactPhone: null,
    backupFlag: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("elderlyRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.elderlyId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加老人基础信息"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _elderlyId = row.elderlyId || ids.value
  getElderly(_elderlyId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改老人基础信息"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["elderlyRef"].validate(valid => {
    if (valid) {
      if (form.value.elderlyId != null) {
        updateElderly(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addElderly(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _elderlyIds = row.elderlyId || ids.value
  proxy.$modal.confirm('是否确认删除老人基础信息编号为"' + _elderlyIds + '"的数据项？').then(function() {
    return delElderly(_elderlyIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('elderly/elderly/export', {
    ...queryParams.value
  }, `elderly_${new Date().getTime()}.xlsx`)
}

getList()
</script>
